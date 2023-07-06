package com.example.inskochi.apies


import com.example.inskochi.AddDataModelClass.GetInfoModelClass
import com.example.inskochi.ragistration.data.VisitorsRegistrationItem
import com.example.inskochi.ui.SubmitInfo
import com.example.inskochi.ui.SubmitInfoModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface Apies {

    @GET("visitors-info")
    suspend fun getVisitorsInfo():GetInfoModelClass

    @POST("submit-loginfo")
    suspend fun submitInfo(@Body submitInfo: MutableList<SubmitInfoModel>):Response<String>

    @POST("register-visitor")
    suspend fun submitRegisteredData(@Body SubmitRegisteredDetails:ArrayList<VisitorsRegistrationItem>):String

    @POST("update-visitor")
    suspend fun updateRegisteredItem(@Body updateRegisteredItem:ArrayList<VisitorsRegistrationItem>):Response<String>

    @POST("register-visitor")
    suspend fun submitRegistrationForm(@Body submitInfo:ArrayList<VisitorsRegistrationItem>):Response<VisitorsRegistrationItem>




}