package com.example.bigschoolexample.data.remote.dto

data class CharactersResponseDto(
    val next: String?,
    val results: List<CharacterDto>,
)
