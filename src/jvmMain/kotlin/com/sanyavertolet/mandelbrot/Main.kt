/**
 * Entry point of app
 */

package com.sanyavertolet.mandelbrot

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.sanyavertolet.mandelbrot.frontend.app

@ExperimentalComposeUiApi
fun main() = application {
    Window(
        title = "Fractals",
        onCloseRequest = ::exitApplication
    ) {
        app()
    }
}
