package com.dzirbel.contextmenu

import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.ContextMenuRepresentation
import androidx.compose.foundation.ContextMenuState
import androidx.compose.foundation.LocalContextMenuRepresentation
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.rememberPopupPositionProviderAtPosition

/**
 * Creates a [MaterialContextMenuRepresentation] with the given arguments.
 *
 * Exists as a standalone [Composable] function to provide the composition-local [MaterialTheme] colors for the
 * [ContextMenuColors].
 */
@ExperimentalComposeUiApi
@Composable
@Suppress("ComposableNaming")
fun MaterialContextMenuRepresentation(
    measurements: ContextMenuMeasurements = ContextMenuMeasurements(),
    colors: ContextMenuColors = ContextMenuColors(MaterialTheme.colors),
    showScrollbarOnOverFlow: Boolean = true,
): ContextMenuRepresentation {
    return MaterialContextMenuRepresentation(
        params = ContextMenuParams(
            measurements = measurements,
            colors = colors,
            showScrollbarOnOverFlow = showScrollbarOnOverFlow,
        ),
    )
}

/**
 * A [ContextMenuRepresentation] which applies theming as specified for Material 2 Menus and augments the default
 * [ContextMenuPopup] with additional features.
 *
 * In particular, it supports additional [ContextMenuItem] types including dividers ([ContextMenuDivider]), nested
 * context menu groups ([ContextMenuGroup]), standard material menu items (with optional icons, shortcuts, etc)
 * ([MaterialContextMenuItem]) and item types for complete customization ([CustomContentContextMenuItem] and
 * [GenericContextMenuItem]).
 *
 * It can be applied by providing it for the [LocalContextMenuRepresentation], i.e.:
 *
 *   CompositionLocalProvider(LocalContextMenuRepresentation provides MaterialContextMenuRepresentation()) { ... }
 *
 * Note that it should be applied _after_ [MaterialTheme] so that the appropriate material colors are used. It can also
 * be used without [MaterialTheme], in which case customizing the [ContextMenuColors] is recommended.
 *
 * TODO height of dropdowns that take up the entire window is a bit too much
 * TODO clicking a menu group item closes the dropdown
 * TODO provide material context items for text fields
 */
@ExperimentalComposeUiApi
class MaterialContextMenuRepresentation(private val params: ContextMenuParams) : ContextMenuRepresentation {
    @Composable
    override fun Representation(state: ContextMenuState, items: () -> List<ContextMenuItem>) {
        val status = state.status
        if (status is ContextMenuState.Status.Open) {
            ContextMenuPopup(
                params = params,
                popupPositionProvider = rememberPopupPositionProviderAtPosition(
                    positionPx = status.rect.topLeft, // rect is 0x0 at the mouse position
                    windowMargin = params.measurements.windowMargin,
                ),
                onDismissRequest = { state.status = ContextMenuState.Status.Closed },
                items = items,
            )
        }
    }
}
