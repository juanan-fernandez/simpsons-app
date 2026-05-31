package com.example.bigschoolexample.ui.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bigschoolexample.domain.usecase.GetCharactersUseCase

class CharactersViewModelFactory(
    private val getCharactersUseCase: GetCharactersUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharactersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CharactersViewModel(getCharactersUseCase) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
