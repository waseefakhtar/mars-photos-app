package com.waseefakhtar.marsphotosapp.domain.repository

import com.waseefakhtar.marsphotosapp.data.remote.dto.Photo

interface MarsPhotosRepository {

    suspend fun getMarsPhotos(rover: String): List<Photo>
}