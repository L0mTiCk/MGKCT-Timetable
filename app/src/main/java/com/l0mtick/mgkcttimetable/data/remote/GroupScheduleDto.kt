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
    .convert(GroupLessonUnion::class, { GroupLessonUnion.fromJson(it) },    { it.toJson() }, true)

data class GroupScheduleDto (
    val response: GroupResponse
) {
    fun toJson() = klaxon.toJsonString(this)

    companion object {
        fun fromJson(json: String) = klaxon.parse<GroupScheduleDto>(json)
    }
}

data class GroupResponse (
    val days: List<GroupDay>,
    val update: Long,
    val changed: Long,
    val lastSuccess: Boolean
)

data class GroupDay (
    val weekday: String,
    val day: String,
    val lessons: List<GroupLessonUnion>
)

sealed class GroupLessonUnion {
    class LessonClassArrayValue(val value: List<GroupLessonClass>) : GroupLessonUnion()
    class LessonClassValue(val value: GroupLessonClass)            : GroupLessonUnion()
    class NullValue()                                         : GroupLessonUnion()

    fun toJson(): String = klaxon.toJsonString(when (this) {
        is LessonClassArrayValue -> this.value
        is LessonClassValue -> this.value
        is NullValue -> "null"
    })

    companion object {
        fun fromJson(jv: JsonValue): GroupLessonUnion = when (jv.inside) {
            is JsonArray<*> -> LessonClassArrayValue(jv.array?.let { klaxon.parseFromJsonArray<GroupLessonClass>(it) }!!)
            is JsonObject   -> LessonClassValue(jv.obj?.let { klaxon.parseFromJsonObject<GroupLessonClass>(it) }!!)
            null            -> NullValue()
            else            -> throw IllegalArgumentException()
        }
    }
}

data class GroupLessonClass (
    val subgroup: Long? = null,
    val lesson: String,
    val type: Type,
    val teacher: String,
    val cabinet: String? = null,
    val comment: String? = null
)

enum class Type(val value: String) {
    Lek("Лек"),
    Lr("ЛР"),
    FV("ф-в"),
    Prka("Пр-ка"),
    Kp("КП"),
    Pr("ПР");

    companion object {
        fun fromValue(value: String): Type = when (value) {
            "Лек" -> Lek
            "ЛР"  -> Lr
            "ф-в" -> FV
            "ПР" -> Pr
            "КП" -> Kp
            "Пр-ка" -> Prka
            else  -> throw IllegalArgumentException()
        }
    }
}
