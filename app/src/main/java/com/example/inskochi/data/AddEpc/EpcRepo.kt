package com.example.inskochi.data.AddEpc

import androidx.lifecycle.LiveData


class EpcRepo(private val epcDao: EpcDAo) {

    val readAllData: LiveData<List<AddEpc>> = epcDao.readAllData()

        suspend fun addEpc(addEpc: AddEpc){
            epcDao.addEpc(addEpc)
        }


        suspend fun deleteAllEPC(){
            epcDao.deleteAllEPC()
        }




}