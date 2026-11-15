package com.example.ds.contract

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Automated verifier for Aura's Component Evaluation Rule Set (`EVAL-001` – `EVAL-006`).
 *
 * Audits every component specification in `docs/components/<category>/<component>.md` against
 * its Kotlin implementation in `:design-system` to enforce continuous `Specs <-> Code` parity.
 */
class SpecCodeConsistencyTest {

    @Test
    fun `all ready component specs and implementations satisfy EVAL-001 through EVAL-006`() {
        val repoRoot = resolveRepoRoot()
        val componentsDocsDir = File(repoRoot, "docs/components")
        val componentSrcDir = File(repoRoot, "design-system/src/main/kotlin/com/example/ds/component")

        val specFiles = componentsDocsDir.walkTopDown()
            .filter { file ->
                file.isFile &&
                    file.extension == "md" &&
                    file.parentFile != componentsDocsDir &&
                    !file.name.startsWith("00-") &&
                    !file.name.startsWith("01-")
            }
            .sortedBy { it.path }
            .toList()

        assertTrue("Expected ready component specs under ${componentsDocsDir.path}", specFiles.size >= 4)

        specFiles.forEach { specFile ->
            val spec = specFile.readText()
            val relativePath = specFile.relativeTo(repoRoot).invariantSeparatorsPath
            val frontmatter = parseFrontmatter(spec)
            val status = frontmatter["status"] ?: "unknown"
            if (status != "ready") return@forEach

            // EVAL-001: Intent & Usage Principles + Do / Don't
            assertTrue("$relativePath [EVAL-001]: missing Intent & Usage Principles section", spec.contains("Intent & Usage Principles"))
            assertTrue("$relativePath [EVAL-001]: missing 'When to Use' section", spec.contains("### When to Use"))
            assertTrue("$relativePath [EVAL-001]: missing 'When NOT to Use' section", spec.contains("### When NOT to Use"))
            assertTrue("$relativePath [EVAL-001]: missing '### Do / Don't' section", spec.contains("### Do / Don't"))
            assertTrue("$relativePath [EVAL-001]: missing '| Do | Don't |' table", spec.contains("| Do | Don't |"))

            // EVAL-002: Semantic Token Bindings + Kotlin Zero Material3 / Raw Hex Check
            assertTrue("$relativePath [EVAL-002]: frontmatter must declare 'tokens:'", spec.contains("\ntokens:"))
            assertTrue("$relativePath [EVAL-002]: missing Semantic Token Bindings section", spec.contains("Token Bindings"))

            val androidPlatform = Regex("""android:\s*"([^"]+)"""").find(spec)?.groupValues?.get(1)
                ?: error("$relativePath [EVAL-006]: missing 'platforms.android' in frontmatter")
            val kotlinSymbolName = androidPlatform.substringAfterLast(".")
            val kotlinFile = File(componentSrcDir, "$kotlinSymbolName.kt")
            assertTrue("$relativePath [EVAL-006]: missing Kotlin source ${kotlinFile.path}", kotlinFile.isFile)

            val ktCode = kotlinFile.readText()
            val ktProdCode = ktCode.substringBefore("@Preview")
            assertFalse(
                "${kotlinFile.name} [EVAL-002]: must not depend on androidx.compose.material3",
                ktCode.contains("androidx.compose.material3"),
            )
            assertFalse(
                "${kotlinFile.name} [EVAL-002]: must not hardcode Color(0x...) in production component code",
                ktProdCode.contains("Color(0x"),
            )

            // EVAL-003: Responsive Layout Behavior (Smallest Width x Display Ratio)
            assertTrue(
                "$relativePath [EVAL-003]: missing Responsive Layout Behavior section",
                spec.contains("Responsive Layout Behavior"),
            )
            listOf("ExtraSmall", "Compact", "Square", "UltraWide", "Expanded").forEach { profile ->
                assertTrue(
                    "$relativePath [EVAL-003]: missing responsive guidance for '$profile'",
                    spec.contains(profile),
                )
            }

            // EVAL-004: Interaction Motion Contract
            assertTrue(
                "$relativePath [EVAL-004]: missing Motion contract section",
                spec.contains("Motion"),
            )
            val declaresMotionToken = frontmatterBlock(spec).contains("motion.")
            if (declaresMotionToken) {
                assertTrue(
                    "${kotlinFile.name} [EVAL-004]: spec declares 'motion.*' so code must consume AuraTheme.motion",
                    ktProdCode.contains("AuraTheme.motion"),
                )
            }
            val codeWithoutComments = ktProdCode
                .replace(Regex("/\\*.*?\\*/", RegexOption.DOT_MATCHES_ALL), "")
                .lines()
                .filterNot { it.trimStart().startsWith("//") }
                .joinToString("\n")
            assertTrue(
                "${kotlinFile.name} [EVAL-004]: hardcoded tween(duration) literal forbidden in component production code",
                !Regex("""\btween\s*(?:<[^>]+>\s*)?\(\s*(?:durationMillis\s*=\s*)?\d""").containsMatchIn(codeWithoutComments),
            )
            assertTrue(
                "${kotlinFile.name} [EVAL-004]: hardcoded spring(dampingRatio/stiffness) literal forbidden in component production code",
                !Regex("""\bspring\s*(?:<[^>]+>\s*)?\(\s*(?:dampingRatio|stiffness)\s*=\s*[\d.]""").containsMatchIn(codeWithoutComments),
            )

            // EVAL-005: State Precedence & Accessibility
            assertTrue(
                "$relativePath [EVAL-005]: missing Accessibility section",
                spec.contains("Accessibility"),
            )
            val category = frontmatter["category"]
            if (category == "actions" || category == "inputs") {
                assertTrue(
                    "$relativePath [EVAL-005]: interactive component must define State Precedence",
                    spec.contains("State Precedence"),
                )
            }

            // EVAL-006: Multi-platform API Blocks & Android Signature Parity
            listOf("ANDROID", "IOS", "WEB").forEach { platform ->
                assertTrue(
                    "$relativePath [EVAL-006]: missing <!-- API:$platform:BEGIN --> block",
                    spec.contains("<!-- API:$platform:BEGIN -->") && spec.contains("<!-- API:$platform:END -->"),
                )
            }
            verifyAndroidApiSync(relativePath, spec, kotlinFile.name, ktProdCode)

            // EVAL-007: Materiality & Elevation Matrix
            val layer = frontmatter["layer"]
            assertTrue(
                "$relativePath [EVAL-007]: frontmatter 'layer' must be one of canvas, content, chrome, spatial (was '$layer')",
                layer in setOf("canvas", "content", "chrome", "spatial"),
            )
            val materialAllowed = (frontmatter["material_allowed"] ?: "")
                .removePrefix("[")
                .removeSuffix("]")
                .split(",")
                .map { it.trim().trim('"', '\'') }
                .filter { it.isNotEmpty() }
            assertTrue(
                "$relativePath [EVAL-007]: frontmatter 'material_allowed' must be a non-empty subset of Solid, Glass, LiquidGlass",
                materialAllowed.isNotEmpty() && materialAllowed.all { it in setOf("Solid", "Glass", "LiquidGlass") },
            )
            if (layer == "content" || layer == "canvas") {
                assertTrue(
                    "${kotlinFile.name} [EVAL-007]: content/canvas component must not invoke GlassSurface(",
                    !ktProdCode.contains("GlassSurface("),
                )
            }
            if (ktProdCode.contains("GlassSurface(")) {
                assertTrue(
                    "$relativePath [EVAL-007]: Glass/LiquidGlass component spec must document Reduced Transparency and 4.5:1 contrast",
                    spec.contains("Reduced Transparency") && spec.contains("4.5:1"),
                )
            }

            // EVAL-008: Dual-Track Motion Profile & MainClock Verification
            val motionTrack = frontmatter["motion_track"]
            assertTrue(
                "$relativePath [EVAL-008]: frontmatter 'motion_track' must be Standard, Expressive, or none (was '$motionTrack')",
                motionTrack in setOf("Standard", "Expressive", "none"),
            )
            if (category != "layout") {
                assertTrue(
                    "$relativePath [EVAL-008]: non-layout component must specify 'motion_track' as Standard or Expressive (was '$motionTrack')",
                    motionTrack in setOf("Standard", "Expressive"),
                )
            }
        }

        val motionBehaviorTest = File(
            repoRoot,
            "design-system/src/test/kotlin/com/example/ds/motion/ComponentMotionBehaviorTest.kt",
        )
        assertTrue(
            "[EVAL-008]: ComponentMotionBehaviorTest.kt must exist at ${motionBehaviorTest.path}",
            motionBehaviorTest.isFile,
        )
        val motionBehaviorSource = motionBehaviorTest.readText()
        listOf("AuraButton", "AuraSwitch", "AuraFab", "AuraBottomIsland", "AuraDialog", "GlassSurface").forEach { symbol ->
            assertTrue(
                "[EVAL-008]: ComponentMotionBehaviorTest.kt must test $symbol",
                motionBehaviorSource.contains(symbol),
            )
        }
    }

    private fun verifyAndroidApiSync(
        specPath: String,
        spec: String,
        ktFileName: String,
        ktProdCode: String,
    ) {
        val androidBlock = spec
            .substringAfter("<!-- API:ANDROID:BEGIN -->")
            .substringBefore("<!-- API:ANDROID:END -->")

        val enumMatches = Regex("""enum class\s+(\w+)""").findAll(androidBlock)
        enumMatches.forEach { match ->
            val enumName = match.groupValues[1]
            assertTrue(
                "$ktFileName [EVAL-006]: missing enum class '$enumName' declared in $specPath",
                ktProdCode.contains("enum class $enumName"),
            )
        }

        val funMatches = Regex("""fun\s+(?:<[^>]+>\s*)?(\w+)\s*\(""").findAll(androidBlock)
        val funList = funMatches.toList()
        assertTrue("$specPath [EVAL-006]: expected at least one Kotlin function in API:ANDROID block", funList.isNotEmpty())

        funList.forEach { match ->
            val funName = match.groupValues[1]
            val specParams = extractTopLevelParamNames(androidBlock, match.range.last)
            val ktFunMatch = Regex("""fun\s+(?:<[^>]+>\s*)?$funName\s*\(""").find(ktProdCode)
                ?: error("$ktFileName [EVAL-006]: missing function '$funName' declared in $specPath")
            val ktParams = extractTopLevelParamNames(ktProdCode, ktFunMatch.range.last)

            assertTrue(
                "$ktFileName [EVAL-006]: parameter mismatch for '$funName' vs $specPath. Spec=$specParams, Code=$ktParams",
                specParams == ktParams,
            )
        }
    }

    private fun extractTopLevelParamNames(source: String, openParenIndex: Int): List<String> {
        val params = mutableListOf<String>()
        var parenDepth = 0
        var angleDepth = 0
        val currentToken = StringBuilder()
        var capturingParam = true
        var i = openParenIndex

        while (i < source.length) {
            val ch = source[i]
            val prev = if (i > 0) source[i - 1] else ' '
            when {
                ch == '(' -> {
                    parenDepth++
                    if (parenDepth == 1) {
                        currentToken.clear()
                        capturingParam = true
                    }
                }
                ch == ')' -> {
                    parenDepth--
                    if (parenDepth == 0) break
                }
                ch == '<' && parenDepth >= 1 -> angleDepth++
                ch == '>' && prev != '-' && angleDepth > 0 -> angleDepth--
                ch == ',' && parenDepth == 1 && angleDepth == 0 -> {
                    currentToken.clear()
                    capturingParam = true
                }
                ch == ':' && parenDepth == 1 && angleDepth == 0 && capturingParam -> {
                    val name = currentToken.toString().trim().substringAfterLast(" ")
                    if (name.isNotBlank()) {
                        params.add(name)
                    }
                    capturingParam = false
                }
                ch == '=' && parenDepth == 1 && angleDepth == 0 -> {
                    capturingParam = false
                }
                parenDepth == 1 && angleDepth == 0 && capturingParam -> {
                    if (ch.isLetterOrDigit() || ch == '_' || ch == ' ') {
                        currentToken.append(ch)
                    } else {
                        currentToken.clear()
                    }
                }
            }
            i++
        }
        return params
    }

    private fun frontmatterBlock(spec: String): String =
        spec.removePrefix("---").substringBefore("---")

    private fun parseFrontmatter(spec: String): Map<String, String> {
        val block = frontmatterBlock(spec)
        return block.lines()
            .mapNotNull { line ->
                val idx = line.indexOf(':')
                if (idx <= 0 || line.startsWith(" ")) {
                    null
                } else {
                    val key = line.substring(0, idx).trim()
                    val value = line.substring(idx + 1).trim().trim('"')
                    key to value
                }
            }
            .toMap()
    }

    private fun resolveRepoRoot(): File {
        val cwd = File(System.getProperty("user.dir") ?: ".")
        return generateSequence(cwd) { it.parentFile }
            .firstOrNull { File(it, "docs/components").isDirectory && File(it, "design-system").isDirectory }
            ?: error("Could not locate repository root from working directory: ${cwd.absolutePath}")
    }
}
