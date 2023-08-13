package com.dzirbel.contextmenu

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.LocalContextMenuRepresentation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@OptIn(ExperimentalComposeUiApi::class)
@Suppress("MagicNumber")
fun main() {
    application {
        Window(
            title = "Context Menu Demo",
            onCloseRequest = ::exitApplication,
            content = {
                CompositionLocalProvider(LocalContextMenuRepresentation provides AugmentedContextMenuRepresentation()) {
                    ContextMenuArea(
                        items = {
                            listOf(
                                ContextMenuItem("Item 1", {}),
                                ContextMenuItem("Item 2", {}),
                                ContextMenuDivider,
                                ContextMenuItem("Item 3", {}),
                            )
                        },
                    ) {
                        Box(Modifier.fillMaxSize())
                    }
                }
            },
        )
    }
}
