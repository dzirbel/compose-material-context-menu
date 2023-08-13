package com.dzirbel.contextmenu

import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.ContextMenuRepresentation
import androidx.compose.foundation.ContextMenuState
import androidx.compose.foundation.LocalContextMenuRepresentation
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.rememberPopupPositionProviderAtPosition

/**
 * A [ContextMenuRepresentation] which augments the default [ContextMenuPopup] with additional features.
 *
 * In particular, it supports theming via the properties of [ContextMenuParams] and additional item types including
 * dividers ([ContextMenuDivider]), nested context menu groups ([ContextMenuGroup]), and items augmented with enabled
 * state, icons, etc. ([AugmentedContextMenuItem]).
 *
 * It can be applied by providing it for the [LocalContextMenuRepresentation], i.e.:
 *
 *   CompositionLocalProvider(LocalContextMenuRepresentation provides AugmentedContextMenuRepresentation()) { ... }
 *
 * TODO default colors from material theme
 * TODO height of dropdowns that take up the entire window is a bit too much
 * TODO clicking a menu group item closes the dropdown
 */
@ExperimentalComposeUiApi
class AugmentedContextMenuRepresentation(
    private val params: ContextMenuParams = ContextMenuParams(),
) : ContextMenuRepresentation {
    @Composable
    override fun Representation(state: ContextMenuState, items: () -> List<ContextMenuItem>) {
        val status = state.status
        if (status is ContextMenuState.Status.Open) {
            ContextMenuPopup(
                params = params,
                popupPositionProvider = rememberPopupPositionProviderAtPosition(
                    positionPx = status.rect.topLeft, // rect is 0x0 at the mouse position
                    windowMargin = params.windowMargin,
                ),
                onDismissRequest = { state.status = ContextMenuState.Status.Closed },
                items = items,
            )
        }
    }
}
