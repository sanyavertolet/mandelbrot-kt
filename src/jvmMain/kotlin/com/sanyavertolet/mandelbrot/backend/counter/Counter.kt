package com.sanyavertolet.mandelbrot.backend.counter

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*

import com.sanyavertolet.mandelbrot.backend.Function
import com.sanyavertolet.mandelbrot.backend.complex.Complex

import kotlin.math.log2
import kotlin.math.max
import kotlinx.coroutines.*

/**
 * Abstract counter that calculates all the values for image
 */
abstract class Counter(
    private val function: Function,
    private val maxIterations: Int,
    private val borderValue: Double,
) {
    /**
     * [CoroutineDispatcher] that is used for fractal calculations
     */
    protected abstract val dispatcher: CoroutineDispatcher

    /**
     * [CoroutineScope] made with [dispatcher] context
     */
    protected val scope: CoroutineScope by lazy { CoroutineScope(dispatcher) }

    /**
     * @param currentPoint
     * @param isSmooth
     * @return "smooth" iteration when [function] calculated iteratively overflows the [borderValue]
     */
    fun calculate(currentPoint: Complex, isSmooth: Boolean): Double {
        var value = function.getStartingValue(currentPoint)
        var iteration = 1
        val constant = function.getConstant(currentPoint)
        while (value.sqr() < borderValue && iteration < maxIterations) {
            value = function(value, constant)
            iteration += 1
        }
        return if (isSmooth) {
            iteration.toDouble().minus(log2(max(1.0, log2(value.abs())))).takeIf { it >= 0 } ?: 0.0
        } else {
            iteration.toDouble()
        }
    }

    /**
     * @param pixelSize canvas [Size] in pixels
     * @param complexRect complex rectangle corresponding to current screen
     * @param isSmooth
     * @return [ImageBitmap] with fractal that should be displayed on screen with [pixelSize] containing [complexRect]
     */
    fun getImage(
        pixelSize: Size,
        complexRect: Rect,
        isSmooth: Boolean,
    ): ImageBitmap = ImageBitmap(pixelSize.width.toInt(), pixelSize.height.toInt()).toAwtImage().apply {
        runBlocking {
            scope.async {
                paintFractalPixels(pixelSize, complexRect, isSmooth) { x, y, argbColor ->
                    setRGB(x, y, argbColor)
                }
            }.await()
        }
    }.toComposeImageBitmap()

    /**
     * @param pixelSize canvas [Size] in pixels
     * @param complexRect complex rectangle corresponding to current screen
     * @param isSmooth
     * @param applyPixel
     */
    protected abstract suspend fun paintFractalPixels(
        pixelSize: Size,
        complexRect: Rect,
        isSmooth: Boolean = true,
        applyPixel: (Int, Int, Int) -> Unit,
    )

    companion object {
        /**
         * Value that should be considered as [Complex] number border - when complex number overflows this value, it is not in set
         */
        const val DEFAULT_BORDER_VALUE = 3.0

        /**
         * Maximum amount of fractal point calculation iterations
         */
        const val DEFAULT_MAX_ITERATIONS = 100
    }
}
