package com.sanyavertolet.mandelbrot.backend.counter

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap

import com.sanyavertolet.mandelbrot.backend.Function
import com.sanyavertolet.mandelbrot.backend.complex.Complex

/**
 * Abstract counter that calculates all the values for image
 */
abstract class Counter(
    private val function: Function,
    private val maxIterations: Int,
    private val borderValue: Double,
) {
    /**
     * @param currentPoint
     * @return iteration when [function] calculated iteratively overflows the [borderValue]
     */
    fun calculate(currentPoint: Complex): Int {
        var value = function.getStartingValue(currentPoint)
        val constant = function.getConstant(currentPoint)
        repeat(maxIterations) { iteration ->
            value = function(value, constant)
            if (value.abs() > borderValue) {
                return iteration
            }
        }
        return maxIterations
    }

    /**
     * @param pixelSize
     * @param complexRect
     * @return [ImageBitmap] with fractal image on it
     */
    abstract suspend fun getImage(pixelSize: Size, complexRect: Rect): ImageBitmap
}
