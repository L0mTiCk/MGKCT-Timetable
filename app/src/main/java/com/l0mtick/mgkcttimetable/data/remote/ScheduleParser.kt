package com.l0mtick.mgkcttimetable.data.remote

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

private const val TIMETABLE_TAG = "timetable_parser"
private const val MGKCT_URL_FOR_GROUPS = "https://mgkct.minskedu.gov.by/%D0%BF%D0%B5%D1%80%D1%81%D0%BE%D0%BD%D0%B0%D0%BB%D0%B8%D0%B8/%D1%83%D1%87%D0%B0%D1%89%D0%B8%D0%BC%D1%81%D1%8F/%D1%80%D0%B0%D1%81%D0%BF%D0%B8%D1%81%D0%B0%D0%BD%D0%B8%D0%B5-%D0%B7%D0%B0%D0%BD%D1%8F%D1%82%D0%B8%D0%B9-%D0%BD%D0%B0-%D0%BD%D0%B5%D0%B4%D0%B5%D0%BB%D1%8E"
private const val MGKCT_URL_FOR_TEACHER = "https://mgkct.minskedu.gov.by/%D0%BF%D0%B5%D1%80%D1%81%D0%BE%D0%BD%D0%B0%D0%BB%D0%B8%D0%B8/%D0%BF%D1%80%D0%B5%D0%BF%D0%BE%D0%B4%D0%B0%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D1%8F%D0%BC/%D1%80%D0%B0%D1%81%D0%BF%D0%B8%D1%81%D0%B0%D0%BD%D0%B8%D0%B5-%D0%B7%D0%B0%D0%BD%D1%8F%D1%82%D0%B8%D0%B9-%D0%BD%D0%B0-%D0%BD%D0%B5%D0%B4%D0%B5%D0%BB%D1%8E"

suspend fun parseRawTimetable(): MutableMap<String, List<Map<Int, List<String>>>>?{
    Log.d("timetable_parser", "parse started")
    val doc_group = withContext(Dispatchers.IO) {
        try {
            Jsoup.connect(MGKCT_URL_FOR_GROUPS).get()
        } catch (e: Exception) {
            Log.d("timetable_parser", "parse failed, message - ${e.message}")
            e.printStackTrace()
            null
        }
    }
    val doc_teacher = withContext(Dispatchers.IO) {
        try {
            Jsoup.connect(MGKCT_URL_FOR_TEACHER).get()
        } catch (e: Exception) {
            Log.d("timetable_parser", "parse failed, message - ${e.message}")
            e.printStackTrace()
            null
        }
    }
    if (doc_group != null && doc_teacher != null) {
        val groupSchedule = mapTimetable(doc = doc_group)
        val teacherSchedule = mapTimetable(doc = doc_teacher)
        return groupSchedule.plus(teacherSchedule).toMutableMap()
    }
    return null
}

fun mapTimetable(doc: Document): MutableMap<String, List<Map<Int, List<String>>>> {
    Log.d("timetable_parser", "parse and save")

    val allGroups: Elements = doc.select("tbody")
    val groups: Elements = doc.select("h2")
    val groupLessonsMap = mutableMapOf<String, List<Map<Int, List<String>>>>()


    for (i in allGroups.indices) {
        val rows: Elements = allGroups[i].select("tr")
        val weekDays: Elements = rows[0].select("[colspan]")
        val numberOfPairs = rows.size


        val lessonMap = mutableMapOf<Int, List<String>>()
        val auditoriumMap = mutableMapOf<Int, List<String>>()
        val lessonNumberMap = mutableMapOf<Int, List<String>>()
        val listOfMap = mutableListOf<Map<Int, List<String>>>()

        val groupName = groups[i].text()

        if (numberOfPairs > 2) {
            val pairs: MutableList<Elements> = List(numberOfPairs - 2) { weekDays }.toMutableList()
            var tempCounter = 2

            for (j in pairs.indices) {
                pairs[j] = rows[tempCounter].select("td")
                tempCounter += 1
            }

            for (dayCounter in 0 until weekDays.size * 2 step 2) {
                val lessonsList = mutableListOf<String>()
                val auditoriumList = mutableListOf<String>()
                val lessonNumberList = mutableListOf<String>()

                for (j in pairs.indices) {
                    val lesson = pairs[j][dayCounter].text()
                    val auditorium = pairs[j][dayCounter + 1].text()
                    if (lesson != "" && auditorium != "") {
                        lessonsList.add(lesson)
                        auditoriumList.add(auditorium)
                        lessonNumberList.add("${j + 1}")
//                        Log.d(
//                            "timetable_parser",
//                            "group - $groupName, day - $dayName, lesson number - ${j + 1}, lesson - $lesson, auditory - $auditorium"
//                        )
                    }
                }
                if (lessonsList.isNotEmpty()) {
                    lessonMap.put(dayCounter / 2, lessonsList)
                    auditoriumMap.put(dayCounter / 2, auditoriumList)
                    lessonNumberMap.put(dayCounter / 2, lessonNumberList)
                }
            }
        }
        listOfMap.add(lessonMap)
        listOfMap.add(auditoriumMap)
        listOfMap.add(lessonNumberMap)
//        Log.d("timetableTest", "$lessonNumberMap")
        groupLessonsMap.put(key = groupName, value = listOfMap)
    }
//    Log.d(TIMETABLE_TAG, "$groupLessonsMap")
//    Log.d(TIMETABLE_TAG, "${groupLessonsMap.get("Преподаватель - Липень А. В.")}")
    Log.d("timetable_parser", "finished")

    return groupLessonsMap
}