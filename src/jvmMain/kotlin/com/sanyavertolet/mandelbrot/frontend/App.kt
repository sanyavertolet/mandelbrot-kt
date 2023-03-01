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
import com.sanyavertolet.mandelbrot.backend.FunctionType
import com.sanyavertolet.mandelbrot.backend.PainterType
import com.sanyavertolet.mandelbrot.backend.counter.CounterType
import com.sanyavertolet.mandelbrot.backend.fractal.AbstractFractal
import com.sanyavertolet.mandelbrot.backend.fractal.JuliaFractal
import com.sanyavertolet.mandelbrot.backend.fractal.MandelbrotFractal
import com.sanyavertolet.mandelbrot.frontend.components.ToolbarItem

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
        var selectedFunctionName: String? by remember { mutableStateOf(null) }
        var selectedCounterName: String? by remember { mutableStateOf(null) }
        var selectedPainterName: String? by remember { mutableStateOf(null) }

        var fractal: AbstractFractal? by remember { mutableStateOf(null) }

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
            canvas(fractal, complexRect)
            toolBar { toolbarItem, selectedOption ->
                when (toolbarItem) {
                    ToolbarItem.COUNTER -> selectedCounterName = selectedOption
                    ToolbarItem.FUNCTION -> selectedFunctionName = selectedOption
                    ToolbarItem.PAINTER -> selectedPainterName = selectedOption
                }
                val canCreateFractal = sequenceOf(selectedCounterName, selectedFunctionName, selectedPainterName).all { it != null }
                if (canCreateFractal) {
                    val functionType = requireNotNull(FunctionType.values().find { it.prettyName == selectedFunctionName })
                    val painterType = requireNotNull(PainterType.values().find { it.prettyName == selectedPainterName })
                    val counterType = requireNotNull(CounterType.values().find { it.prettyName == selectedCounterName })
                    val counter = counterType.getInstance(functionType.instance, painterType.instance, 100, 3.0)
                    fractal = when (functionType) {
                        FunctionType.MANDELBROT -> MandelbrotFractal(counter)
                        FunctionType.JULIA -> JuliaFractal(counter)
                    }
                }
            }
        }
    }
}
