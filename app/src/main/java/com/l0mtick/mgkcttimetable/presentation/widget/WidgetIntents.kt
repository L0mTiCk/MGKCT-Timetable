package com.l0mtick.mgkcttimetable.presentation.widget

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

object WidgetIntents {
    fun createSettingsIntent(context: Context): PendingIntent {
        val intent = Intent(
            Intent.ACTION_VIEW,
            "mgkcttimetable://settings".toUri()
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun createGroupIntent(context: Context): PendingIntent {
        val intent = Intent(
            Intent.ACTION_VIEW,
            "mgkcttimetable://group".toUri()
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun createTeacherIntent(context: Context): PendingIntent {
        val intent = Intent(
            Intent.ACTION_VIEW,
            "mgkcttimetable://teacher".toUri()
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
