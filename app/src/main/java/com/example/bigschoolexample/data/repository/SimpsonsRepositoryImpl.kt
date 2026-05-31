package com.example.bigschoolexample.data.repository

import com.example.bigschoolexample.data.mapper.toDomain
import com.example.bigschoolexample.data.remote.NoInternetException
import com.example.bigschoolexample.data.remote.SimpsonsApiService
import com.example.bigschoolexample.domain.model.Character
import com.example.bigschoolexample.domain.model.NetworkResult
import com.example.bigschoolexample.domain.repository.SimpsonsRepository
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class SimpsonsRepositoryImpl(
    private val apiService: SimpsonsApiService,
) : SimpsonsRepository {

    override fun getCharacters(): Flow<NetworkResult<List<Character>>> = flow {
        emit(NetworkResult.Loading)

        try {
            val characters = apiService.getCharacters().map { it.toDomain() }
            emit(NetworkResult.Success(characters))
        } catch (exception: NoInternetException) {
            emit(NetworkResult.Error(exception, exception.message ?: "No internet connection available."))
        } catch (exception: HttpException) {
            emit(NetworkResult.Error(exception, exception.message ?: "Unable to fetch characters."))
        } catch (exception: IOException) {
            emit(NetworkResult.Error(exception, exception.message ?: "Network error while fetching characters."))
        } catch (exception: Exception) {
            emit(NetworkResult.Error(exception, exception.message ?: "Unexpected error while fetching characters."))
        }
    }.flowOn(Dispatchers.IO)
}
