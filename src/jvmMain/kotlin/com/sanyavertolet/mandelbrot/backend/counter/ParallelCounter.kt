package com.sanyavertolet.mandelbrot.backend.counter

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*

import com.sanyavertolet.mandelbrot.backend.Function
import com.sanyavertolet.mandelbrot.backend.Painter
import com.sanyavertolet.mandelbrot.backend.cartesianProduct
import com.sanyavertolet.mandelbrot.backend.cartesianToComplex
import com.sanyavertolet.mandelbrot.backend.listUntil

import kotlinx.coroutines.*

/**
 * [Counter] that calculates all the values asynchronously (even in multithreaded mode)
 */
@Suppress("MAGIC_NUMBER")
class ParallelCounter(
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
    @OptIn(DelicateCoroutinesApi::class)
    private val threadPool = newFixedThreadPoolContext(8, javaClass.name)
    private val scope: CoroutineScope = CoroutineScope(threadPool)

    override fun paintFractalPixels(
        pixelSize: Size,
        complexRect: Rect,
        isSmooth: Boolean,
        applyPixel: (Int, Int, Int) -> Unit
    ) = cartesianProduct(
        listUntil(pixelSize.width.toInt()),
        listUntil(pixelSize.height.toInt())
    )
        .map { (x, y) ->
            val currentPoint = cartesianToComplex(x, y, pixelSize, complexRect)
            scope.async {
                val finalIteration = calculate(currentPoint, isSmooth)
                applyPixel(x, y, painter(finalIteration).toArgb())
            }
        }
        .let { Unit }
}
