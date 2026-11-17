package com.example.ds.preview.ui.showcase

/** Builds a [ShowcaseStatus.Planned] card for a comparison whose components do not exist yet. */
internal fun planned(
    koA: String,
    koB: String,
    enA: String,
    enB: String,
    useWhen: String,
    avoidWhen: String,
): ShowcaseComparison = ShowcaseComparison(
    ko = listOf(koA, koB),
    en = listOf(enA, enB),
    status = ShowcaseStatus.Planned,
    useWhen = useWhen,
    avoidWhen = avoidWhen,
)
