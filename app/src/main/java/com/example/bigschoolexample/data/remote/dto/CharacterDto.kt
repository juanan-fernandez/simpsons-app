package com.example.bigschoolexample.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CharacterDto(
    val id: Int,
    val name: String,
    val age: Int?,
    val occupation: String?,
    @SerializedName("portrait_path") val portraitPath: String?,
    val phrases: List<String>?,
    val status: String?,
)
