package com.example.inskochi.data.AddEpc

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "epc_table")
data class AddEpc(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val ept:String,
    val time:String)