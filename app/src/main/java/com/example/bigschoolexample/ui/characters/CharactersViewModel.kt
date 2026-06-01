package com.example.bigschoolexample.ui.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bigschoolexample.domain.model.Character
import com.example.bigschoolexample.domain.model.CharactersPage
import com.example.bigschoolexample.domain.model.NetworkErrorKind
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

    companion object {
        private const val PAGE_BATCH_SIZE = 10
    }

    private val _uiState = MutableStateFlow(CharactersUiState())
    val uiState: StateFlow<CharactersUiState> = _uiState.asStateFlow()

    private var allCharacters: List<Character> = emptyList()
    private var visibleCharactersCount = 0
    private var nextPage = 1
    private var hasNextPage = true
    private var loadCharactersJob: Job? = null

    init {
        loadMoreCharacters()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = query,
                characters = displayedCharacters(query),
                errorMessage = null,
                isOfflineError = false,
                loadMoreErrorMessage = null,
            )
        }

        if (query.isNotBlank() && loadCharactersJob?.isActive != true && hasNextPage) {
            requestNextCharactersPage()
        }
    }

    fun retry() {
        if (allCharacters.isEmpty()) {
            resetPagination()
        }

        if (_uiState.value.searchQuery.isNotBlank()) {
            if (loadCharactersJob?.isActive != true && hasNextPage) {
                requestNextCharactersPage()
            }
            return
        }

        loadMoreCharacters()
    }

    fun loadMoreCharacters() {
        if (_uiState.value.searchQuery.isNotBlank()) {
            return
        }

        if (loadCharactersJob?.isActive == true) {
            return
        }

        if (revealMoreLoadedCharacters()) {
            return
        }

        if (!hasNextPage) {
            _uiState.update { it.copy(hasMoreCharacters = false) }
            return
        }

        requestNextCharactersPage()
    }

    private fun requestNextCharactersPage() {
        val isInitialLoad = allCharacters.isEmpty()

        loadCharactersJob = viewModelScope.launch {
            var shouldContinueLoading = true
            var isFirstRequest = isInitialLoad

            while (shouldContinueLoading) {
                var pageLoadedSuccessfully = false

                getCharactersUseCase(nextPage).collect { result ->
                    when (result) {
                        is NetworkResult.Loading -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = isFirstRequest,
                                    isLoadingMore = !isFirstRequest,
                                    isOfflineError = false,
                                    errorMessage = if (isFirstRequest) null else it.errorMessage,
                                    loadMoreErrorMessage = null,
                                )
                            }
                        }

                        is NetworkResult.Success -> {
                            appendCharactersPage(result.data)
                            pageLoadedSuccessfully = true
                        }

                        is NetworkResult.Error -> {
                            handleLoadError(
                                message = result.message,
                                isInitialLoad = isFirstRequest,
                                kind = result.kind,
                            )
                            shouldContinueLoading = false
                        }
                    }
                }

                if (!pageLoadedSuccessfully) {
                    break
                }

                isFirstRequest = false
                shouldContinueLoading = shouldContinueSearchingCatalog()
            }
        }
    }

    private fun appendCharactersPage(page: CharactersPage) {
        allCharacters = allCharacters + page.characters
        nextPage += 1
        hasNextPage = page.hasNextPage
        visibleCharactersCount = (visibleCharactersCount + PAGE_BATCH_SIZE).coerceAtMost(allCharacters.size)

        _uiState.update { currentState ->
            currentState.copy(
                isLoading = false,
                isLoadingMore = false,
                isOfflineError = false,
                characters = displayedCharacters(currentState.searchQuery),
                errorMessage = null,
                loadMoreErrorMessage = null,
                hasMoreCharacters = hasMoreCharacters(),
            )
        }
    }

    private fun handleLoadError(
        message: String,
        isInitialLoad: Boolean,
        kind: NetworkErrorKind = NetworkErrorKind.Unknown,
    ) {
        _uiState.update {
            it.copy(
                isLoading = false,
                isLoadingMore = false,
                isOfflineError = kind == NetworkErrorKind.NoInternet,
                errorMessage = if (isInitialLoad) message else it.errorMessage,
                loadMoreErrorMessage = if (isInitialLoad) null else message,
                hasMoreCharacters = hasMoreCharacters(),
            )
        }
    }

    private fun revealMoreLoadedCharacters(): Boolean {
        if (visibleCharactersCount >= allCharacters.size) {
            return false
        }

        visibleCharactersCount = (visibleCharactersCount + PAGE_BATCH_SIZE).coerceAtMost(allCharacters.size)
        _uiState.update { currentState ->
            currentState.copy(
                characters = displayedCharacters(currentState.searchQuery),
                hasMoreCharacters = hasMoreCharacters(),
                loadMoreErrorMessage = null,
            )
        }

        return true
    }

    private fun displayedCharacters(query: String): List<Character> {
        return if (query.isBlank()) {
            filterCharacters(query, allCharacters.take(visibleCharactersCount))
        } else {
            filterCharacters(query, allCharacters)
        }
    }

    private fun shouldContinueSearchingCatalog(): Boolean {
        return _uiState.value.searchQuery.isNotBlank() && hasNextPage
    }

    private fun hasMoreCharacters(): Boolean {
        return visibleCharactersCount < allCharacters.size || hasNextPage
    }

    private fun resetPagination() {
        loadCharactersJob?.cancel()
        allCharacters = emptyList()
        visibleCharactersCount = 0
        nextPage = 1
        hasNextPage = true
        _uiState.value = CharactersUiState()
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
