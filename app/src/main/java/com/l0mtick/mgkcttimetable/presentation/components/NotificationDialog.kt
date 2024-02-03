package com.l0mtick.mgkcttimetable.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.l0mtick.mgkcttimetable.R

@Composable
fun NotificationDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        shape = MaterialTheme.shapes.large,
        title = {
            Text(text = stringResource(id = R.string.notification_dialog_header))
        },
        text = {
            Text(text = stringResource(id = R.string.notification_dialog_main))
        },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text(text = stringResource(id = R.string.proceed_text))
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    )
}

@Preview
@Composable
fun NotificationDialogPreview() {
    NotificationDialog {

    }
}