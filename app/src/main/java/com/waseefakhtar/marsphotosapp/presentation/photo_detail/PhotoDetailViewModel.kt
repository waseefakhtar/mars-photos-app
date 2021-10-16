package com.waseefakhtar.marsphotosapp.presentation.photo_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waseefakhtar.marsphotosapp.common.Resource
import com.waseefakhtar.marsphotosapp.domain.model.PhotoDetail
import com.waseefakhtar.marsphotosapp.domain.use_case.get_photo_detail.GetPhotoDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    private val getPhotoDetailUseCase: GetPhotoDetailUseCase
) : ViewModel() {

    fun photoDetailState(): LiveData<Resource<PhotoDetail>> = photoDetailState
    private val photoDetailState: MutableLiveData<Resource<PhotoDetail>> = MutableLiveData()

    fun onLoad(id: Int) {
        photoDetailState.value = Resource.Loading()
        viewModelScope.launch {
            try {
                photoDetailState.value = Resource.Success(getPhotoDetailUseCase.getPhotoDetailById(id))
            } catch (e: HttpException) {
                photoDetailState.value = Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
            } catch (e: IOException) {
                photoDetailState.value = Resource.Error("Couldn't reach server. Check your internet connection")
            }
        }
    }
}