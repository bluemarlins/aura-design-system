package io.github.bluemarlins.aura.preview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.bluemarlins.aura.component.AuraAdaptiveTwoPane
import io.github.bluemarlins.aura.component.AuraCard
import io.github.bluemarlins.aura.component.AuraCardPadding
import io.github.bluemarlins.aura.component.AuraCardVariant
import io.github.bluemarlins.aura.component.AuraChipButton
import io.github.bluemarlins.aura.component.AuraChipSize
import io.github.bluemarlins.aura.component.AuraChipVariant
import io.github.bluemarlins.aura.component.AuraIcon
import io.github.bluemarlins.aura.component.AuraIconButton
import io.github.bluemarlins.aura.component.AuraIconButtonVariant
import io.github.bluemarlins.aura.component.AuraRatingBar
import io.github.bluemarlins.aura.component.AuraRatingBarSize
import io.github.bluemarlins.aura.component.AuraScaffold
import io.github.bluemarlins.aura.component.AuraSlider
import io.github.bluemarlins.aura.component.AuraSwitch
import io.github.bluemarlins.aura.component.AuraSwitchSize
import io.github.bluemarlins.aura.component.AuraText
import io.github.bluemarlins.aura.foundation.theme.AuraTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SliderRatingPreviewScreen(
    onBack: () -> Unit,
    initialFocusRating: Boolean = false,
) {
    var blurIntensity by remember { mutableFloatStateOf(0.72f) }
    var bodyScore by remember { mutableFloatStateOf(4f) }
    var tanninScore by remember { mutableFloatStateOf(3f) }
    var servingTemp by remember { mutableFloatStateOf(16f) }
    var sliderEnabled by remember { mutableStateOf(true) }

    var enjoymentRating by remember { mutableFloatStateOf(4.5f) }
    var allowHalfRating by remember { mutableStateOf(true) }
    var ratingSize by remember { mutableStateOf(AuraRatingBarSize.Large) }
    var ratingEnabled by remember { mutableStateOf(true) }

    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val title = if (initialFocusRating) "AuraRatingBar" else "AuraSlider"

    val sliderPane: @Composable () -> Unit = {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            AuraText(text = "AuraSlider · Continuous & Stepped", style = t.titleMedium)

            AuraCard(
                variant = AuraCardVariant.Outlined,
                padding = AuraCardPadding.Medium,
                modifier = Modifier.fillMaxWidth(),
            ) {
                AuraSlider(
                    value = blurIntensity,
                    onValueChange = { blurIntensity = it },
                    enabled = sliderEnabled,
                    label = "Home Launcher · Backdrop Blur",
                    valueLabel = "${(blurIntensity * 100).roundToInt()}%",
                    supportingText = "Continuous float range (0f..1f) with spring thumb glow.",
                )

                AuraSlider(
                    value = servingTemp,
                    onValueChange = { servingTemp = it },
                    enabled = sliderEnabled,
                    valueRange = 6f..20f,
                    steps = 13,
                    label = "Serving Temperature",
                    valueLabel = "${servingTemp.roundToInt()}°C",
                    modifier = Modifier.padding(top = 14.dp),
                )
            }

            AuraCard(
                variant = AuraCardVariant.Filled,
                padding = AuraCardPadding.Medium,
                modifier = Modifier.fillMaxWidth(),
            ) {
                AuraText(text = "My Drink Diary · Sensory Rubric", style = t.labelLarge, color = c.brand.primary)
                AuraSlider(
                    value = bodyScore,
                    onValueChange = { bodyScore = it },
                    enabled = sliderEnabled,
                    valueRange = 1f..5f,
                    steps = 3,
                    label = "Body",
                    valueLabel = "${bodyScore.roundToInt()} / 5",
                    modifier = Modifier.padding(top = 10.dp),
                )
                AuraSlider(
                    value = tanninScore,
                    onValueChange = { tanninScore = it },
                    enabled = sliderEnabled,
                    valueRange = 1f..5f,
                    steps = 3,
                    label = "Tannin",
                    valueLabel = "${tanninScore.roundToInt()} / 5",
                    modifier = Modifier.padding(top = 8.dp),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(c.surface.default)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AuraText(text = "Slider Enabled", style = t.bodyMedium)
                AuraSwitch(
                    checked = sliderEnabled,
                    onCheckedChange = { sliderEnabled = it },
                    size = AuraSwitchSize.Small,
                )
            }
        }
    }

    val ratingPane: @Composable () -> Unit = {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            AuraText(text = "AuraRatingBar · Star & Half-Star", style = t.titleMedium)

            AuraCard(
                variant = AuraCardVariant.Elevated,
                padding = AuraCardPadding.Medium,
                modifier = Modifier.fillMaxWidth(),
            ) {
                AuraRatingBar(
                    rating = enjoymentRating,
                    onRatingChange = { enjoymentRating = it },
                    allowHalfRating = allowHalfRating,
                    size = ratingSize,
                    enabled = ratingEnabled,
                    label = "Overall Enjoyment Score (Tap or Drag)",
                )
            }

            AuraText(text = "Star Size", style = t.labelLarge)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AuraRatingBarSize.entries.forEach { size ->
                    AuraChipButton(
                        label = size.name,
                        selected = ratingSize == size,
                        variant = AuraChipVariant.Filter,
                        size = AuraChipSize.Small,
                        onClick = { ratingSize = size },
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(c.surface.default)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AuraText(text = "Half-Star Precision (0.5)", style = t.bodyMedium)
                AuraSwitch(
                    checked = allowHalfRating,
                    onCheckedChange = { allowHalfRating = it },
                    size = AuraSwitchSize.Small,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(c.surface.default)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AuraText(text = "Rating Enabled", style = t.bodyMedium)
                AuraSwitch(
                    checked = ratingEnabled,
                    onCheckedChange = { ratingEnabled = it },
                    size = AuraSwitchSize.Small,
                )
            }

            AuraCard(
                variant = AuraCardVariant.Outlined,
                padding = AuraCardPadding.Medium,
                modifier = Modifier.fillMaxWidth(),
            ) {
                AuraText(text = "Read-Only Card Summary", style = t.labelLarge, color = c.content.secondary)
                AuraRatingBar(
                    rating = enjoymentRating,
                    onRatingChange = null,
                    size = AuraRatingBarSize.Small,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = { AuraText(text = title, style = t.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = c.background.primary,
                    titleContentColor = c.content.primary,
                    navigationIconContentColor = c.content.primary,
                ),
                navigationIcon = {
                    AuraIconButton(
                        onClick = onBack,
                        variant = AuraIconButtonVariant.Ghost,
                        contentDescription = "Back",
                    ) {
                        AuraIcon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        AuraAdaptiveTwoPane(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            primaryPane = if (initialFocusRating) ratingPane else sliderPane,
            secondaryPane = if (initialFocusRating) sliderPane else ratingPane,
        )
    }
}
