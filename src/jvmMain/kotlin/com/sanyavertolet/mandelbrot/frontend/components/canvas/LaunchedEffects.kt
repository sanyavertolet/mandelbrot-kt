/**
 * File that contains definitions of different asynchronous hooks (as useEffect in React)
 */

package com.sanyavertolet.mandelbrot.frontend.components.canvas

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.sanyavertolet.mandelbrot.backend.complex.Complex
import com.sanyavertolet.mandelbrot.backend.fractal.AbstractFractal
import com.sanyavertolet.mandelbrot.common.fixRatio
import com.sanyavertolet.mandelbrot.common.scale
import com.sanyavertolet.mandelbrot.frontend.components.StateUpdater

private const val DEFAULT_SCALING_COEFFICIENT = 0.05f

/**
 * Effect that triggers rerender on zoom event
 *
 * @param updateComplexRect callback to update complex plane rectangle subset
 * @param scaleCoefficient coefficient that defines the ratio that should appear/disappear after zoom event, [DEFAULT_SCALING_COEFFICIENT] by default
 * @param updateImage callback to trigger rerender
 * @return callback to set number of times to apply the zoom with [scaleCoefficient]
 */
@Composable
@Suppress("LAMBDA_IS_NOT_LAST_PARAMETER")
fun scaleEffect(
    updateComplexRect: StateUpdater<Rect>,
    scaleCoefficient: Float = DEFAULT_SCALING_COEFFICIENT,
    updateImage: suspend () -> Unit,
): (Int) -> Unit {
    var scale: Int by remember { mutableStateOf(0) }
    LaunchedEffect(scale) {
        if (scale != 0) {
            updateComplexRect { it.scale(scaleCoefficient * scale) }
            scale = 0
            updateImage()
        }
    }
    return { scale = it }
}

/**
 * Effect that triggers rerender on move event
 *
 * @param updateComplexRect callback to update complex plane rectangle subset
 * @param updateImage callback to trigger rerender
 * @return callback to set [Offset] that the complex plane rectangle subset should be moved with
 */
@Composable
fun offsetEffect(
    updateComplexRect: StateUpdater<Rect>,
    updateImage: suspend () -> Unit,
): (Offset) -> Unit {
    var offset by remember { mutableStateOf(Offset.Zero) }
    LaunchedEffect(offset) {
        if (offset != Offset.Zero) {
            updateComplexRect { it.translate(offset) }
            offset = Offset.Zero
            updateImage()
        }
    }
    return { offset = it }
}

/**
 * Effect that triggers rerender on [screenSize], [constant] or [fractal] change
 *
 * @param fractal currently built fractal
 * @param constant [Complex] constant that makes sense only for Julia set
 * @param screenSize canvas [Size] in pixels
 * @param updateComplexRect callback to update complex plane rectangle subset
 * @param updateFractal callback to trigger rerender
 */
@Composable
fun mainEffect(
    fractal: AbstractFractal?,
    constant: Complex,
    screenSize: Size?,
    updateComplexRect: StateUpdater<Rect>,
    updateFractal: suspend () -> Unit,
) {
    LaunchedEffect(fractal, constant, screenSize) {
        screenSize?.let { size ->
            updateComplexRect { it.fixRatio(size.width / size.height) }
            updateFractal()
        }
    }
}
