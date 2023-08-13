package com.dzirbel.contextmenu

import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.ContextMenuRepresentation
import androidx.compose.foundation.ContextMenuState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberPopupPositionProviderAtPosition

@Suppress("MagicNumber")
data class ContextMenuParams(
    val minWidth: Dp = 112.dp,
    val maxWidth: Dp = 280.dp,
    val itemMinHeight: Dp = 48.dp,
    val padding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    val iconPadding: Dp = 20.dp,
    val elevation: Dp = 8.dp,
    val windowMargin: Dp = itemMinHeight,
    val popupShape: Shape = RoundedCornerShape(4.dp),
    val dividerHeight: Dp = 1.dp, // TODO optional additional padding above/below divider
    // TODO default colors from material theme
    val dividerColor: Color = Color.Gray,
    val backgroundColor: Color = Color.White,
)

// TODO height of dropdowns that take up the entire window is a bit too much
// TODO clicking a menu group item closes the dropdown
@OptIn(ExperimentalComposeUiApi::class)
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
