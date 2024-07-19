package com.l0mtick.mgkcttimetable.presentation.widget

import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.l0mtick.mgkcttimetable.R

object TimetableWidget: GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent { 
            WidgetContent()
        }
    }

    @Composable
    fun WidgetContent(modifier: Modifier = Modifier, header: String = "Богдановская О.Н.") {

        val context = androidx.glance.LocalContext.current
        val settingsIntent = createSettingsIntent(context)

        val scheduleRowTextStyle = TextStyle(
            color = GlanceTheme.colors.onBackground,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Column(
            modifier = GlanceModifier
                .background(GlanceTheme.colors.widgetBackground)
                .height(180.dp)
        ) {
            Row(
                modifier = GlanceModifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = header,
                    modifier = GlanceModifier
                        .padding(8.dp)
                        .defaultWeight()
                        .clickable {
                            //TODO: navigate to schedule screen
                        }
                        .cornerRadius(8.dp),
                    style = TextStyle(
                        color = GlanceTheme.colors.onBackground,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                CircleIconButton(
                    imageProvider = ImageProvider(
                        R.drawable.outline_settings_24
                    ),
                    contentDescription = "Settings button",
                    onClick = {
                        settingsIntent.send()
                    },
                    contentColor = GlanceTheme.colors.onSecondaryContainer,
                    backgroundColor = GlanceTheme.colors.background,
                    modifier = GlanceModifier
                        .size(36.dp)
                )
                Spacer(
                    modifier = GlanceModifier
                        .width(6.dp)
                )
                CircleIconButton(
                    imageProvider = ImageProvider(
                        R.drawable.rounded_refresh_24
                    ),
                    contentDescription = "Update button",
                    onClick = {
                        //TODO
                    },
                    contentColor = GlanceTheme.colors.onSecondaryContainer,
                    backgroundColor = GlanceTheme.colors.background,
                    modifier = GlanceModifier
                        .size(36.dp)
                )
                
            }
            LazyColumn(
                modifier = GlanceModifier
                    .background(GlanceTheme.colors.secondaryContainer)
                    .cornerRadius(16.dp)
            ) {
                repeat(6){
                    item {
                        Row(
                            modifier = GlanceModifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.Horizontal.Start
                        ) {
                            Text(
                                text = "1.",
                                style = scheduleRowTextStyle
                            )
                            Text(
                                text = "КПИЯП (ЭКЗ)",
                                modifier = GlanceModifier
                                    .padding(horizontal = 2.dp)
                                    .defaultWeight(),
                                style = scheduleRowTextStyle
                            )
                            Text(
                                text = "3-212",
                                style = scheduleRowTextStyle
                            )
                        }
                    }
                }
            }
        }
    }
}

class TimetableWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = TimetableWidget
}