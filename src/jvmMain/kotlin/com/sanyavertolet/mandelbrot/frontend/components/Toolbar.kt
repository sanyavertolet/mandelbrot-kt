/**
 * Topbar
 */

@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.sanyavertolet.mandelbrot.frontend.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sanyavertolet.mandelbrot.backend.FunctionType
import com.sanyavertolet.mandelbrot.backend.PainterType
import com.sanyavertolet.mandelbrot.backend.complex.Complex
import com.sanyavertolet.mandelbrot.backend.counter.CounterType
import com.sanyavertolet.mandelbrot.backend.fractal.AbstractFractal
import com.sanyavertolet.mandelbrot.backend.fractal.JuliaFractal

private typealias OnChangeFunction = (ToolbarItem, String) -> Unit

/**
 * @property prettyName
 * @property options
 */
enum class ToolbarItem(val prettyName: String, val options: List<String>) {
    COUNTER("Counter", CounterType.values().map { it.prettyName }),
    FUNCTION("Function", FunctionType.values().map { it.prettyName }),
    PAINTER("Painter", PainterType.values().map { it.prettyName }),
    ;
}

/**
 * @param fractal selected fractal
 * @param setConstant callback to set the constant for Julia set
 * @param onChange callback that should be invoked when dropdown entity is selected
 */
@Composable
@Suppress("MAGIC_NUMBER", "LAMBDA_IS_NOT_LAST_PARAMETER")
fun toolBar(fractal: AbstractFractal?, setConstant: (Complex) -> Unit, onChange: OnChangeFunction) {
    TopAppBar(
        title = { Text("Fractals", color = Color.White) },
        actions = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                buttons(onChange)
                constantScroller(fractal) { real, imaginary ->
                    setConstant(Complex(real.toDouble(), imaginary.toDouble()))
                }
            }
        },
        backgroundColor = Color.Gray,
        elevation = 2.dp,
        modifier = Modifier.requiredHeight(80.dp),
    )
}

@Composable
@Suppress("MAGIC_NUMBER")
private fun constantScroller(fractal: AbstractFractal?, setConstant: (Float, Float) -> Unit) {
    Column {
        val sliderColors = SliderDefaults.colors(
            thumbColor = Color.White,
            activeTrackColor = Color.LightGray,
            inactiveTrackColor = Color.DarkGray,
        )
        var realPart: Float by remember { mutableStateOf(0f) }
        var imaginaryPart: Float by remember { mutableStateOf(0f) }
        Slider(
            realPart,
            { realPart = it },
            enabled = fractal is JuliaFractal,
            valueRange = -1f..1f,
            onValueChangeFinished = { setConstant(realPart, imaginaryPart) },
            colors = sliderColors,
        )
        Slider(
            imaginaryPart,
            { imaginaryPart = it },
            enabled = fractal is JuliaFractal,
            valueRange = -1f..1f,
            onValueChangeFinished = { setConstant(realPart, imaginaryPart) },
            colors = sliderColors,
        )
    }
}

@Composable
private fun buttons(onChange: OnChangeFunction) {
    var selectedToolbarItem: ToolbarItem? by remember { mutableStateOf(null) }
    Row {
        ToolbarItem.values().forEach { item ->
            Button({ selectedToolbarItem = item },
                @Suppress("MAGIC_NUMBER") Modifier.padding(3.dp),
                colors = ButtonDefaults.buttonColors(Color.LightGray),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Text(item.prettyName)
            }
        }
    }
    dropdown(selectedToolbarItem, selectedToolbarItem?.options ?: emptyList(), onChange) { selectedToolbarItem = null }
}

@Composable
@Suppress("")
private fun dropdown(
    toolbarItem: ToolbarItem?,
    options: List<String>,
    onChange: OnChangeFunction,
    onClose: () -> Unit,
) {
    DropdownMenu(
        toolbarItem != null,
        onClose,
    ) {
        options.forEach { option ->
            DropdownMenuItem({
                toolbarItem?.let { toolbarItem ->
                    onChange(toolbarItem, option)
                    onClose()
                }
            }) {
                Text(option)
            }
        }
    }
}
