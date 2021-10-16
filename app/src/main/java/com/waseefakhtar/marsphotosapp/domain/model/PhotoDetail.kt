package com.waseefakhtar.marsphotosapp.domain.model

data class PhotoDetail(
    val rover: String,
    val launchDate: String,
    val landingDate: String,
    val imgSrc: String,
    val status: String
)