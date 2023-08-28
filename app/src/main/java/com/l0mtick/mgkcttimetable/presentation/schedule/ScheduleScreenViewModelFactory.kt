package com.l0mtick.mgkcttimetable.presentation.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository

class ScheduleScreenViewModelFactory(private val scheduleRepository: ScheduleRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleScreenViewModel::class.java)) {
            return ScheduleScreenViewModel(scheduleRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}