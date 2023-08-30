package com.l0mtick.mgkcttimetable.presentation.schedule.group

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository
import com.l0mtick.mgkcttimetable.presentation.schedule.ScheduleEvent
import com.l0mtick.mgkcttimetable.presentation.schedule.ScheduleState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class GroupScheduleScreenViewModel(private val scheduleRepository: ScheduleRepository): ViewModel() {

    private val _state = MutableStateFlow(
        ScheduleState(
        selectedGroup = "63"
    )
    )
    val state = _state.asStateFlow()
    fun onEvent(event: ScheduleEvent) {
        when (event) {
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
            ScheduleEvent.UpdateSchedule -> {
                viewModelScope.launch {
                    onEvent(ScheduleEvent.OnUpdatingStart)
                    try {
                        scheduleRepository.parseTimetable()
                    } catch (e: Exception) {
                        Log.d("timetableTest", "${e.message}")
                    }
                    _state.update {
                        it.copy(
                            groupSchedule = scheduleRepository.getDbGroupTimetable(0) ?: listOf(emptyMap(), emptyMap(), emptyMap()),
                            selectedGroup = scheduleRepository.getSavedGroup() ?: "Никто не выбран",
                            isScheduleUpdating = false
                        )
                    }
                    onEvent(ScheduleEvent.OnUpdatingFinished)
                    Log.d("timetableTest", "${_state.value.groupSchedule}")
                    Log.d("timetableTest", "${_state.value.currentDayOfWeek}")
                    Log.d("timetableTest", "${_state.value.selectedDay}")
                }
            }

            ScheduleEvent.OnUpdatingFinished -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(isScheduleUpdating = false)
                    }
                }
            }
            ScheduleEvent.OnUpdatingStart -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(isScheduleUpdating = true)
                    }
                }
            }
        }
    }

    init {
        onEvent(ScheduleEvent.UpdateSchedule)
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