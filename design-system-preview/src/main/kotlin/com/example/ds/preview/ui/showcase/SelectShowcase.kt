package com.example.ds.preview.ui.showcase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ds.component.AuraCheckbox
import com.example.ds.component.AuraChipButton
import com.example.ds.component.AuraChipSize
import com.example.ds.component.AuraRadio
import com.example.ds.component.AuraSegmentedButton
import com.example.ds.component.AuraSegmentedSize
import com.example.ds.component.AuraSwitch
import com.example.ds.preview.catalog.ComponentCatalog

/** How to let the user choose: immediate toggles, committed options, or view switches. */
val SelectShowcase = ShowcaseGroup(
    id = ComponentCatalog.SHOWCASE_SELECT_ID,
    title = "사용자가 고르게 하기",
    subtitle = "즉시 적용되는 설정, 저장 후 적용되는 선택, 보기 전환을 구분해서 선택 컨트롤을 고릅니다.",
    comparisons = listOf(
        ShowcaseComparison(
            ko = listOf("스위치", "체크박스"),
            en = listOf("Switch", "Checkbox"),
            status = ShowcaseStatus.Ready,
            useWhen = "스위치: 켜는 즉시 적용되는 설정. 체크박스: 저장·제출 시 함께 반영되는 선택.",
            avoidWhen = "폼 안의 동의 항목에 스위치를 쓰지 않기. 즉시 적용 설정에 체크박스를 쓰지 않기.",
            targetCatalogId = ComponentCatalog.SWITCH_ID,
            specimen = {
                var switchOn by remember { mutableStateOf(true) }
                var checked by remember { mutableStateOf(false) }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AuraSwitch(checked = switchOn, onCheckedChange = { switchOn = it })
                    AuraCheckbox(checked = checked, onCheckedChange = { checked = it })
                }
            },
        ),
        ShowcaseComparison(
            ko = listOf("라디오", "체크박스"),
            en = listOf("Radio", "Checkbox"),
            status = ShowcaseStatus.Ready,
            useWhen = "라디오: 여러 옵션 중 정확히 하나. 체크박스: 0개 이상 복수 선택.",
            avoidWhen = "라디오로 선택 해제를 허용하지 않기. 옵션이 2개뿐인 즉시 설정은 스위치 고려.",
            targetCatalogId = ComponentCatalog.RADIO_ID,
            specimen = {
                var selected by remember { mutableIntStateOf(0) }
                var first by remember { mutableStateOf(true) }
                var second by remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        repeat(2) { index ->
                            AuraRadio(
                                selected = selected == index,
                                onClick = { selected = index },
                                label = "옵션 ${index + 1}",
                            )
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        AuraCheckbox(checked = first, onCheckedChange = { first = it }, label = "항목 A")
                        AuraCheckbox(checked = second, onCheckedChange = { second = it }, label = "항목 B")
                    }
                }
            },
        ),
        ShowcaseComparison(
            ko = listOf("세그먼트", "칩 필터"),
            en = listOf("Segmented", "Filter chip"),
            status = ShowcaseStatus.Ready,
            useWhen = "세그먼트: 2~5개 중 하나로 보기를 즉시 전환. 칩: 많은 필터를 가로로 나열하거나 복수 선택.",
            avoidWhen = "세그먼트를 저장되는 폼 값에 쓰지 않기(BTN-RULE-007). 옵션이 6개 이상이면 칩.",
            targetCatalogId = ComponentCatalog.SEGMENTED_BUTTON_ID,
            specimen = {
                var segment by remember { mutableIntStateOf(1) }
                var chipA by remember { mutableStateOf(true) }
                var chipB by remember { mutableStateOf(false) }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    AuraSegmentedButton(
                        options = listOf("일", "주", "월"),
                        selectedIndex = segment,
                        onOptionSelected = { segment = it },
                        size = AuraSegmentedSize.Small,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AuraChipButton(label = "무료배송", selected = chipA, onClick = { chipA = !chipA }, size = AuraChipSize.Small)
                        AuraChipButton(label = "당일", selected = chipB, onClick = { chipB = !chipB }, size = AuraChipSize.Small)
                    }
                }
            },
        ),
        planned("탭", "세그먼트", "Tabs", "Segmented", "탭: 서로 다른 콘텐츠 영역으로 이동. 세그먼트: 같은 데이터의 보기 방식 전환.", "탭을 필터 용도로 쓰지 않기."),
        planned("드롭다운", "라디오", "Dropdown", "Radio", "드롭다운: 옵션이 많고 공간이 좁을 때. 라디오: 옵션이 5개 이하이고 한눈에 비교해야 할 때.", "옵션이 3개 이하인데 드롭다운으로 숨기지 않기."),
    ),
)
