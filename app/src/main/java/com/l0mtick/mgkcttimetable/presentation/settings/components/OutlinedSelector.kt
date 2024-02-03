package com.l0mtick.mgkcttimetable.presentation.settings.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.l0mtick.mgkcttimetable.R
import com.l0mtick.mgkcttimetable.presentation.settings.SettingsEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedSelector(label: String, value: String, elements: List<Any>, onEvent: (SettingsEvent) -> Unit) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {
            isExpanded = it
        },
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            label = {
                Text(text = label)
            },
            value = value,
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
            onDismissRequest = { isExpanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (elements.isNotEmpty()) {
                elements.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(text = it.toString())
                        },
                        onClick = {
                            isExpanded = false
                            onEvent(SettingsEvent.OnSpecificTeacherClick(it.toString()))
                        },
                    )
                }
            } else {
                DropdownMenuItem(
                    text = { Text(text = "Нет данных") },
                    onClick = { isExpanded = false }
                )
            }
        }
    }
}