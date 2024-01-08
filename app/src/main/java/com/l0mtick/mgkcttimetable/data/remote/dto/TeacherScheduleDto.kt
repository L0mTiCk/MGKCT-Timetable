package com.l0mtick.mgkcttimetable.data.remote.dto
import com.beust.klaxon.*

private fun <T> Klaxon.convert(k: kotlin.reflect.KClass<*>, fromJson: (JsonValue) -> T, toJson: (T) -> String, isUnion: Boolean = false) =
    this.converter(object: Converter {
        @Suppress("UNCHECKED_CAST")
        override fun toJson(value: Any)        = toJson(value as T)
        override fun fromJson(jv: JsonValue)   = fromJson(jv) as Any
        override fun canConvert(cls: Class<*>) = cls == k.java || (isUnion && cls.superclass == k.java)
    })

private val klaxon = Klaxon()
    .convert(Type::class, { Type.fromValue(it.string!!) }, { "\"${it.value}\"" })

data class TeacherScheduleDto (
    val response: Response? = null
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<TeacherScheduleDto>(json)
    }
}

data class Response (
    val days: List<Day>? = null,
    val update: Long? = null,
    val changed: Long? = null,
    val lastSuccess: Boolean? = null
)

data class Day (
    val weekday: String? = null,
    val day: String? = null,
    val lessons: List<Lesson?>? = null
)

data class Lesson (
    val lesson: String? = null,
    val type: String? = null,
    val group: String? = null,
    val cabinet: String? = null,
    val comment: String? = null
)
