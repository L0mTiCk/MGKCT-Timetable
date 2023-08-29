package com.l0mtick.mgkcttimetable.presentation.settings.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun ContactDeveloperRowItem(context: Context) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "text/plain"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("blashkoaleksej@duck.com"))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Тема")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Сообщение")
            ContextCompat.startActivity(context, Intent.createChooser(emailIntent, "Send email"), null)
        }
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            Column {
                Text(
                    text = "Связаться с разработчиком",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Для связи выберете Gmail и отредактируйте текст в письме.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .8f)
                )
            }
        }
        Spacer(modifier = Modifier.width(15.dp))
        Icon(
            imageVector = Icons.Outlined.Email,
            contentDescription = "Mail icon",
            modifier = Modifier.size(25.dp)
        )
    }
}