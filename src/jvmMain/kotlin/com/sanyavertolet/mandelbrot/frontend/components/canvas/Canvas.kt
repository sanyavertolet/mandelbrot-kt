/**
 * Unified canvas that should be reused on specific fractal render
 */

package com.sanyavertolet.mandelbrot.frontend.components.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.sanyavertolet.mandelbrot.backend.complex.Complex
import com.sanyavertolet.mandelbrot.backend.fractal.AbstractFractal
import com.sanyavertolet.mandelbrot.frontend.components.StateUpdater
import org.slf4j.LoggerFactory
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

private val logger = LoggerFactory.getLogger(object {}.javaClass.enclosingClass)

private typealias ImageUpdater = suspend (AbstractFractal?, Complex, ImageBitmap?, Size?, Rect, StateUpdater<ImageBitmap?>) -> Unit

/**
 * Get default [Modifier] for [fractalCanvas]
 *
 * @param screenSize canvas [Size] in pixels
 * @param complexRect complex rectangle corresponding to current screen
 * @param updateOffset callback to update offset
 * @param updateScale callback to update scale
 * @return default [Modifier] for [fractalCanvas]
 */
@Composable
fun defaultCanvasModifier(
    screenSize: Size?,
    complexRect: Rect,
    updateOffset: (Offset) -> Unit,
    updateScale: (Int) -> Unit,
) = Modifier.fillMaxSize().onScrollPointerEvent(updateScale).onMovePointerEvent(screenSize, complexRect, updateOffset)

/**
 * [Canvas] that is suitable for fractal rendering and future re-rendering
 *
 * @param fractal currently built fractal
 * @param constant [Complex] constant that makes sense only for Julia set
 * @param imageUpdater fractal points strategy
 * @param drawImage fractal drawing strategy
 */
@Composable
@OptIn(ExperimentalTime::class)
internal fun fractalCanvas(
    fractal: AbstractFractal?,
    constant: Complex,
    imageUpdater: ImageUpdater,
    drawImage: DrawScope.(ImageBitmap?) -> Unit,
) {
    var complexRect by remember { mutableStateOf(AbstractFractal.defaultComplexRect) }
    val updateComplexRect: ((Rect) -> Rect) -> Unit = { complexRect = it(complexRect) }

    var screenSize: Size? by remember { mutableStateOf(null) }
    var image: ImageBitmap? by remember { mutableStateOf(null) }
    val updateImage: suspend () -> Unit = {
        measureTime {
            imageUpdater(fractal, constant, image, screenSize, complexRect) { image = it(image) }
        }
            .also { logger.debug("Fractal rendering took $it") }
    }

    val updateScale = scaleEffect(updateComplexRect, updateImage = updateImage)
    val updateOffset = offsetEffect(updateComplexRect, updateImage)
    mainEffect(fractal, constant, screenSize, updateComplexRect, updateImage)

    Canvas(modifier = defaultCanvasModifier(screenSize, complexRect, updateOffset, updateScale)) {
        screenSize = size
        image ?: run { image = ImageBitmap(size.width.toInt(), size.height.toInt()) }
        drawImage(image)
    }
}
