/**
 * Canvas that doesn't wait until all the Bitmap is prepared and renders each point separately
 */

package com.sanyavertolet.mandelbrot.frontend.components.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import com.sanyavertolet.mandelbrot.Pixel
import com.sanyavertolet.mandelbrot.backend.complex.Complex
import com.sanyavertolet.mandelbrot.backend.fractal.AbstractFractal
import com.sanyavertolet.mandelbrot.backend.fractal.JuliaFractal

import kotlinx.coroutines.flow.*

private val emptyBitmap = ImageBitmap(2, 2)

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
