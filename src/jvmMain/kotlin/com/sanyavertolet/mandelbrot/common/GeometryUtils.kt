/**
 * Utility functions that should help with geometryy problems
 */

package com.sanyavertolet.mandelbrot.common

import androidx.compose.ui.geometry.*
import kotlin.math.abs

/**
 * Should be used only with complex size as in complex numbers y-axis is up-directed
 *
 * @param ratio requested [Size.width] / [Size.height]
 * @return [Size] with requested [ratio]
 */
fun Size.fixRatio(ratio: Float): Size = Size(
    if (abs(width / height) < ratio) {
        abs(height) * ratio
    } else {
        width
    },
    if (abs(width / height) > ratio) {
        width / ratio
    } else {
        height
    },
)

/**
 * Change Rect size according to [scale]
 *
 * @param scale scale coefficient
 * @return [Rect] zoomed by [scale]
 */
@Suppress("FLOAT_IN_ACCURATE_CALCULATIONS", "MAGIC_NUMBER")
fun Rect.scale(scale: Float): Rect = Offset(width * scale, height * scale).let { delta ->
    Rect(topLeft + delta / 2f, bottomRight - delta / 2f)
}

/**
 * Should be used only with complex size as in complex numbers y-axis is up-directed
 *
 * @param size new [Size] with requested [Size.width] / [Size.height]
 * @return [Rect] with requested [size]
 */
fun Rect.resize(size: Size): Rect = Rect(center - size.center, size)

/**
 * Should be used only with complex size as in complex numbers y-axis is up-directed
 *
 * @param ratio requested [Size.width] / [Size.height]
 * @return [Rect] with requested [ratio]
 */
fun Rect.fixRatio(ratio: Float): Rect = resize(size.fixRatio(ratio))
