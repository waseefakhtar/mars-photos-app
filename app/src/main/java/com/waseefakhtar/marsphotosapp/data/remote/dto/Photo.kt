package com.waseefakhtar.marsphotosapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Photo(
    val camera: Camera,
    @SerializedName("earth_date")
    val earthDate: String,
    val id: Int,
    val img_src: String,
    val rover: Rover,
    val sol: Int
)