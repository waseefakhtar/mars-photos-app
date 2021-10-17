package com.waseefakhtar.marsphotosapp.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.scope.MainCoroutineScopeRule
import com.waseefakhtar.marsphotosapp.data.remote.dto.MarsPhotosDto
import com.waseefakhtar.marsphotosapp.generateMarsPhotosDto
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.amshove.kluent.`should equal`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NasaApiTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get: Rule
    val mainCoroutineScopeRule: MainCoroutineScopeRule = MainCoroutineScopeRule()

    private lateinit var api: NasaApi
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createServer() {
        mockWebServer = MockWebServer()
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NasaApi::class.java)
    }

    @After
    fun stopServer() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Should get api response successfully`() {
        val randomMarsPhoto = generateMarsPhotosDto().photos.random()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("{\"photos\":[{\"it\":${randomMarsPhoto.id},\"sol\":${randomMarsPhoto.sol},\"camera\":{\"id\":${randomMarsPhoto.camera.id},\"name\":\"${randomMarsPhoto.camera.name}\",\"rover_id\":${randomMarsPhoto.camera.rover_id},\"full_name\":\"${randomMarsPhoto.camera.fullName}\"},\"img_src\":\"${randomMarsPhoto.imgSrc}\",\"earth_date\":\"${randomMarsPhoto.earthDate}\",\"rover\":{\"id\":${randomMarsPhoto.rover.id},\"name\":\"${randomMarsPhoto.rover.name}\",\"landing_date\":\"${randomMarsPhoto.rover.landingDate}\",\"launch_date\":\"${randomMarsPhoto.rover.launchDate}\",\"status\":\"${randomMarsPhoto.rover.status}\"}}]}")
        )

        runBlocking {
            val marsPhotos = api.getMarsPhotos()
            marsPhotos.`should equal`(MarsPhotosDto(listOf(randomMarsPhoto)))
        }
    }
}