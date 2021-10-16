package com.waseefakhtar.marsphotosapp.domain.use_case.get_photo_detail

import com.waseefakhtar.marsphotosapp.coroutines.DispatcherProvider
import com.waseefakhtar.marsphotosapp.data.remote.dto.Photo
import com.waseefakhtar.marsphotosapp.domain.model.PhotoDetail
import com.waseefakhtar.marsphotosapp.domain.repository.MarsPhotosRepository
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*
import javax.inject.Inject

class GetPhotoDetailUseCase @Inject constructor(
    private val repository: MarsPhotosRepository,
    private val dispatcherProvider: DispatcherProvider
) {

    @Throws(Throwable::class)
    suspend fun getPhotoDetailById(id: Int): PhotoDetail {
        return withContext(dispatcherProvider.io()) {
            repository.getMarsPhotos().find { it.id == id }?.toPhotoDetail() ?: throw IOException()
        }
    }
}

private fun Photo.toPhotoDetail(): PhotoDetail =
    PhotoDetail(
        rover = rover.name,
        launchDate = rover.launchDate,
        landingDate = rover.landingDate,
        imgSrc = imgSrc,
        status = rover.status.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    )