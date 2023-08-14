package com.dzirbel.contextmenu

import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

/**
 * A [ContextMenuItem] with additional properties for Material styling.
 *
 * @param label the text label of this item
 * @param onClick callback invoked when this item is clicked
 * @param enabled whether the item is enabled (clickable and not faded)
 * @param leadingIcon the [ContextMenuIcon] displayed at the start of the item
 * @param trailingIcon the [ContextMenuIcon] displayed at the end of the item
 */
@Suppress("OutdatedDocumentation")
open class MaterialContextMenuItem(
    label: String,
    onClick: () -> Unit,
    val enabled: Boolean = true,
    val leadingIcon: ContextMenuIcon? = null,
    val trailingIcon: ContextMenuIcon? = null,
) : CustomContentContextMenuItem(label = label, onClick = onClick) {
    override val clickable: Boolean
        get() = enabled

    /**
     * Displays the [label] of this item as a [Text] Composable; may be overridden for custom text styling.
     */
    @Composable
    open fun Text(modifier: Modifier = Modifier) {
        Text(label, modifier = modifier)
    }

    @Composable
    override fun Content(onDismissRequest: () -> Unit, params: ContextMenuParams) {
        val contentAlpha = if (enabled) LocalContentAlpha.current else ContentAlpha.disabled
        CompositionLocalProvider(LocalContentAlpha provides contentAlpha) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(params.measurements.iconPadding),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leadingIcon?.IconContent(params)

                Text(Modifier.weight(1f))

                trailingIcon?.IconContent(params)
            }
        }
    }
}

/**
 * A [ContextMenuItem] which provides its own Composable [Content].
 *
 * Standard styling is applied to the item, including padding, sizing, and clickable behavior (enabled if [clickable] is
 * true). For complete control over the item, use [GenericContextMenuItem].
 */
abstract class CustomContentContextMenuItem(onClick: () -> Unit, label: String = "") :
    ContextMenuItem(label = label, onClick = onClick) {

    open val clickable: Boolean = true

    /**
     * The Composable content of this item, wrapped inside a layout with standard styling.
     *
     * @param onDismissRequest callback which may be invoked to close the context menu; provided for items which may
     *  close the menu on conditions in addition to the standard click handling
     * @param params [ContextMenuParams] provided to the context menu (to be optionally used for padding, etc)
     */
    @Composable
    abstract fun Content(onDismissRequest: () -> Unit, params: ContextMenuParams)
}

/**
 * A [ContextMenuItem] which provides its own Composable [Content] and has no generic styling applied to it.
 */
abstract class GenericContextMenuItem : ContextMenuItem(
    label = "",
    onClick = { error("generic item should not be clickable") },
) {
    /**
     * The Composable content of this item.
     *
     * @param onDismissRequest callback which may be invoked to close the context menu; provided for items which
     *  implement their own click handling (e.g. to close the menu on click)
     * @param params [ContextMenuParams] provided to the context menu (to be optionally used for padding, etc)
     * @param modifier [Modifier] which must be applied to the root element (in order for hover states of nested menus
     *  to work properly)
     */
    @Composable
    abstract fun Content(onDismissRequest: () -> Unit, params: ContextMenuParams, modifier: Modifier)
}

/**
 * A [ContextMenuItem] which displays a nested dropdown provided by [items].
 */
open class ContextMenuGroup(
    label: String,
    val items: () -> List<ContextMenuItem>,
) : ContextMenuItem(label = label, onClick = { error("group should not be clickable") }) {
    /**
     * The icon displayed at the end of the item; by default an arrow pointing to the right.
     */
    @Composable
    open fun EndIcon(params: ContextMenuParams) {
        Icon(painter = painterResource("arrow_right.svg"), contentDescription = "Expand", tint = params.colors.icon)
    }
}

/**
 * A [ContextMenuItem] which displays a simple divider between items.
 */
object ContextMenuDivider : ContextMenuItem(
    label = "",
    onClick = { error("divider should not be clickable") },
)
