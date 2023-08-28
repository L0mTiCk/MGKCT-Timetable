package com.l0mtick.mgkcttimetable.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository

class SettingsScreenViewModelFactory(private val scheduleRepository: ScheduleRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsScreenViewModel::class.java)) {
            return SettingsScreenViewModel(scheduleRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}