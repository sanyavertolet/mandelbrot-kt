/**
 * Utils
 */

package com.sanyavertolet.mandelbrot.backend

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.sanyavertolet.mandelbrot.backend.complex.Complex

/**
 * @param x horizontal screen coordinates
 * @param y vertical screen coordinates
 * @param pixelSize [Size] of canvas in pixels
 * @param complexRect complex rectangle corresponding to current screen
 * @return [Complex] coordinates of dot with coordinates [x] and [y]
 */
fun cartesianToComplex(
    x: Int,
    y: Int,
    pixelSize: Size,
    complexRect: Rect
): Complex = Complex(
    complexRect.left + x * (complexRect.size.width / pixelSize.width).toDouble(),
    complexRect.top + y * (complexRect.size.height / pixelSize.height).toDouble(),
)

/**
 * @param lhs first list
 * @param rhs second list
 * @return cartesian product of [lhs] and [rhs]
 */
fun cartesianProduct(lhs: List<Int>, rhs: List<Int>): List<Pair<Int, Int>> = lhs.flatMap { lhsElem -> rhs.map { rhsElem -> lhsElem to rhsElem } }

/**
 * @param number
 * @return list of sorted numbers from 0 to [number] (number is exclusive)
 */
fun listUntil(number: Int) = (0 until number).toList()
