package com.arabam.android.assigment.data.repository

import com.arabam.android.assigment.data.model.ErrorResponse
import com.arabam.android.assigment.util.Resource
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import java.io.IOException

open class BaseRepository(private val moshi: Moshi) {

    protected suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return try {
            Resource.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> Resource.Error(throwable)
                is HttpException -> {
                    val errorResponse = convertErrorBody(throwable)
                    Resource.Error(throwable)
                }
                else -> {
                    Resource.Error(throwable)
                }
            }
        }
    }

    private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
        return try {
            throwable.response()?.errorBody()?.source()?.let {
                val moshiAdapter = moshi.adapter(ErrorResponse::class.java)
                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            null
        }
    }
}