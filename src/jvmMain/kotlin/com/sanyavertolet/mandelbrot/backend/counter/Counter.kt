package com.sanyavertolet.mandelbrot.backend.counter

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*

import com.sanyavertolet.mandelbrot.Pixel
import com.sanyavertolet.mandelbrot.backend.*
import com.sanyavertolet.mandelbrot.backend.Function
import com.sanyavertolet.mandelbrot.backend.complex.Complex

import kotlin.math.log2
import kotlin.math.max
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Abstract counter that calculates all the values for image
 */
abstract class Counter(
    private val function: Function,
    private val maxIterations: Int,
    private val borderValue: Double,
    private val painter: Painter
) {
    /**
     * [CoroutineDispatcher] that is used for fractal calculations
     */
    protected abstract val dispatcher: CoroutineDispatcher

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
        paintFractalPixels(pixelSize, complexRect, isSmooth) { x, y, argbColor ->
            setRGB(x, y, argbColor)
        }
    }.toComposeImageBitmap()

    /**
     * @param pixelSize canvas [Size] in pixels
     * @param complexRect complex rectangle corresponding to current screen
     * @param isSmooth
     * @param applyPixel
     */
    protected abstract fun paintFractalPixels(
        pixelSize: Size,
        complexRect: Rect,
        isSmooth: Boolean = true,
        applyPixel: (Int, Int, Int) -> Unit,
    )

    /**
     * WIP
     *
     * @param bitmap
     * @param complexRect complex rectangle corresponding to current screen
     * @param isSmooth
     * @return [Flow] of [Pixel]s
     */
    fun getPixelsToPaint(bitmap: ImageBitmap, complexRect: Rect, isSmooth: Boolean = true): Flow<Pixel> = flow {
        cartesianProduct(
            listUntil(bitmap.width),
            listUntil(bitmap.height),
        ).map { (x, y) ->
            val currentPoint = cartesianToComplex(x, y, Size(bitmap.width.toFloat(), bitmap.height.toFloat()), complexRect)
            emit(Pixel(x, y, painter(calculate(currentPoint, isSmooth))))
        }
    }.flowOn(dispatcher).buffer()

    companion object {
        /**
         * Value that should be considered as [Complex] number border - when complex number overflows this value, it is not in set
         */
        const val DEFAULT_BORDER_VALUE = 3.0

        /**
         * Maximum amount of fractal point calculation iterations
         */
        const val DEFAULT_MAX_ITERATIONS = 200
    }
}
