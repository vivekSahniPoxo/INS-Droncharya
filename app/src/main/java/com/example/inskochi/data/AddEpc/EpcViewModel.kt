package com.example.inskochi.data.AddEpc

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.inskochi.data.User
import com.example.inskochi.data.UserDatabase
import com.example.inskochi.data.UserRepository
import com.example.inskochi.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EpcViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<AddEpc>>
    private var repository: EpcRepo


    init {
        val epcDao = EpcDataBase.getEpcDatabase(application).epcDao()
        repository = EpcRepo(epcDao)
        readAllData = repository.readAllData
    }

    fun addEpc(epc: AddEpc){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addEpc(epc)
        }
    }


    fun deleteAllEPC(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllEPC()
        }
    }

}