package com.l0mtick.mgkcttimetable.presentation.schedule.teacher

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

class TeacherScheduleScreenViewModel(private val scheduleRepository: ScheduleRepository) :
    ViewModel() {

    private val _state = MutableStateFlow(
        ScheduleState(
            selectedGroup = scheduleRepository.getSavedGroup()
                ?: scheduleRepository.getEmptySelectedString()
        )
    )
    val state = _state.asStateFlow()
    fun onEvent(event: ScheduleEvent) {
        when (event) {
            is ScheduleEvent.OnSpecificDayClick -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            selectedDay = if (event.date != _state.value.selectedDay) {
                                event.date
                            } else {
                                ""
                            },
                        )
                    }
                }
            }

            ScheduleEvent.UpdateSchedule -> {
                viewModelScope.launch {
                    onEvent(ScheduleEvent.OnUpdatingStart)
                    try {
                        _state.update {
                            it.copy(
                                selectedGroup = scheduleRepository.getSavedTeacher()
                                    ?: "Никто не выбран"
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("timetableTest", "Error while updating saved teacher: $e")
                    }
                    delay(100)
                    try {
                        _state.update {
                            it.copy(
                                groupSchedule = scheduleRepository.parseTeacherTimetable(_state.value.selectedGroup),
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("timetableTest", "${e.message}")
                    }
                    _state.update {
                        it.copy(
                            currentLesson = scheduleRepository.getCurrentLesson()
                        )
                    }
                    onEvent(ScheduleEvent.OnUpdatingFinished)
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

            is ScheduleEvent.OnNetworkChange -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isConnected = event.isConnected
                        )
                    }
                }
            }
        }
    }

    init {
        onEvent(ScheduleEvent.UpdateSchedule)
        scheduleRepository.getConnectionStatus {
            onEvent(ScheduleEvent.OnNetworkChange(it))
        }
    }
}