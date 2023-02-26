package com.sanyavertolet.mandelbrot.fractal

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import com.sanyavertolet.mandelbrot.cartesianProduct
import com.sanyavertolet.mandelbrot.cartesianToComplex
import com.sanyavertolet.mandelbrot.complex.Complex
import com.sanyavertolet.mandelbrot.listUntil
import kotlinx.coroutines.*

/**
 * Interface that should be implemented by all Set counters
 */
interface SetCounter {
    /**
     * Name of [SetCounter]
     */
    val name: String

    /**
     * [CoroutineScope] that [parallelCounter] should work over
     */
    val scope: CoroutineScope

    /**
     * Dispatcher that should process [scope] coroutine
     */
    val dispatcher: CoroutineDispatcher

    /**
     * @param point current point
     * @param prevValue previously calculated number
     * @return calculated fractal function
     */
    fun function(point: Complex, prevValue: Complex = Complex()): Complex

    /**
     * @param point
     * @param border
     * @return iteration when [function] of [point] calculated iteratively overflows the [border]
     */
    fun getPointCharacteristics(point: Complex, border: Int = 1000): Int {
        var value = Complex()
        repeat(ITERATIONS) { iteration ->
            value = function(point, value)
            if (value.abs() > border) {
                return iteration
            }
        }
        return ITERATIONS
    }

    /**
     * @param pixelSize
     * @param complexRect
     * @return map where key is coordinate and value is [Deferred] of rgb color
     */
    suspend fun parallelCounter(pixelSize: Size, complexRect: Rect) = cartesianProduct(
        listUntil(pixelSize.width.toInt()),
        listUntil(pixelSize.height.toInt())
    ).associate { (x, y) ->
        (x to y) to scope.async {
            getCellColor(cartesianToComplex(x, y, pixelSize, complexRect))
        }
    }

    /**
     * @param point
     * @return color of [point]
     */
    suspend fun getCellColor(point: Complex): Int?

    /**
     * @param pixelSize
     * @param complexRect
     * @return [ImageBitmap] with fractal that should be displayed on screen with [pixelSize] containing [complexRect]
     */
    suspend fun getImage(
        pixelSize: Size,
        complexRect: Rect,
    ): ImageBitmap = ImageBitmap(pixelSize.width.toInt(), pixelSize.height.toInt()).toAwtImage().apply {
        parallelCounter(pixelSize, complexRect).forEach { (x, y), color ->
            scope.launch {
                color.await()?.let { color ->
                    setRGB(x, y, color)
                }
            }
        }
    }.toComposeImageBitmap()

    companion object {
        const val ITERATIONS = 100
        private val empty = object : SetCounter {
            override val name: String = "Empty"
            override val dispatcher: CoroutineDispatcher = Dispatchers.Unconfined
            override val scope: CoroutineScope = CoroutineScope(dispatcher)
            override fun function(point: Complex, prevValue: Complex): Complex {
                throw NotImplementedError()
            }

            // override fun parallelCounter(size: Size): Def { }
            override suspend fun getCellColor(point: Complex): Int = 0
        }
        val availableSets = listOf(
            empty,
            MandelbrotSet(),
        )
    }
}
