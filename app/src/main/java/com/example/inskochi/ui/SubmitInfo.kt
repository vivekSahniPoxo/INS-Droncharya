package com.example.inskochi.ui

import com.google.gson.annotations.SerializedName

data class SubmitInfo( @SerializedName("ePC")
                       val ePC: String,
                       @SerializedName("time")
                       val time: String)