package com.l0mtick.mgkcttimetable.presentation.schedule.teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository

class TeacherScheduleScreenViewModelFactory(private val scheduleRepository: ScheduleRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeacherScheduleScreenViewModel::class.java)) {
            return TeacherScheduleScreenViewModel(scheduleRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}