package com.l0mtick.mgkcttimetable.presentation.schedule.group

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository
import com.l0mtick.mgkcttimetable.presentation.schedule.ScheduleEvent
import com.l0mtick.mgkcttimetable.presentation.schedule.ScheduleState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GroupScheduleScreenViewModel(private val scheduleRepository: ScheduleRepository): ViewModel() {

    private val _state = MutableStateFlow(
        ScheduleState(
        selectedGroup = "Никто не выбран"
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

            //TODO: check internet or time, or is local db exist
            ScheduleEvent.UpdateSchedule -> {
                viewModelScope.launch {
                    onEvent(ScheduleEvent.OnUpdatingStart)
                    try {
                        _state.update {
                            it.copy(
                                groupSchedule = scheduleRepository.parseGroupTimetable(_state.value.selectedGroup),
                                selectedGroup = scheduleRepository.getSavedGroup() ?: "Никто не выбран"
                            )
                        }
                    } catch (e: Exception) {
                        Log.d("timetableTest", "${e.message}")
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
    }
}