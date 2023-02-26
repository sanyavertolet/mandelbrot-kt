package com.sanyavertolet.mandelbrot.fractal

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.sanyavertolet.mandelbrot.complex.Complex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext

/**
 * [SetCounter] of Mandelbrot set
 */
class MandelbrotSet : SetCounter {
    override val name: String = "Mandelbrot"

    @OptIn(DelicateCoroutinesApi::class)
    override val dispatcher = newFixedThreadPoolContext(Runtime.getRuntime().availableProcessors(), "${name}Worker")
    override val scope: CoroutineScope = CoroutineScope(dispatcher)

    override fun function(point: Complex, prevValue: Complex): Complex = prevValue.times(prevValue).plus(point)

    override suspend fun getCellColor(point: Complex): Int? = Color.Black.toArgb().takeIf {
        getPointCharacteristics(point) == SetCounter.ITERATIONS
    }
}
