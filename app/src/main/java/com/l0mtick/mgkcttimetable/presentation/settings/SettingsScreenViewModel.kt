package com.l0mtick.mgkcttimetable.presentation.settings

import android.app.Activity
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsScreenViewModel(private val scheduleRepository: ScheduleRepository) : ViewModel() {

    private val _state = MutableStateFlow(
        SettingsState(
            selectedGroup = scheduleRepository.getSavedGroup() ?: "",
            selectedTeacher = scheduleRepository.getSavedTeacher() ?: "",
            isNotificationsEnabled = scheduleRepository.getNotificationPermissionStatus(),
            isWidgetGroupSelected = scheduleRepository.isWidgetGroupSelected()
        )
    )

    val state = _state.asStateFlow()

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnSpecificGroupClick -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            selectedGroup = event.groupName
                        )
                    }
                    scheduleRepository.saveGroup(event.groupName)
                }
            }

            SettingsEvent.OnAppInfoClick -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isAppInfoDialogOpen = true
                        )
                    }
                }
            }

            SettingsEvent.OnDialogDismiss -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isAppInfoDialogOpen = false
                        )
                    }
                }
            }

            is SettingsEvent.OnSpecificTeacherClick -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            selectedTeacher = event.teacherName
                        )
                    }
                    scheduleRepository.saveTeacher(event.teacherName)
                }
            }

            SettingsEvent.OnDataUpdate -> {
                viewModelScope.launch {
                    try {
                        val groupsDeferred =
                            async(Dispatchers.IO) { scheduleRepository.getAllGroupNames() }
                        val teachersDeferred =
                            async(Dispatchers.IO) { scheduleRepository.getAllTeacherNames() }
                        val allGroups = groupsDeferred.await() ?: emptyList()
                        val allTeachers = teachersDeferred.await() ?: emptyList()
                        _state.update {
                            it.copy(
                                allGroups = allGroups.sorted(),
                                allTeachers = allTeachers.sorted(),
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("timetableTest", e.toString())
                    }
                }
            }

            SettingsEvent.OnNotificationClick -> {
                if (_state.value.isNotificationsEnabled) {
                    viewModelScope.launch {
                        _state.update {
                            it.copy(
                                isNotificationsEnabled = false
                            )
                        }
                        scheduleRepository.disableNotifications()
                    }
                } else {
                    viewModelScope.launch {
                        _state.update {
                            it.copy(
                                isNotificationsEnabled = scheduleRepository.enableNotifications(

                                )
                            )
                        }
                    }
                }
            }

            is SettingsEvent.OnWidgetSelectionChanged -> {
                viewModelScope.launch {
                    scheduleRepository.saveWidgetSelection(event.isGroup)
                    _state.update {
                        it.copy(
                            isWidgetGroupSelected = event.isGroup
                        )
                    }
                }
            }
        }
    }

    init {
        Log.d("timetableTest", "settings viewmodel init")
        onEvent(SettingsEvent.OnDataUpdate)
    }
}