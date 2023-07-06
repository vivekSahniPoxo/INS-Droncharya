package com.example.inskochi.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.inskochi.AddDataModelClass.GetInfoModelClass
import com.example.inskochi.apies.Apies
import com.example.inskochi.ragistration.data.VisitorsRegistrationItem
import com.example.inskochi.utils.NetworkResult
import com.example.inskochi.utils.SingleLiveEvent
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class VisitorsRepo  @Inject constructor(private val apies: Apies) {


    fun getAllVisitorsInfo() = flow{
        emit(NetworkResult.Loading())
        val response = apies.getVisitorsInfo()
        emit(NetworkResult.Success(response))
    }.catch { e->
        emit(NetworkResult.Error(e.message ?: "UnknownError"))
    }


    fun submitRegis(visitorsRegistrationItem: ArrayList<VisitorsRegistrationItem>) = flow{
        emit(NetworkResult.Loading())
        val response = apies.submitRegisteredData(visitorsRegistrationItem)
        emit(NetworkResult.Success(response))
    }.catch { e->
        emit(NetworkResult.Error(e.message ?: "UnknownError"))
    }


    fun updateRegisteredItem(updateRegisteredItem:ArrayList<VisitorsRegistrationItem>)= flow{
        emit(NetworkResult.Loading())
        val response = apies.updateRegisteredItem(updateRegisteredItem)
        emit(NetworkResult.Success(response))
    }.catch { e->
        emit(NetworkResult.Error(e.message ?: "UnknownError"))
    }


    private val _updataDataResponeLiveData = SingleLiveEvent<NetworkResult<String>>()
    val updateDataResponeLiveData: SingleLiveEvent<NetworkResult<String>> get() = _updataDataResponeLiveData
    suspend fun updateRegisteredData(updateRegisteredItem:ArrayList<VisitorsRegistrationItem>){
        _updataDataResponeLiveData.postValue(NetworkResult.Loading())
        val response = apies.updateRegisteredItem(updateRegisteredItem)
        handleUpdateRespons(response)
    }


    private fun handleUpdateRespons(response: Response<String>){
        try {
            if (response.isSuccessful && response.body() !=null){
                _updataDataResponeLiveData.postValue(NetworkResult.Success(response.body()!!))
            }
            else if (response.errorBody()!=null){
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _updataDataResponeLiveData.postValue(NetworkResult.Error(errorObj.getString("Something Went Wrong")))
            }
            else {
                _updataDataResponeLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            } }
        catch (e:Exception){
        }
    }



    private val _dispatchStatusCreateLiveData = SingleLiveEvent<NetworkResult<String>>()
    val dispatchStatusCreateLiveData: SingleLiveEvent<NetworkResult<String>> get() = _dispatchStatusCreateLiveData

    suspend fun submitInfo(submitInfo:  MutableList<SubmitInfoModel>){
        _dispatchStatusCreateLiveData.postValue(NetworkResult.Loading())
        val response = apies.submitInfo(submitInfo)
        handleCreateRfidTagStatus(response)

    }



    private val _submitRegistrationForm = SingleLiveEvent<NetworkResult<VisitorsRegistrationItem>>()
    val submitRegistrationForm: SingleLiveEvent<NetworkResult<VisitorsRegistrationItem>> get() = _submitRegistrationForm

    suspend fun submitRegistrationForm(submitRegistrationForm:  ArrayList<VisitorsRegistrationItem>){
        _dispatchStatusCreateLiveData.postValue(NetworkResult.Loading())
        val response = apies.submitRegistrationForm(submitRegistrationForm)
        handleRegisteredData(response)

    }



    private fun handleRegisteredData(response: Response<VisitorsRegistrationItem>){
        try {
            if (response.isSuccessful && response.body() !=null){
                _submitRegistrationForm.postValue(NetworkResult.Success(response.body()!!))
            }
            else if (response.errorBody()!=null){
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _submitRegistrationForm.postValue(NetworkResult.Error(errorObj.getString("Something Went Wrong")))
            }
            else {
                _submitRegistrationForm.postValue(NetworkResult.Error("Something Went Wrong"))
            } }
        catch (e:Exception){
        }
    }


    private fun handleCreateRfidTagStatus(response: Response<String>){
        try {
            if (response.isSuccessful && response.body() !=null){
                _dispatchStatusCreateLiveData.postValue(NetworkResult.Success(response.body()!!))
            }
            else if (response.errorBody()!=null){
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _dispatchStatusCreateLiveData.postValue(NetworkResult.Error(errorObj.getString("Something Went Wrong")))
            }
            else {
                _dispatchStatusCreateLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            } }
        catch (e:Exception){
        }
    }




}