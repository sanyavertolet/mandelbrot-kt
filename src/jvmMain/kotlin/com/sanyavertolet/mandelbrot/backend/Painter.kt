/**
 * Painter classes
 */

package com.sanyavertolet.mandelbrot.backend

import androidx.compose.ui.graphics.Color

/**
 * Class that colors the pixel depending on iteration amount
 */
sealed class Painter {
    /**
     * @param iterations number of iterations that it took to overcome border value of the function
     * @return [Color] that the pixel should be painted to
     */
    abstract operator fun invoke(iterations: Int): Color
}

/**
 * @property prettyName
 * @property instance
 */
enum class PainterType(val prettyName: String, val instance: Painter) {
    BLACK("Black'n'white", BlackPainter),
    ;
}

private object BlackPainter : Painter() {
    override fun invoke(iterations: Int): Color = Color.Black.takeIf {
        iterations == 100
    } ?: Color.Unspecified
}
