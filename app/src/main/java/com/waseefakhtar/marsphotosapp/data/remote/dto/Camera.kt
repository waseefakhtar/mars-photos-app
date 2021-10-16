package com.waseefakhtar.marsphotosapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Camera(
    @SerializedName("full_name")
    val fullName: String,
    val id: Int,
    val name: String,
    val rover_id: Int
)