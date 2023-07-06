package com.example.inskochi.ragistration.data


import com.google.gson.annotations.SerializedName

//class VisitorsRegistration : ArrayList<VisitorsRegistration.VisitorsRegistrationItem>(){
    data class VisitorsRegistrationItem(

    @SerializedName("address")
    val address: String,
    @SerializedName("adhaarNo")
    val adhaarNo: String,
    @SerializedName("age")
    val age: String,
    @SerializedName("dateofissue")
    val dateofissue: String,
    @SerializedName("designation")
    val designation: String,
    @SerializedName("height")
    val height: String,
    @SerializedName("idPhoto")
    val idPhoto: String,
    @SerializedName("identificationMark")
    val identificationMark: String,
    @SerializedName("mobileNo")
    val mobileNo: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("nameofHost")
    val nameofHost: String,
    @SerializedName("passnoo")
    val passnoo: String,
    @SerializedName("relation")
    val relation: String,
    @SerializedName("rfidno")
    val rfidno: String,
    @SerializedName("validity")
    val validity: String,
    @SerializedName("visitorType")
    val visitorType: String,
    @SerializedName("workStation")
    val workStation: String

//        @SerializedName("address")
//        val address: String,
//        @SerializedName("adhaarNo")
//        val adhaarNo: String,
//        @SerializedName("age")
//        val age: String,
//        @SerializedName("dateofissue")
//        val dateofissue: String,
//        @SerializedName("designation")
//        val designation: String,
//        @SerializedName("height")
//        val height: String,
//        @SerializedName("idPhoto")
//        val idPhoto: String,
//        @SerializedName("identificationMark")
//        val identificationMark: String,
//        @SerializedName("mobileNo")
//        val mobileNo: String,
//        @SerializedName("name")
//        val name: String,
//        @SerializedName("nameofHost")
//        val nameofHost: String,
//        @SerializedName("passnoo")
//        val passnoo: String,
//        @SerializedName("relation")
//        val relation: String,
//        @SerializedName("rfidno")
//        val rfidno: String,
//        @SerializedName("validity")
//        val validity: String,
//        @SerializedName("visitorType")
//        val visitorType: String,
//        @SerializedName("workStation")
//        val workStation: String
    )
//}