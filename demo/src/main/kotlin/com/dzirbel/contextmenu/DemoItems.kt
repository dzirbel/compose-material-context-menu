package com.dzirbel.contextmenu

import androidx.compose.foundation.ContextMenuItem

internal fun demoItems(): List<ContextMenuItem> {
    return List(50) {
        ContextMenuItem("Item ${it + 1}") {}
    }
}
