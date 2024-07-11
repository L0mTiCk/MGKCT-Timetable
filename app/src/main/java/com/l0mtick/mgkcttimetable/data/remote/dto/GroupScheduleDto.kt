package com.l0mtick.mgkcttimetable.data.remote.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement

@Serializable
data class GroupScheduleDto (
    val response: GroupResponse? = null
)

@Serializable
data class GroupResponse (
    val days: List<GroupDay>? = null,
    val update: Long? = null,
    val changed: Long? = null,
    val lastSuccess: Boolean? = null
)

@Serializable
data class GroupDay (
    val weekday: String? = null,
    val day: String? = null,
    val lessons: List<LessonUnion>? = null
)

@Serializable(with = LessonUnionSerializer::class)
sealed class LessonUnion {
    @Serializable
    data class LessonClassArrayValue(val value: List<LessonClass>) : LessonUnion()

    @Serializable
    data class LessonClassValue(val value: LessonClass) : LessonUnion()

    object NullValue : LessonUnion()
}

@Serializable
data class LessonClass (
    val subgroup: Long? = null,
    val lesson: String? = null,
    val type: Type? = null,
    val teacher: String? = null,
    val cabinet: String? = null,
    val comment: String? = null
)

@Serializable
enum class Type(val value: String) {
    @SerialName("Лек") Lec("Лек"),
    @SerialName("ЛР") Lr("ЛР"),
    @SerialName("Пр-ка") PrKa("Пр-ка"),
    @SerialName("ф-в") Fv("Ф-в"),
    @SerialName("Экз") Ex("Экз"),
    @SerialName("ПР") PR("ПР"),
    @SerialName("КП") KP("КП"),
    @SerialName("конс") CONS("Конс"),
}

object LessonUnionSerializer : KSerializer<LessonUnion> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("LessonUnion") {
        element<List<LessonClass>>("LessonClassArrayValue")
        element<LessonClass>("LessonClassValue")
    }

    override fun serialize(encoder: Encoder, value: LessonUnion) {
        val jsonEncoder = encoder as? JsonEncoder
            ?: throw SerializationException("This class can be saved only by Json")
        val jsonElement = when (value) {
            is LessonUnion.LessonClassArrayValue -> jsonEncoder.json.encodeToJsonElement(value)
            is LessonUnion.LessonClassValue -> jsonEncoder.json.encodeToJsonElement(value)
            is LessonUnion.NullValue -> JsonNull
        }
        jsonEncoder.encodeJsonElement(jsonElement)
    }

    override fun deserialize(decoder: Decoder): LessonUnion {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("This class can be loaded only by Json")
        val jsonElement = jsonDecoder.decodeJsonElement()
        return when (jsonElement) {
            is JsonArray -> LessonUnion.LessonClassArrayValue(jsonDecoder.json.decodeFromJsonElement(jsonElement))
            is JsonObject -> LessonUnion.LessonClassValue(jsonDecoder.json.decodeFromJsonElement(jsonElement))
            JsonNull -> LessonUnion.NullValue
            else -> throw SerializationException("Unexpected JSON element")
        }
    }
}
