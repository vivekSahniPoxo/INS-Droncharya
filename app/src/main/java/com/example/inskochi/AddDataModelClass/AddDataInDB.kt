package com.example.inskochi.AddDataModelClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "Add_db_details_table",indices = [Index(value = ["rfidTagNo"], unique = true)])
data class AddDataInDB(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "rfidTagNo")
    val rfidTagNo:String,
    val firstName: String,
    val image:ByteArray,
    val aadhaarNo:String
)