package com.l0mtick.mgkcttimetable.presentation.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.l0mtick.mgkcttimetable.presentation.settings.SettingsEvent

@Composable
fun AppInfoDialogBox(onEvent: (SettingsEvent) -> Unit) {
    Dialog(
        onDismissRequest = {
            onEvent(SettingsEvent.OnDialogDismiss)
        }
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Это неофициальное приложение, предоставляющее расписание занятий МГКЦТ.\n" +
                            "\nПожалуйста, помните, что оно не имеет официальной связи с учебным заведением и не гарантирует 100% " +
                            "правильность расписания.",
                )
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = {
                        onEvent(SettingsEvent.OnDialogDismiss)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}