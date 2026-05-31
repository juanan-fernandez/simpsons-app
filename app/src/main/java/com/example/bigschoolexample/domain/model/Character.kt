package com.example.bigschoolexample.domain.model

data class Character(
    val id: Int,
    val name: String,
    val age: Int,
    val occupation: String,
    val portraitUrl: String,
    val phrases: List<String>,
)
