package com.example.inskochi.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.inskochi.data.RegistrationModel
import com.example.inskochi.utils.NetworkResult
import com.example.inskochi.data.User
import com.example.inskochi.data.UserDatabase
import com.example.inskochi.data.UserRepository
import com.example.inskochi.utils.SingleLiveEvent
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {

     val readAllData: LiveData<List<User>>

     val readAllRegisteredData: LiveData<List<RegistrationModel>>
    private var repository: UserRepository

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        readAllData = repository.readAllData
        readAllRegisteredData = repository.readAllRegisteredData
    }

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun registerUser(registrationModel: RegistrationModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.registerUser(registrationModel)
        }
    }

    fun updateRegistration(registrationModel: RegistrationModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateRegistration(registrationModel)
        }
    }

    val clientSearchResults: MutableLiveData<List<User>>

    init {
        val clientDB = UserDatabase.getDatabase(application)
        val clientDao = clientDB.userDao()
        repository = UserRepository(clientDao)
        clientSearchResults = repository.userSearchResults
    }




    private var _getResponseFromDb = SingleLiveEvent<NetworkResult<User>>()
    val getResponseFromDb:SingleLiveEvent<NetworkResult<User>> = _getResponseFromDb
    fun getVisitInfo(rfidTagNo:String){
        viewModelScope.launch(Dispatchers.Main) {
            repository.getUserDetails(rfidTagNo).collect{
                _getResponseFromDb.postValue(it)
            }
        }
    }



    suspend fun findRfid(rfid:String):User{
        val deferred: Deferred<User> = viewModelScope.async {
            repository.findRfid(rfid)
        }
        return deferred.await()
    }


    suspend fun findRfidFromRegisTable(rfid:String):RegistrationModel{
        val deferred: Deferred<RegistrationModel> = viewModelScope.async {
            repository.findRfidFromResige(rfid)
        }
        return deferred.await()
    }






    fun deleteAllRfid(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllRfid()
        }
    }

    fun deleteRegisteredData(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRegisteredData()
        }

    }





}