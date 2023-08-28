package com.l0mtick.mgkcttimetable.presentation.settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    repository: ScheduleRepository,
    navController: NavController
) {
    val viewModelFactory = SettingsScreenViewModelFactory(scheduleRepository = repository)
    val viewModel: SettingsScreenViewModel = viewModel(factory = viewModelFactory)
    val state = viewModel.state.collectAsState().value
    val onEvent = viewModel::onEvent
    Scaffold(
        bottomBar = {
        }
    ) {
        Text(text = "Settings")
    }
}