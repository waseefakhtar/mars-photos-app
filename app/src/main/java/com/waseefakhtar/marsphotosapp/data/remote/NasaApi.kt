package com.waseefakhtar.marsphotosapp.data.remote

import com.waseefakhtar.marsphotosapp.data.remote.dto.MarsPhotosDto
import retrofit2.http.GET
import retrofit2.http.Path

interface NasaApi {

    @GET("/mars-photos/api/v1/rovers/{rover}/photos?sol=1000&api_key=u2FFOB2llm85bEqkd8XpnappiSzCYcOUEIFDZFIe")
    suspend fun getMarsPhotos(@Path("rover") rover: String): MarsPhotosDto
}