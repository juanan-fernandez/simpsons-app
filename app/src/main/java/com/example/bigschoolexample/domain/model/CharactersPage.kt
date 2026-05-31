package com.example.bigschoolexample.domain.model

data class CharactersPage(
    val characters: List<Character>,
    val hasNextPage: Boolean,
)
