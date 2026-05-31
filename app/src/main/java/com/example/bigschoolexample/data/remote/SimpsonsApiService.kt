package com.example.bigschoolexample.data.remote

import com.example.bigschoolexample.data.remote.dto.CharacterDto
import com.example.bigschoolexample.data.remote.dto.CharactersResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SimpsonsApiService {

    @GET("characters")
    suspend fun getCharacters(@Query("page") page: Int): CharactersResponseDto

    @GET("characters/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): CharacterDto
}
