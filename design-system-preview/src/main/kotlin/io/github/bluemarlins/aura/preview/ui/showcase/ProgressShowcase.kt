package io.github.bluemarlins.aura.preview.ui.showcase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.bluemarlins.aura.component.AuraBadge
import io.github.bluemarlins.aura.component.AuraBadgeVariant
import io.github.bluemarlins.aura.component.AuraChipButton
import io.github.bluemarlins.aura.component.AuraChipSize
import io.github.bluemarlins.aura.component.AuraLinearProgressBar
import io.github.bluemarlins.aura.component.AuraStepProgressBar
import io.github.bluemarlins.aura.preview.catalog.ComponentCatalog

/** How to show waiting, progress, and status. */
val ProgressShowcase = ShowcaseGroup(
    id = ComponentCatalog.SHOWCASE_PROGRESS_ID,
    title = "기다림·진행 보여주기",
    subtitle = "진행 정도와 상태를 사용자가 예측할 수 있도록 표현 방식을 비교합니다.",
    comparisons = listOf(
        ShowcaseComparison(
            ko = listOf("선형 진행바", "단계 진행바"),
            en = listOf("Linear progress", "Step progress"),
            status = ShowcaseStatus.Ready,
            useWhen = "선형: 업로드처럼 연속적인 진행률. 단계: 가입·결제처럼 정해진 단계 수가 있을 때.",
            avoidWhen = "진행률을 알 수 없는데 임의의 퍼센트를 보여주지 않기.",
            targetCatalogId = ComponentCatalog.PROGRESS_BAR_ID,
            specimen = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    AuraLinearProgressBar(progress = 0.6f)
                    AuraStepProgressBar(currentStep = 2, totalSteps = 4)
                }
            },
        ),
        ShowcaseComparison(
            ko = listOf("뱃지", "칩"),
            en = listOf("Badge", "Chip"),
            status = ShowcaseStatus.Ready,
            useWhen = "뱃지: 읽기 전용 상태·개수 표시. 칩: 눌러서 선택·필터링.",
            avoidWhen = "상태 표시용 뱃지에 터치 동작을 붙이지 않기.",
            targetCatalogId = ComponentCatalog.BADGE_ID,
            specimen = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AuraBadge(label = "진행 중", variant = AuraBadgeVariant.Info)
                    AuraChipButton(label = "진행 중", selected = true, onClick = {}, size = AuraChipSize.Small)
                }
            },
        ),
        planned("스피너", "스켈레톤", "Spinner", "Skeleton", "스피너: 짧고 결과 형태를 알 수 없는 대기. 스켈레톤: 레이아웃이 정해진 콘텐츠 로딩.", "2초 이상 걸리는 목록을 스피너만으로 두지 않기."),
    ),
)
