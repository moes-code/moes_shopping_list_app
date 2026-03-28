package com.moes_code.moes_shopping_list_app.view.components

import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlin.math.roundToInt

/**
 * Represents the current swipe state of a SwipeableItem
 */
enum class SwipeState {
    /** Item is at rest position */
    Settled,
    /** Item has been swiped to the left (typically delete action) */
    SwipedLeft,
    /** Item has been swiped to the right (typically edit action) */
    SwipedRight
}

/**
 * Represents the current swipe direction while dragging
 */
enum class SwipeDirection {
    None,
    Left,
    Right
}

/**
 * A composable that provides swipe-to-action functionality using the modern
 * AnchoredDraggable API instead of the deprecated SwipeToDismissBox.
 *
 * @param onSwipeLeft Callback triggered when item is fully swiped left
 * @param onSwipeRight Callback triggered when item is fully swiped right
 * @param modifier Modifier to be applied to the container
 * @param swipeThreshold The fraction of the swipe distance that must be reached to trigger action (0.0-1.0)
 * @param enableSwipeLeft Whether left swipe is enabled
 * @param enableSwipeRight Whether right swipe is enabled
 * @param resetTrigger When this value changes, the swipe state will be reset to Settled. Useful for resetting after dialogs close.
 * @param backgroundContent Content displayed behind the item while swiping, receives current direction
 * @param content The main content of the swipeable item
 */
@Composable
fun SwipeableItem(
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier,
    swipeThreshold: Float = 0.35f,
    enableSwipeLeft: Boolean = true,
    enableSwipeRight: Boolean = true,
    resetTrigger: Any? = null,
    backgroundContent: @Composable BoxScope.(SwipeDirection) -> Unit,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    
    // Swipe distance in pixels (using a fixed distance for consistent feel)
    val swipeDistancePx = with(density) { 120.dp.toPx() }
    
    // Create state with simple constructor
    val state = rememberSaveable(saver = AnchoredDraggableState.Saver()) {
        AnchoredDraggableState(initialValue = SwipeState.Settled)
    }
    
    // Reset swipe state when resetTrigger changes (e.g., when dialogs close)
    LaunchedEffect(resetTrigger) {
        if (resetTrigger != null && state.currentValue != SwipeState.Settled) {
            state.animateTo(SwipeState.Settled)
        }
    }
    
    // Update anchors when enabled directions change
    SideEffect {
        state.updateAnchors(
            DraggableAnchors {
                SwipeState.Settled at 0f
                if (enableSwipeRight) {
                    SwipeState.SwipedRight at swipeDistancePx
                }
                if (enableSwipeLeft) {
                    SwipeState.SwipedLeft at -swipeDistancePx
                }
            }
        )
    }
    
    // Handle swipe completion - trigger action and reset
    LaunchedEffect(state) {
        snapshotFlow { state.currentValue }
            .filter { it != SwipeState.Settled }
            .collectLatest { value ->
                when (value) {
                    SwipeState.SwipedLeft -> {
                        onSwipeLeft()
                        state.animateTo(SwipeState.Settled)
                    }
                    SwipeState.SwipedRight -> {
                        onSwipeRight()
                        state.animateTo(SwipeState.Settled)
                    }
                    SwipeState.Settled -> { /* Should not happen due to filter */ }
                }
            }
    }
    
    // Derive current swipe direction from offset (using safe access with NaN check)
    val currentDirection by remember {
        derivedStateOf {
            val offset = state.offset
            when {
                offset.isNaN() -> SwipeDirection.None
                offset > 10f -> SwipeDirection.Right
                offset < -10f -> SwipeDirection.Left
                else -> SwipeDirection.None
            }
        }
    }
    
    Box(modifier = modifier) {
        // Background layer (revealed when swiping) - matches the size of foreground
        // Uses matchParentSize() to match the foreground content's size
        Box(modifier = Modifier.matchParentSize()) {
            backgroundContent(currentDirection)
        }
        
        // Foreground content (draggable) - determines the size of the container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { 
                    val currentOffset = state.offset.takeIf { !it.isNaN() } ?: 0f
                    IntOffset(currentOffset.roundToInt(), 0) 
                }
                .anchoredDraggable(
                    state = state,
                    orientation = Orientation.Horizontal,
                    flingBehavior = AnchoredDraggableDefaults.flingBehavior(
                        state = state,
                        positionalThreshold = { distance -> distance * swipeThreshold }
                    )
                )
        ) {
            content()
        }
    }
}
