package com.example.inskochi.data.AddEpc

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface  EpcDAo {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addEpc(epc: AddEpc)

    @Query("SELECT * FROM epc_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<AddEpc>>

    @Query("DELETE FROM epc_table")
    suspend fun deleteAllEPC()

    @Query("SELECT * From epc_table JOIN registration_table ON epc_table.adhaarNo=registration_table.rfidno")
    fun getJoinData(): List<AddEpc?>?


}