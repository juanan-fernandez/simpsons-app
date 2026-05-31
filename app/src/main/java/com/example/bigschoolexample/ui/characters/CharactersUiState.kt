package com.example.bigschoolexample.ui.characters

import com.example.bigschoolexample.domain.model.Character

data class CharactersUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val characters: List<Character> = emptyList(),
    val errorMessage: String? = null,
)
