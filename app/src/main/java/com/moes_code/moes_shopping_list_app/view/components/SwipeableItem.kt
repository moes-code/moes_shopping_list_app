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
import com.moes_code.moes_shopping_list_app.view.theme.Dimensions
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlin.math.roundToInt

enum class SwipeState {
    Settled,
    SwipedLeft,
    SwipedRight
}

enum class SwipeDirection {
    None,
    Left,
    Right
}

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
    
    // Swipe distance in pixels
    val swipeDistancePx = with(density) { Dimensions.swipeDistance.toPx() }
    val swipeThresholdPx = with(density) { Dimensions.swipeDirectionThreshold.toPx() }
    
    // Create state with simple constructor
    val state = rememberSaveable(saver = AnchoredDraggableState.Saver()) {
        AnchoredDraggableState(initialValue = SwipeState.Settled)
    }
    
    // Reset swipe state when resetTrigger changes
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
    
    // Handle swipe completion
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
                    SwipeState.Settled -> { }
                }
            }
    }
    
    // Derive current swipe direction from offset
    val currentDirection by remember {
        derivedStateOf {
            val offset = state.offset
            when {
                offset.isNaN() -> SwipeDirection.None
                offset > swipeThresholdPx -> SwipeDirection.Right
                offset < -swipeThresholdPx -> SwipeDirection.Left
                else -> SwipeDirection.None
            }
        }
    }
    
    Box(modifier = modifier) {
        // Background layer
        Box(modifier = Modifier.matchParentSize()) {
            backgroundContent(currentDirection)
        }
        
        // Foreground content
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
