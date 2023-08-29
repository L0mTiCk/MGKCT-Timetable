package com.l0mtick.mgkcttimetable.presentation.settings

data class SettingsState (
    val allGroups: List<String> = emptyList(),
    val selectedGroup: String = "",
    val isGroupMenuExpanded: Boolean = false,
    val isAppInfoDialogOpen: Boolean = false
)