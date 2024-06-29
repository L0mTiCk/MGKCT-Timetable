package com.l0mtick.mgkcttimetable.data.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity

fun Context.getActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is AppCompatActivity) {
            return currentContext as Activity
        }
        currentContext = currentContext.baseContext
    }
    return null
}