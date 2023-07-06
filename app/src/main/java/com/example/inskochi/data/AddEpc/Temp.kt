package com.example.inskochi.data.AddEpc


import com.google.gson.annotations.SerializedName

class Temp : ArrayList<Temp.TempItem>(){
    data class TempItem(
        @SerializedName("ePC")
        val ePC: String,
        @SerializedName("time")
        val time: String
    )
}