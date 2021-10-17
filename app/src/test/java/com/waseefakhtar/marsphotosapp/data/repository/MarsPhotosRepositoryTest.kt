package com.waseefakhtar.marsphotosapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.scope.MainCoroutineScopeRule
import com.waseefakhtar.marsphotosapp.data.remote.NasaApi
import com.waseefakhtar.marsphotosapp.generateMarsPhotosDto
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.When
import org.amshove.kluent.`should equal`
import org.amshove.kluent.calling
import org.amshove.kluent.mock
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito

class MarsPhotosRepositoryTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get: Rule
    val mainCoroutineScopeRule: MainCoroutineScopeRule = MainCoroutineScopeRule()

    private val nasaApi: NasaApi = mock()
    private val marsPhotosRepository = MarsPhotosRepositoryImpl(nasaApi)

    @Before
    fun setUp() {
        Mockito.reset(nasaApi)
    }

    @Test
    fun `Should return marsPhotos from API successfully`() = mainCoroutineScopeRule.runBlockingTest {
        val marsPhotosDto = generateMarsPhotosDto()
        val photoList = marsPhotosDto.photos
        val rover = photoList.random().rover.name
        When calling nasaApi.getMarsPhotos(rover) doReturn marsPhotosDto

        val result = marsPhotosRepository.getMarsPhotos(rover)

        result.`should equal`(photoList)
    }
}