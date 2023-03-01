/**
 * Fractals
 */

package com.sanyavertolet.mandelbrot.backend.fractal

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size

import com.sanyavertolet.mandelbrot.backend.Function
import com.sanyavertolet.mandelbrot.backend.FunctionType
import com.sanyavertolet.mandelbrot.backend.JuliaFunction
import com.sanyavertolet.mandelbrot.backend.complex.Complex
import com.sanyavertolet.mandelbrot.backend.counter.Counter

/**
 * @property function
 */
sealed class AbstractFractal(
    private val counter: Counter,
    protected val function: Function,
) {
    /**
     * @param pixelSize
     * @param complexRect
     */
    suspend fun getImage(pixelSize: Size, complexRect: Rect) = counter.getImage(pixelSize, complexRect)
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
