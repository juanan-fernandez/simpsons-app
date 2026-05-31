package com.example.bigschoolexample.domain.repository

import com.example.bigschoolexample.domain.model.Character
import com.example.bigschoolexample.domain.model.NetworkResult
import kotlinx.coroutines.flow.Flow

interface SimpsonsRepository {
    fun getCharacters(): Flow<NetworkResult<List<Character>>>
}
