package com.l0mtick.mgkcttimetable.presentation.settings

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository
import com.l0mtick.mgkcttimetable.presentation.settings.components.AppInfoDialogBox
import com.l0mtick.mgkcttimetable.presentation.settings.components.AppInfoRowItem
import com.l0mtick.mgkcttimetable.presentation.settings.components.ContactDeveloperRowItem

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
        val context = LocalContext.current
        var isExpanded by remember {
            mutableStateOf(false)
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize()
        ) {
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = {
                    isExpanded = it
                },
                modifier = Modifier
                    .padding(top = it.calculateTopPadding())
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
                Spacer(modifier = Modifier.height(10.dp))
                ContactDeveloperRowItem(context = context)
                Spacer(modifier = Modifier.height(20.dp))
                AppInfoRowItem(onEvent = onEvent)
            }
        }
    }
    AnimatedVisibility(visible = state.isAppInfoDialogOpen) {
        AppInfoDialogBox(onEvent = onEvent)
    }
}