package com.example.bigschoolexample.data.remote

import com.example.bigschoolexample.data.remote.dto.CharacterDto
import retrofit2.http.GET
import retrofit2.http.Path

interface SimpsonsApiService {

    @GET("characters")
    suspend fun getCharacters(): List<CharacterDto>

    @GET("characters/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): CharacterDto
}
