package io.github.bluemarlins.aura.preview.ui.showcase

import androidx.compose.runtime.Composable

enum class ShowcaseStatus { Ready, Planned }

/**
 * One "A vs B" card in a showcase group.
 *
 * [ko] / [en] hold the compared names in order; more than one name renders as "A vs B".
 * [specimen] is null for [ShowcaseStatus.Planned] cards, which render a placeholder instead.
 * [targetCatalogId] links the card to an existing component preview screen when set.
 */
data class ShowcaseComparison(
    val ko: List<String>,
    val en: List<String>,
    val status: ShowcaseStatus,
    val useWhen: String,
    val avoidWhen: String,
    val targetCatalogId: String? = null,
    val specimen: (@Composable () -> Unit)? = null,
)

data class ShowcaseGroup(
    val id: String,
    val title: String,
    val subtitle: String,
    val comparisons: List<ShowcaseComparison>,
)

object ShowcaseGroups {
    val all: List<ShowcaseGroup> = listOf(
        NotifyShowcase,
        SelectShowcase,
        InputShowcase,
        ActionShowcase,
        ProgressShowcase,
        ContainShowcase,
    )

    fun byId(id: String): ShowcaseGroup? = all.find { it.id == id }
}
