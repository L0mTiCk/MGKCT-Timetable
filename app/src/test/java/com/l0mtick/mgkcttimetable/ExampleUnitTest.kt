package com.l0mtick.mgkcttimetable

import android.text.format.DateFormat
import com.l0mtick.mgkcttimetable.domain.model.schedule.Lesson
import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testTimeConvert() {
        val netDate = Date(1704464681035)
        val sdf = SimpleDateFormat("dd MMMM HH:mm:ss", Locale.getDefault())
        val formattedDate = sdf.format(netDate)
        println(formattedDate)
    }

    @Test
    fun nullListElement() {
        val a = listOf(null, Lesson(), Lesson())
        a.forEach {
            println(it)
        }
    }
}