package com.waseefakhtar.marsphotosapp.presentation.photo_info_list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.scope.MainCoroutineScopeRule
import com.waseefakhtar.marsphotosapp.common.Resource
import com.waseefakhtar.marsphotosapp.domain.model.PhotoInfo
import com.waseefakhtar.marsphotosapp.domain.use_case.get_photo_info_list.GetPhotoInfoListUseCase
import com.waseefakhtar.marsphotosapp.generatePhotoList
import com.waseefakhtar.marsphotosapp.toPhotoInfo
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.When
import org.amshove.kluent.`should equal`
import org.amshove.kluent.calling
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import com.nhaarman.mockitokotlin2.mock
import org.amshove.kluent.Verify
import org.amshove.kluent.`Verify no further interactions`
import org.amshove.kluent.called
import org.amshove.kluent.on
import org.amshove.kluent.that
import org.amshove.kluent.was
import java.io.IOException

class PhotoInfoListViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get: Rule
    val mainCoroutineScopeRule: MainCoroutineScopeRule = MainCoroutineScopeRule()

    private val getPhotoInfoListUseCase: GetPhotoInfoListUseCase = mock()
    private val photoInfoListStates = mutableListOf<Resource<List<PhotoInfo>>>()
    private val photoInfoListViewModel = PhotoInfoListViewModel(getPhotoInfoListUseCase)

    @Before
    fun setUp() {
        photoInfoListStates.clear()
        Mockito.reset(getPhotoInfoListUseCase)
        observeViewModel(photoInfoListViewModel)
    }

    @Test
    fun `Should load photoInfoList successfully`() = mainCoroutineScopeRule.runBlockingTest {
        val photoInfoList = generatePhotoList().map { it.toPhotoInfo() }
        val randomRover = photoInfoList.random().rover
        val rover = Rover.values().random()
        When calling getPhotoInfoListUseCase.getPhotoInfoList(rover) doReturn photoInfoList
        val expectedStates = listOf<Resource<List<PhotoInfo>>>(
            Resource.Loading(),
            Resource.Success(photoInfoList)
        )

        photoInfoListViewModel.onLoad(rover)

        photoInfoListStates.`should equal`(expectedStates)
        Verify on getPhotoInfoListUseCase that getPhotoInfoListUseCase.getPhotoInfoList(rover) was called
        `Verify no further interactions` on getPhotoInfoListUseCase
    }

    @Test
    fun `Should show error when exception occurs`() = mainCoroutineScopeRule.runBlockingTest {
        val rover = Rover.values().random()
        val exception = IOException()
        val expectedStates = listOf<Resource<List<PhotoInfo>>>(
            Resource.Loading(),
            Resource.Error("Couldn't reach server. Check your internet connection")
        )
        When calling getPhotoInfoListUseCase.getPhotoInfoList(rover) doThrow exception

        photoInfoListViewModel.onLoad(rover)

        photoInfoListStates.`should equal`(expectedStates)
        Verify on getPhotoInfoListUseCase that getPhotoInfoListUseCase.getPhotoInfoList(rover) was called
        `Verify no further interactions` on getPhotoInfoListUseCase
    }

    private fun observeViewModel(viewModel: PhotoInfoListViewModel) {
        viewModel.photoInfoListState().observeForever { photoInfoListState -> photoInfoListStates.add(photoInfoListState) }
    }
}