package com.l0mtick.mgkcttimetable.presentation.settings.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.l0mtick.mgkcttimetable.presentation.settings.SettingsEvent

@Composable
fun AppInfoDialogBox(onEvent: (SettingsEvent) -> Unit) {
    AlertDialog(
        onDismissRequest = {
            onEvent(SettingsEvent.OnDialogDismiss)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onEvent(SettingsEvent.OnDialogDismiss)
                },
            ) {
                Text("Confirm")
            }
        },
        title = {
            Text(text = "Внимание!")
        },
        icon = {
            Icon(imageVector = Icons.TwoTone.Warning, contentDescription = "Warning sign")
        },
        text = {
            Text(
                text = "Это неофициальное приложение, предоставляющее расписание занятий МГКЦТ.\n" +
                        "\nПожалуйста, помните, что оно не имеет официальной связи с учебным заведением и не гарантирует 100% " +
                        "правильность расписания.\n\nP.S. Thx Keller18306 for API",
            )
        },
        shape = MaterialTheme.shapes.large,
    )
}