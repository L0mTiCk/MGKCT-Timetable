package com.l0mtick.mgkcttimetable.presentation.settings

sealed interface SettingsEvent {

    data class OnSpecificGroupClick(val groupName: String): SettingsEvent

}