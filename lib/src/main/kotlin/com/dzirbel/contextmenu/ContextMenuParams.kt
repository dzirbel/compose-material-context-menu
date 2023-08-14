package com.dzirbel.contextmenu

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Wrapper class containing configuration options for [MaterialContextMenuRepresentation].
 */
data class ContextMenuParams(
    val measurements: ContextMenuMeasurements,

    val colors: ContextMenuColors,

    /**
     * Whether a scrollbar should be shown when the height of the context menu exceeds the height of the window.
     *
     * If false, the overflowing content menu will still be scrollable, but no scrollbar will be visible.
     */
    val showScrollbarOnOverFlow: Boolean = true,
)

data class ContextMenuMeasurements(
    /**
     * Minimum width of the context menu; by default 112 dp per the Material spec.
     */
    val minWidth: Dp = 112.dp,

    /**
     * Maximum width of the context menu; by default 280 dp per the Material spec.
     */
    val maxWidth: Dp = 280.dp,

    /**
     * Minimum height of each item in the context menu; by default 48 dp per the Material spec.
     */
    val itemMinHeight: Dp = 48.dp,

    /**
     * Padding applied to the top of the menu; by default 8 dp per the Material spec.
     */
    val menuTopPadding: Dp = 8.dp,

    /**
     * Padding applied to the top of the menu; by default 8 dp per the Material spec.
     */
    val menuBottomPadding: Dp = 8.dp,

    /**
     * Padding applied to each context menu item; by default 12 dp horizontally per the Material spec and 8 dp
     * vertically (not specified by Material but a reasonable default for cases where the item height is large).
     */
    val itemPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 8.dp),

    /**
     * Size of item icons; by default 24 dp per the Material spec.
     */
    val iconSize: Dp = 24.dp,

    /**
     * Padding between [MaterialContextMenuItem] icons and text; by default 12 dp per the Material spec.
     */
    val iconPadding: Dp = 12.dp,

    /**
     * Elevation of the context menu.
     */
    val elevation: Dp = 8.dp,

    /**
     * Required margin between the context menu and the edge of the window.
     *
     * TODO also allow specifying maximum height of the context menu relative to the window to match the material spec's
     *  recommendation that at least one item is left below
     */
    val windowMargin: Dp = 4.dp,

    /**
     * [Shape] of the context menu; by default a rounded rectangle with 4 dp corners per the Material spec.
     */
    val popupShape: Shape = RoundedCornerShape(4.dp),

    /**
     * Height of the visible [ContextMenuDivider] line; by default 1 dp per the Material spec.
     */
    val dividerLineHeight: Dp = 1.dp,

    /**
     * Height occupied by the [ContextMenuDivider]; by default 16 dp per the Material spec.
     */
    val dividerHeight: Dp = 16.dp,
)

data class ContextMenuColors(
    /**
     * [Color] of the menu surface background.
     */
    val surface: Color,

    /**
     * [Color] of menu item text.
     */
    val text: Color,

    /**
     * [Color] of menu icons.
     */
    val icon: Color,

    /**
     * [Color] of menu dividers.
     */
    val divider: Color,
) {
    constructor(materialColors: Colors) : this(
        surface = materialColors.surface,
        text = materialColors.onSurface,
        icon = materialColors.onSurface,
        // divider color is not strongly specified by Material 2, but 12% opacity appears to be the default
        divider = materialColors.onSurface.copy(alpha = 0.12f),
    )
}
