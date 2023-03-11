/**
 * File that contains pointer event handler configurations
 */

package com.sanyavertolet.mandelbrot.frontend.components.canvas

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.positionChange
import com.sanyavertolet.mandelbrot.common.pixelsToComplex

/**
 * Applies handling [PointerEventType.Scroll] event updating scale with [updateScale] callback
 *
 * @param updateScale callback to update scale, that should be received from [scaleEffect]
 * @return [Modifier] with [PointerEventType.Scroll] event handler applied
 */
@Composable
@OptIn(ExperimentalComposeUiApi::class)
@Suppress("MAGIC_NUMBER")
fun Modifier.onScrollPointerEvent(updateScale: (Int) -> Unit) = onPointerEvent(PointerEventType.Scroll) { event ->
    updateScale(event.changes.first().scrollDelta.y
        .times(10).toInt())
}

/**
 * Applies handling [PointerEventType.Move] event updating [Offset] with [updateOffset] callback
 *
 * @param screenSize canvas [Size] in pixels
 * @param complexRect complex rectangle corresponding to current screen
 * @param updateOffset callback to update [Offset], that should be received from [offsetEffect]
 * @return [Modifier] with [PointerEventType.Scroll] event handler applied
 */
@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.onMovePointerEvent(screenSize: Size?, complexRect: Rect, updateOffset: (Offset) -> Unit) = onPointerEvent(PointerEventType.Move) { event ->
    if (event.buttons.isPrimaryPressed) {
        screenSize?.let { pixelSize -> updateOffset(-event.changes.first().positionChange().pixelsToComplex(pixelSize, complexRect.size)) }
    }
}
