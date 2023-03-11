/**
 * Canvas
 */

package com.sanyavertolet.mandelbrot.frontend.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.*
import com.sanyavertolet.mandelbrot.Pixel
import com.sanyavertolet.mandelbrot.backend.complex.Complex
import com.sanyavertolet.mandelbrot.backend.fractal.AbstractFractal
import com.sanyavertolet.mandelbrot.backend.fractal.JuliaFractal
import com.sanyavertolet.mandelbrot.backend.scale
import com.sanyavertolet.mandelbrot.common.pixelsToComplex

import kotlinx.coroutines.flow.*

private const val DEFAULT_SCALING_COEFFICIENT = 0.05f

private val emptyBitmap = ImageBitmap(2, 2)

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
    var complexRect: Rect by remember { mutableStateOf(AbstractFractal.getInitialComplexRect(fractal)) }

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

    LaunchedEffect(screenSize) {
        /*
         * todo: Window resize processing
         */
        updateFractal()
    }

    LaunchedEffect(fractal, constant) {
        updateFractal()
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

/**
 * WIP
 *
 * @param fractal
 * @param constant
 * @param complexRect
 * todo: implement drawing fractal asynchronously
 */
@Composable
fun asyncCanvas(fractal: AbstractFractal?, constant: Complex, complexRect: Rect) {
    var screenSize by remember { mutableStateOf<Size?>(null) }
    var bitmap: ImageBitmap? by remember { mutableStateOf(null) }
    var dots: List<Pixel> by remember { mutableStateOf(emptyList()) }

    LaunchedEffect(complexRect, fractal, screenSize, constant) {
        screenSize?.let {
            bitmap ?: run { bitmap = ImageBitmap(it.width.toInt(), it.height.toInt()) }
        }

        val pixelFlow = fractal.apply {
            if (this is JuliaFractal) {
                setConstant(constant)
            }
        }?.let { fractal ->
            bitmap?.let { bitmap ->
                fractal.getPixelsToPaint(bitmap, complexRect)
            }
        } ?: emptyFlow()

        dots = pixelFlow.toList()

        pixelFlow.map {
            bitmap = bitmap?.toAwtImage()?.apply { setRGB(it.x, it.y, it.color.toArgb()) }?.toComposeImageBitmap()
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        screenSize = size
        drawIntoCanvas { canvas ->
            canvas.withSave {
                canvas.drawImage(
                    bitmap ?: emptyBitmap,
                    Offset(0f, 0f),
                    Paint()
                )
            }
        }
    }
}
