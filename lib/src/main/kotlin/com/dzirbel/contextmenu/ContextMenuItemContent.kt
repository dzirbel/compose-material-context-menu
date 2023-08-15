package com.dzirbel.contextmenu

import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.Layout

@Composable
internal fun ContextMenuItemContent(
    item: ContextMenuItem,
    params: ContextMenuParams,
    onDismissRequest: () -> Unit,
    menuOpen: Boolean,
    modifier: Modifier = Modifier,
) {
    @Suppress("NamedArguments")
    when (item) {
        is CustomContentContextMenuItem -> CustomContentContextMenuItem(item, params, onDismissRequest, modifier)
        is GenericContextMenuItem -> GenericContextMenuItem(item, params, onDismissRequest, modifier)
        is ContextMenuGroup -> ContextMenuGroup(item, params, onDismissRequest, menuOpen, modifier)
        is ContextMenuDivider -> ContextMenuDivider(params, modifier)
        else -> DefaultContextMenuItem(item, params, onDismissRequest, modifier)
    }
}

@Composable
private fun CustomContentContextMenuItem(
    item: CustomContentContextMenuItem,
    params: ContextMenuParams,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .itemSize(params)
            .clickable(enabled = item.clickable) {
                onDismissRequest()
                item.onClick()
            }
            .padding(params.measurements.itemPadding),
        contentAlignment = Alignment.CenterStart,
    ) {
        item.Content(onDismissRequest = onDismissRequest, params = params)
    }
}

@Composable
private fun GenericContextMenuItem(
    item: GenericContextMenuItem,
    params: ContextMenuParams,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    item.Content(onDismissRequest = onDismissRequest, params = params, modifier = modifier)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ContextMenuGroup(
    item: ContextMenuGroup,
    params: ContextMenuParams,
    onDismissRequest: () -> Unit,
    menuOpen: Boolean,
    modifier: Modifier = Modifier,
) {
    // hacky, but works: re-uses the same enter interaction every time
    val enterInteraction = remember { HoverInteraction.Enter() }
    val clickableInteractionSource = remember { MutableInteractionSource() }
    LaunchedEffect(menuOpen) {
        clickableInteractionSource.emit(if (menuOpen) enterInteraction else HoverInteraction.Exit(enterInteraction))
    }

    val hoverInteractionSource = remember { MutableInteractionSource() }
    val hovering = hoverInteractionSource.collectIsHoveredAsState()

    Box(
        modifier = modifier
            .itemSize(params)
            // use clickable() to display hover indication (not provided by just hoverable()), but without any click
            // action; provide a custom interaction source which also adds a hover interaction when the menu is open
            .clickable(
                enabled = false,
                interactionSource = clickableInteractionSource,
                indication = LocalIndication.current,
                onClick = {},
            )
            .hoverable(hoverInteractionSource),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            // apply padding to the inner row so that the position of the nested menu is correct
            modifier = Modifier.fillMaxWidth().padding(params.measurements.itemPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(item.label, color = params.colors.text)
            item.EndIcon(params)
        }

        if (menuOpen) {
            ContextMenuPopup(
                params = params,
                popupPositionProvider = rememberNestedDropdownPositionProvider(
                    windowMargin = params.measurements.windowMargin,
                ),
                onDismissRequest = {
                    // prevent clicks on this item from closing the nested menu (and cascading to close all menus)
                    if (!hovering.value) onDismissRequest()
                },
                items = item.items,
            )
        }
    }
}

@Composable
private fun ContextMenuDivider(params: ContextMenuParams, modifier: Modifier = Modifier) {
    Layout(
        modifier = modifier
            .itemWidth(params)
            .drawWithContent {
                val width: Float = params.measurements.dividerLineHeight.toPx()
                val y: Float = (size.height - width) / 2
                drawLine(
                    color = params.colors.divider,
                    start = Offset(x = 0f, y = y),
                    end = Offset(x = size.width, y = y),
                    strokeWidth = width,
                )
            },
        measurePolicy = { _, constraints ->
            layout(width = constraints.minWidth, height = params.measurements.dividerHeight.roundToPx()) {}
        },
        content = {},
    )
}

@Composable
private fun DefaultContextMenuItem(
    item: ContextMenuItem,
    params: ContextMenuParams,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier
            .itemSize(params)
            .clickable {
                onDismissRequest()
                item.onClick()
            }
            .padding(params.measurements.itemPadding)
            .wrapContentHeight(), // center the text vertically
        text = item.label,
        color = params.colors.text,
    )
}

@Stable
private fun Modifier.itemWidth(params: ContextMenuParams): Modifier {
    return fillMaxWidth().widthIn(min = params.measurements.minWidth, max = params.measurements.maxWidth)
}

@Stable
private fun Modifier.itemHeight(params: ContextMenuParams) = heightIn(min = params.measurements.itemMinHeight)

@Stable
private fun Modifier.itemSize(params: ContextMenuParams) = itemWidth(params).itemHeight(params)
