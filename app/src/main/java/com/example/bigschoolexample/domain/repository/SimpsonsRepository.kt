package com.example.bigschoolexample.domain.repository

import com.example.bigschoolexample.domain.model.CharactersPage
import com.example.bigschoolexample.domain.model.NetworkResult
import kotlinx.coroutines.flow.Flow

interface SimpsonsRepository {
    fun getCharacters(page: Int): Flow<NetworkResult<CharactersPage>>
}
