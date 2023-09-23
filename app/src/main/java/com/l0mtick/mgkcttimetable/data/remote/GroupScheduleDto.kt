package com.l0mtick.mgkcttimetable.data.remote

import com.beust.klaxon.*

private fun <T> Klaxon.convert(k: kotlin.reflect.KClass<*>, fromJson: (JsonValue) -> T, toJson: (T) -> String, isUnion: Boolean = false) =
    this.converter(object: Converter {
        @Suppress("UNCHECKED_CAST")
        override fun toJson(value: Any)        = toJson(value as T)
        override fun fromJson(jv: JsonValue)   = fromJson(jv) as Any
        override fun canConvert(cls: Class<*>) = cls == k.java || (isUnion && cls.superclass == k.java)
    })

private val klaxon = Klaxon()
    .convert(Type::class,        { Type.fromValue(it.string!!) }, { "\"${it.value}\"" })
    .convert(LessonUnion::class, { LessonUnion.fromJson(it) },    { it.toJson() }, true)

data class GroupScheduleDto (
    val response: Response
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<GroupScheduleDto>(json)
    }
}

data class Response (
    val days: List<Day>,
    val update: Long,
    val changed: Long,
    val lastSuccess: Boolean
)

data class Day (
    val weekday: String,
    val day: String,
    val lessons: List<LessonUnion>
)

sealed class LessonUnion {
    class LessonClassArrayValue(val value: List<LessonClass>) : LessonUnion()
    class LessonClassValue(val value: LessonClass)            : LessonUnion()
    class NullValue()                                         : LessonUnion()

    public fun toJson(): String = klaxon.toJsonString(when (this) {
        is LessonClassArrayValue -> this.value
        is LessonClassValue -> this.value
        is NullValue -> "null"
    })

    companion object {
        public fun fromJson(jv: JsonValue): LessonUnion = when (jv.inside) {
            is JsonArray<*> -> LessonClassArrayValue(jv.array?.let { klaxon.parseFromJsonArray<LessonClass>(it) }!!)
            is JsonObject   -> LessonClassValue(jv.obj?.let { klaxon.parseFromJsonObject<LessonClass>(it) }!!)
            null            -> NullValue()
            else            -> throw IllegalArgumentException()
        }
    }
}

data class LessonClass (
    val subgroup: Long? = null,
    val lesson: String,
    val type: Type,
    val teacher: String,
    val cabinet: String? = null,
    val comment: String? = null
)

enum class Type(val value: String) {
    Лек("Лек"),
    Лр("ЛР"),
    ФВ("ф-в");

    companion object {
        public fun fromValue(value: String): Type = when (value) {
            "Лек" -> Лек
            "ЛР"  -> Лр
            "ф-в" -> ФВ
            else  -> throw IllegalArgumentException()
        }
    }
}
