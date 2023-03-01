/**
 * Topbar
 */

@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.sanyavertolet.mandelbrot.frontend.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sanyavertolet.mandelbrot.backend.FunctionType
import com.sanyavertolet.mandelbrot.backend.PainterType
import com.sanyavertolet.mandelbrot.backend.counter.CounterType

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
 * @param onChange callback that should be invoked when dropdown entity is selected
 */
@Composable
fun toolBar(onChange: OnChangeFunction) {
    TopAppBar(
        title = { Text("Fractals", color = Color.White) },
        actions = {
            dotsDropdown(onChange)
        },
        backgroundColor = Color.Gray,
        elevation = 2.dp,
    )
}

@Composable
private fun dotsDropdown(onChange: OnChangeFunction) {
    var selectedToolbarItem: ToolbarItem? by remember { mutableStateOf(null) }
    ToolbarItem.values().forEach { item ->
        Button({ selectedToolbarItem = item },
            Modifier.padding(3.dp),
            colors = ButtonDefaults.buttonColors(Color.LightGray),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Text(item.prettyName)
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
