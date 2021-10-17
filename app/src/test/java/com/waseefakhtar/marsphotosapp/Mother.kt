package com.waseefakhtar.marsphotosapp

import com.google.gson.annotations.SerializedName
import com.waseefakhtar.marsphotosapp.data.remote.dto.Camera
import com.waseefakhtar.marsphotosapp.data.remote.dto.MarsPhotosDto
import com.waseefakhtar.marsphotosapp.data.remote.dto.Photo
import com.waseefakhtar.marsphotosapp.data.remote.dto.Rover
import com.waseefakhtar.marsphotosapp.domain.model.PhotoDetail
import com.waseefakhtar.marsphotosapp.domain.model.PhotoInfo
import java.util.*
import java.util.concurrent.ThreadLocalRandom

private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
val random
    get() = ThreadLocalRandom.current()

fun generatePhotoList(
    size: Int = randomPositiveInt(10),
    creationFunction: (Int) -> Photo = { generatePhoto() }
): List<Photo> =
    (0..size).map { creationFunction(it) }

fun generatePhoto(): Photo =
    Photo(
        camera = generateCamera(),
        earthDate = randomString(),
        id = randomInt(),
        imgSrc = randomString(),
        rover = generateRover(),
        sol = randomInt()
    )

private fun generateCamera(): Camera =
    Camera(
        fullName = randomString(),
        id = randomInt(),
        name = randomString(),
        rover_id = randomInt()
    )

private fun generateRover(): Rover =
    Rover(
        id = randomInt(),
        landingDate = randomString(),
        launchDate = randomString(),
        name = randomString(),
        status = randomString()
    )

fun generateMarsPhotosDto(): MarsPhotosDto =
    MarsPhotosDto(
        photos = generatePhotoList()
    )

fun Photo.toPhotoInfo(): PhotoInfo =
    PhotoInfo(
        id = id,
        rover = rover.name,
        earthDate = earthDate,
        camera = camera.fullName
    )

fun Photo.toPhotoDetail(): PhotoDetail =
    PhotoDetail(
        rover = rover.name,
        launchDate = rover.launchDate,
        landingDate = rover.landingDate,
        imgSrc = imgSrc,
        status = rover.status.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    )


fun randomPositiveInt(maxInt: Int = Int.MAX_VALUE - 1): Int = random.nextInt(maxInt + 1).takeIf { it > 0 } ?: randomPositiveInt(maxInt)
fun randomPositiveLong(maxLong: Long = Long.MAX_VALUE - 1): Long = random.nextLong(maxLong + 1).takeIf { it > 0 } ?: randomPositiveLong(maxLong)
fun randomInt() = random.nextInt()
fun randomIntBetween(min: Int, max: Int) = random.nextInt(max - min) + min
fun randomLong() = random.nextLong()
fun randomBoolean() = random.nextBoolean()
fun randomString(size: Int = 20): String = (0..size)
    .map { charPool[random.nextInt(0, charPool.size)] }
    .joinToString("")