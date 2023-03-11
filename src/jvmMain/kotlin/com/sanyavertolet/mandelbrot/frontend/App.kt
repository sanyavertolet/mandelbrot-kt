/**
 * Main app
 */

package com.sanyavertolet.mandelbrot.frontend

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import com.sanyavertolet.mandelbrot.backend.complex.Complex
import com.sanyavertolet.mandelbrot.backend.fractal.AbstractFractal
import com.sanyavertolet.mandelbrot.frontend.components.ToolbarItem

import com.sanyavertolet.mandelbrot.frontend.components.canvas.syncCanvas
import com.sanyavertolet.mandelbrot.frontend.components.toolBar

/**
 * Main app
 */
@Composable
@Preview
@ExperimentalComposeUiApi
@Suppress("MAGIC_NUMBER", "LOCAL_VARIABLE_EARLY_DECLARATION")
fun app() {
    MaterialTheme {
        var selectedFunctionName: String? by remember { mutableStateOf(null) }
        var selectedCounterName: String? by remember { mutableStateOf(null) }
        var selectedPainterName: String? by remember { mutableStateOf(null) }

        var fractal: AbstractFractal? by remember { mutableStateOf(null) }
        var constant: Complex by remember { mutableStateOf(Complex()) }

        Column {
            toolBar(fractal, { constant = it }) { toolbarItem, selectedOption ->
                when (toolbarItem) {
                    ToolbarItem.COUNTER -> selectedCounterName = selectedOption
                    ToolbarItem.FUNCTION -> selectedFunctionName = selectedOption
                    ToolbarItem.PAINTER -> selectedPainterName = selectedOption
                }
                val canCreateFractal = sequenceOf(selectedCounterName, selectedFunctionName, selectedPainterName).all { it != null }
                if (canCreateFractal) {
                    fractal = AbstractFractal.factory(selectedCounterName, selectedFunctionName, selectedPainterName)
                }
            }
            syncCanvas(fractal, constant)
        }
    }
}
