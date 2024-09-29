package com.l0mtick.mgkcttimetable.presentation.widget

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.Visibility
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.visibility
import com.l0mtick.mgkcttimetable.R
import com.l0mtick.mgkcttimetable.domain.model.schedule.DaySchedule
import com.l0mtick.mgkcttimetable.domain.model.schedule.Lesson
import com.l0mtick.mgkcttimetable.domain.model.schedule.ScheduleUnion
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository
import com.l0mtick.mgkcttimetable.ui.theme.GlanceCustomTheme
import org.koin.compose.koinInject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimetableWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme(
                colors = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    GlanceTheme.colors
                else
                    GlanceCustomTheme.colors
            ) {
                WidgetContent()
            }
        }
    }

    @Composable
    fun WidgetContent(
        modifier: Modifier = Modifier,
        scheduleRepository: ScheduleRepository = koinInject(),
    ) {
        val scheduleRowTextStyle = TextStyle(
            color = GlanceTheme.colors.onBackground,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        val context = androidx.glance.LocalContext.current
        val settingsIntent by remember {
            mutableStateOf(WidgetIntents.createSettingsIntent(context))
        }
        val groupIntent by remember {
            mutableStateOf(WidgetIntents.createGroupIntent(context))
        }
        val teacherIntent by remember {
            mutableStateOf(WidgetIntents.createTeacherIntent(context))
        }

        var daySchedule by remember {
            mutableStateOf(DaySchedule())
        }

        var header by remember {
            mutableStateOf("")
        }

        var isUpdating by remember {
            mutableStateOf(true)
        }

        LaunchedEffect(key1 = isUpdating) {
            if (isUpdating) {
                val localDate =
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                header = scheduleRepository.getWidgetHeader()
                val weekSchedule =
                    scheduleRepository.parseWidgetTimetable()
                weekSchedule.days?.forEach { day ->
                    //TODO: remove hardcoded date
                    if (day.date == localDate) {
                        daySchedule = day
                        isUpdating = false
                        Log.d("widget_test", daySchedule.toString())
                    }
                }
                isUpdating = false
            }
        }


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
                    text = if (header.isNotEmpty() && header.isDigitsOnly()) "Группа $header" else header,
                    modifier = GlanceModifier
                        .padding(8.dp)
                        .defaultWeight()
                        .clickable {
                            if (scheduleRepository.isWidgetGroupSelected())
                                groupIntent.send()
                            else
                                teacherIntent.send()
                        }
                        .cornerRadius(8.dp),
                    style = TextStyle(
                        color = GlanceTheme.colors.onBackground,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1
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
                        isUpdating = true
                    },
                    contentColor = GlanceTheme.colors.onSecondaryContainer,
                    backgroundColor = GlanceTheme.colors.background,
                    modifier = GlanceModifier
                        .size(36.dp)
                )

            }
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .cornerRadius(16.dp)
                    .background(GlanceTheme.colors.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = GlanceModifier
                        .visibility(if (!isUpdating) Visibility.Visible else Visibility.Invisible),
                ) {
                    if (daySchedule.lessons.isNullOrEmpty()) {
                        Text(
                            text = context.getString(R.string.screen_no_lessons),
                            style = scheduleRowTextStyle
                        )
                    } else {
                        LazyColumn(
                            modifier = GlanceModifier
                                .fillMaxWidth()
                                .background(GlanceTheme.colors.secondaryContainer)
                                .cornerRadius(16.dp)
                        ) {
                            if (!daySchedule.lessons.isNullOrEmpty()) {
                                items(daySchedule.lessons!!) { lesson ->
                                    if (lesson != null)
                                        Row(
                                            modifier = GlanceModifier
                                                .padding(horizontal = 8.dp, vertical = 8.dp)
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.Vertical.CenterVertically
                                        ) {
                                            when (lesson) {
                                                is ScheduleUnion.LessonValue -> {
                                                    Text(
                                                        text = lesson.value.number.toString() + ".",
                                                        style = scheduleRowTextStyle,
                                                        modifier = GlanceModifier
                                                            .padding(end = 6.dp)
                                                    )
                                                    GlanceLessonRow(
                                                        lesson = lesson.value,
                                                        scheduleRowTextStyle = scheduleRowTextStyle
                                                    )
                                                }

                                                is ScheduleUnion.LessonArrayValue -> {
                                                    Text(
                                                        text = lesson.value[0].number.toString() + ".",
                                                        style = scheduleRowTextStyle,
                                                        modifier = GlanceModifier
                                                            .padding(end = 6.dp)
                                                    )
                                                    Column {
                                                        lesson.value.forEach {
                                                            GlanceLessonRow(
                                                                lesson = it,
                                                                scheduleRowTextStyle = scheduleRowTextStyle
                                                            )
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                }
                            }
                        }
                    }
                }
                Box(
                    modifier = GlanceModifier
                        .background(GlanceTheme.colors.widgetBackground)
                        .cornerRadius(6.dp)
                        .size(32.dp)
                        .visibility(if (isUpdating) Visibility.Visible else Visibility.Invisible),

                    ) {
                    CircularProgressIndicator(
                        modifier = GlanceModifier
                            .padding(6.dp),
                        color = GlanceTheme.colors.onSecondaryContainer
                    )
                }
            }
        }
    }
}

class TimetableWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = TimetableWidget()
}

@Composable
fun GlanceLessonRow(lesson: Lesson, scheduleRowTextStyle: TextStyle) {
    Row(
        modifier = GlanceModifier.fillMaxWidth(),
        verticalAlignment = Alignment.Vertical.CenterVertically
    ) {
        Text(
            text = getLessonString(lesson),
            modifier = GlanceModifier
                .padding(horizontal = 2.dp)
                .defaultWeight(),
            style = scheduleRowTextStyle
        )
        Text(
            text = lesson.cabinet ?: "-",
            style = scheduleRowTextStyle
        )
    }
}

private fun getLessonString(lesson: Lesson): String {
    val subgroupString = if (lesson.subgroup != null) {
        "${lesson.subgroup}. "
    } else {
        ""
    }
    val teacherString = if (lesson.teacher != null) {
        "\n${lesson.teacher}"
    } else {
        ""
    }
    val commentString = if (teacherString == "" && !lesson.comment.isNullOrEmpty())
        "\n${(lesson.comment)}"
    else
        "  ${(lesson.comment ?: "")}"
    val mainString = subgroupString +
            lesson.name.toString() +
            " (${lesson.type ?: ""})" +
            teacherString +
            commentString
    return mainString
}