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
    .convert(Type::class,        { Type.fromValue(it.string!!) }, { "\"${it.value}\"" })
    .convert(LessonUnion::class, { LessonUnion.fromJson(it) },    { it.toJson() }, true)

data class GroupScheduleDto (
    val response: GroupResponse? = null
) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<GroupScheduleDto>(json)
    }
}

data class GroupResponse (
    val days: List<GroupDay>? = null,
    val update: Long? = null,
    val changed: Long? = null,
    val lastSuccess: Boolean? = null
)

data class GroupDay (
    val weekday: String? = null,
    val day: String? = null,
    val lessons: List<LessonUnion>? = null
)

sealed class LessonUnion {
    class LessonClassArrayValue(val value: List<LessonClass>) : LessonUnion()
    class LessonClassValue(val value: LessonClass)            : LessonUnion()

    public fun toJson(): String = klaxon.toJsonString(when (this) {
        is LessonClassArrayValue -> this.value
        is LessonClassValue      -> this.value
    })

    companion object {
        public fun fromJson(jv: JsonValue): LessonUnion = when (jv.inside) {
            is JsonArray<*> -> LessonClassArrayValue(jv.array?.let { klaxon.parseFromJsonArray<LessonClass>(it) }!!)
            is JsonObject   -> LessonClassValue(jv.obj?.let { klaxon.parseFromJsonObject<LessonClass>(it) }!!)
            else            -> throw IllegalArgumentException()
        }
    }
}

data class LessonClass (
    val subgroup: Long? = null,
    val lesson: String? = null,
    val type: Type? = null,
    val teacher: String? = null,
    val cabinet: String? = null,
    val comment: String? = null
)

enum class Type(val value: String) {
    Лек("Лек"),
    Лр("ЛР"),
    Экз("Экз"),
    Прка("Пр-ка"),
    Пр("ПР"),
    Кп("КП"),
    ФВ("ф-в");

    companion object {
        public fun fromValue(value: String): Type = when (value) {
            "Лек" -> Лек
            "ЛР"  -> Лр
            "ф-в" -> ФВ
            "Экз" -> Экз
            "Пр-ка" -> Прка
            "ПР" -> Пр
            "КП" -> Кп
            else  -> throw IllegalArgumentException()
        }
    }
}

