package com.l0mtick.mgkcttimetable.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.l0mtick.mgkcttimetable.Greeting

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentScheduleScreen(
    state: ScheduleState,
    onEvent: (ScheduleEvent) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomAppBar {

            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = it
        ) {
            item {
                Greeting(name = "temp")
            }
//            items(state.groupSchedule) {
//
//            }
        }
    }
}