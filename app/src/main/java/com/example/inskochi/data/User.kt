package com.example.inskochi.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_table",indices = [Index(value = ["rfidno"], unique = true)])
data class User(@PrimaryKey(autoGenerate = true)
    val id: Int,

val address: String,

val adhaarNo: String,

val age: String,

val dateofissue: String,

val designation: String,

val height: String,

val idPhoto: String,

val identificationMark: String,

val mobileNo: String,

val name: String,

val nameofHost: String,

val passnoo: String,

val relation: String,

val rfidno: String,

val validity: String,

val visitorType: String,

val workStation: String
)


//@ColumnInfo(name = "rfidTagNo")
//val rfidTagNo:String,
//val firstName: String,
//val memberId: String,
//val personnelNo:String,
//val sailorOfficer:String,
//val Rank: String,
//val unit:String,
//val image:ByteArray,
//val aadhaarNo:String,
//val choose:String,
//val data:String