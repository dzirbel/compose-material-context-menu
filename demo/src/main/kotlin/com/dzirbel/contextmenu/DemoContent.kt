package com.dzirbel.contextmenu

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuDataProvider
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
internal fun DemoContent(lightTheme: Boolean, setLightTheme: (Boolean) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(TITLE, style = MaterialTheme.typography.h5)

            IconButton(onClick = { setLightTheme(!lightTheme) }) {
                Icon(
                    painter = painterResource(if (lightTheme) "light_mode.svg" else "dark_mode.svg"),
                    contentDescription = "Toggle theme",
                )
            }
        }

        var text: String by remember { mutableStateOf("Text field") }
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        )

        val scrollState = rememberScrollState()
        Box {
            Column(Modifier.verticalScroll(scrollState).padding(16.dp)) {
                repeat(20) {
                    DemoItem(name = "Item ${it + 1}")
                }
            }

            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd),
                adapter = rememberScrollbarAdapter(scrollState),
            )
        }
    }
}

@Composable
private fun DemoItem(name: String) {
    ContextMenuDataProvider(items = ::demoItems) {
        ContextMenuArea(items = { emptyList() }) {
            Row(
                modifier = Modifier
                    .clickable {}
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ContextMenuOverflowButton()

                    Text(name)
                }

                ContextMenuOverflowButton()
            }
        }
    }
}
