package com.waseefakhtar.marsphotosapp.presentation.photo_detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.scope.MainCoroutineScopeRule
import com.waseefakhtar.marsphotosapp.common.Resource
import com.waseefakhtar.marsphotosapp.domain.model.PhotoDetail
import com.waseefakhtar.marsphotosapp.domain.model.PhotoInfo
import com.waseefakhtar.marsphotosapp.domain.use_case.get_photo_detail.GetPhotoDetailUseCase
import com.waseefakhtar.marsphotosapp.generatePhotoList
import com.waseefakhtar.marsphotosapp.toPhotoDetail
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.Verify
import org.amshove.kluent.When
import org.amshove.kluent.`Verify no further interactions`
import org.amshove.kluent.`should equal`
import org.amshove.kluent.called
import org.amshove.kluent.calling
import org.amshove.kluent.on
import org.amshove.kluent.that
import org.amshove.kluent.was
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import java.io.IOException

class PhotoDetailViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get: Rule
    val mainCoroutineScopeRule: MainCoroutineScopeRule = MainCoroutineScopeRule()

    private val getPhotoDetailUseCase: GetPhotoDetailUseCase = mock()
    private val photoDetailStates = mutableListOf<Resource<PhotoDetail>>()
    private val photoDetailViewModel = PhotoDetailViewModel(getPhotoDetailUseCase)

    @Before
    fun setUp() {
        photoDetailStates.clear()
        Mockito.reset(getPhotoDetailUseCase)
        observeViewModel(photoDetailViewModel)
    }

    @Test
    fun `Should load photoDetail successfully`() = mainCoroutineScopeRule.runBlockingTest {
        val photoList = generatePhotoList()
        val randomPhoto = photoList.random()
        val photoDetail = randomPhoto.toPhotoDetail()
        When calling getPhotoDetailUseCase.getPhotoDetailById(randomPhoto.id, randomPhoto.rover.name) doReturn photoDetail
        val expectedStates = listOf<Resource<PhotoDetail>>(
            Resource.Loading(),
            Resource.Success(photoDetail)
        )

        photoDetailViewModel.onLoad(randomPhoto.id, randomPhoto.rover.name)

        photoDetailStates.`should equal`(expectedStates)
        Verify on getPhotoDetailUseCase that getPhotoDetailUseCase.getPhotoDetailById(randomPhoto.id, randomPhoto.rover.name) was called
        `Verify no further interactions` on getPhotoDetailUseCase
    }

    @Test
    fun `Should show error when exception occurs`() = mainCoroutineScopeRule.runBlockingTest {
        val photoList = generatePhotoList()
        val randomPhoto = photoList.random()
        val exception = IOException()
        val expectedStates = listOf<Resource<List<PhotoInfo>>>(
            Resource.Loading(),
            Resource.Error("Couldn't reach server. Check your internet connection")
        )
        When calling getPhotoDetailUseCase.getPhotoDetailById(randomPhoto.id, randomPhoto.rover.name) doThrow exception

        photoDetailViewModel.onLoad(randomPhoto.id, randomPhoto.rover.name)

        photoDetailStates.`should equal`(expectedStates)
        Verify on getPhotoDetailUseCase that getPhotoDetailUseCase.getPhotoDetailById(randomPhoto.id, randomPhoto.rover.name) was called
        `Verify no further interactions` on getPhotoDetailUseCase
    }

    private fun observeViewModel(viewModel: PhotoDetailViewModel) {
        viewModel.photoDetailState().observeForever { photoDetailState -> photoDetailStates.add(photoDetailState) }
    }
}