package com.example.ds.component

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.example.ds.foundation.motion.AuraMotion
import com.example.ds.foundation.motion.AuraMotionDefaults
import com.example.ds.foundation.theme.AuraTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.math.abs

internal object AdaptiveBarThresholds {
    val CollapseThreshold: Dp = 160.dp
    val HideScrollDelta: Dp = 24.dp
    val RevealScrollDelta: Dp = 12.dp
    val JitterSlop: Dp = 12.dp
    val CompactHeightBreakpoint: Dp = 480.dp
    val TabletMaxFloatingWidth: Dp = 640.dp
    val FloatingHorizontalMargin: Dp = 16.dp
    val FloatingTopOffset: Dp = 8.dp
    val FloatingCornerRadius: Dp = 28.dp
    val FloatingTwoTierCornerRadius: Dp = 24.dp
    val ExpandedExtraHeight: Dp = 56.dp
    val CollapsedSingleLineHeight: Dp = 56.dp
    val CollapsedTwoTierHeight: Dp = 104.dp
    val CompactDockedHeight: Dp = 48.dp
    val FloatingBlurRadius: Dp = 26.dp
}

/**
 * Pure deterministic phase resolver implementing `FAB-01`–`FAB-09` and Edge Case 1.
 */
internal fun resolveAdaptiveFloatingBarPhase(
    scrollOffsetPx: Float,
    maxScrollOffsetPx: Float,
    collapseThresholdPx: Float,
    accumulatedDeltaPx: Float,
    hideDeltaPx: Float,
    revealDeltaPx: Float,
    jitterSlopPx: Float,
    isFocused: Boolean,
    isMenuOpen: Boolean,
    isImeVisible: Boolean,
    windowHeightDp: Int,
    currentPhase: AdaptiveFloatingBarPhase,
): AdaptiveFloatingBarPhase {
    // Edge Case 1: Non-scrollable or short screens (< 160dp scrollable range) stay docked.
    if (maxScrollOffsetPx.isFinite() && maxScrollOffsetPx < collapseThresholdPx) {
        return AdaptiveFloatingBarPhase.DockedExpanded
    }

    // FAB-09: Compact Docked when IME is visible or vertical viewport height < 480dp.
    if (isImeVisible || windowHeightDp < 480) {
        return AdaptiveFloatingBarPhase.CompactDocked
    }

    val safeOffset = scrollOffsetPx.coerceAtLeast(0f)

    // FAB-01 / FAB-07: At top-of-content, dock expanded flat header.
    if (safeOffset <= 0.5f) {
        return AdaptiveFloatingBarPhase.DockedExpanded
    }

    // FAB-02 / FAB-07: Inside 0dp..160dp morphing zone.
    if (safeOffset < collapseThresholdPx) {
        return AdaptiveFloatingBarPhase.CollapsingTransition
    }

    // FAB-08: Search focus or open action menu pins the floating bar visible.
    if (isFocused || isMenuOpen) {
        return AdaptiveFloatingBarPhase.FloatingPinned
    }

    // FAB-05: Filter micro-jitter below Touch Slop (12dp).
    if (abs(accumulatedDeltaPx) < jitterSlopPx) {
        return when (currentPhase) {
            AdaptiveFloatingBarPhase.FloatingHidden -> AdaptiveFloatingBarPhase.FloatingHidden
            else -> AdaptiveFloatingBarPhase.FloatingVisible
        }
    }

    // FAB-03: Sustained downward scroll beyond +24dp hides the floating chrome.
    if (accumulatedDeltaPx > hideDeltaPx) {
        return AdaptiveFloatingBarPhase.FloatingHidden
    }

    // FAB-04: Reverse upward scroll beyond -12dp immediately reveals the floating pill.
    if (accumulatedDeltaPx < -revealDeltaPx) {
        return AdaptiveFloatingBarPhase.FloatingVisible
    }

    return when (currentPhase) {
        AdaptiveFloatingBarPhase.FloatingHidden -> AdaptiveFloatingBarPhase.FloatingHidden
        else -> AdaptiveFloatingBarPhase.FloatingVisible
    }
}

/**
 * Computes `[0f, 1f]` collapse progress over `[0dp, collapseThresholdPx]`.
 */
internal fun calculateCollapseProgress(
    scrollOffsetPx: Float,
    collapseThresholdPx: Float,
): Float {
    if (collapseThresholdPx <= 0f || scrollOffsetPx <= 0f) return 0f
    return (scrollOffsetPx / collapseThresholdPx).coerceIn(0f, 1f)
}

/**
 * FAB-06 snap/settle decision on release (`ACTION_UP` / fling end):
 * completes to `1f` when `progress >= 0.5f`, or springs back to `0f` when `< 0.5f`.
 */
internal fun calculateSnapTargetProgress(progress: Float): Float =
    if (progress >= 0.5f) 1f else 0f

/**
 * CompositionLocal providing the active [AuraAdaptiveBarState] from [FloatingGlassScaffold]
 * to [AuraTopAppBar] and [FloatingBottomIsland] for same-frame synchronized show/hide.
 */
val LocalAuraAdaptiveBarState = compositionLocalOf<AuraAdaptiveBarState?> { null }

/**
 * Creates and remembers an [AuraAdaptiveBarState] tuned with `160.dp` collapse threshold,
 * `+24.dp` hide delta, `-12.dp` reveal delta, and `12.dp` jitter slop.
 */
@Composable
fun rememberAuraAdaptiveBarState(
    collapseThreshold: Dp = AdaptiveBarThresholds.CollapseThreshold,
    hideScrollDelta: Dp = AdaptiveBarThresholds.HideScrollDelta,
    revealScrollDelta: Dp = AdaptiveBarThresholds.RevealScrollDelta,
    jitterSlop: Dp = AdaptiveBarThresholds.JitterSlop,
): AuraAdaptiveBarState = rememberAuraAdaptiveBarState(
    collapseThreshold = collapseThreshold,
    hideScrollDelta = hideScrollDelta,
    revealScrollDelta = revealScrollDelta,
    jitterSlop = jitterSlop,
    minimizeOnScrollDown = false,
)

/**
 * Creates and remembers an [AuraAdaptiveBarState] with iOS 26 `.tabBarMinimizeBehavior(.onScrollDown)`
 * fluid compacting support when [minimizeOnScrollDown] is `true`.
 */
@Composable
fun rememberAuraAdaptiveBarState(
    collapseThreshold: Dp = AdaptiveBarThresholds.CollapseThreshold,
    hideScrollDelta: Dp = AdaptiveBarThresholds.HideScrollDelta,
    revealScrollDelta: Dp = AdaptiveBarThresholds.RevealScrollDelta,
    jitterSlop: Dp = AdaptiveBarThresholds.JitterSlop,
    minimizeOnScrollDown: Boolean,
): AuraAdaptiveBarState {
    val density = LocalDensity.current
    val motion = AuraTheme.motion
    val window = AuraTheme.window
    val scope = rememberCoroutineScope()
    val imeBottom = WindowInsets.ime.getBottom(density)
    val isImeVisible = imeBottom > 0

    val thresholdPx = with(density) { collapseThreshold.toPx().coerceAtLeast(1f) }
    val hidePx = with(density) { hideScrollDelta.toPx().coerceAtLeast(1f) }
    val revealPx = with(density) { revealScrollDelta.toPx().coerceAtLeast(1f) }
    val slopPx = with(density) { jitterSlop.toPx().coerceAtLeast(0f) }

    val state = remember(thresholdPx, hidePx, revealPx, slopPx, scope) {
        AuraAdaptiveBarState(
            collapseThresholdPx = thresholdPx,
            hideDeltaPx = hidePx,
            revealDeltaPx = revealPx,
            jitterSlopPx = slopPx,
            coroutineScope = scope,
            initialMotion = motion,
            initialWindowHeightDp = window.heightDp,
            initialMinimizeOnScrollDown = minimizeOnScrollDown,
        )
    }

    SideEffect {
        state.minimizeOnScrollDown = minimizeOnScrollDown
        state.updateEnvironment(
            motion = motion,
            windowHeightDp = window.heightDp,
            isImeVisible = isImeVisible,
        )
    }
    return state
}

/**
 * State & scroll coordinator for [AuraTopAppBarScrollBehavior.AdaptiveFloating] and synchronized
 * [FloatingBottomIsland] transitions (`FAB-01`–`FAB-09`).
 */
@Stable
class AuraAdaptiveBarState internal constructor(
    internal val collapseThresholdPx: Float,
    internal val hideDeltaPx: Float,
    internal val revealDeltaPx: Float,
    internal val jitterSlopPx: Float,
    private val coroutineScope: CoroutineScope? = null,
    initialMotion: AuraMotion = AuraMotionDefaults.Standard,
    initialWindowHeightDp: Int = 800,
    initialMinimizeOnScrollDown: Boolean = false,
) {
    private var motion: AuraMotion = initialMotion
    private var hideAnimJob: Job? = null
    private var morphAnimJob: Job? = null

    private val hideAnimatable = Animatable(0f)
    private val settledMorphAnimatable = Animatable(0f)

    var minimizeOnScrollDown: Boolean by mutableStateOf(initialMinimizeOnScrollDown)

    var scrollOffsetPx: Float by mutableFloatStateOf(0f)
        private set

    var maxScrollOffsetPx: Float by mutableFloatStateOf(Float.POSITIVE_INFINITY)
        private set

    var phase: AdaptiveFloatingBarPhase by mutableStateOf(AdaptiveFloatingBarPhase.DockedExpanded)
        private set

    var isFocused: Boolean by mutableStateOf(false)
        private set

    var isMenuOpen: Boolean by mutableStateOf(false)
        private set

    var isImeVisible: Boolean by mutableStateOf(false)
        private set

    var windowHeightDp: Int by mutableIntStateOf(initialWindowHeightDp)
        private set

    internal var isExternallyBound: Boolean = false
        private set

    private var accumulatedDeltaPx: Float = 0f
    private var isUserDragging: Boolean by mutableStateOf(false)
    private var useSettledMorph: Boolean by mutableStateOf(false)

    val isScrollableContent: Boolean
        get() = !maxScrollOffsetPx.isFinite() || maxScrollOffsetPx >= collapseThresholdPx

    /** Continuous `[0f, 1f]` collapse progress over `0dp..160dp`. */
    val collapseProgress: Float
        get() = if (!isScrollableContent) {
            0f
        } else {
            calculateCollapseProgress(scrollOffsetPx, collapseThresholdPx)
        }

    /** Visual morph progress (`0f` = Docked, `1f` = Floating Pill), honoring `FAB-06` settle. */
    val morphFraction: Float
        get() = when {
            phase == AdaptiveFloatingBarPhase.CompactDocked -> 0f
            !isScrollableContent -> 0f
            phase == AdaptiveFloatingBarPhase.DockedExpanded -> 0f
            phase == AdaptiveFloatingBarPhase.CollapsingTransition && useSettledMorph ->
                settledMorphAnimatable.value.coerceIn(0f, 1f)
            phase == AdaptiveFloatingBarPhase.CollapsingTransition -> collapseProgress
            else -> 1f
        }

    /** Synchronized hide progress (`0f` = visible, `1f` = hidden) shared by Top Bar and Bottom Island. */
    val hideFraction: Float
        get() = hideAnimatable.value.coerceIn(0f, 1f)

    /**
     * iOS 26 `.tabBarMinimizeBehavior(.onScrollDown)` fluid collapse fraction (`0f` = full expanded island,
     * `1f` = compact minimized pill) when [minimizeOnScrollDown] is enabled.
     */
    val minimizeFraction: Float
        get() = if (minimizeOnScrollDown) hideFraction else 0f

    val isPinned: Boolean
        get() = isFocused || isMenuOpen

    /**
     * Fluidly restores the bottom island and top bar from a minimized state (e.g. when the user taps
     * a minimized tab capsule).
     */
    fun expandFromMinimized() {
        accumulatedDeltaPx = 0f
        if (phase == AdaptiveFloatingBarPhase.FloatingHidden) {
            phase = AdaptiveFloatingBarPhase.FloatingVisible
            animateHideTo(0f, isAppearing = true)
        }
    }

    internal fun updateEnvironment(
        motion: AuraMotion,
        windowHeightDp: Int,
        isImeVisible: Boolean,
    ) {
        this.motion = motion
        val envChanged = this.windowHeightDp != windowHeightDp || this.isImeVisible != isImeVisible
        this.windowHeightDp = windowHeightDp
        this.isImeVisible = isImeVisible
        if (envChanged) {
            recomputePhase(animated = false)
        }
    }

    fun updatePinnedFlags(
        focused: Boolean = this.isFocused,
        menuOpen: Boolean = this.isMenuOpen,
    ) {
        if (isFocused == focused && isMenuOpen == menuOpen) return
        isFocused = focused
        isMenuOpen = menuOpen
        recomputePhase(animated = true)
    }

    fun updateScrollBounds(
        scrollOffsetPx: Float,
        maxScrollOffsetPx: Float = this.maxScrollOffsetPx,
    ) {
        val prevOffset = this.scrollOffsetPx
        this.scrollOffsetPx = scrollOffsetPx.coerceAtLeast(0f)
        this.maxScrollOffsetPx = maxScrollOffsetPx
        if (!isUserDragging && abs(this.scrollOffsetPx - prevOffset) > 0.5f) {
            useSettledMorph = false
        }
        recomputePhase(animated = true)
    }

    internal fun markExternallyBound(bound: Boolean) {
        isExternallyBound = bound
    }

    fun reset() {
        accumulatedDeltaPx = 0f
        scrollOffsetPx = 0f
        maxScrollOffsetPx = Float.POSITIVE_INFINITY
        isUserDragging = false
        useSettledMorph = false
        phase = AdaptiveFloatingBarPhase.DockedExpanded
        snapHideTo(0f)
        snapMorphTo(0f)
    }

    internal fun onScrollDelta(deltaYPx: Float, isFromUserDrag: Boolean) {
        if (abs(deltaYPx) < 0.01f) return
        isUserDragging = isFromUserDrag
        useSettledMorph = false

        // Reset directional accumulator when scroll direction reverses.
        if ((accumulatedDeltaPx > 0f && deltaYPx < 0f) || (accumulatedDeltaPx < 0f && deltaYPx > 0f)) {
            accumulatedDeltaPx = 0f
        }
        accumulatedDeltaPx += deltaYPx

        if (!isExternallyBound) {
            scrollOffsetPx = (scrollOffsetPx + deltaYPx).coerceAtLeast(0f)
        }

        // During drag above threshold, drive continuous hide fraction beyond jitter slop for FAB-06 snap.
        if (isFromUserDrag &&
            scrollOffsetPx >= collapseThresholdPx &&
            isScrollableContent &&
            !isPinned &&
            !isImeVisible &&
            windowHeightDp >= 480
        ) {
            val absDelta = abs(accumulatedDeltaPx)
            if (absDelta >= jitterSlopPx) {
                val span = (hideDeltaPx - jitterSlopPx).coerceAtLeast(1f)
                if (accumulatedDeltaPx > 0f && phase != AdaptiveFloatingBarPhase.FloatingHidden) {
                    val dragP = ((accumulatedDeltaPx - jitterSlopPx) / span).coerceIn(0f, 1f)
                    snapHideTo(dragP)
                } else if (accumulatedDeltaPx < 0f && phase == AdaptiveFloatingBarPhase.FloatingHidden) {
                    val revealSpan = (revealDeltaPx).coerceAtLeast(1f)
                    val dragP = (1f - ((absDelta - jitterSlopPx) / revealSpan)).coerceIn(0f, 1f)
                    snapHideTo(dragP)
                }
            }
        }

        recomputePhase(animated = true)
    }

    internal fun onDragOrFlingEnd() {
        isUserDragging = false
        accumulatedDeltaPx = 0f

        if (phase == AdaptiveFloatingBarPhase.FloatingVisible || phase == AdaptiveFloatingBarPhase.FloatingHidden) {
            if (!hideAnimatable.isRunning) {
                val targetHide = when {
                    phase == AdaptiveFloatingBarPhase.FloatingHidden && hideFraction <= 0.01f -> 1f
                    phase == AdaptiveFloatingBarPhase.FloatingVisible && hideFraction >= 0.99f -> 0f
                    else -> calculateSnapTargetProgress(hideFraction)
                }
                phase = if (targetHide >= 0.5f) {
                    AdaptiveFloatingBarPhase.FloatingHidden
                } else {
                    AdaptiveFloatingBarPhase.FloatingVisible
                }
                animateHideTo(targetHide, isAppearing = targetHide < 0.5f)
            }
        } else if (phase == AdaptiveFloatingBarPhase.CollapsingTransition) {
            val targetMorph = calculateSnapTargetProgress(collapseProgress)
            useSettledMorph = true
            snapMorphTo(collapseProgress)
            animateSettledMorphTo(targetMorph)
        }
    }

    private fun recomputePhase(animated: Boolean) {
        val next = resolveAdaptiveFloatingBarPhase(
            scrollOffsetPx = scrollOffsetPx,
            maxScrollOffsetPx = maxScrollOffsetPx,
            collapseThresholdPx = collapseThresholdPx,
            accumulatedDeltaPx = accumulatedDeltaPx,
            hideDeltaPx = hideDeltaPx,
            revealDeltaPx = revealDeltaPx,
            jitterSlopPx = jitterSlopPx,
            isFocused = isFocused,
            isMenuOpen = isMenuOpen,
            isImeVisible = isImeVisible,
            windowHeightDp = windowHeightDp,
            currentPhase = phase,
        )

        val prevPhase = phase
        phase = next

        val targetHide = when (next) {
            AdaptiveFloatingBarPhase.FloatingHidden -> 1f
            AdaptiveFloatingBarPhase.DockedExpanded,
            AdaptiveFloatingBarPhase.CollapsingTransition,
            AdaptiveFloatingBarPhase.FloatingVisible,
            AdaptiveFloatingBarPhase.FloatingPinned,
            AdaptiveFloatingBarPhase.CompactDocked -> 0f
        }

        if (!isUserDragging || next != prevPhase || next != AdaptiveFloatingBarPhase.FloatingVisible) {
            if (animated && coroutineScope != null) {
                if (!isUserDragging || targetHide == 0f || next == AdaptiveFloatingBarPhase.FloatingHidden) {
                    animateHideTo(targetHide, isAppearing = targetHide < 0.5f)
                }
            } else {
                snapHideTo(targetHide)
            }
        }
    }

    private fun snapHideTo(value: Float) {
        hideAnimJob?.cancel()
        if (coroutineScope != null) {
            hideAnimJob = coroutineScope.launch {
                hideAnimatable.snapTo(value.coerceIn(0f, 1f))
            }
        } else {
            runCatching {
                kotlinx.coroutines.runBlocking { hideAnimatable.snapTo(value.coerceIn(0f, 1f)) }
            }
        }
    }

    private fun animateHideTo(target: Float, isAppearing: Boolean) {
        val scope = coroutineScope ?: return snapHideTo(target)
        hideAnimJob?.cancel()
        hideAnimJob = scope.launch {
            val spec = if (isAppearing) {
                motion.floatingBarAppearSpec<Float>()
            } else {
                motion.floatingBarDisappearSpec<Float>()
            }
            hideAnimatable.animateTo(target.coerceIn(0f, 1f), animationSpec = spec)
        }
    }

    private fun snapMorphTo(value: Float) {
        morphAnimJob?.cancel()
        if (coroutineScope != null) {
            morphAnimJob = coroutineScope.launch {
                settledMorphAnimatable.snapTo(value.coerceIn(0f, 1f))
            }
        } else {
            runCatching {
                kotlinx.coroutines.runBlocking { settledMorphAnimatable.snapTo(value.coerceIn(0f, 1f)) }
            }
        }
    }

    private fun animateSettledMorphTo(target: Float) {
        val scope = coroutineScope ?: return snapMorphTo(target)
        morphAnimJob?.cancel()
        morphAnimJob = scope.launch {
            settledMorphAnimatable.animateTo(
                targetValue = target.coerceIn(0f, 1f),
                animationSpec = motion.adaptiveBarMorphSpring(),
            )
        }
    }

    val nestedScrollConnection: NestedScrollConnection = object : NestedScrollConnection {
        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource,
        ): Offset {
            // Compose screen coordinates: scrolling content down yields negative consumed.y.
            val deltaY = -consumed.y
            if (abs(deltaY) > 0.01f) {
                onScrollDelta(
                    deltaYPx = deltaY,
                    isFromUserDrag = source == NestedScrollSource.UserInput,
                )
            }
            return Offset.Zero
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            onDragOrFlingEnd()
            return Velocity.Zero
        }
    }
}

/**
 * Binds a [ScrollState] to [adaptiveState] so exact scroll offset `Y` and `maxScrollOffset`
 * (Edge Case 1 short-content detection) drive [AdaptiveFloatingBarPhase] accurately.
 */
@Composable
fun BindAdaptiveBarToScrollState(
    scrollState: ScrollState,
    adaptiveState: AuraAdaptiveBarState? = LocalAuraAdaptiveBarState.current,
) {
    LaunchedEffect(scrollState, adaptiveState) {
        if (adaptiveState == null) return@LaunchedEffect
        adaptiveState.markExternallyBound(true)
        snapshotFlow {
            val maxVal = if (scrollState.maxValue == Int.MAX_VALUE) {
                Float.POSITIVE_INFINITY
            } else {
                scrollState.maxValue.toFloat()
            }
            scrollState.value.toFloat() to maxVal
        }
            .distinctUntilChanged()
            .collect { (offset, maxOffset) ->
                adaptiveState.updateScrollBounds(
                    scrollOffsetPx = offset,
                    maxScrollOffsetPx = maxOffset,
                )
            }
    }
}

/**
 * Binds a [LazyListState] to [adaptiveState] for exact top offset `Y` and short-list detection.
 */
@Composable
fun BindAdaptiveBarToLazyListState(
    listState: LazyListState,
    adaptiveState: AuraAdaptiveBarState? = LocalAuraAdaptiveBarState.current,
) {
    LaunchedEffect(listState, adaptiveState) {
        if (adaptiveState == null) return@LaunchedEffect
        adaptiveState.markExternallyBound(true)
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val visible = layoutInfo.visibleItemsInfo
            val offsetPx = if (listState.firstVisibleItemIndex == 0) {
                listState.firstVisibleItemScrollOffset.toFloat()
            } else {
                val avgItemHeight = if (visible.isNotEmpty()) {
                    visible.sumOf { it.size }.toFloat() / visible.size.toFloat()
                } else {
                    adaptiveState.collapseThresholdPx
                }
                (listState.firstVisibleItemIndex * avgItemHeight) + listState.firstVisibleItemScrollOffset.toFloat()
            }

            val maxScrollPx = when {
                !listState.canScrollForward && listState.firstVisibleItemIndex == 0 ->
                    listState.firstVisibleItemScrollOffset.toFloat()
                visible.isNotEmpty() && visible.size == layoutInfo.totalItemsCount -> {
                    val contentSpan = (visible.last().offset + visible.last().size) - visible.first().offset +
                        layoutInfo.beforeContentPadding + layoutInfo.afterContentPadding
                    val viewportSpan = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
                    (contentSpan - viewportSpan).coerceAtLeast(0).toFloat()
                }
                else -> Float.POSITIVE_INFINITY
            }
            offsetPx to maxScrollPx
        }
            .distinctUntilChanged()
            .collect { (offset, maxOffset) ->
                adaptiveState.updateScrollBounds(
                    scrollOffsetPx = offset,
                    maxScrollOffsetPx = maxOffset,
                )
            }
    }
}

/**
 * Binds a [LazyGridState] to [adaptiveState] for exact top offset `Y` and short-grid detection.
 */
@Composable
fun BindAdaptiveBarToLazyGridState(
    gridState: LazyGridState,
    adaptiveState: AuraAdaptiveBarState? = LocalAuraAdaptiveBarState.current,
) {
    LaunchedEffect(gridState, adaptiveState) {
        if (adaptiveState == null) return@LaunchedEffect
        adaptiveState.markExternallyBound(true)
        snapshotFlow {
            val layoutInfo = gridState.layoutInfo
            val visible = layoutInfo.visibleItemsInfo
            val offsetPx = if (gridState.firstVisibleItemIndex == 0) {
                gridState.firstVisibleItemScrollOffset.toFloat()
            } else {
                val firstRowHeight = visible.firstOrNull()?.size?.height?.toFloat()
                    ?: adaptiveState.collapseThresholdPx
                adaptiveState.collapseThresholdPx.coerceAtLeast(firstRowHeight) +
                    gridState.firstVisibleItemScrollOffset.toFloat()
            }

            val maxScrollPx = when {
                !gridState.canScrollForward && gridState.firstVisibleItemIndex == 0 ->
                    gridState.firstVisibleItemScrollOffset.toFloat()
                visible.isNotEmpty() && visible.size == layoutInfo.totalItemsCount -> {
                    val firstTop = visible.minOf { it.offset.y }
                    val lastBottom = visible.maxOf { it.offset.y + it.size.height }
                    val contentSpan = (lastBottom - firstTop) +
                        layoutInfo.beforeContentPadding + layoutInfo.afterContentPadding
                    val viewportSpan = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
                    (contentSpan - viewportSpan).coerceAtLeast(0).toFloat()
                }
                else -> Float.POSITIVE_INFINITY
            }
            offsetPx to maxScrollPx
        }
            .distinctUntilChanged()
            .collect { (offset, maxOffset) ->
                adaptiveState.updateScrollBounds(
                    scrollOffsetPx = offset,
                    maxScrollOffsetPx = maxOffset,
                )
            }
    }
}
