/**
 * Topbar
 */

package com.sanyavertolet.mandelbrot.frontend.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.sanyavertolet.mandelbrot.fractal.SetCounter

/**
 * @param onChange callback that should be invoked when dropdown entity is selected
 */
@Composable
fun toolBar(onChange: (SetCounter) -> Unit) {
    TopAppBar(
        title = { Text("Fractals", color = Color.White) },
        actions = {
            dotsDropdown(SetCounter.availableSets, onChange)
        },
        backgroundColor = Color.Gray,
    )
}

@Composable
private fun dotsDropdown(items: List<SetCounter>, onChange: (SetCounter) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = true }) {
        Icon(
            Icons.Default.MoreVert,
            contentDescription = "Показать меню",
            tint = Color.White,
        )
    }
    setsDropdown(expanded, items, { expanded = false }, onChange)
}

@Composable
private fun setsDropdown(
    expanded: Boolean,
    items: List<SetCounter>,
    onClose: () -> Unit,
    onChange: (SetCounter) -> Unit,
) {
    DropdownMenu(
        expanded,
        onClose,
    ) {
        items.forEach { counter ->
            DropdownMenuItem({
                onChange(counter)
                onClose()
            }) {
                Text(counter.name)
            }
        }
    }
}
