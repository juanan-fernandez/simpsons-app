package com.example.bigschoolexample.domain.usecase

import com.example.bigschoolexample.domain.model.CharactersPage
import com.example.bigschoolexample.domain.model.NetworkResult
import com.example.bigschoolexample.domain.repository.SimpsonsRepository
import kotlinx.coroutines.flow.Flow

class GetCharactersUseCase(
    private val repository: SimpsonsRepository,
) {

    operator fun invoke(page: Int): Flow<NetworkResult<CharactersPage>> {
        return repository.getCharacters(page)
    }
}
