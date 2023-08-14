package com.dzirbel.contextmenu

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text.LocalTextContextMenu
import androidx.compose.foundation.text.TextContextMenu
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLocalization

/**
 * Provides [MaterialContextMenuItem] items with icons and shortcuts for text field context menus, making them slightly
 * prettier.
 *
 * It can be applied by providing it for the [LocalTextContextMenu], i.e.:
 *
 *   CompositionLocalProvider(LocalTextContextMenu provides MaterialTextContextMenu) { ... }
 */
@ExperimentalFoundationApi
object MaterialTextContextMenu : TextContextMenu {
    @Composable
    override fun Area(
        textManager: TextContextMenu.TextManager,
        state: ContextMenuState,
        content: @Composable () -> Unit,
    ) {
        val localization = LocalLocalization.current
        val items = {
            listOfNotNull(
                textManager.cut?.let {
                    MaterialContextMenuItem(
                        label = localization.cut,
                        onClick = it,
                        leadingIcon = ContextMenuIcon.OfPainterResource("content_cut.svg"),
                        trailingIcon = ContextMenuIcon.OfShortcuts(ContextMenuShortcut("X")),
                    )
                },
                textManager.copy?.let {
                    MaterialContextMenuItem(
                        label = localization.copy,
                        onClick = it,
                        leadingIcon = ContextMenuIcon.OfPainterResource("content_copy.svg"),
                        trailingIcon = ContextMenuIcon.OfShortcuts(ContextMenuShortcut("C")),
                    )
                },
                textManager.paste?.let {
                    MaterialContextMenuItem(
                        label = localization.paste,
                        onClick = it,
                        leadingIcon = ContextMenuIcon.OfPainterResource("content_paste.svg"),
                        trailingIcon = ContextMenuIcon.OfShortcuts(ContextMenuShortcut("V")),
                    )
                },
                textManager.selectAll?.let {
                    MaterialContextMenuItem(
                        label = localization.selectAll,
                        onClick = it,
                        // an empty icon would be wonky if there were no other items, but that should never be the case
                        leadingIcon = ContextMenuIcon.Empty,
                    )
                },
            )
        }

        ContextMenuArea(items, state, content = content)
    }
}
