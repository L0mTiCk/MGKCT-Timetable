package com.l0mtick.mgkcttimetable

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.l0mtick.mgkcttimetable.data.database.AppDatabase
import com.l0mtick.mgkcttimetable.data.repository.ScheduleRepositoryImpl
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository
import com.l0mtick.mgkcttimetable.presentation.ScheduleEvent
import com.l0mtick.mgkcttimetable.presentation.ScheduleScreenViewModel
import com.l0mtick.mgkcttimetable.presentation.StudentScheduleScreen
import com.l0mtick.mgkcttimetable.ui.theme.MGKCTTimetableTheme

class MainActivity : ComponentActivity() {
    companion object {
        lateinit var database: AppDatabase
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "my-database")
            .fallbackToDestructiveMigration()
            .build()
        val sharedPreferences = getSharedPreferences("MGKCT-Timetable", Context.MODE_PRIVATE)
        val scheduleRepository: ScheduleRepository = ScheduleRepositoryImpl(sharedPreferences, database.scheduleDao())
//        CoroutineScope(Dispatchers.IO).launch {
//            database.clearAllTables()
//        }
        val viewModel by lazy {  ScheduleScreenViewModel(scheduleRepository) }
        viewModel.onEvent(ScheduleEvent.OnFirstLoad)
        setContent {
            MGKCTTimetableTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val state = viewModel.state.collectAsState()
//                    Greeting("Android")
//                    Column {
//                        Button(onClick = {
//                            Log.d("timetableTest", "${scheduleRepository.getSavedGroup()}")
//                        }) {
//
//                        }
//                    }
                    StudentScheduleScreen(state = state.value, onEvent = viewModel::onEvent)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MGKCTTimetableTheme {
        Greeting("Android")
    }
}