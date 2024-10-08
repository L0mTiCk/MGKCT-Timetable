package com.l0mtick.mgkcttimetable

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.l0mtick.mgkcttimetable.data.remote.ScheduleApiImpl
import com.l0mtick.mgkcttimetable.data.repository.ScheduleRepositoryImpl
import com.l0mtick.mgkcttimetable.data.utils.Constants
import com.l0mtick.mgkcttimetable.domain.model.NavigationItem
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleApi
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository
import com.l0mtick.mgkcttimetable.presentation.components.NotificationDialog
import com.l0mtick.mgkcttimetable.presentation.schedule.group.GroupScheduleScreen
import com.l0mtick.mgkcttimetable.presentation.schedule.group.GroupScheduleScreenViewModel
import com.l0mtick.mgkcttimetable.presentation.schedule.teacher.TeacherScheduleScreen
import com.l0mtick.mgkcttimetable.presentation.schedule.teacher.TeacherScheduleScreenViewModel
import com.l0mtick.mgkcttimetable.presentation.settings.SettingsScreen
import com.l0mtick.mgkcttimetable.presentation.settings.SettingsScreenViewModel
import com.l0mtick.mgkcttimetable.ui.theme.MGKCTTimetableTheme
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.getViewModel
import java.io.InputStream
import java.util.Properties

class MainActivity : ComponentActivity() {
//    companion object {
//        lateinit var database: AppDatabase
//    }

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        firebaseAnalytics = Firebase.analytics
        val sharedPreferences =
            getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val scheduleRepository = get<ScheduleRepository>()
        val isNotificationRequestRequired = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        val navItems = listOf(
            NavigationItem(
                id = "group",
                title = getString(R.string.navigation_group),
                selectedIcon = Icons.Filled.List,
                unselectedIcon = Icons.Outlined.List,
            ),
            NavigationItem(
                id = "teacher",
                title = getString(R.string.navigation_teacher),
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
            )
        )
        setContent {
            MGKCTTimetableTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isFirstLaunch by remember {
                        mutableStateOf(
                            sharedPreferences.getBoolean(
                                Constants.IS_FIRST_LAUNCH,
                                true
                            )
                        )
                    }
                    if (isFirstLaunch && isNotificationRequestRequired) {
                        NotificationDialog(onDismiss = {
                            sharedPreferences.edit().putBoolean(Constants.IS_FIRST_LAUNCH, false)
                                .apply()
                            isFirstLaunch = false
                            requestNotificationPermission()
                        })
                    }
                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentDestination = navBackStackEntry?.destination
                                navItems.forEach { screen ->
                                    NavigationBarItem(
                                        label = { Text(screen.title) },
                                        selected = currentDestination?.hierarchy?.any { it.route == screen.id.lowercase() } == true,
                                        icon = {
                                            Icon(
                                                if (currentDestination?.hierarchy?.any { it.route == screen.id.lowercase() } == true) {
                                                    screen.selectedIcon
                                                } else {
                                                    screen.unselectedIcon
                                                },
                                                contentDescription = null
                                            )
                                        },
                                        onClick = {
                                            scheduleRepository.saveStartDestinationRoute(screen.id.lowercase())
                                            navController.navigate(screen.id.lowercase()) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = scheduleRepository.getSavedStartDestinationRoute()
                                ?: "group",
                            modifier = Modifier.padding(it)
                        ) {
                            composable(
                                route = "group",
                                deepLinks = listOf(navDeepLink {
                                    uriPattern = "mgkcttimetable://group"
                                })
                            ) {
                                GroupScheduleScreen(
                                    navController = navController,
                                    groupScheduleScreenViewModel = getViewModel<GroupScheduleScreenViewModel>()
                                )
                            }

                            composable(
                                route = "settings",
                                deepLinks = listOf(navDeepLink {
                                    uriPattern = "mgkcttimetable://settings"
                                })
                            ) {
                                SettingsScreen(
                                    navController = navController,
                                    settingsScreenViewModel = getViewModel<SettingsScreenViewModel>()
                                )
                            }

                            composable(
                                route = "teacher",
                                deepLinks = listOf(navDeepLink {
                                    uriPattern = "mgkcttimetable://teacher"
                                })
                            ) {
                                TeacherScheduleScreen(
                                    navController = navController,
                                    teacherScheduleScreenViewModel = getViewModel<TeacherScheduleScreenViewModel>()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }
}