package com.sanyavertolet.mandelbrot

import androidx.compose.ui.graphics.Color

/**
 * @property x
 * @property y
 * @property color color to paint pixel
 */
data class Pixel(val x: Int, val y: Int, val color: Color)
