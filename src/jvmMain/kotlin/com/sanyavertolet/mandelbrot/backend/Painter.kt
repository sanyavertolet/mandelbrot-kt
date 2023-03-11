/**
 * Painter classes
 */

package com.sanyavertolet.mandelbrot.backend

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.sanyavertolet.mandelbrot.backend.counter.Counter
import kotlin.math.ceil

/**
 * Class that colors the pixel depending on iteration amount
 */
sealed class Painter {
    /**
     * This mode of painter uses a life-hack that provides floating-point iteration number - "smooth" iterations.
     *
     * @param iterations number of iterations that it took to overcome border value of the function
     * @return [Color] that the pixel should be painted to
     */
    abstract operator fun invoke(iterations: Double): Color
}

/**
 * Class that colors the pixel with linear gradient with colors in [colorPalette]
 */
@ExperimentalStdlibApi
abstract class Gradient : Painter() {
    /**
     * List of [Color]s that should be used to colorize the fractal
     */
    abstract val colorPalette: List<Color>
    private val colorLength by lazy { Counter.DEFAULT_MAX_ITERATIONS / colorPalette.size.toDouble() }
    override operator fun invoke(iterations: Double): Color = getLowerColorIndex(iterations).let { lowerColorIndex ->
        lowerColorIndex to getOverflow(iterations, lowerColorIndex)
    }
        .let { (lowerColorIndex, overflow) ->
            lerp(colorPalette[lowerColorIndex], colorPalette[getHigherColorIndex(lowerColorIndex)], (overflow / colorLength).toFloat())
        }

    private fun getOverflow(iterations: Double, lowerColorIndex: Int) = (iterations - lowerColorIndex * colorLength).toFloat()
    private fun getLowerColorIndex(iterations: Double): Int = colorPalette.indices.lastOrNull { colorLength * it < iterations } ?: 0
    private fun getHigherColorIndex(lowerColorIndex: Int): Int = if (lowerColorIndex + 1 == colorPalette.size) lowerColorIndex else lowerColorIndex + 1
}

/**
 * @property prettyName
 * @property instance
 */
enum class PainterType(val prettyName: String, val instance: Painter) {
    BLACK("BnW", BlackPainter),
    BLACK_GRAD("BnW-G", BlackGradientPainter),
    BLUE_AND_YELLOW_GRAD("BnY-G", BlueAndYellowGradientPainter),
    ;
}

private object BlackPainter : Painter() {
    override fun invoke(iterations: Double): Color = Color.Black.takeIf { ceil(iterations).toInt() == Counter.DEFAULT_MAX_ITERATIONS } ?: Color.Unspecified
}

@OptIn(ExperimentalStdlibApi::class)
private object BlackGradientPainter : Gradient() {
    override val colorPalette: List<Color> = listOf(Color.Black, Color.White)
}

@OptIn(ExperimentalStdlibApi::class)
@Suppress("MAGIC_NUMBER")
private object BlueAndYellowGradientPainter : Gradient() {
    override val colorPalette = listOf(
        Color(0x00, 0x00, 0x00),
        Color(0x00, 0x2b, 0xbf),
        Color(0x01, 0x3c, 0xff),
        Color(0x01, 0x7f, 0xff),
        Color(0x02, 0xbf, 0xff),
        Color(0x02, 0xff, 0xff),
        Color(0x41, 0xfd, 0xbf),
        Color(0x80, 0xfc, 0x80),
        Color(0xbf, 0xfb, 0x40),
        Color(0xff, 0xfa, 0x00),
        Color(0xff, 0xbf, 0x00),
        Color(0xff, 0x80, 0x00),
        Color(0xff, 0x40, 0x00),
        Color(0xff, 0x01, 0x00),
        Color(0xbf, 0x00, 0x00),
        Color(0x80, 0x00, 0x00),
        Color(0x3b, 0x17, 0x66),
    )
}
