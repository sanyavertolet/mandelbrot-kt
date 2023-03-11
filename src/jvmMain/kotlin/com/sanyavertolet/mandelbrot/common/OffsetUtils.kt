/**
 * Utility methods for Offset class
 */

package com.sanyavertolet.mandelbrot.common

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlin.collections.sumOf

/**
 * @see [sumOf]
 *
 * @param selector function that produces [Offset] for each element from [Iterable]
 * @return the sum of all values produced by [selector] function applied to each element in the array.
 */
@JvmName("sumOfOffset")
inline fun <T> Iterable<T>.sumOf(selector: (T) -> Offset): Offset {
    var sum: Offset = Offset.Zero
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

/**
 * @param pixelSize [Size] filled with integers - screen size
 * @param complexSize [Size] filled with floats - complex plane fragment size
 * @return [Offset] filled with complex values corresponding to initial pixel values
 */
fun Offset.pixelsToComplex(pixelSize: Size, complexSize: Size) = Offset(x * complexSize.width / pixelSize.width, y * complexSize.height / pixelSize.height)
