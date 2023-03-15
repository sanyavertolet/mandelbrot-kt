/**
 * Fractals
 */

package com.sanyavertolet.mandelbrot.backend.fractal

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*

import com.sanyavertolet.mandelbrot.backend.Function
import com.sanyavertolet.mandelbrot.backend.FunctionType
import com.sanyavertolet.mandelbrot.backend.JuliaFunction
import com.sanyavertolet.mandelbrot.backend.PainterType
import com.sanyavertolet.mandelbrot.backend.complex.Complex
import com.sanyavertolet.mandelbrot.backend.counter.Counter
import com.sanyavertolet.mandelbrot.backend.counter.CounterType

/**
 * @property function fractal function, e.g. z_{n+1} = z_{n}^2 + c
 */
sealed class AbstractFractal(
    private val counter: Counter,
    protected val function: Function,
) {
    /**
     * @param pixelSize canvas [Size] in pixels
     * @param complexRect complex rectangle corresponding to current screen
     * @param isSmooth
     */
    fun getImage(pixelSize: Size, complexRect: Rect, isSmooth: Boolean) = counter.getImage(pixelSize, complexRect, isSmooth)

    companion object {
        /**
         * Default [Rect] - subset of complex rect
         */
        @Suppress("MAGIC_NUMBER")
        val defaultComplexRect = Rect(-2f, 2f, 2f, -2f)

        /**
         * @param selectedCounterName
         * @param selectedFunctionName
         * @param selectedPainterName
         * @return [AbstractFractal] with counter with [selectedCounterName], function with [selectedFunctionName], painter with [selectedPainterName]
         */
        fun factory(selectedCounterName: String?, selectedFunctionName: String?, selectedPainterName: String?): AbstractFractal {
            val functionType = requireNotNull(FunctionType.values().find { it.prettyName == selectedFunctionName })
            val painterType = requireNotNull(PainterType.values().find { it.prettyName == selectedPainterName })
            val counterType = requireNotNull(CounterType.values().find { it.prettyName == selectedCounterName })
            val counter = counterType.getInstance(functionType.instance, painterType.instance, Counter.DEFAULT_MAX_ITERATIONS, Counter.DEFAULT_BORDER_VALUE)
            return when (functionType) {
                FunctionType.MANDELBROT -> MandelbrotFractal(counter)
                FunctionType.JULIA -> JuliaFractal(counter)
            }
        }
    }
}

/**
 * Julia fractal
 *
 * @see <a href="https://en.wikipedia.org/wiki/Julia_set">Julia on Wiki</a>
 */
class JuliaFractal(
    counter: Counter,
) : AbstractFractal(counter, FunctionType.JULIA.instance) {
    /**
     * @param newConstant constant to be set
     */
    fun setConstant(newConstant: Complex) {
        (function as JuliaFunction).constant = newConstant
    }
}

/**
 * Mandelbrot set
 *
 * @see <a href="https://en.wikipedia.org/wiki/Mandelbrot_set">Mandelbrot on Wiki</a>
 */
class MandelbrotFractal(
    counter: Counter,
) : AbstractFractal(counter, FunctionType.MANDELBROT.instance)
