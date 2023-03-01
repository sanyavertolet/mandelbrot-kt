package com.sanyavertolet.mandelbrot.backend.counter

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.toComposeImageBitmap

import com.sanyavertolet.mandelbrot.backend.Function
import com.sanyavertolet.mandelbrot.backend.Painter
import com.sanyavertolet.mandelbrot.backend.cartesianProduct
import com.sanyavertolet.mandelbrot.backend.cartesianToComplex
import com.sanyavertolet.mandelbrot.backend.listUntil

import java.awt.image.BufferedImage

/**
 * [Counter] that calculates all the values synchronously
 */
class SerialCounter(
    private val painter: Painter,
    function: Function,
    maxIterations: Int = 100,
    borderValue: Double = 3.0,
) : Counter(function, maxIterations, borderValue) {
    /**
     * @param pixelSize
     * @param complexRect
     * @return [ImageBitmap] with fractal that should be displayed on screen with [pixelSize] containing [complexRect]
     */
    override suspend fun getImage(
        pixelSize: Size,
        complexRect: Rect,
    ): ImageBitmap = ImageBitmap(pixelSize.width.toInt(), pixelSize.height.toInt()).toAwtImage().apply {
        paintFractalPixels(pixelSize, complexRect)
    }.toComposeImageBitmap()

    private fun BufferedImage.paintFractalPixels(pixelSize: Size, complexRect: Rect) = cartesianProduct(
        listUntil(pixelSize.width.toInt()),
        listUntil(pixelSize.height.toInt())
    )
        .map { (x, y) ->
            val currentPoint = cartesianToComplex(x, y, pixelSize, complexRect)
            val finalIteration = calculate(currentPoint)
            setRGB(x, y, painter(finalIteration).toArgb())
        }
}
