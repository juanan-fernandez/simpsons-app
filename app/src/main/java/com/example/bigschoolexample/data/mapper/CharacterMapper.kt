package com.example.bigschoolexample.data.mapper

import com.example.bigschoolexample.data.remote.dto.CharacterDto
import com.example.bigschoolexample.domain.model.Character

fun CharacterDto.toDomain(): Character {
    return Character(
        id = id,
        name = name,
        age = age,
        occupation = occupation,
        portraitUrl = portraitPath,
        phrases = phrases,
    )
}
