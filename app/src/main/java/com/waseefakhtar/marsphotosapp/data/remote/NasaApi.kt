package com.waseefakhtar.marsphotosapp.data.remote

import com.waseefakhtar.marsphotosapp.data.remote.dto.MarsPhotosDto
import retrofit2.http.GET

interface NasaApi {

    @GET("/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&api_key=DEMO_KEY")
    suspend fun getMarsPhotos(): MarsPhotosDto
}