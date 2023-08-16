package com.dzirbel.contextmenu

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.ContextMenuState
import androidx.compose.foundation.LocalContextMenuRepresentation
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect

/**
 * An [IconButton] which opens a context menu when clicked.
 *
 * The context menu is populated by the composition-local data provided to
 * [androidx.compose.foundation.ContextMenuDataProvider]; additional items may be added via [items]. This allows reusing
 * items in a [ContextMenuArea] and [ContextMenuOverflowButton]; for example:
 *
 *   ContextMenuDataProvider(items = { listOf(item1, item2, item3) }) {
 *     ContextMenuArea(items = { emptyList() }) {
 *       // ... regular content
 *       ContextMenuOverflowButton()
 *     }
 *   }
 */
@Composable
fun ContextMenuOverflowButton(
    modifier: Modifier = Modifier,
    items: () -> List<ContextMenuItem> = { emptyList() },
    enabled: Boolean = true,
    state: ContextMenuState = remember { ContextMenuState() },
) {
    CompositionLocalProvider(LocalContextMenuButtonAnchor provides true) {
        ContextMenuArea(
            items = items,
            enabled = false, // do not open on right click
            state = state,
        ) {
            IconButton(
                modifier = modifier,
                enabled = enabled,
                onClick = { state.status = ContextMenuState.Status.Open(rect = Rect.Zero) },
            ) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
            }
        }
    }
}

/**
 * A simple [CompositionLocal] which provides whether the current [LocalContextMenuRepresentation] is being opened from
 * a [ContextMenuOverflowButton].
 *
 * If true, the context menu should position itself relative to the anchor rather than the cursor.
 */
val LocalContextMenuButtonAnchor = staticCompositionLocalOf { false }
