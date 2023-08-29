package com.l0mtick.mgkcttimetable.presentation.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
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
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Settings") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.TwoTone.ArrowBack,
                            contentDescription = "Back arrow"
                        )
                    }
                }
            )
        }
    ) {
        var isExpanded by remember {
            mutableStateOf(false)
        }
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = {
                isExpanded = it
            },
            modifier = Modifier
                .padding(vertical = it.calculateTopPadding(), horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                label = {
                    Text(text = "Group")
                },
                value = state.selectedGroup,
                readOnly = true,
                enabled = false,
                onValueChange = {},
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                    disabledBorderColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledTextColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                state.allGroups.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(text = it)
                        },
                        onClick = {
                            isExpanded = false
                            onEvent(SettingsEvent.OnSpecificGroupClick(it))
                        }
                    )
                }
            }
        }
    }
}