package com.waseefakhtar.marsphotosapp.data.repository

import com.waseefakhtar.marsphotosapp.data.remote.NasaApi
import com.waseefakhtar.marsphotosapp.data.remote.dto.Photo
import com.waseefakhtar.marsphotosapp.domain.repository.MarsPhotosRepository
import java.util.*
import javax.inject.Inject

class MarsPhotosRepositoryImpl @Inject constructor(
    private val api: NasaApi
) : MarsPhotosRepository {

    override suspend fun getMarsPhotos(rover: String): List<Photo> {
        return api.getMarsPhotos(rover).photos
    }
}