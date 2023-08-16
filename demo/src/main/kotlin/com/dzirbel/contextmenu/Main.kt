package com.dzirbel.contextmenu

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalContextMenuRepresentation
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.LocalTextContextMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

const val TITLE = "Material Context Menu Demo"

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
                        LocalContextMenuRepresentation provides MaterialContextMenuRepresentation(
                            measurements = ContextMenuMeasurements(windowMargin = 30.dp),
                        ),
                        LocalTextContextMenu provides MaterialTextContextMenu,
                    ) {
                        Surface {
                            DemoContent(
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
