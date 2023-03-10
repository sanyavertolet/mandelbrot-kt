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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
     * @param currentPoint
     * @param isSmooth
     * @return "smooth" iteration when [function] calculated iteratively overflows the [borderValue]
     */
    fun calculate(currentPoint: Complex, isSmooth: Boolean): Double {
        var value = function.getStartingValue(currentPoint)
        var iteration = 0
        val constant = function.getConstant(currentPoint)
        while (value.sqr() < borderValue && iteration < maxIterations) {
            value = function(value, constant)
            iteration += 1
        }
        return if (isSmooth) {
            iteration.toDouble() - log2(max(1.0, log2(value.abs())))
        } else {
            iteration.toDouble()
        }
    }

    /**
     * @param pixelSize
     * @param complexRect
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
     * @param pixelSize
     * @param complexRect
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
     * @param complexRect
     * @param isSmooth
     * @return [Flow] of [Pixel]s
     */
    fun getPixelsToPaint(bitmap: ImageBitmap, complexRect: Rect, isSmooth: Boolean = true): Flow<Pixel> = flow {
        cartesianProduct(
            listUntil(bitmap.width),
            listUntil(bitmap.height)
        ).map { (x, y) ->
            val currentPoint = cartesianToComplex(x, y, Size(bitmap.width.toFloat(), bitmap.height.toFloat()), complexRect)
            emit(Pixel(x, y, painter(calculate(currentPoint, isSmooth))))
        }
    }
    companion object {
        const val BORDER_VALUE = 3.0
        const val MAX_ITERATIONS = 100
    }
}
