package io.github.bluemarlins.aura.preview.ui.showcase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.bluemarlins.aura.component.AuraCard
import io.github.bluemarlins.aura.component.AuraCardPadding
import io.github.bluemarlins.aura.component.AuraCardVariant
import io.github.bluemarlins.aura.component.AuraNumberField
import io.github.bluemarlins.aura.component.AuraSearchBar
import io.github.bluemarlins.aura.component.AuraSlider
import io.github.bluemarlins.aura.component.AuraTextField
import io.github.bluemarlins.aura.component.AuraTextFieldVariant
import io.github.bluemarlins.aura.preview.catalog.ComponentCatalog

/** How to take a value from the user: free text, numbers, ranges, or search. */
val InputShowcase = ShowcaseGroup(
    id = ComponentCatalog.SHOWCASE_INPUT_ID,
    title = "값을 입력받기",
    subtitle = "텍스트, 숫자, 범위, 검색처럼 입력의 성격에 맞는 컨트롤을 비교합니다.",
    comparisons = listOf(
        ShowcaseComparison(
            ko = listOf("아웃라인", "채움", "페이퍼리스"),
            en = listOf("Outlined", "Filled", "Paperless"),
            status = ShowcaseStatus.Ready,
            useWhen = "아웃라인: 구조화된 폼·금융 입력. 채움: 밀도 높은 메타데이터. 페이퍼리스: 일기·메모처럼 차분한 글쓰기.",
            avoidWhen = "한 화면에 세 가지 변형을 섞지 않기.",
            targetCatalogId = ComponentCatalog.TEXT_FIELD_ID,
            specimen = {
                var a by remember { mutableStateOf("") }
                var b by remember { mutableStateOf("") }
                var c by remember { mutableStateOf("") }
                // Filled fields use the recessed fill, so they need a raised surface to stay visible.
                AuraCard(
                    modifier = Modifier.fillMaxWidth(),
                    variant = AuraCardVariant.Elevated,
                    padding = AuraCardPadding.Compact,
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        AuraTextField(value = a, onValueChange = { a = it }, placeholder = "Outlined")
                        AuraTextField(value = b, onValueChange = { b = it }, placeholder = "Filled", variant = AuraTextFieldVariant.Filled)
                        AuraTextField(value = c, onValueChange = { c = it }, placeholder = "Paperless", variant = AuraTextFieldVariant.Paperless)
                    }
                }
            },
        ),
        ShowcaseComparison(
            ko = listOf("숫자 필드", "슬라이더"),
            en = listOf("Number field", "Slider"),
            status = ShowcaseStatus.Ready,
            useWhen = "숫자 필드: 정확한 값(금액·수량). 슬라이더: 범위 안에서 대략적인 값을 빠르게 조절.",
            avoidWhen = "정밀한 금액을 슬라이더로 받지 않기.",
            targetCatalogId = ComponentCatalog.NUMBER_FIELD_ID,
            specimen = {
                var amount by remember { mutableStateOf("12") }
                var level by remember { mutableFloatStateOf(0.4f) }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    AuraNumberField(value = amount, onValueChange = { amount = it }, showSteppers = true)
                    AuraSlider(value = level, onValueChange = { level = it })
                }
            },
        ),
        ShowcaseComparison(
            ko = listOf("검색바", "텍스트 필드"),
            en = listOf("Search bar", "Text field"),
            status = ShowcaseStatus.Ready,
            useWhen = "검색바: 목록을 찾거나 필터링. 텍스트 필드: 제출할 데이터를 입력.",
            avoidWhen = "폼 입력 칸을 검색바 모양으로 만들지 않기.",
            targetCatalogId = ComponentCatalog.SEARCH_BAR_ID,
            specimen = {
                var query by remember { mutableStateOf("") }
                var text by remember { mutableStateOf("") }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    AuraSearchBar(query = query, onQueryChange = { query = it })
                    AuraTextField(value = text, onValueChange = { text = it }, label = "이름")
                }
            },
        ),
        planned("스테퍼", "슬라이더", "Stepper", "Slider", "스테퍼: 1씩 늘고 줄이는 작은 정수. 슬라이더: 넓은 범위의 연속 값.", "큰 범위를 스테퍼 버튼으로만 조절하게 하지 않기."),
    ),
)
