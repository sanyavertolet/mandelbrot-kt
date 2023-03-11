/**
 * Canvas that waits until all the Bitmap is prepared
 */

package com.sanyavertolet.mandelbrot.frontend.components.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.withSave
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.positionChange
import com.sanyavertolet.mandelbrot.backend.complex.Complex
import com.sanyavertolet.mandelbrot.backend.fractal.AbstractFractal
import com.sanyavertolet.mandelbrot.backend.fractal.JuliaFractal
import com.sanyavertolet.mandelbrot.common.fixRatio
import com.sanyavertolet.mandelbrot.common.pixelsToComplex
import com.sanyavertolet.mandelbrot.common.scale

private const val DEFAULT_SCALING_COEFFICIENT = 0.05f

/**
 * @param fractal current fractal or null if none is selected
 * @param constant
 */
@Composable
@ExperimentalComposeUiApi
@Suppress("MAGIC_NUMBER", "TOO_LONG_FUNCTION")
fun syncCanvas(fractal: AbstractFractal?, constant: Complex) {
    var image by remember { mutableStateOf<ImageBitmap?>(null) }
    var screenSize by remember { mutableStateOf<Size?>(null) }
    var complexRect: Rect by remember { mutableStateOf(Rect(-2f, 2f, 2f, -2f)) }

    val updateFractal = {
        fractal.apply {
            if (this is JuliaFractal) {
                setConstant(constant)
            }
        }
            ?.let { fractal ->
                screenSize?.let { size ->
                    image = fractal.getImage(size, complexRect, true)
                }
            }
    }

    var deltaScale by remember { mutableStateOf(0) }
    LaunchedEffect(deltaScale) {
        if (deltaScale != 0) {
            complexRect = complexRect.scale(DEFAULT_SCALING_COEFFICIENT * deltaScale)
            deltaScale = 0
            updateFractal()
        }
    }

    var deltaOffset by remember { mutableStateOf(Offset.Zero) }
    LaunchedEffect(deltaOffset) {
        if (deltaOffset != Offset.Zero) {
            complexRect = complexRect.translate(deltaOffset)
            deltaOffset = Offset.Zero
            updateFractal()
        }
    }

    LaunchedEffect(fractal, constant, screenSize) {
        screenSize?.let { size ->
            complexRect = complexRect.fixRatio(size.width / size.height)
            updateFractal()
        }
    }

    Canvas(
        modifier = Modifier.fillMaxSize()
            .onPointerEvent(PointerEventType.Scroll) { event ->
                val toScale = event.changes.first().scrollDelta.y
                    .times(10).toInt()
                deltaScale = toScale
            }
            .onPointerEvent(PointerEventType.Move) { event ->
                if (event.buttons.isPrimaryPressed) {
                    screenSize?.let { pixelSize ->
                        deltaOffset = -event.changes.first().positionChange().pixelsToComplex(pixelSize, complexRect.size)
                    }
                }
            }
    ) {
        screenSize = size
        image?.let { completedImage ->
            drawIntoCanvas { canvas ->
                canvas.withSave {
                    canvas.drawImage(
                        completedImage,
                        Offset.Zero,
                        Paint().apply { color = Color.Green },
                    )
                }
            }
        }
    }
}
