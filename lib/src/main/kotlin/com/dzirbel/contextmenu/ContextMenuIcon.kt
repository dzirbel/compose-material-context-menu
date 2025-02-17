package com.dzirbel.contextmenu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.dzirbel.contextmenu.ContextMenuIcon.Empty
import com.dzirbel.contextmenu.ContextMenuIcon.OfShortcuts
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import java.util.Locale

/**
 * Wrapper around the various types of icons and other content that can be displayed at the start or end of a
 * [MaterialContextMenuItem].
 *
 * These are generally icons (via [Painter]s, resource paths, [ImageVector]s, or [ImageBitmap]s), but can also be any
 * kind of content. To align the text of icon-less items with those that have an icon, [Empty] can be used. Keyboard
 * shortcuts associated with the item (copy, paste, tc) may be provided by [OfShortcuts]. Finally, As a `fun interface`
 * it is convenient to provide ad-hac lambdas for custom [IconContent].
 */
fun interface ContextMenuIcon {
    /**
     * Renders the content of the icon, given the [params] of the context menu (for styling).
     */
    @Composable
    fun IconContent(params: ContextMenuParams)

    /**
     * An empty area with the same size as other icons ([ContextMenuMeasurements.iconSize]).
     */
    object Empty : ContextMenuIcon {
        @Composable
        override fun IconContent(params: ContextMenuParams) {
            Box(Modifier.size(params.measurements.iconSize))
        }
    }

    /**
     * Renders an [Icon] with the given [Painter], and optional [contentDescription] and [tint] overriding the default
     * icon color [ContextMenuColors.icon].
     */
    data class OfPainter(
        val painter: Painter,
        val contentDescription: String? = null,
        val tint: Color? = null,
    ) : ContextMenuIcon {
        @Composable
        override fun IconContent(params: ContextMenuParams) {
            Icon(
                painter = painter,
                contentDescription = contentDescription,
                modifier = Modifier.size(params.measurements.iconSize),
                tint = tint ?: params.colors.icon,
            )
        }
    }

    /**
     * Renders an [Icon] with the [Painter] loaded from the given [resource], and optional [contentDescription] and
     * [tint] overriding the default icon color [ContextMenuColors.icon].
     */
    data class OfResource(
        val resource: DrawableResource,
        val contentDescription: String? = null,
        val tint: Color? = null,
    ) : ContextMenuIcon {
        @Composable
        override fun IconContent(params: ContextMenuParams) {
            Icon(
                painter = painterResource(resource),
                contentDescription = contentDescription,
                modifier = Modifier.size(params.measurements.iconSize),
                tint = tint ?: params.colors.icon,
            )
        }
    }

    /**
     * Renders an [Icon] with the given [ImageVector], and optional [contentDescription] and [tint] overriding the
     * default icon color [ContextMenuColors.icon].
     */
    data class OfVector(
        val imageVector: ImageVector,
        val contentDescription: String? = null,
        val tint: Color? = null,
    ) : ContextMenuIcon {
        @Composable
        override fun IconContent(params: ContextMenuParams) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                modifier = Modifier.size(params.measurements.iconSize),
                tint = tint ?: params.colors.icon,
            )
        }
    }

    /**
     * Renders an [Icon] with the given [ImageBitmap], and optional [contentDescription] and [tint] overriding the
     * default icon color [ContextMenuColors.icon].
     */
    data class OfBitmap(
        val imageBitmap: ImageBitmap,
        val contentDescription: String? = null,
        val tint: Color? = null,
    ) : ContextMenuIcon {
        @Composable
        override fun IconContent(params: ContextMenuParams) {
            Icon(
                bitmap = imageBitmap,
                contentDescription = contentDescription,
                modifier = Modifier.size(params.measurements.iconSize),
                tint = tint ?: params.colors.icon,
            )
        }
    }

    /**
     * Renders shortcut texts for the given [shortcuts], with optional [color] overriding the default shortcut text
     * color [ContextMenuColors.shortcutText].
     */
    data class OfShortcuts(val shortcuts: List<ContextMenuShortcut>, val color: Color? = null) : ContextMenuIcon {
        constructor(vararg shortcuts: ContextMenuShortcut) : this(shortcuts.toList())

        @Composable
        override fun IconContent(params: ContextMenuParams) {
            Text(shortcuts.joinToString(separator = " "), color = color ?: params.colors.shortcutText)
        }
    }
}

/**
 * Represents a keyboard shortcut, which can be included as the leading or, typically, trailing icon of a
 * [MaterialContextMenuItem].
 *
 * This is not a perfect representation of keyboard shortcuts, but is intended to be a simple and readable for the
 * common cases. It attempts to use Mac-style symbols for modifier keys on Mac, and the Windows-style names for modifier
 * keys elsewhere.
 */
data class ContextMenuShortcut(
    val key: String,
    val ctrl: Boolean = true,
    val alt: Boolean = false,
    val shift: Boolean = false,
) {
    override fun toString(): String {
        return if (isMac) {
            buildString {
                if (alt) append("⌥")
                if (shift) append("⇧")
                if (ctrl) append("⌘")
                append(key)
            }
        } else {
            buildString {
                if (ctrl) append("Ctrl+")
                if (alt) append("Alt+")
                if (shift) append("Shift+")
                append(key)
            }
        }
    }
}

private val isMac by lazy { System.getProperty("os.name").lowercase(Locale.getDefault()).contains("mac") }
