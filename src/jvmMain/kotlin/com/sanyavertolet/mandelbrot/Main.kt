/**
 * Entry point of app
 */

package com.sanyavertolet.mandelbrot

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.sanyavertolet.mandelbrot.frontend.app

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        app()
    }
}
