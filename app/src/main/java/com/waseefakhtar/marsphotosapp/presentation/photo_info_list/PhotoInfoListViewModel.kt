package com.waseefakhtar.marsphotosapp.presentation.photo_info_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waseefakhtar.marsphotosapp.common.Resource
import com.waseefakhtar.marsphotosapp.domain.model.PhotoInfo
import com.waseefakhtar.marsphotosapp.domain.use_case.get_photo_info_list.GetPhotoInfoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PhotoInfoListViewModel @Inject constructor(
    private val getPhotoInfoListUseCase: GetPhotoInfoListUseCase
) : ViewModel() {

    fun photoInfoListState(): LiveData<Resource<List<PhotoInfo>>> = photoInfoListState
    private val photoInfoListState: MutableLiveData<Resource<List<PhotoInfo>>> = MutableLiveData()

    fun onLoad() {
        photoInfoListState.value = Resource.Loading()
        viewModelScope.launch {
            try {
                photoInfoListState.value = Resource.Success(getPhotoInfoListUseCase.getPhotoInfoList())
            } catch (e: HttpException) {
                photoInfoListState.value = Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
            } catch (e: IOException) {
                photoInfoListState.value = Resource.Error("Couldn't reach server. Check your internet connection")
            }
        }
    }
}