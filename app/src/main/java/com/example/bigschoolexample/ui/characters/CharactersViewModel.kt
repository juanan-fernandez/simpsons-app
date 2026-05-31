package com.example.bigschoolexample.ui.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bigschoolexample.domain.model.Character
import com.example.bigschoolexample.domain.model.NetworkResult
import com.example.bigschoolexample.domain.usecase.GetCharactersUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharactersViewModel(
    private val getCharactersUseCase: GetCharactersUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharactersUiState())
    val uiState: StateFlow<CharactersUiState> = _uiState.asStateFlow()

    private var allCharacters: List<Character> = emptyList()
    private var loadCharactersJob: Job? = null

    init {
        loadCharacters()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = query,
                characters = filterCharacters(query, allCharacters),
            )
        }
    }

    fun retry() {
        loadCharacters()
    }

    private fun loadCharacters() {
        loadCharactersJob?.cancel()
        loadCharactersJob = viewModelScope.launch {
            getCharactersUseCase().collect { result ->
                when (result) {
                    is NetworkResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    }

                    is NetworkResult.Success -> {
                        allCharacters = result.data
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                characters = filterCharacters(currentState.searchQuery, allCharacters),
                                errorMessage = null,
                            )
                        }
                    }

                    is NetworkResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun filterCharacters(query: String, characters: List<Character>): List<Character> {
        if (query.isBlank()) {
            return characters
        }

        return characters.filter { character ->
            character.name.contains(query, ignoreCase = true)
        }
    }
}
