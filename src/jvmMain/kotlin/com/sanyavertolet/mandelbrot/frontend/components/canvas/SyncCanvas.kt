/**
 * Canvas that waits until all the Bitmap is prepared
 */

package com.sanyavertolet.mandelbrot.frontend.components.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import com.sanyavertolet.mandelbrot.backend.complex.Complex
import com.sanyavertolet.mandelbrot.backend.fractal.AbstractFractal
import com.sanyavertolet.mandelbrot.backend.fractal.JuliaFractal
import com.sanyavertolet.mandelbrot.frontend.components.StateUpdater

/**
 * @param image
 */
fun DrawScope.syncDraw(image: ImageBitmap?) {
    image?.let { completedImage ->
        drawIntoCanvas { canvas ->
            canvas.withSave { canvas.drawImage(completedImage, Offset.Zero, Paint()) }
        }
    }
}

/**
 * [Canvas] that is suitable for fractal rendering and future re-rendering with sync strategy
 *
 * @param fractal current fractal or null if none is selected
 * @param constant [Complex] constant that makes sense only for Julia set
 * @return [Unit]
 */
@Composable
fun syncCanvas(fractal: AbstractFractal?, constant: Complex) = fractalCanvas(fractal, constant, ::syncImageUpdater, DrawScope::syncDraw)

@Suppress("TOO_MANY_PARAMETERS")
private suspend fun syncImageUpdater(
    abstractFractal: AbstractFractal?,
    constant: Complex,
    image: ImageBitmap?,
    screenSize: Size?,
    complexRect: Rect,
    updateImage: StateUpdater<ImageBitmap?>,
) {
    abstractFractal.apply {
        if (this is JuliaFractal) {
            setConstant(constant)
        }
    }
        ?.let { fractal ->
            screenSize?.let { size ->
                updateImage { fractal.getImage(size, complexRect, true) }
            }
        }
}
