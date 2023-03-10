package com.sanyavertolet.mandelbrot.backend.counter

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*

import com.sanyavertolet.mandelbrot.backend.Function
import com.sanyavertolet.mandelbrot.backend.Painter
import com.sanyavertolet.mandelbrot.backend.cartesianProduct
import com.sanyavertolet.mandelbrot.backend.cartesianToComplex
import com.sanyavertolet.mandelbrot.backend.listUntil

/**
 * [Counter] that calculates all the values synchronously
 */
class SerialCounter(
    private val painter: Painter,
    function: Function,
    maxIterations: Int = MAX_ITERATIONS,
    borderValue: Double = BORDER_VALUE,
) : Counter(
    function,
    maxIterations,
    borderValue,
    painter
) {
    override fun paintFractalPixels(
        pixelSize: Size,
        complexRect: Rect,
        isSmooth: Boolean,
        applyPixel: (Int, Int, Int) -> Unit
    ): Unit = cartesianProduct(
        listUntil(pixelSize.width.toInt()),
        listUntil(pixelSize.height.toInt())
    )
        .map { (x, y) ->
            val currentPoint = cartesianToComplex(x, y, pixelSize, complexRect)
            val finalIteration = calculate(currentPoint, isSmooth)
            applyPixel(x, y, painter(finalIteration).toArgb())
        }
        .let { Unit }
}
