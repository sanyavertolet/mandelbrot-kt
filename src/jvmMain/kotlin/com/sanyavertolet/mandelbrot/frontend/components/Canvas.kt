/**
 * Canvas
 */

package com.sanyavertolet.mandelbrot.frontend.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.withSave
import androidx.compose.ui.unit.dp
import com.sanyavertolet.mandelbrot.fractal.SetCounter

/**
 * @param selectedSet current [SetCounter] or null if none is selected
 * @param complexRect rectangle in complex coordinates
 */
@Composable
fun canvas(selectedSet: SetCounter?, complexRect: Rect) {
    var image by remember { mutableStateOf<ImageBitmap?>(null) }

    var screenSize by remember { mutableStateOf<Size?>(null) }

    LaunchedEffect(complexRect, selectedSet, screenSize) {
        selectedSet?.let { setCounter ->
            screenSize?.let { size ->
                image = setCounter.getImage(size, complexRect)
            }
        }
    }

    Canvas(
        modifier = image?.let { Modifier.size(it.width.dp, it.height.dp) } ?: Modifier.fillMaxSize()
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