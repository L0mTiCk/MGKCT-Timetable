package com.l0mtick.mgkcttimetable.presentation.settings

sealed interface SettingsEvent {

    data class OnSpecificGroupClick(val groupName: String): SettingsEvent
    data class OnSpecificTeacherClick(val teacherName: String): SettingsEvent
    object OnAppInfoClick: SettingsEvent
    object OnDialogDismiss: SettingsEvent
    object OnDataUpdate: SettingsEvent
    object OnNotificationClick: SettingsEvent
}