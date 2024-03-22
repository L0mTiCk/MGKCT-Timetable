package com.l0mtick.mgkcttimetable.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.l0mtick.mgkcttimetable.R
import com.l0mtick.mgkcttimetable.presentation.schedule.ScheduleState

@Composable
fun UpdateStatusBar(state: ScheduleState) {
    Column(
        modifier = Modifier
            .padding(20.dp, 8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        if (state.groupSchedule.changed.isNotEmpty() && state.groupSchedule.update.isNotEmpty()) {
            Text(
                text = "${stringResource(id = R.string.screen_updated)}: ${state.groupSchedule.update}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = MaterialTheme.typography.labelLarge.fontSize
            )
            Text(
                text = "${stringResource(id = R.string.screen_changed)}: ${state.groupSchedule.changed}",
                modifier = Modifier.padding(0.dp, 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = MaterialTheme.typography.labelLarge.fontSize
            )
        }
    }
}
