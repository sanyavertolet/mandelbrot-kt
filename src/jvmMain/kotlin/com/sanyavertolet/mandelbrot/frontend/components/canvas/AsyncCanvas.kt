/**
 * Canvas that doesn't wait until all the Bitmap is prepared and renders each point separately
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

import kotlinx.coroutines.flow.*

private val emptyBitmap = ImageBitmap(2, 2)

private fun DrawScope.asyncDraw(image: ImageBitmap?) = drawIntoCanvas { canvas ->
    canvas.withSave {
        canvas.drawImage(
            image ?: emptyBitmap,
            Offset.Zero,
            Paint(),
        )
    }
}

/**
 * [Canvas] that is suitable for fractal rendering and future re-rendering with async strategy
 *
 * @param fractal currently built fractal
 * @param constant [Complex] constant that makes sense only for Julia set
 * @return [Unit]
 */
@Composable
fun asyncCanvas(fractal: AbstractFractal?, constant: Complex) = fractalCanvas(fractal, constant, ::asyncImageUpdater, DrawScope::asyncDraw)

@Suppress("TOO_MANY_PARAMETERS")
private suspend fun asyncImageUpdater(
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
            image?.let { bitmap ->
                fractal.getPixelsToPaint(bitmap, complexRect)
            }
        }
        ?.map { pixel ->
            updateImage { image?.toAwtImage()?.apply { setRGB(pixel.x, pixel.y, pixel.color.toArgb()) }?.toComposeImageBitmap() }
        }
        ?.collect()
}
