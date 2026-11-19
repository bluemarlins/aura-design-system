package com.example.ds.preview.ui.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ds.component.AuraButton
import com.example.ds.component.AuraButtonSize
import com.example.ds.component.AuraButtonVariant
import com.example.ds.component.AuraCard
import com.example.ds.component.AuraCardPadding
import com.example.ds.component.AuraCardVariant
import com.example.ds.component.AuraDivider
import com.example.ds.component.AuraDividerTone
import com.example.ds.component.AuraText
import com.example.ds.foundation.theme.AuraTheme
import com.example.ds.preview.catalog.ComponentCatalog

/** How to group and separate content. */
val ContainShowcase = ShowcaseGroup(
    id = ComponentCatalog.SHOWCASE_CONTAIN_ID,
    title = "묶고 구분하기",
    subtitle = "콘텐츠를 묶는 면(surface)과 구분선, 모달의 역할을 비교합니다.",
    comparisons = listOf(
        ShowcaseComparison(
            ko = listOf("카드 변형"),
            en = listOf("Card variants"),
            status = ShowcaseStatus.Ready,
            useWhen = "Elevated: 떠 있는 핵심 콘텐츠. Filled: 배경과 구분되는 보조 영역. Outlined: 밀도 높은 목록. Glass: 배경 이미지 위.",
            avoidWhen = "한 목록에서 변형을 섞어 위계를 흐리지 않기.",
            targetCatalogId = ComponentCatalog.CARD_ID,
            specimen = {
                // Filled cards use the recessed fill, so they need a raised surface to stay visible.
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AuraTheme.colors.surface.raised, AuraTheme.shapes.medium)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        AuraCard(
                            modifier = Modifier.weight(1f),
                            variant = AuraCardVariant.Elevated,
                            padding = AuraCardPadding.Compact,
                        ) { AuraText(text = "Elevated", style = AuraTheme.typography.labelMedium) }
                        AuraCard(
                            modifier = Modifier.weight(1f),
                            variant = AuraCardVariant.Filled,
                            padding = AuraCardPadding.Compact,
                        ) { AuraText(text = "Filled", style = AuraTheme.typography.labelMedium) }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        AuraCard(
                            modifier = Modifier.weight(1f),
                            variant = AuraCardVariant.Outlined,
                            padding = AuraCardPadding.Compact,
                        ) { AuraText(text = "Outlined", style = AuraTheme.typography.labelMedium) }
                        AuraCard(
                            modifier = Modifier.weight(1f),
                            variant = AuraCardVariant.Glass,
                            padding = AuraCardPadding.Compact,
                        ) { AuraText(text = "Glass", style = AuraTheme.typography.labelMedium) }
                    }
                }
            },
        ),
        ShowcaseComparison(
            ko = listOf("구분선", "여백"),
            en = listOf("Divider", "Spacing"),
            status = ShowcaseStatus.Ready,
            useWhen = "구분선: 같은 면 안에서 성격이 다른 항목을 나눌 때. 여백: 관련 그룹 사이를 느슨하게 나눌 때.",
            avoidWhen = "모든 항목 사이에 구분선을 넣어 화면을 복잡하게 만들지 않기.",
            targetCatalogId = ComponentCatalog.DIVIDER_ID,
            specimen = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    AuraText(text = "항목 A", style = AuraTheme.typography.bodyMedium)
                    AuraDivider(tone = AuraDividerTone.Default)
                    AuraText(text = "항목 B", style = AuraTheme.typography.bodyMedium)
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        AuraText(text = "항목 C", style = AuraTheme.typography.bodyMedium)
                    }
                }
            },
        ),
        ShowcaseComparison(
            ko = listOf("카드", "다이얼로그"),
            en = listOf("Card", "Dialog"),
            status = ShowcaseStatus.Ready,
            useWhen = "카드: 화면 안에서 관련 정보를 묶을 때. 다이얼로그: 흐름을 멈추고 결정을 받을 때.",
            avoidWhen = "카드를 모달처럼 띄우지 않기. 다이얼로그를 정보 나열에 쓰지 않기.",
            targetCatalogId = ComponentCatalog.DIALOG_ID,
            specimen = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    AuraCard(variant = AuraCardVariant.Outlined, padding = AuraCardPadding.Compact) {
                        AuraText(text = "주문 요약", style = AuraTheme.typography.labelLarge)
                    }
                    AuraCard(variant = AuraCardVariant.Elevated, padding = AuraCardPadding.Compact) {
                        AuraText(text = "주문을 취소할까요?", style = AuraTheme.typography.labelLarge)
                        AuraButton(
                            text = "취소하기",
                            onClick = {},
                            variant = AuraButtonVariant.Destructive,
                            size = AuraButtonSize.Small,
                        )
                    }
                }
            },
        ),
    ),
)
