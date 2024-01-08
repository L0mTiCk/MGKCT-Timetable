package com.l0mtick.mgkcttimetable.presentation.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsScreenViewModel(private val scheduleRepository: ScheduleRepository) : ViewModel() {

    private val _state = MutableStateFlow(
        SettingsState(
            selectedGroup = scheduleRepository.getSavedGroup() ?: "",
            selectedTeacher = scheduleRepository.getSavedTeacher() ?: ""
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
                        delay(500)
                        val teachersDeferred =
                            async(Dispatchers.IO) { scheduleRepository.getAllTeacherNames() }

                        val allGroups = groupsDeferred.await() ?: emptyList()
                        val allTeachers = teachersDeferred.await() ?: emptyList()
                        _state.update {
                            it.copy(
                                allGroups = allGroups,
                                allTeachers = allTeachers
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("timetableTest", e.toString())
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