package com.waseefakhtar.marsphotosapp.domain.use_case.get_photo_info_list

import com.waseefakhtar.marsphotosapp.coroutines.DispatcherProvider
import com.waseefakhtar.marsphotosapp.data.remote.dto.Photo
import com.waseefakhtar.marsphotosapp.domain.model.PhotoInfo
import com.waseefakhtar.marsphotosapp.domain.repository.MarsPhotosRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPhotoInfoListUseCase @Inject constructor(
    private val repository: MarsPhotosRepository,
    private val dispatcherProvider: DispatcherProvider
) {

    @Throws(Throwable::class)
    suspend fun getPhotoInfoList(): List<PhotoInfo> {
        return withContext(dispatcherProvider.io()) {
            repository.getMarsPhotos().map { it.toPhotoInfo() }
        }
    }
}

private fun Photo.toPhotoInfo(): PhotoInfo =
    PhotoInfo(
        id = id,
        rover = rover.name,
        earthDate = earthDate,
        camera = camera.fullName
    )