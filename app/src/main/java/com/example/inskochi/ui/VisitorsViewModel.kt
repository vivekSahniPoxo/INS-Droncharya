package com.example.inskochi.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inskochi.AddDataModelClass.GetInfoModelClass
import com.example.inskochi.ragistration.data.VisitorsRegistrationItem
import com.example.inskochi.utils.Event
import com.example.inskochi.utils.NetworkResult

import com.example.inskochi.utils.SingleLiveEvent
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class VisitorsViewModel  @Inject constructor(val visitorsRepo: VisitorsRepo): ViewModel() {


    private var _getVisitorsInfo = SingleLiveEvent<NetworkResult<GetInfoModelClass>>()
    val getVisitorsInfo: SingleLiveEvent<NetworkResult<GetInfoModelClass>> = _getVisitorsInfo

    fun getVisitorsInfo(){
        viewModelScope.launch {
            visitorsRepo.getAllVisitorsInfo().collect{
                _getVisitorsInfo.postValue(it)
            }
        }
    }

    private var _submitRegisTeredData = SingleLiveEvent<NetworkResult<String>>()
    val submitRegisTeredData: SingleLiveEvent<NetworkResult<String>> = _submitRegisTeredData
    fun submitRegis(SubmitRegis:ArrayList<VisitorsRegistrationItem>){
        viewModelScope.launch(Dispatchers.Default) {
            visitorsRepo.submitRegis(SubmitRegis).collect{
                _submitRegisTeredData.postValue(it)

            }
        }
    }

    private var _updateRegistredItem = SingleLiveEvent<NetworkResult<Response<String>>>()
    val updateRegistredItem: SingleLiveEvent<NetworkResult<Response<String>>> = _updateRegistredItem
    fun updateRegisteredItem(visitorsRegistrationItem: ArrayList<VisitorsRegistrationItem>){
        viewModelScope.launch(Dispatchers.IO) {
            visitorsRepo.updateRegisteredItem(visitorsRegistrationItem).collect{
                _updateRegistredItem.postValue(it)

            }
        }
    }


    val binOutRepairResponseLiveData: SingleLiveEvent<NetworkResult<String>>
    get() = visitorsRepo.dispatchStatusCreateLiveData
    fun submitVisitorsInfo(submitInfo:  MutableList<SubmitInfoModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            visitorsRepo.submitInfo(submitInfo)
        }
    }


    val updateResponseLiveData: SingleLiveEvent<NetworkResult<String>> get() = visitorsRepo.updateDataResponeLiveData
    fun updateData(visitorsRegistrationItem: ArrayList<VisitorsRegistrationItem>){
        viewModelScope.launch(Dispatchers.IO) {
            visitorsRepo.updateRegisteredData(visitorsRegistrationItem)
        }

    }



    val submitRegistrationFormResponseLiveData: SingleLiveEvent<NetworkResult<VisitorsRegistrationItem>> get() = visitorsRepo.submitRegistrationForm
    fun submitRegistrationForm(submitInfo:  ArrayList<VisitorsRegistrationItem>) {
        viewModelScope.launch(Dispatchers.Default) {
            visitorsRepo.submitRegistrationForm(submitInfo)
        }
    }



}