package com.example.ds.preview.ui

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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.ds.component.AuraAdaptiveTwoPane
import com.example.ds.component.AuraCard
import com.example.ds.component.AuraCardPadding
import com.example.ds.component.AuraCardVariant
import com.example.ds.component.AuraChipButton
import com.example.ds.component.AuraChipSize
import com.example.ds.component.AuraChipVariant
import com.example.ds.component.AuraDateTimeMode
import com.example.ds.component.AuraDateTimePicker
import com.example.ds.component.AuraIcon
import com.example.ds.component.AuraIconButton
import com.example.ds.component.AuraIconButtonVariant
import com.example.ds.component.AuraScaffold
import com.example.ds.component.AuraSwitch
import com.example.ds.component.AuraSwitchSize
import com.example.ds.component.AuraText
import com.example.ds.foundation.theme.AuraTheme

private fun twoDigits(v: Int): String = if (v < 10) "0$v" else v.toString()

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DateTimePickerPreviewScreen(
    onBack: () -> Unit,
) {
    var mode by remember { mutableStateOf(AuraDateTimeMode.DateTime) }
    var year by remember { mutableIntStateOf(2026) }
    var month by remember { mutableIntStateOf(11) }
    var day by remember { mutableIntStateOf(15) }
    var hour by remember { mutableIntStateOf(19) }
    var minute by remember { mutableIntStateOf(30) }
    var enabled by remember { mutableStateOf(true) }

    var vintageYear by remember { mutableIntStateOf(2016) }

    val c = AuraTheme.colors
    val t = AuraTheme.typography

    AuraScaffold(
        topBar = {
            TopAppBar(
                title = { AuraText(text = "AuraDateTimePicker", style = t.titleLarge) },
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
            primaryPane = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    AuraText(text = "Mode & Selection State", style = t.titleMedium)

                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AuraDateTimeMode.entries.forEach { m ->
                            AuraChipButton(
                                label = m.name,
                                selected = mode == m,
                                variant = AuraChipVariant.Filter,
                                size = AuraChipSize.Small,
                                onClick = { mode = m },
                            )
                        }
                    }

                    AuraDateTimePicker(
                        selectedYear = year,
                        selectedMonth = month,
                        selectedDay = day,
                        onDateChange = { y, m, d ->
                            year = y
                            month = m
                            day = d
                        },
                        mode = mode,
                        selectedHour = hour,
                        selectedMinute = minute,
                        onTimeChange = { h, m ->
                            hour = h
                            minute = m
                        },
                        enabled = enabled,
                        label = when (mode) {
                            AuraDateTimeMode.Date -> "My Finance · Settlement Date"
                            AuraDateTimeMode.DateTime -> "My Planner · Schedule Block"
                            AuraDateTimeMode.YearVintage -> "My Drink Diary · Harvest Vintage"
                        },
                        supportingText = "Selected: $year-${twoDigits(month)}-${twoDigits(day)}" +
                            if (mode == AuraDateTimeMode.DateTime) " ${twoDigits(hour)}:${twoDigits(minute)}" else "",
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(c.surface.default)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AuraText(text = "Enabled", style = t.bodyMedium)
                        AuraSwitch(
                            checked = enabled,
                            onCheckedChange = { enabled = it },
                            size = AuraSwitchSize.Small,
                        )
                    }
                }
            },
            secondaryPane = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    AuraText(text = "Vintage Grid Mode Showcase", style = t.titleMedium)

                    AuraCard(
                        variant = AuraCardVariant.Filled,
                        padding = AuraCardPadding.Medium,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        AuraText(
                            text = "Bottle Harvest Year: $vintageYear",
                            style = t.titleSmall,
                            color = c.brand.primary,
                        )
                        AuraText(
                            text = "Dedicated 12-year matrix lets wine collectors jump across decades in two taps.",
                            style = t.bodySmall,
                            color = c.content.secondary,
                            modifier = Modifier.padding(top = 4.dp, bottom = 10.dp),
                        )
                        AuraDateTimePicker(
                            selectedYear = vintageYear,
                            selectedMonth = 1,
                            selectedDay = 1,
                            onDateChange = { y, _, _ -> vintageYear = y },
                            mode = AuraDateTimeMode.YearVintage,
                            minYear = 1980,
                            maxYear = 2027,
                        )
                    }
                }
            },
        )
    }
}
