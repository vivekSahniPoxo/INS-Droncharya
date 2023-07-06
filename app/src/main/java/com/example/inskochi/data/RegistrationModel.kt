package com.example.inskochi.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "registration_table",indices = [Index(value = ["rfidno"], unique = true)])
data class RegistrationModel(@PrimaryKey(autoGenerate = true)
                val id: Int,

                val address: String,

                val adhaarNo: String,

                val age: String,

                val dateofissue: String,

               // val designation: String,

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