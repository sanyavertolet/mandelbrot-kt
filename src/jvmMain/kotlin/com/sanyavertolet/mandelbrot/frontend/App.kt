/**
 * Main app
 */

package com.sanyavertolet.mandelbrot.frontend

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect

import com.sanyavertolet.mandelbrot.fractal.SetCounter
import com.sanyavertolet.mandelbrot.frontend.components.canvas
import com.sanyavertolet.mandelbrot.frontend.components.toolBar

/**
 * Main app
 */
@Composable
@Preview
@Suppress("MAGIC_NUMBER", "LOCAL_VARIABLE_EARLY_DECLARATION")
fun app() {
    MaterialTheme {
        var selectedSet: SetCounter? by remember { mutableStateOf(null) }
        val complexRect: Rect by remember {
            mutableStateOf(
                Rect(
                    top = 1f,
                    bottom = -1f,
                    left = -2f,
                    right = 1f,
                )
            )
        }
        Box(
            Modifier.wrapContentSize(Alignment.CenterStart),
            Alignment.TopStart,
        ) {
            canvas(selectedSet, complexRect)
            toolBar { selectedSet = it }
        }
    }
}
