package com.example.inskochi.ui


import com.google.gson.annotations.SerializedName
import java.time.Instant


data class SubmitInfoModel(
    @SerializedName("ePC")
    val ePC: String,
    @SerializedName("time")
    val time: String
    )
