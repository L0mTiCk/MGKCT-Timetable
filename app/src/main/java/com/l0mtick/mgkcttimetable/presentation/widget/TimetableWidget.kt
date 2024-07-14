package com.l0mtick.mgkcttimetable.presentation.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

object TimetableWidget: GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent { 
            WidgetContent()
        }
    }

    @Composable
    fun WidgetContent(modifier: Modifier = Modifier) {

        val scheduleRowTextStyle = TextStyle(
            color = GlanceTheme.colors.onBackground,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Column(
            modifier = GlanceModifier
                .background(GlanceTheme.colors.widgetBackground)
        ) {
            Row(
                modifier = GlanceModifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Группа 63",
                    modifier = GlanceModifier
                        .padding(8.dp)
                        .defaultWeight()
                    ,
                    style = TextStyle(
                        color = GlanceTheme.colors.onBackground,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Button(text = "Update", onClick = {

                })
            }
            repeat(3){
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

class TimetableWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = TimetableWidget
}