package com.l0mtick.mgkcttimetable.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsScreenViewModel(private val scheduleRepository: ScheduleRepository): ViewModel() {

    private val _state = MutableStateFlow(
        SettingsState(
            selectedGroup = scheduleRepository.getSavedGroup() ?: "",
            selectedTeacher = scheduleRepository.getSavedTeacher() ?: ""
        )
    )

    val state = _state.asStateFlow()

    fun onEvent(event: SettingsEvent) {
        when (event){
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
        }
    }

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    allGroups = scheduleRepository.getAllGroupNames() ?: emptyList(),
                    allTeachers = scheduleRepository.getAllTeacherNames() ?: emptyList()
                )
            }
        }
    }
}