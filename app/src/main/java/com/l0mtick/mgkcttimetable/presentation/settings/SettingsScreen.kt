package com.l0mtick.mgkcttimetable.presentation.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
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
import com.l0mtick.mgkcttimetable.presentation.settings.components.AppInfoDialogBox
import com.l0mtick.mgkcttimetable.presentation.settings.components.AppInfoRowItem

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

    var isGroupExpanded by remember {
        mutableStateOf(false)
    }
    var isTeacherExpanded by remember {
        mutableStateOf(false)
    }

    AnimatedVisibility(
        visible = state.isAppInfoDialogOpen,
        enter = fadeIn(animationSpec = tween(100)),
        exit = fadeOut(animationSpec = tween(100))
    ) {
        AppInfoDialogBox(onEvent = onEvent)
    }

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
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize()
        ) {
            ExposedDropdownMenuBox(
                expanded = isGroupExpanded,
                onExpandedChange = {
                    isGroupExpanded = it
                },
                modifier = Modifier
                    .padding(top = it.calculateTopPadding())
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    label = {
                        Text(text = "Группа")
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
                    expanded = isGroupExpanded,
                    onDismissRequest = { isGroupExpanded = false }
                ) {
                    if (state.allGroups.isNotEmpty()) {
                        state.allGroups.forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(text = it.toString())
                                },
                                onClick = {
                                    isGroupExpanded = false
                                    onEvent(SettingsEvent.OnSpecificGroupClick(it.toString()))
                                }
                            )
                        }
                    } else {
                        DropdownMenuItem(
                            text = { Text(text = "Нет данных") },
                            onClick = { isGroupExpanded = false }
                        )
                    }
                }
            }
            ExposedDropdownMenuBox(
                expanded = isTeacherExpanded,
                onExpandedChange = {
                    isTeacherExpanded = it
                },
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    label = {
                        Text(text = "Преподаватель")
                    },
                    value = state.selectedTeacher,
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
                    expanded = isTeacherExpanded,
                    onDismissRequest = { isTeacherExpanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (state.allTeachers.isNotEmpty()) {
                        state.allTeachers.forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(text = it)
                                },
                                onClick = {
                                    isTeacherExpanded = false
                                    onEvent(SettingsEvent.OnSpecificTeacherClick(it))
                                },
                            )
                        }
                    } else {
                        DropdownMenuItem(
                            text = { Text(text = "Нет данных") },
                            onClick = { isTeacherExpanded = false }
                        )
                    }
                }
            }
            Divider(
                modifier = Modifier.padding(top = 30.dp)
            )
            Column(
                modifier = Modifier.padding(vertical = 20.dp)
            ) {
                Text(
                    text = "Прочее",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
//                Spacer(modifier = Modifier.height(10.dp))
//                ContactDeveloperRowItem()
                Spacer(modifier = Modifier.height(20.dp))
                AppInfoRowItem(onEvent = onEvent)
            }
        }
    }
}