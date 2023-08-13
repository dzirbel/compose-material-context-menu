package com.dzirbel.contextmenu

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.LocalContextMenuRepresentation
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    application {
        Window(
            title = "Context Menu Demo",
            onCloseRequest = ::exitApplication,
            content = {
                MaterialTheme {
                    CompositionLocalProvider(
                        LocalContextMenuRepresentation provides AugmentedContextMenuRepresentation(),
                    ) {
                        Content()
                    }
                }
            },
        )
    }
}

private data class DemoItem(val name: String)

@Composable
private fun Content() {
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

@Composable
private fun Item(demoItem: DemoItem) {
    var counter by remember { mutableStateOf(0) }
    ContextMenuArea(
        items = {
            demoItem.contextMenu(
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

            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
        }
    }
}

private fun DemoItem.contextMenu(incrementCounter: () -> Unit, decrementCounter: () -> Unit): List<ContextMenuItem> {
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
                        object : AugmentedContextMenuItem("Double-nested item ${index + 1}", onClick = {}) {
                            @Composable
                            override fun StartIcon() {
                                Icon(imageVector = outerIcon, contentDescription = null)
                            }

                            @Composable
                            override fun EndIcon() {
                                Icon(imageVector = innerIcon, contentDescription = null)
                            }
                        }
                    }
                }
            }
        },
        ContextMenuDivider,
        AugmentedContextMenuItem("Disabled item", onClick = {}, enabled = false),
        AugmentedContextMenuItem("Last item", onClick = {}),
    )
}

private val icons = listOf(
    Icons.Default.Edit,
    Icons.Default.Call,
    Icons.Default.Search,
    Icons.Default.Email,
    Icons.Default.ThumbUp,
)
