package com.example.bigschoolexample

import android.content.Context
import com.example.bigschoolexample.data.remote.RetrofitClient
import com.example.bigschoolexample.data.remote.SimpsonsApiService
import com.example.bigschoolexample.data.repository.SimpsonsRepositoryImpl
import com.example.bigschoolexample.domain.repository.SimpsonsRepository
import com.example.bigschoolexample.domain.usecase.GetCharactersUseCase

class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    val simpsonsApiService: SimpsonsApiService by lazy {
        RetrofitClient.getSimpsonsApiService(appContext)
    }

    val simpsonsRepository: SimpsonsRepository by lazy {
        SimpsonsRepositoryImpl(simpsonsApiService)
    }

    val getCharactersUseCase: GetCharactersUseCase by lazy {
        GetCharactersUseCase(simpsonsRepository)
    }
}
