package com.l0mtick.mgkcttimetable.presentation.schedule.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository

class GroupScheduleScreenViewModelFactory(private val scheduleRepository: ScheduleRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupScheduleScreenViewModel::class.java)) {
            return GroupScheduleScreenViewModel(scheduleRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}