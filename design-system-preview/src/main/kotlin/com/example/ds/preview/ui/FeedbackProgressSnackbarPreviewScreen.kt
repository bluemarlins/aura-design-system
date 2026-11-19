package com.example.ds.preview.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ds.component.AuraButton
import com.example.ds.component.AuraButtonSize
import com.example.ds.component.AuraButtonVariant
import com.example.ds.component.AuraCard
import com.example.ds.component.AuraCardPadding
import com.example.ds.component.AuraCardVariant
import com.example.ds.component.AuraChipButton
import com.example.ds.component.AuraChipSize
import com.example.ds.component.AuraChipVariant
import com.example.ds.component.AuraIcon
import com.example.ds.component.AuraIconButton
import com.example.ds.component.AuraIconButtonVariant
import com.example.ds.component.AuraFloatingActionBar
import com.example.ds.component.AuraLinearProgressBar
import com.example.ds.component.AuraSkeleton
import com.example.ds.component.AuraSkeletonText
import com.example.ds.component.AuraProgressVariant
import com.example.ds.component.AuraScaffold
import com.example.ds.component.AuraSnackbarDuration
import com.example.ds.component.AuraSnackbarHost
import com.example.ds.component.AuraSnackbarResult
import com.example.ds.component.AuraSnackbarVariant
import com.example.ds.component.AuraStepProgressBar
import com.example.ds.component.AuraText
import com.example.ds.component.rememberAuraSnackbarHostState
import com.example.ds.foundation.theme.AuraMaterial
import com.example.ds.foundation.theme.AuraTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FeedbackProgressSnackbarPreviewScreen(
    onBack: () -> Unit,
    initialFocusSnackbar: Boolean = false,
) {
    val c = AuraTheme.colors
    val t = AuraTheme.typography
    val layout = AuraTheme.layout
    val snackbarHostState = rememberAuraSnackbarHostState()
    val scope = rememberCoroutineScope()

    var progress by remember { mutableFloatStateOf(0.65f) }
    var currentStep by remember { mutableIntStateOf(2) }
    var selectedProgressVariant by remember { mutableStateOf(AuraProgressVariant.Brand) }
    var snackbarMaterial by remember { mutableStateOf(AuraMaterial.Solid) }
    var lastSnackbarResult by remember { mutableStateOf("Tap a trigger to show AuraSnackbar.") }

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = {
                    AuraText(
                        text = if (initialFocusSnackbar) "AuraSnackbar" else "AuraProgressBar",
                        style = t.titleLarge,
                    )
                },
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = c.background.primary,
                    titleContentColor = c.content.primary,
                    navigationIconContentColor = c.content.primary,
                ),
            )
        },
        snackbarHost = {
            AuraSnackbarHost(
                hostState = snackbarHostState,
                material = snackbarMaterial,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = layout.screenMarginHorizontal,
                    vertical = layout.screenMarginVertical,
                ),
            verticalArrangement = Arrangement.spacedBy(layout.sectionSpacing),
        ) {
            val progressSection: @Composable () -> Unit = {
                AuraCard(
                    variant = AuraCardVariant.Outlined,
                    padding = AuraCardPadding.Large,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        AuraText(text = "AuraLinearProgressBar", style = t.titleMedium)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AuraProgressVariant.entries.forEach { variant ->
                                AuraChipButton(
                                    label = variant.name,
                                    selected = selectedProgressVariant == variant,
                                    variant = AuraChipVariant.Filter,
                                    size = AuraChipSize.Small,
                                    onClick = { selectedProgressVariant = variant },
                                )
                            }
                        }
                        AuraLinearProgressBar(
                            progress = progress,
                            variant = selectedProgressVariant,
                            contentDescription = "${(progress * 100).toInt()} percent complete",
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AuraButton(
                                text = "-20%",
                                onClick = { progress = (progress - 0.2f).coerceIn(0f, 1f) },
                                variant = AuraButtonVariant.Outline,
                                size = AuraButtonSize.Small,
                            )
                            AuraButton(
                                text = "+20%",
                                onClick = { progress = (progress + 0.2f).coerceIn(0f, 1f) },
                                variant = AuraButtonVariant.Secondary,
                                size = AuraButtonSize.Small,
                            )
                        }
                    }
                }

                AuraCard(
                    variant = AuraCardVariant.Outlined,
                    padding = AuraCardPadding.Large,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        AuraText(text = "AuraStepProgressBar ($currentStep / 5)", style = t.titleMedium)
                        AuraStepProgressBar(
                            currentStep = currentStep,
                            totalSteps = 5,
                            variant = selectedProgressVariant,
                            contentDescription = "Step $currentStep of 5",
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AuraButton(
                                text = "Prev Step",
                                onClick = { currentStep = (currentStep - 1).coerceAtLeast(0) },
                                variant = AuraButtonVariant.Outline,
                                size = AuraButtonSize.Small,
                            )
                            AuraButton(
                                text = "Next Step",
                                onClick = { currentStep = (currentStep + 1).coerceAtMost(5) },
                                variant = AuraButtonVariant.Secondary,
                                size = AuraButtonSize.Small,
                            )
                        }
                    }
                }

                AuraCard(
                    variant = AuraCardVariant.Outlined,
                    padding = AuraCardPadding.Large,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        AuraText(text = "AuraSkeleton", style = t.titleMedium)
                        AuraText(
                            text = "Shown until the first load lands, instead of a false empty state.",
                            style = t.bodySmall,
                            color = c.content.secondary,
                        )
                        AuraSkeleton(
                            modifier = Modifier.fillMaxWidth().height(72.dp),
                            shape = AuraTheme.shapes.large,
                            contentDescription = "Loading preview",
                        )
                        AuraSkeletonText(lines = 3)
                    }
                }

                AuraCard(
                    variant = AuraCardVariant.Outlined,
                    padding = AuraCardPadding.Large,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        AuraText(text = "AuraFloatingActionBar", style = t.titleMedium)
                        AuraText(
                            text = "Task-screen dock: keeps Save reachable while the form scrolls.",
                            style = t.bodySmall,
                            color = c.content.secondary,
                        )
                        AuraFloatingActionBar(
                            supportingText = if (progress < 1f) "Fill the bar to enable Save" else null,
                        ) {
                            AuraButton(
                                text = "Cancel",
                                onClick = {},
                                variant = AuraButtonVariant.Ghost,
                                size = AuraButtonSize.Large,
                            )
                            AuraButton(
                                text = "Save",
                                onClick = {},
                                enabled = progress >= 1f,
                                size = AuraButtonSize.Large,
                                fullWidth = true,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }

            val snackbarSection: @Composable () -> Unit = {
                AuraCard(
                    variant = AuraCardVariant.Outlined,
                    padding = AuraCardPadding.Large,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        AuraText(text = "AuraSnackbar", style = t.titleMedium)
                        AuraText(text = lastSnackbarResult, style = t.bodySmall, color = c.content.secondary)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(AuraMaterial.Solid, AuraMaterial.Glass).forEach { mat ->
                                AuraChipButton(
                                    label = mat.name,
                                    selected = snackbarMaterial == mat,
                                    variant = AuraChipVariant.Filter,
                                    size = AuraChipSize.Small,
                                    onClick = { snackbarMaterial = mat },
                                )
                            }
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            AuraButton(
                                text = "Show Neutral Snackbar (With Action)",
                                onClick = {
                                    scope.launch {
                                        val result = snackbarHostState.showSnackbar(
                                            message = "Tasting note saved to cellar.",
                                            actionLabel = "Undo",
                                            duration = AuraSnackbarDuration.Short,
                                            variant = AuraSnackbarVariant.Neutral,
                                        )
                                        lastSnackbarResult = "Last result: $result"
                                    }
                                },
                                fullWidth = true,
                            )
                            AuraButton(
                                text = "Show Success Snackbar",
                                onClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Backup exported cleanly.",
                                            variant = AuraSnackbarVariant.Success,
                                        )
                                    }
                                },
                                variant = AuraButtonVariant.Secondary,
                                fullWidth = true,
                            )
                            AuraButton(
                                text = "Show Warning Snackbar",
                                onClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Low cellar inventory for selected bottle.",
                                            variant = AuraSnackbarVariant.Warning,
                                        )
                                    }
                                },
                                variant = AuraButtonVariant.Outline,
                                fullWidth = true,
                            )
                            AuraButton(
                                text = "Show Error Snackbar",
                                onClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Could not load photo from storage.",
                                            actionLabel = "Retry",
                                            variant = AuraSnackbarVariant.Error,
                                        )
                                    }
                                },
                                variant = AuraButtonVariant.Destructive,
                                fullWidth = true,
                            )
                        }
                    }
                }
            }

            if (initialFocusSnackbar) {
                snackbarSection()
                progressSection()
            } else {
                progressSection()
                snackbarSection()
            }
        }
    }
}
