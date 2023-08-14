package com.dzirbel.contextmenu

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
 * An [IconButton] which opens a context menu with the given [items] when clicked.
 *
 * TODO get items from LocalContextMenuItems, but it is private
 */
@Composable
fun ContextMenuOverflowButton(
    items: () -> List<ContextMenuItem>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    state: ContextMenuState = remember { ContextMenuState() },
) {
    IconButton(
        modifier = modifier,
        enabled = enabled,
        onClick = { state.status = ContextMenuState.Status.Open(rect = Rect.Zero) },
    ) {
        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
        CompositionLocalProvider(LocalContextMenuButtonAnchor provides true) {
            LocalContextMenuRepresentation.current.Representation(state, items)
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
