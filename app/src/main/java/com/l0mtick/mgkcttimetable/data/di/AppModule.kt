package com.l0mtick.mgkcttimetable.data.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.l0mtick.mgkcttimetable.data.remote.ScheduleApiImpl
import com.l0mtick.mgkcttimetable.data.repository.ScheduleRepositoryImpl
import com.l0mtick.mgkcttimetable.data.utils.Constants
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleApi
import com.l0mtick.mgkcttimetable.domain.repository.ScheduleRepository
import com.l0mtick.mgkcttimetable.presentation.schedule.group.GroupScheduleScreenViewModel
import com.l0mtick.mgkcttimetable.presentation.schedule.teacher.TeacherScheduleScreenViewModel
import com.l0mtick.mgkcttimetable.presentation.settings.SettingsScreenViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.InputStream
import java.util.Properties

val appModule = module {
    single<ScheduleApi> {

        ScheduleApiImpl(get(qualifier = named("api_key")))
    }

    single(qualifier = named("api_key")) {
        val properties = Properties()
        val inputStream: InputStream = get<Application>().assets.open("config.properties")
        properties.load(inputStream)
        properties.getProperty("api_key")
    }

    single<ScheduleRepository> {
        ScheduleRepositoryImpl(androidApplication(), get())
    }

    viewModel {
        GroupScheduleScreenViewModel(get())
    }

    viewModel {
        TeacherScheduleScreenViewModel(get())
    }

    viewModel {
        SettingsScreenViewModel(get())
    }

}