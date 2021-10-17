package com.waseefakhtar.marsphotosapp.domain.use_case.get_photo_detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.scope.MainCoroutineScopeRule
import com.waseefakhtar.marsphotosapp.coroutines.DispatcherProvider
import com.waseefakhtar.marsphotosapp.domain.repository.MarsPhotosRepository
import com.waseefakhtar.marsphotosapp.generatePhotoList
import com.waseefakhtar.marsphotosapp.randomInt
import com.waseefakhtar.marsphotosapp.randomString
import com.waseefakhtar.marsphotosapp.toPhotoDetail
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

class GetPhotoDetailUseCaseTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get: Rule
    val mainCoroutineScopeRule: MainCoroutineScopeRule = MainCoroutineScopeRule()

    private val repository: MarsPhotosRepository = mock()
    private val dispatcherProvider: DispatcherProvider = mock()
    private val getPhotoDetailUseCase: GetPhotoDetailUseCase = GetPhotoDetailUseCase(repository, dispatcherProvider)

    @Before
    fun setUp() {
        When calling dispatcherProvider.io() doReturn mainCoroutineScopeRule.testDispatcher
        Mockito.reset(repository)
    }

    @Test
    fun `Should return photoDetail successfully`() = mainCoroutineScopeRule.runBlockingTest {
        val photoList = generatePhotoList()
        val randomPhoto = photoList.random()
        val photoDetail = randomPhoto.toPhotoDetail()
        val rover = photoDetail.rover
        When calling repository.getMarsPhotos(rover) doReturn photoList

        val result = getPhotoDetailUseCase.getPhotoDetailById(randomPhoto.id, rover)

        result.`should equal`(photoDetail)
        Verify on repository that repository.getMarsPhotos(rover) was called
        `Verify no further interactions` on repository
    }

    @Test
    fun `Should return exception when getting photoInfoList is unsuccessful`() = mainCoroutineScopeRule.runBlockingTest {
        val id = randomInt()
        val rover = randomString()
        val exception = RuntimeException()
        When calling repository.getMarsPhotos(rover) doThrow exception

        val result = try {
            getPhotoDetailUseCase.getPhotoDetailById(id, rover)
            true
        } catch (throwable: Throwable) {
            false
        }

        result.`should equal`(false)
        Verify on repository that repository.getMarsPhotos(rover) was called
        `Verify no further interactions` on repository
    }
}