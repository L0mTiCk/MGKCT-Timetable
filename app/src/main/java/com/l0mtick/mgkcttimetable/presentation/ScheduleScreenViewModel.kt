package com.l0mtick.mgkcttimetable.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ScheduleScreenViewModel(private val scheduleRepository: ScheduleRepository): ViewModel() {

    private val _state = MutableStateFlow(ScheduleState(
        selectedGroup = "63"
    ))
    val state = _state.asStateFlow()
    fun onEvent(event: ScheduleEvent) {
        when (event) {
            is ScheduleEvent.OnNewLessonsParsed -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isScheduleUpdating = false
                        )
                    }
                }
            }

            is ScheduleEvent.OnSpecificDayClick -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            selectedDay = if (event.id != _state.value.selectedDay) {
                                event.id
                            } else {
                                   -1
                            },
                        )
                    }
                }
            }

            //TODO: check internet or time, or is local db exist
            ScheduleEvent.OnFirstLoad -> {
                viewModelScope.launch {
                    try {
                        scheduleRepository.parseTimetable()
                    } catch (e: Exception) {
                        Log.d("timetableTest", "${e.message}")
                    }
                    _state.update {
                        it.copy(
                            groupSchedule = scheduleRepository.getDbGroupTimetable() ?: emptyList(),
                            selectedGroup = scheduleRepository.getSavedGroup() ?: "",
                            isScheduleUpdating = false
                        )
                    }
                    Log.d("timetableTest", "${_state.value.groupSchedule}")
                    Log.d("timetableTest", "${_state.value.currentDayOfWeek}")
                    Log.d("timetableTest", "${_state.value.selectedDay}")
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            var currentHour = LocalDateTime.now().hour
            var delay = 1000 * 60 * 60 - (LocalDateTime.now().minute) * 60 * 1000L
            while (true) {
                delay(delay)
                currentHour += 1
                delay = 1000 * 60 * 60
                _state.update {
                    it.copy(currentHour = currentHour)
                }
            }
        }
    }
}