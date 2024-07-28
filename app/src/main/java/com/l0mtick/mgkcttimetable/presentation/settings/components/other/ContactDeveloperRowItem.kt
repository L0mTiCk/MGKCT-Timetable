package com.l0mtick.mgkcttimetable.presentation.settings.components.other

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.l0mtick.mgkcttimetable.R

@Composable
fun ContactDeveloperRowItem() {
    val context = LocalContext.current
    OtherSectionRowBase(
        onRowClick = {
            Firebase.analytics.logEvent("contact_dev_pressed") { }
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "text/plain"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("blashkoaleksej@duck.com"))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Тема")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Сообщение")
            ContextCompat.startActivity(
                context,
                Intent.createChooser(emailIntent, "Send email"),
                null
            )
        },
        title = stringResource(id = R.string.settings_contact_title),
        subtitle = stringResource(id = R.string.settings_contact_main)
    ) {
        Icon(
            imageVector = Icons.Outlined.Email,
            contentDescription = "Mail icon",
            modifier = Modifier.size(25.dp)
        )
    }
}