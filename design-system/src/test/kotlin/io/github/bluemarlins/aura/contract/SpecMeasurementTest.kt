package io.github.bluemarlins.aura.contract

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.toSize
import io.github.bluemarlins.aura.component.AuraBadge
import io.github.bluemarlins.aura.component.AuraBadgeSize
import io.github.bluemarlins.aura.component.AuraButton
import io.github.bluemarlins.aura.component.AuraButtonSize
import io.github.bluemarlins.aura.component.AuraCheckbox
import io.github.bluemarlins.aura.component.AuraChipButton
import io.github.bluemarlins.aura.component.AuraChipSize
import io.github.bluemarlins.aura.component.AuraFab
import io.github.bluemarlins.aura.component.AuraFabSize
import io.github.bluemarlins.aura.component.AuraIconButton
import io.github.bluemarlins.aura.component.AuraIconButtonSize
import io.github.bluemarlins.aura.component.AuraRadio
import io.github.bluemarlins.aura.component.AuraSwitch
import io.github.bluemarlins.aura.component.AuraSwitchSize
import io.github.bluemarlins.aura.foundation.AuraPartKey
import io.github.bluemarlins.aura.foundation.theme.AuraTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import kotlin.math.abs

/**
 * Measures rendered components against the `<!-- QUICK-SPEC -->` block in each spec's §3.
 *
 * Each Quick Spec key is `<Part ID>.<metric>`; parts are located through [AuraPartKey]
 * (`aura.component.<name>/<Part ID>`). The test prints a per-value report and a match rate,
 * and fails when any measurable value differs from the spec.
 *
 * Metrics: `height`, `width`, `size` (width and height) — equal within 0.5dp; `minHeight`,
 * `minWidth` — at least the value; `paddingHorizontal` — start inset from the part to its
 * first child part; `fontSize`, `lineHeight` — sp from the text layout. Other metrics
 * (for example `cornerRadius`) are reported as unmeasured until a visual test covers them.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class SpecMeasurementTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /** Renders one size of a component. Keep content minimal so min sizes apply. */
    private val renderers: Map<String, Map<String, @Composable () -> Unit>> = mapOf(
        "aura.component.button" to AuraButtonSize.entries.associate { size ->
            size.name to @Composable {
                AuraButton(
                    text = "Go",
                    onClick = {},
                    size = size,
                    leadingIcon = { Box(Modifier.fillMaxSize()) },
                )
            }
        },
        "aura.component.icon-button" to AuraIconButtonSize.entries.associate { size ->
            size.name to @Composable {
                AuraIconButton(onClick = {}, size = size, contentDescription = "Go") { Box(Modifier.fillMaxSize()) }
            }
        },
        "aura.component.chip-button" to AuraChipSize.entries.associate { size ->
            size.name to @Composable {
                AuraChipButton(
                    label = "Go",
                    onClick = {},
                    size = size,
                    leadingIcon = { Box(Modifier.fillMaxSize()) },
                )
            }
        },
        "aura.component.fab" to AuraFabSize.entries.associate { size ->
            size.name to @Composable {
                AuraFab(onClick = {}, size = size, contentDescription = "Add") { Box(Modifier.fillMaxSize()) }
            }
        },
        "aura.component.switch" to AuraSwitchSize.entries.associate { size ->
            size.name to @Composable { AuraSwitch(checked = false, onCheckedChange = {}, size = size) }
        },
        "aura.component.checkbox" to mapOf(
            "Default" to @Composable { AuraCheckbox(checked = false, onCheckedChange = {}) },
        ),
        "aura.component.radio" to mapOf(
            "Default" to @Composable { AuraRadio(selected = false, onClick = {}) },
        ),
        "aura.component.badge" to AuraBadgeSize.entries.associate { size ->
            size.name to @Composable {
                AuraBadge(count = if (size == AuraBadgeSize.Dot) null else 4, size = size)
            }
        },
    )

    @Test
    fun `rendered components match their Quick Spec values`() {
        val specs = loadQuickSpecs()
        assertTrue("Expected at least one spec with a QUICK-SPEC block", specs.isNotEmpty())
        specs.forEach { spec ->
            val sizes = renderers[spec.id]
            assertTrue("${spec.path}: no renderer registered for ${spec.id} in SpecMeasurementTest", sizes != null)
            spec.sizes.keys.forEach { size ->
                assertTrue("${spec.path}: renderer for ${spec.id} has no size '$size'", sizes!!.containsKey(size))
            }
            spec.sizes.values.flatMap { it.keys }.map { it.substringBeforeLast('.') }.toSet().forEach { part ->
                assertTrue(
                    "${spec.path}: Quick Spec part '$part' is not listed in the §2 Part ID table",
                    spec.anatomy.contains("| `$part` |"),
                )
            }
        }

        composeTestRule.setContent {
            AuraTheme {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    specs.forEach { spec ->
                        spec.sizes.keys.forEach { size ->
                            Box(Modifier.testTag(scopeTag(spec.id, size))) {
                                renderers.getValue(spec.id).getValue(size)()
                            }
                        }
                    }
                }
            }
        }

        val density = Density(composeTestRule.density.density, composeTestRule.density.fontScale)
        val results = mutableListOf<Result>()
        specs.forEach { spec ->
            spec.sizes.forEach { (size, values) ->
                values.forEach { (key, expected) ->
                    val partId = key.substringBeforeLast('.')
                    val metric = key.substringAfterLast('.')
                    val actual = measure(spec.id, size, partId, metric, density)
                    results += Result(spec.id, size, key, expected, actual, matches(metric, expected, actual))
                }
            }
        }

        val measured = results.filter { it.actual != null }
        val passed = measured.count { it.ok }
        results.forEach { r ->
            val status = when {
                r.actual == null -> "UNMEASURED"
                r.ok -> "OK"
                else -> "MISMATCH"
            }
            println("SPEC ${r.id} [${r.size}] ${r.key}: spec=${r.expected} actual=${r.actual?.let { "%.2f".format(it) } ?: "-"} $status")
        }
        val rate = if (measured.isEmpty()) 0.0 else passed * 100.0 / measured.size
        println(
            "SPEC MATCH $passed/${measured.size} measured values (${"%.1f".format(rate)}%), " +
                "${results.size - measured.size} unmeasured",
        )

        val mismatches = measured.filterNot { it.ok }
        assertTrue(
            "Spec/code mismatches:\n" + mismatches.joinToString("\n") {
                "${it.id} [${it.size}] ${it.key}: spec=${it.expected}, actual=${"%.2f".format(it.actual)}"
            },
            mismatches.isEmpty(),
        )
    }

    private fun measure(id: String, size: String, partId: String, metric: String, density: Density): Float? {
        val node = findPart(id, size, partId) ?: return null
        val bounds = node.unclippedBounds()
        fun Float.pxToDp(): Float = with(density) { this@pxToDp.toDp().value }
        return when (metric) {
            "height", "minHeight" -> bounds.height.pxToDp()
            "width", "minWidth" -> bounds.width.pxToDp()
            "size" -> if (abs(bounds.width - bounds.height) < 1f) bounds.width.pxToDp() else Float.NaN
            "paddingHorizontal" -> descendantParts(node)
                .map { it.unclippedBounds().left }
                .minOrNull()
                ?.let { (it - bounds.left).pxToDp() }
            "fontSize", "lineHeight" -> {
                val layouts = mutableListOf<TextLayoutResult>()
                node.config.getOrNull(SemanticsActions.GetTextLayoutResult)?.action?.invoke(layouts)
                val style = layouts.firstOrNull()?.layoutInput?.style ?: return null
                val unit = if (metric == "fontSize") style.fontSize else style.lineHeight
                if (unit.isSp) unit.value else null
            }
            else -> null
        }
    }

    private fun matches(metric: String, expected: Float, actual: Float?): Boolean = when {
        actual == null || actual.isNaN() -> false
        metric.startsWith("min") -> actual >= expected - 0.5f
        else -> abs(actual - expected) <= 0.5f
    }

    private fun partNodes(id: String, size: String): List<SemanticsNode> =
        composeTestRule.onAllNodes(
            SemanticsMatcher("has Aura part of $id") { it.config.getOrNull(AuraPartKey)?.startsWith("$id/") == true } and
                hasAnyAncestor(hasTestTag(scopeTag(id, size))),
            useUnmergedTree = true,
        ).fetchSemanticsNodes()

    /** Layout bounds ignoring clipping, so parts scrolled outside the window still measure. */
    private fun SemanticsNode.unclippedBounds(): Rect = Rect(positionInRoot, size.toSize())

    private fun descendantParts(node: SemanticsNode): List<SemanticsNode> =
        node.children.flatMap { child ->
            val self = if (child.config.getOrNull(AuraPartKey) != null) listOf(child) else emptyList()
            self + descendantParts(child)
        }

    private fun findPart(id: String, size: String, partId: String): SemanticsNode? =
        partNodes(id, size).firstOrNull { it.config.getOrNull(AuraPartKey) == "$id/$partId" }

    private fun scopeTag(id: String, size: String) = "quick-spec/$id/$size"

    private fun <T> androidx.compose.ui.semantics.SemanticsConfiguration.getOrNull(
        key: androidx.compose.ui.semantics.SemanticsPropertyKey<T>,
    ): T? = getOrElseNullable(key) { null }

    private data class Result(
        val id: String,
        val size: String,
        val key: String,
        val expected: Float,
        val actual: Float?,
        val ok: Boolean,
    )

    private data class QuickSpec(
        val path: String,
        val id: String,
        val anatomy: String,
        val sizes: Map<String, Map<String, Float>>,
    )

    private fun loadQuickSpecs(): List<QuickSpec> {
        val root = generateSequence(File(System.getProperty("user.dir") ?: ".").absoluteFile) { it.parentFile }
            .first { File(it, "docs/components").isDirectory && File(it, "design-system").isDirectory }
        val componentsDir = File(root, "docs/components")
        return componentsDir.walkTopDown()
            .filter { it.isFile && it.extension == "md" && it.parentFile != componentsDir }
            .filter { it.readText().contains("<!-- QUICK-SPEC:BEGIN -->") }
            .sortedBy { it.path }
            .map { file ->
                val text = file.readText()
                val id = Regex("""(?m)^id:\s*"([^"]+)"""").find(text)?.groupValues?.get(1)
                    ?: error("${file.name}: missing frontmatter id")
                val block = text.substringAfter("<!-- QUICK-SPEC:BEGIN -->").substringBefore("<!-- QUICK-SPEC:END -->")
                val anatomy = text.substringAfter("## 2. Anatomy & Canonical Part IDs").substringBefore("## 3.")
                QuickSpec(file.relativeTo(root).invariantSeparatorsPath, id, anatomy, parseSizes(file.name, block))
            }
            .toList()
    }

    /** Parses the restricted Quick Spec YAML: `sizes:` → `<Size>:` → `<part>.<metric>: <number>`. */
    private fun parseSizes(fileName: String, block: String): Map<String, Map<String, Float>> {
        val sizes = linkedMapOf<String, MutableMap<String, Float>>()
        var current: MutableMap<String, Float>? = null
        block.lines()
            .map { it.substringBefore('#').trimEnd() }
            .filter { it.isNotBlank() && !it.trimStart().startsWith("```") }
            .forEach { line ->
                val indent = line.length - line.trimStart().length
                val content = line.trim()
                when {
                    indent == 0 && content == "sizes:" -> Unit
                    indent == 2 && content.endsWith(":") -> {
                        current = linkedMapOf<String, Float>().also { sizes[content.removeSuffix(":")] = it }
                    }
                    indent == 4 -> {
                        val (key, value) = content.split(":", limit = 2).map { it.trim() }
                        val number = value.toFloatOrNull() ?: error("$fileName: '$key' must be a number (was '$value')")
                        (current ?: error("$fileName: '$key' is outside a size")) [key] = number
                    }
                    else -> error("$fileName: unexpected Quick Spec line '$line'")
                }
            }
        return sizes
    }
}
