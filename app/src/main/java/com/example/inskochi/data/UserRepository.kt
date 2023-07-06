package com.example.inskochi.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.inskochi.utils.NetworkResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class UserRepository(private val userDao: UserDao) {

    val readAllData: LiveData<List<User>> = userDao.readAllData()

    val readAllRegisteredData: LiveData<List<RegistrationModel>> = userDao.readAllRegisteredData()

     suspend fun addUser(user: User){
        userDao.addUser(user)
    }

    suspend fun registerUser(registrationModel: RegistrationModel){
        userDao.registerUser(registrationModel)
    }

    suspend fun updateRegistration(registrationModel: RegistrationModel){
        userDao.updateRegistration(registrationModel)
    }


    val userSearchResults = MutableLiveData<List<User>>()



    suspend fun getUserDetails(rfidTagNo: String) = flow{
        emit(NetworkResult.Loading())
        val response = userDao.getUserDetails(rfidTagNo)
        emit(NetworkResult.Success(response))
    }.catch { e->
        emit(NetworkResult.Error("No Data Found"?: "UnknownError"))
    }


    suspend fun findRfid(rfid:String):User{
        return userDao.getUserDetails(rfid)
    }

    suspend fun findRfidFromResige(rfid:String):RegistrationModel{
        return userDao.getRfidFromResigTable(rfid)
    }


    suspend fun deleteAllRfid(){
        userDao.deleteAllRfid()
    }

    suspend fun deleteRegisteredData(){
        userDao.deleteRegisteredData()
    }

}