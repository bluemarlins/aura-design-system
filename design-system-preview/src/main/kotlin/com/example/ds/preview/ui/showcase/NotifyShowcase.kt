package com.example.ds.preview.ui.showcase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ds.component.AuraBadge
import com.example.ds.component.AuraBadgeSize
import com.example.ds.component.AuraBadgeVariant
import com.example.ds.component.AuraButton
import com.example.ds.component.AuraButtonSize
import com.example.ds.component.AuraButtonVariant
import com.example.ds.component.AuraCard
import com.example.ds.component.AuraCardPadding
import com.example.ds.component.AuraCardVariant
import com.example.ds.component.AuraSnackbar
import com.example.ds.component.AuraSnackbarVariant
import com.example.ds.component.AuraText
import com.example.ds.foundation.theme.AuraTheme
import com.example.ds.preview.catalog.ComponentCatalog

/** How to tell the user something: transient, blocking, or ambient. */
val NotifyShowcase = ShowcaseGroup(
    id = ComponentCatalog.SHOWCASE_NOTIFY_ID,
    title = "사용자에게 알리기",
    subtitle = "작업 결과, 오류, 안내를 전달하는 방법을 방해 정도(transient → blocking)로 비교합니다.",
    comparisons = listOf(
        ShowcaseComparison(
            ko = listOf("스낵바", "다이얼로그"),
            en = listOf("Snackbar", "Dialog"),
            status = ShowcaseStatus.Ready,
            useWhen = "스낵바: 흐름을 막지 않는 결과 안내와 되돌리기. 다이얼로그: 결정이 있어야 진행되는 경우.",
            avoidWhen = "스낵바에 필수 결정을 넣지 않기. 다이얼로그를 단순 완료 안내에 쓰지 않기.",
            targetCatalogId = ComponentCatalog.SNACKBAR_ID,
            specimen = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    AuraSnackbar(message = "저장했어요", actionLabel = "되돌리기", onActionClick = {})
                    AuraCard(variant = AuraCardVariant.Elevated, padding = AuraCardPadding.Compact) {
                        AuraText(text = "삭제할까요?", style = AuraTheme.typography.titleSmall)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AuraButton(
                                text = "취소",
                                onClick = {},
                                variant = AuraButtonVariant.Outline,
                                size = AuraButtonSize.Small,
                            )
                            AuraButton(
                                text = "삭제",
                                onClick = {},
                                variant = AuraButtonVariant.Destructive,
                                size = AuraButtonSize.Small,
                            )
                        }
                    }
                }
            },
        ),
        ShowcaseComparison(
            ko = listOf("스낵바 상태 변형"),
            en = listOf("Snackbar variants"),
            status = ShowcaseStatus.Ready,
            useWhen = "결과의 성격(중립·성공·경고·오류)을 색으로 즉시 구분해야 할 때.",
            avoidWhen = "색만으로 의미를 전달하지 않기. 항상 메시지 텍스트를 함께 쓰기.",
            targetCatalogId = ComponentCatalog.SNACKBAR_ID,
            specimen = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    AuraSnackbar(message = "복사했어요", variant = AuraSnackbarVariant.Success)
                    AuraSnackbar(message = "연결이 불안정해요", variant = AuraSnackbarVariant.Warning)
                    AuraSnackbar(message = "저장하지 못했어요", variant = AuraSnackbarVariant.Error)
                }
            },
        ),
        ShowcaseComparison(
            ko = listOf("뱃지 점", "뱃지 숫자"),
            en = listOf("Dot badge", "Count badge"),
            status = ShowcaseStatus.Ready,
            useWhen = "점: 새 항목이 있다는 사실만 알릴 때. 숫자: 개수가 판단에 도움이 될 때.",
            avoidWhen = "중요한 오류를 뱃지로만 알리지 않기.",
            targetCatalogId = ComponentCatalog.BADGE_ID,
            specimen = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AuraBadge(size = AuraBadgeSize.Dot, variant = AuraBadgeVariant.Error)
                    AuraBadge(count = 2, variant = AuraBadgeVariant.Error)
                    AuraBadge(count = 120, variant = AuraBadgeVariant.Brand)
                }
            },
        ),
        planned("토스트", "스낵바", "Toast", "Snackbar", "토스트: 액션 없는 짧은 확인. 스낵바: 되돌리기 같은 액션이 필요할 때.", "토스트에 액션 버튼을 넣지 않기."),
        planned("모달", "바텀시트", "Modal", "Bottom sheet", "모달: 짧고 중요한 결정. 바텀시트: 맥락을 유지한 채 선택지·상세를 보여줄 때.", "긴 폼을 모달에 넣지 않기."),
        planned("배너", "스낵바", "Banner", "Snackbar", "배너: 화면에 머무는 상태 안내. 스낵바: 일시적 결과.", "사라져도 되는 정보를 배너로 고정하지 않기."),
        planned("툴팁", "팝오버", "Tooltip", "Popover", "툴팁: 아이콘 라벨 같은 한 줄 설명. 팝오버: 조작이 포함된 작은 패널.", "툴팁에 필수 정보를 숨기지 않기."),
        planned("인라인 오류", "다이얼로그", "Inline error", "Dialog", "인라인: 입력 필드 옆에서 바로 고칠 수 있는 오류. 다이얼로그: 진행 불가 상황.", "필드 검증 오류를 다이얼로그로 띄우지 않기."),
        planned("코치마크", "툴팁", "Coachmark", "Tooltip", "코치마크: 처음 사용하는 기능의 단계별 안내. 툴팁: 상시 보조 설명.", "코치마크를 반복 노출하지 않기."),
    ),
)
