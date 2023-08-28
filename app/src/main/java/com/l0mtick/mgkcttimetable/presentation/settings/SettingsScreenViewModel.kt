package com.l0mtick.mgkcttimetable.presentation.settings

import androidx.lifecycle.ViewModel
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsScreenViewModel(private val scheduleRepository: ScheduleRepository): ViewModel() {

    private val _state = MutableStateFlow(
        SettingsState(
            selectedGroup = scheduleRepository.getSavedGroup() ?: "63"
        )
    )

    val state = _state.asStateFlow()

    fun onEvent(event: SettingsEvent) {

    }
}