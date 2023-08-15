package com.dzirbel.contextmenu

import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

internal fun demoItems(): List<ContextMenuItem> {
    return listOf(
        MaterialContextMenuItem(
            label = "Material item",
            onClick = {},
            leadingIcon = ContextMenuIcon.OfVector(Icons.Default.Star),
            trailingIcon = ContextMenuIcon.OfShortcuts(ContextMenuShortcut(key = "M")),
        ),
        MaterialContextMenuItem("Disabled item", onClick = {}, enabled = false),
        MaterialContextMenuItem(
            label = "Custom icons",
            onClick = {},
            leadingIcon = {
                Icon(Icons.Default.ThumbUp, contentDescription = null, tint = MaterialTheme.colors.primary)
            },
            trailingIcon = { params ->
                Box(
                    modifier = Modifier
                        .size(params.measurements.iconSize)
                        .background(
                            brush = Brush.horizontalGradient(listOf(Color.Green, Color.Blue)),
                            shape = RoundedCornerShape(4.dp),
                        ),
                )
            },
        ),
        ContextMenuDivider,
        ContextMenuGroup("Long group") {
            List(50) {
                ContextMenuItem("Nested item ${it + 1}") {}
            }
        },
        ContextMenuGroup("Nested group") {
            List(5) { index ->
                val groupName = 'A' + index
                ContextMenuGroup("Inner group $groupName") {
                    List(5) { index ->
                        MaterialContextMenuItem(
                            label = "Item $groupName${index + 1}",
                            onClick = {},
                        )
                    }
                }
            }
        },
        ContextMenuDivider,
        object : GenericContextMenuItem() {
            @Composable
            override fun Content(onDismissRequest: () -> Unit, params: ContextMenuParams, modifier: Modifier) {
                val text = remember { mutableStateOf("Custom text field item") }
                OutlinedTextField(
                    value = text.value,
                    onValueChange = { text.value = it },
                    modifier = Modifier.padding(4.dp),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = params.colors.icon)
                    },
                )
            }
        },
    )
}
