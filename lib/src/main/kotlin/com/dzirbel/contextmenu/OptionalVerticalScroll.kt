package com.dzirbel.contextmenu

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

/**
 * Adds a vertical scrollbar to [content] either when necessary, always if [includeScrollbarWhenUnused] is true, or
 * never if [includeScrollbarWhenUsed] is false.
 */
@Composable
internal fun OptionalVerticalScroll(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    includeScrollbarWhenUnused: Boolean = false,
    includeScrollbarWhenUsed: Boolean = true,
    content: @Composable () -> Unit,
) {
    val adapter = rememberScrollbarAdapter(scrollState)
    Layout(
        modifier = modifier,
        measurePolicy = { measurables, constraints ->
            val contentMeasurable = measurables[0]
            val needScrollbar = includeScrollbarWhenUsed &&
                (includeScrollbarWhenUnused || adapter.contentSize > adapter.viewportSize)

            if (needScrollbar) {
                val scrollbarMeasurable = measurables[1]
                val scrollbarPlaceable = scrollbarMeasurable.measure(constraints)
                val contentPlaceable = contentMeasurable.measure(
                    constraints.copy(maxWidth = constraints.maxWidth - scrollbarPlaceable.width),
                )

                layout(contentPlaceable.width + scrollbarPlaceable.width, contentPlaceable.height) {
                    contentPlaceable.place(0, 0)
                    scrollbarPlaceable.place(contentPlaceable.width, 0)
                }
            } else {
                val contentPlaceable = contentMeasurable.measure(constraints)

                layout(contentPlaceable.width, contentPlaceable.height) {
                    contentPlaceable.place(0, 0)
                }
            }
        },
        content = {
            content()
            VerticalScrollbar(adapter = adapter)
        },
    )
}
