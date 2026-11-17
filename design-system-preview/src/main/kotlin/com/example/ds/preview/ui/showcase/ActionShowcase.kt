package com.example.ds.preview.ui.showcase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.ds.component.AuraButton
import com.example.ds.component.AuraButtonSize
import com.example.ds.component.AuraButtonVariant
import com.example.ds.component.AuraChipButton
import com.example.ds.component.AuraChipSize
import com.example.ds.component.AuraChipVariant
import com.example.ds.component.AuraFab
import com.example.ds.component.AuraFabSize
import com.example.ds.component.AuraIcon
import com.example.ds.component.AuraIconButton
import com.example.ds.component.AuraIconButtonVariant
import com.example.ds.preview.catalog.ComponentCatalog

/** How to invite an action: emphasis ladder and button family. */
val ActionShowcase = ShowcaseGroup(
    id = ComponentCatalog.SHOWCASE_ACTION_ID,
    title = "행동 유도하기",
    subtitle = "강조 단계와 형태에 따라 버튼 계열을 고릅니다.",
    comparisons = listOf(
        ShowcaseComparison(
            ko = listOf("버튼 위계"),
            en = listOf("Button emphasis"),
            status = ShowcaseStatus.Ready,
            useWhen = "Primary는 화면당 하나, 보조 행동은 Secondary·Outline·Ghost, 되돌릴 수 없는 행동은 Destructive.",
            avoidWhen = "Primary를 한 화면에 여러 개 두지 않기.",
            targetCatalogId = ComponentCatalog.BUTTON_ID,
            specimen = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        AuraButton(text = "확인", onClick = {}, size = AuraButtonSize.Small)
                        AuraButton(text = "보조", onClick = {}, variant = AuraButtonVariant.Secondary, size = AuraButtonSize.Small)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        AuraButton(text = "취소", onClick = {}, variant = AuraButtonVariant.Outline, size = AuraButtonSize.Small)
                        AuraButton(text = "건너뛰기", onClick = {}, variant = AuraButtonVariant.Ghost, size = AuraButtonSize.Small)
                        AuraButton(text = "삭제", onClick = {}, variant = AuraButtonVariant.Destructive, size = AuraButtonSize.Small)
                    }
                }
            },
        ),
        ShowcaseComparison(
            ko = listOf("버튼", "아이콘 버튼", "FAB"),
            en = listOf("Button", "Icon button", "FAB"),
            status = ShowcaseStatus.Ready,
            useWhen = "버튼: 텍스트로 설명되는 행동. 아이콘 버튼: 익숙한 보조 행동. FAB: 화면의 대표 생성 행동 하나.",
            avoidWhen = "의미가 모호한 아이콘을 라벨 없이 쓰지 않기. FAB을 화면에 둘 이상 두지 않기.",
            targetCatalogId = ComponentCatalog.FAB_ID,
            specimen = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AuraButton(text = "저장", onClick = {}, size = AuraButtonSize.Small)
                    AuraIconButton(onClick = {}, variant = AuraIconButtonVariant.Tonal, contentDescription = "편집") {
                        AuraIcon(imageVector = Icons.Filled.Edit, contentDescription = null)
                    }
                    AuraFab(onClick = {}, size = AuraFabSize.Small, contentDescription = "추가", icon = {
                        AuraIcon(imageVector = Icons.Filled.Add, contentDescription = null)
                    })
                }
            },
        ),
        ShowcaseComparison(
            ko = listOf("어시스트 칩", "버튼"),
            en = listOf("Assist chip", "Button"),
            status = ShowcaseStatus.Ready,
            useWhen = "어시스트 칩: 현재 맥락에서 제안되는 가벼운 바로가기. 버튼: 화면의 핵심 행동.",
            avoidWhen = "핵심 CTA를 칩으로 만들지 않기.",
            targetCatalogId = ComponentCatalog.CHIP_BUTTON_ID,
            specimen = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AuraChipButton(label = "길 안내", onClick = {}, variant = AuraChipVariant.Assist, size = AuraChipSize.Small)
                    AuraButton(text = "예약하기", onClick = {}, size = AuraButtonSize.Small)
                }
            },
        ),
    ),
)
