package io.github.bluemarlins.aura.contract

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import io.github.bluemarlins.aura.component.AuraBadge
import io.github.bluemarlins.aura.component.AuraBadgeSize
import io.github.bluemarlins.aura.component.AuraBottomIsland
import io.github.bluemarlins.aura.component.AuraBottomIslandItem
import io.github.bluemarlins.aura.component.AuraButton
import io.github.bluemarlins.aura.component.AuraButtonSize
import io.github.bluemarlins.aura.component.AuraCheckbox
import io.github.bluemarlins.aura.component.AuraChipButton
import io.github.bluemarlins.aura.component.AuraChipSize
import io.github.bluemarlins.aura.component.AuraFab
import io.github.bluemarlins.aura.component.AuraFabSize
import io.github.bluemarlins.aura.component.AuraIconButton
import io.github.bluemarlins.aura.component.AuraIconButtonSize
import io.github.bluemarlins.aura.component.AuraLinearProgressBar
import io.github.bluemarlins.aura.component.AuraNumberField
import io.github.bluemarlins.aura.component.AuraRadio
import io.github.bluemarlins.aura.component.AuraRatingBar
import io.github.bluemarlins.aura.component.AuraRatingBarSize
import io.github.bluemarlins.aura.component.AuraSearchBar
import io.github.bluemarlins.aura.component.AuraSegmentedButton
import io.github.bluemarlins.aura.component.AuraSegmentedSize
import io.github.bluemarlins.aura.component.AuraSlider
import io.github.bluemarlins.aura.component.AuraStepProgressBar
import io.github.bluemarlins.aura.component.AuraSwitch
import io.github.bluemarlins.aura.component.AuraSwitchSize
import io.github.bluemarlins.aura.component.AuraTextField
import io.github.bluemarlins.aura.component.AuraTextFieldVariant
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
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Measures rendered components against the `<!-- QUICK-SPEC -->` block in each spec's §3.
 *
 * Each Quick Spec key is `<Part ID>.<metric>`; parts are located through [AuraPartKey]
 * (`aura.component.<name>/<Part ID>`). The test prints a per-value report and a match rate,
 * and fails when any measurable value differs from the spec.
 *
 * Metrics: `height`, `width`, `size` (width and height) — equal within 0.5dp; `minHeight`,
 * `minWidth` — at least the value; `paddingHorizontal` — start inset from the part to its
 * first child part; `fontSize`, `lineHeight` — sp from the text layout; `cornerRadius` — from
 * captured pixels over a magenta backdrop, equal within 1dp. Other metrics are reported as unmeasured.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], qualifiers = "xxhdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class SpecMeasurementTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val squareVector: ImageVector = ImageVector.Builder(
        name = "square",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(2f, 2f)
            lineTo(22f, 2f)
            lineTo(22f, 22f)
            lineTo(2f, 22f)
            close()
        }
    }.build()

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
        "aura.component.search-bar" to mapOf(
            "Default" to @Composable { AuraSearchBar(query = "", onQueryChange = {}, onFilterClick = {}) },
        ),
        "aura.component.rating-bar" to AuraRatingBarSize.entries.associate { size ->
            size.name to @Composable { AuraRatingBar(rating = 3f, onRatingChange = null, size = size) }
        } + ("Interactive" to @Composable { AuraRatingBar(rating = 3f, onRatingChange = {}) }),
        "aura.component.segmented-button" to AuraSegmentedSize.entries.associate { size ->
            size.name to @Composable {
                AuraSegmentedButton(
                    options = listOf("A", "B"),
                    selectedIndex = 0,
                    onOptionSelected = {},
                    size = size,
                    optionIcons = listOf({ Box(Modifier.fillMaxSize()) }, null),
                )
            }
        },
        "aura.component.slider" to mapOf(
            "Default" to @Composable { AuraSlider(value = 0.5f, onValueChange = {}) },
        ),
        "aura.component.text-field" to listOf(AuraTextFieldVariant.Outlined, AuraTextFieldVariant.Paperless).associate { variant ->
            variant.name to @Composable { AuraTextField(value = "Go", onValueChange = {}, variant = variant) }
        },
        "aura.component.number-field" to mapOf(
            "Default" to @Composable { AuraNumberField(value = "1", onValueChange = {}) },
        ),
        "aura.component.progress-bar" to mapOf(
            "Linear" to @Composable { AuraLinearProgressBar(progress = 0.5f) },
            "Step" to @Composable { AuraStepProgressBar(currentStep = 1, totalSteps = 3) },
        ),
        "aura.component.bottom-island" to mapOf(
            "Default" to @Composable {
                AuraBottomIsland(
                    items = listOf(
                        AuraBottomIslandItem(id = "a", label = "A", icon = squareVector),
                        AuraBottomIslandItem(id = "b", label = "B", icon = squareVector),
                    ),
                    selectedIndex = 0,
                    onItemSelected = {},
                    splitActionIcon = squareVector,
                    splitActionLabel = "Add",
                    onSplitActionClick = {},
                )
            },
        ),
    )

    @Test
    fun `rendered components match their Quick Spec values`() = verifyQuickSpecs("default")

    // §7 Responsive: fixed component dimensions and body/label type must hold in every window profile.
    @Test
    @Config(qualifiers = "w320dp-h640dp-xxhdpi")
    fun `Quick Spec holds on ExtraSmall windows`() = verifyQuickSpecs("ExtraSmall")

    @Test
    @Config(qualifiers = "w411dp-h891dp-xxhdpi")
    fun `Quick Spec holds on Compact windows`() = verifyQuickSpecs("Compact")

    @Test
    @Config(qualifiers = "w840dp-h800dp-xxhdpi")
    fun `Quick Spec holds on Square windows`() = verifyQuickSpecs("Square")

    @Test
    @Config(qualifiers = "w915dp-h360dp-xxhdpi")
    fun `Quick Spec holds on UltraWide windows`() = verifyQuickSpecs("UltraWide")

    @Test
    @Config(qualifiers = "w1280dp-h800dp-xxhdpi")
    fun `Quick Spec holds on Expanded windows`() = verifyQuickSpecs("Expanded")

    private fun verifyQuickSpecs(profile: String) {
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
                            Box(Modifier.testTag(scopeTag(spec.id, size)).background(Backdrop)) {
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
            println("SPEC [$profile] ${r.id} [${r.size}] ${r.key}: spec=${r.expected} actual=${r.actual?.let { "%.2f".format(it) } ?: "-"} $status")
        }
        val rate = if (measured.isEmpty()) 0.0 else passed * 100.0 / measured.size
        println(
            "SPEC MATCH [$profile] $passed/${measured.size} measured values (${"%.1f".format(rate)}%), " +
                "${results.size - measured.size} unmeasured",
        )

        val mismatches = measured.filterNot { it.ok }
        assertTrue(
            "Spec/code mismatches ($profile):\n" +mismatches.joinToString("\n") {
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
            "cornerRadius" -> cornerRadiusPx(id, size, partId)?.pxToDp()
            else -> null
        }
    }

    /**
     * Estimates a uniform corner radius from pixels: the part is drawn over [Backdrop], and the
     * backdrop showing through each corner square covers `r² (1 − π/4)`. Anti-aliased edge pixels
     * count by their blend between the backdrop and the part's fill, so the estimate is sub-pixel.
     */
    private fun cornerRadiusPx(id: String, size: String, partId: String): Float? {
        val interaction = composeTestRule.onNode(
            SemanticsMatcher.expectValue(AuraPartKey, "$id/$partId") and hasAnyAncestor(hasTestTag(scopeTag(id, size))),
            useUnmergedTree = true,
        )
        interaction.performScrollTo()
        composeTestRule.waitForIdle()
        // captureToImage() waits for a hardware redraw that Robolectric's paused looper never
        // delivers, so draw the window into a software bitmap and crop the part instead.
        val bounds = interaction.fetchSemanticsNode().boundsInWindow
        val window = composeTestRule.activity.window.decorView
        val bitmap = Bitmap.createBitmap(window.width, window.height, Bitmap.Config.ARGB_8888)
        window.draw(Canvas(bitmap))
        val left = bounds.left.roundToInt()
        val top = bounds.top.roundToInt()
        val width = bounds.width.roundToInt()
        val height = bounds.height.roundToInt()
        if (left < 0 || top < 0 || left + width > bitmap.width || top + height > bitmap.height) return null
        fun pixel(x: Int, y: Int) = Color(bitmap.getPixel(left + x, top + y))
        val half = minOf(width, height) / 2
        if (half < 2) return null
        // Fill reference: top-center, clear of the corners and of centered content.
        val fill = pixel(width / 2, maxOf(1, height / 8))
        val span = colorDistance(fill, Backdrop)
        if (span < 0.2f) return null
        fun exposed(x: Int, y: Int) = 1f - (colorDistance(pixel(x, y), Backdrop) / span).coerceIn(0f, 1f)

        // Straight edges also show a sub-pixel band of backdrop (fractional bounds, borders).
        // Measure each band away from the corners and remove it from the corner squares.
        val band = 4
        val edgeLeft = (0 until band).sumOf { exposed(it, height / 2).toDouble() }.toFloat()
        val edgeRight = (width - band until width).sumOf { exposed(it, height / 2).toDouble() }.toFloat()
        val edgeTop = (0 until band).sumOf { exposed(width / 2, it).toDouble() }.toFloat()
        val edgeBottom = (height - band until height).sumOf { exposed(width / 2, it).toDouble() }.toFloat()
        // Corner squares of side `window` anchored at each corner; side/cap are the edge bands.
        fun radiusIn(window: Int): Float {
            data class Corner(val x0: Int, val y0: Int, val side: Float, val cap: Float)
            val corners = listOf(
                Corner(0, 0, edgeLeft, edgeTop),
                Corner(width - window, 0, edgeRight, edgeTop),
                Corner(0, height - window, edgeLeft, edgeBottom),
                Corner(width - window, height - window, edgeRight, edgeBottom),
            )
            return corners.map { c ->
                var area = 0f
                for (x in c.x0 until c.x0 + window) {
                    for (y in c.y0 until c.y0 + window) area += exposed(x, y)
                }
                val cornerArea = (area - (c.side + c.cap) * window + c.side * c.cap).coerceAtLeast(0f)
                sqrt(cornerArea / (1f - PI.toFloat() / 4f))
            }.average().toFloat()
        }
        // A wide first pass can pick up content near the corners (icons, text); a second pass
        // over a window just larger than the first estimate excludes it.
        val rough = radiusIn(half)
        return radiusIn(minOf(half, rough.roundToInt() + 2))
    }

    private fun colorDistance(a: Color, b: Color): Float =
        sqrt((a.red - b.red).pow(2) + (a.green - b.green).pow(2) + (a.blue - b.blue).pow(2))

    private fun matches(metric: String, expected: Float, actual: Float?): Boolean = when {
        actual == null || actual.isNaN() -> false
        metric.startsWith("min") -> actual >= expected - 0.5f
        metric == "cornerRadius" -> abs(actual - expected) <= 1f
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

    private companion object {
        /** Backdrop behind every rendered case; no Aura token is pure magenta. */
        val Backdrop = Color(0xFFFF00FF)
    }

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
