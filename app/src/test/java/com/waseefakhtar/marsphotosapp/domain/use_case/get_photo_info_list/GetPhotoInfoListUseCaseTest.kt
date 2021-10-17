package com.waseefakhtar.marsphotosapp.domain.use_case.get_photo_info_list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.scope.MainCoroutineScopeRule
import com.waseefakhtar.marsphotosapp.coroutines.DispatcherProvider
import com.waseefakhtar.marsphotosapp.domain.repository.MarsPhotosRepository
import com.waseefakhtar.marsphotosapp.generatePhotoList
import com.waseefakhtar.marsphotosapp.toPhotoInfo
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.Verify
import org.amshove.kluent.When
import org.amshove.kluent.`Verify no further interactions`
import org.amshove.kluent.`should equal`
import org.amshove.kluent.called
import org.amshove.kluent.calling
import org.amshove.kluent.mock
import org.amshove.kluent.on
import org.amshove.kluent.that
import org.amshove.kluent.was
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import java.lang.RuntimeException

class GetPhotoInfoListUseCaseTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get: Rule
    val mainCoroutineScopeRule: MainCoroutineScopeRule = MainCoroutineScopeRule()

    private val repository: MarsPhotosRepository = mock()
    private val dispatcherProvider: DispatcherProvider = mock()
    private val getPhotoInfoListUseCase: GetPhotoInfoListUseCase = GetPhotoInfoListUseCase(repository, dispatcherProvider)

    @Before
    fun setUp() {
        When calling dispatcherProvider.io() doReturn mainCoroutineScopeRule.testDispatcher
        Mockito.reset(repository)
    }

    @Test
    fun `Should return photoInfoList successfully`() = mainCoroutineScopeRule.runBlockingTest {
        val photoList = generatePhotoList()
        val photoInfoList = photoList.map { it.toPhotoInfo() }
        When calling repository.getMarsPhotos() doReturn photoList

        val result = getPhotoInfoListUseCase.getPhotoInfoList()

        result.`should equal`(photoInfoList)
        Verify on repository that repository.getMarsPhotos() was called
        `Verify no further interactions` on repository
    }

    @Test
    fun `Should return exception when getting photoInfoList is unsuccessful`() = mainCoroutineScopeRule.runBlockingTest {
        val exception = RuntimeException()
        When calling repository.getMarsPhotos() doThrow exception

        val result = try {
            getPhotoInfoListUseCase.getPhotoInfoList()
            true
        } catch (throwable: Throwable) {
            false
        }

        result.`should equal`(false)
        Verify on repository that repository.getMarsPhotos() was called
        `Verify no further interactions` on repository
    }
}