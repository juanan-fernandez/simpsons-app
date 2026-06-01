package com.example.bigschoolexample.data.mapper

import com.example.bigschoolexample.data.remote.dto.CharacterDto
import com.example.bigschoolexample.domain.model.Character

private const val IMAGES_BASE_URL = "https://cdn.thesimpsonsapi.com/500"

fun CharacterDto.toDomain(): Character {
    return Character(
        id = id,
        name = name,
        age = age,
        occupation = occupation.orEmpty().ifBlank { "Unknown" },
        portraitUrl = portraitPath.toAbsolutePortraitUrl(),
        phrases = phrases.orEmpty(),
        status = status.orEmpty().ifBlank { "Unknown" },
    )
}

private fun String?.toAbsolutePortraitUrl(): String {
    if (this.isNullOrBlank()) {
        return ""
    }

    return if (startsWith("http://") || startsWith("https://")) {
        this
    } else {
        "$IMAGES_BASE_URL$this"
    }
}
