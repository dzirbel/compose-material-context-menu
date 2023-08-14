package com.dzirbel.contextmenu

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalContextMenuRepresentation
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.LocalTextContextMenu
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.darkColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

private const val TITLE = "Material Context Menu Demo"

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
fun main() {
    application {
        Window(
            title = TITLE,
            onCloseRequest = ::exitApplication,
            content = {
                val systemTheme = !isSystemInDarkTheme()
                val lightTheme = remember { mutableStateOf(systemTheme) }
                val colors = if (lightTheme.value) lightColors() else darkColors()
                MaterialTheme(colors = colors) {
                    CompositionLocalProvider(
                        LocalContextMenuRepresentation provides MaterialContextMenuRepresentation(),
                        LocalTextContextMenu provides MaterialTextContentMenu,
                    ) {
                        Surface {
                            Content(
                                lightTheme = lightTheme.value,
                                setLightTheme = { lightTheme.value = it },
                            )
                        }
                    }
                }
            },
        )
    }
}

private data class DemoItem(val name: String)

@Composable
private fun Content(lightTheme: Boolean, setLightTheme: (Boolean) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(TITLE, style = MaterialTheme.typography.h5)

            IconButton(onClick = { setLightTheme(!lightTheme) }) {
                Icon(
                    painter = painterResource(if (lightTheme) "light_mode.svg" else "dark_mode.svg"),
                    contentDescription = "Toggle theme",
                )
            }
        }

        var text: String by remember { mutableStateOf("Text field demo") }
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        )

        val items = remember {
            List(20) { DemoItem(name = "Item ${it + 1}") }
        }

        val scrollState = rememberScrollState()
        Box {
            Column(Modifier.verticalScroll(scrollState).padding(16.dp)) {
                for (item in items) {
                    Item(item)
                }
            }

            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd),
                adapter = rememberScrollbarAdapter(scrollState),
            )
        }
    }
}

@Composable
private fun Item(demoItem: DemoItem) {
    var counter by remember { mutableStateOf(0) }
    ContextMenuArea(
        items = {
            contextMenu(
                incrementCounter = { counter++ },
                decrementCounter = { counter-- },
            )
        },
    ) {
        Row(
            modifier = Modifier
                .clickable {}
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(demoItem.name)

            TextButton(onClick = { counter++ }) {
                Text("Counter: $counter")
            }

            // TODO support context menu from overflow button click
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
        }
    }
}

private fun contextMenu(incrementCounter: () -> Unit, decrementCounter: () -> Unit): List<ContextMenuItem> {
    return listOf(
        ContextMenuItem("Increment counter", onClick = incrementCounter),
        ContextMenuItem("Decrement counter", onClick = decrementCounter),
        ContextMenuDivider,
        ContextMenuGroup("Group") {
            List(50) {
                ContextMenuItem("Nested item ${it + 1}") {}
            }
        },
        ContextMenuGroup("Nested group") {
            icons.map { outerIcon ->
                ContextMenuGroup("Inner group") {
                    icons.mapIndexed { index, innerIcon ->
                        MaterialContextMenuItem(
                            label = "Double-nested item ${index + 1}",
                            onClick = {},
                            leadingIcon = ContextMenuIcon.OfVector(outerIcon),
                            trailingIcon = ContextMenuIcon.OfVector(innerIcon),
                        )
                    }
                }
            }
        },
        ContextMenuDivider,
        MaterialContextMenuItem("Disabled item", onClick = {}, enabled = false),
        MaterialContextMenuItem(
            label = "Custom icon",
            onClick = {},
            leadingIcon = { Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colors.primary) },
        ),
        ContextMenuDivider,
        MaterialContextMenuItem(
            label = "Copy",
            onClick = {},
            leadingIcon = ContextMenuIcon.OfPainterResource("content_copy.svg"),
            trailingIcon = ContextMenuIcon.OfShortcuts(ContextMenuShortcut(key = "C")),
        ),
        MaterialContextMenuItem(
            label = "Cut",
            onClick = {},
            leadingIcon = ContextMenuIcon.OfPainterResource("content_cut.svg"),
            trailingIcon = ContextMenuIcon.OfShortcuts(ContextMenuShortcut(key = "X")),
        ),
        MaterialContextMenuItem(
            label = "Paste",
            onClick = {},
            leadingIcon = ContextMenuIcon.OfPainterResource("content_paste.svg"),
            trailingIcon = ContextMenuIcon.OfShortcuts(ContextMenuShortcut(key = "V")),
        ),
        MaterialContextMenuItem(label = "Aligned action", onClick = {}, leadingIcon = ContextMenuIcon.Empty),
    )
}

private val icons = listOf(
    Icons.Default.Edit,
    Icons.Default.Call,
    Icons.Default.Search,
    Icons.Default.Email,
    Icons.Default.ThumbUp,
)
