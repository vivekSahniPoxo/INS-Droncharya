package com.example.inskochi

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.inskochi.data.AddEpc.EpcDataBase
import com.example.inskochi.data.AddEpc.EpcViewModel
import com.example.inskochi.data.User
import com.example.inskochi.data.UserViewModel
import com.example.inskochi.databinding.ActivityHomeBinding
import com.example.inskochi.ragistration.RaginstrationActivity
import com.example.inskochi.ragistration.data.VisitorsRegistrationItem
import com.example.inskochi.ui.SubmitInfoModel
import com.example.inskochi.ui.VisitorsViewModel
import com.example.inskochi.utils.NetworkResult
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.apache.poi.EncryptedDocumentException
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    private val epcViewModel: EpcViewModel by viewModels()
    private val visitorsViewModel: VisitorsViewModel by viewModels()
    private val addInfoViewModel: UserViewModel by viewModels()
    private val roomViewModel: UserViewModel by viewModels()
    lateinit var submit:MutableList<SubmitInfoModel>
    private var pressedTime: Long = 0
     lateinit var submitRegisteredData:ArrayList<VisitorsRegistrationItem>

    var filepath = File(Environment.getExternalStorageDirectory().toString() + "/VisitorsLogs.xls")

    var RFIDNo = ""
    var time = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        submit = mutableListOf()
        submitRegisteredData = ArrayList()





        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                val getpermission = Intent()
                getpermission.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivity(getpermission)
            }
        }




        binding.btnExport.setOnClickListener {
            if (filepath.exists()) {
                UpdateExcelWrite()
            } else {
                createExcelSheet()
            }
//            visitorsViewModel.getVisitorsInfo()
//            bindObserverToGetVisitorsInfo()

        }

        binding.mcardView.setOnClickListener {
            val intent = Intent(this, RaginstrationActivity::class.java)
            startActivity(intent)

        }

        binding.mCardVisitor.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }

                binding.imSynce.setOnClickListener {
                   // if (checkForInternet(this)) {
                        roomViewModel.readAllRegisteredData.observe(this, androidx.lifecycle.Observer {
                            Toast.makeText(this,"knkjnk",Toast.LENGTH_SHORT).show()
                            it.forEach {
                                submitRegisteredData.add(VisitorsRegistrationItem(it.address,it.adhaarNo,it.age,it.dateofissue,"",it.height,it.idPhoto,it.identificationMark, it.mobileNo,it.name,it.nameofHost,it.passnoo,it.relation,it.rfidno,it.validity,it.visitorType,it.workStation))
                            }
                            visitorsViewModel.updateRegisteredItem(submitRegisteredData)
                            updateData()
                        })

        }


    }



    private fun updateData(){
        visitorsViewModel.updateRegistredItem.observe(this, androidx.lifecycle.Observer {
            binding.imSyncProgress.isVisible = false
            binding.imSynce.isVisible = true
            when(it){
                is NetworkResult.Success->{

                    Log.d("Success",it.toString())
                    roomViewModel.deleteRegisteredData()
                    visitorsViewModel.getVisitorsInfo()
                    bindObserverToGetVisitorsInfo()

                }
                is NetworkResult.Error->{
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    Log.d("Error", "Something went wrong")

                }
                is NetworkResult.Loading->{
                    binding.imSyncProgress.isVisible = true
                    binding.imSynce.isVisible = false
                }
            }
        })
    }



    private fun bindSubmitRegisteredData(){
        visitorsViewModel.submitRegisTeredData.observe(this, Observer {
            binding.imSyncProgress.isVisible = false
            binding.imSynce.isVisible = true
            when(it){
                is NetworkResult.Success->{
                    Toast.makeText(this, it.data.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Error->{
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

                }
                is NetworkResult.Loading->{
                    binding.imSyncProgress.isVisible = true
                    binding.imSynce.isVisible = false
                }
            }
        })
    }







    private fun bindsubmitInfo() {
        visitorsViewModel.binOutRepairResponseLiveData.observe(this, Observer {

            binding.imSyncProgress.isVisible = false
            binding.imSynce.isVisible = true


            when (it) {
                is NetworkResult.Success -> {

                    epcViewModel.deleteAllEPC()
                    //roomViewModel.deleteAllRfid()
                    Thread {
                        EpcDataBase.getEpcDatabase(this).clearAllTables()
                    }.start() //clear all rows from database


                    Log.d("Submit", it.data.toString())
                    // Toast.makeText(this,"SuccessFully Submitted",Toast.LENGTH_SHORT).show()
                    Snackbar.make(binding.root,"SuccessFully Submitted", Snackbar.LENGTH_SHORT).show()






                }is NetworkResult.Error -> {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                Log.d("SubmitAPIERROR Response", it.message.toString())
                it.message?.let { it1 -> Log.d("Error", it1)
                }
            }
                is NetworkResult.Loading -> {
                    binding.imSyncProgress.isVisible = true
                    binding.imSynce.isVisible = false

                }
            }
        })

    }


    private fun bindObserverToGetVisitorsInfo() {
        visitorsViewModel.getVisitorsInfo.observe(this, Observer {
            binding.imSyncProgress.isVisible = false
            binding.imSynce.isVisible = true
            //progressBar.hide()

            when (it) {
                is NetworkResult.Success -> {
                    roomViewModel.deleteAllRfid()
                    epcViewModel.readAllData.observe(this, Observer {
                        it.forEach {
                            RFIDNo = it.ept
                            time = it.time
                            submit.add(SubmitInfoModel(it.ept, it.time)) }
                            visitorsViewModel.submitVisitorsInfo(submit)
                            bindsubmitInfo()
//                        if (filepath.exists()) {
//                            UpdateExcelWrite()
//                        } else {
//                            createExcelSheet()
//                        }
                        })
                    //epcViewModel.deleteAllEPC()

//                    binding.apply {
//                        tvName.text = ""
//                        tvAadhar.text = ""
//                        tvAddress.text = ""
//                        tvPassno.text = ""
//                        tvAddress.text = ""
//                        imEmpImg.setBackgroundResource(R.drawable.priest)
//                        ll3.setBackgroundResource(R.drawable.bg_box)
//                    }



                    Log.d("Error", "Success")
                    for (i in it.data!!) {
                        Log.d("Address", i.address.toString())
                        // binding.tvNothingFound.isVisible = it.data?.results?.isEmpty() == true
                        val address = i.address.toString()
                        val aadhar = i.adhaarNo.toString()
                        val age = i.age.toString()
                        val dateOfIssue = i.dateofissue.toString()
                        val designation = i.designation.toString()
                        val height = i.height.toString()
                        val image = i.idPhoto.toString()
                        val identification = i.identificationMark.toString()
                        val phoneNo = i.mobileNo.toString()
                        val name = i.name.toString()
                        val hostName = i.nameofHost.toString()
                        val passN0 = i.passnoo.toString()
                        val relation = i.relation.toString()
                        val rfidNo = i.rfidno.toString()
                        val validity = i.validity.toString()
                        val visitorType = i.visitorType.toString()
                        val workStation = i.workStation.toString()

                        Log.d("bytArray",image.toByteArray().toString())

                        val visitrosInfo = User(
                            0,
                            address,
                            aadhar,
                            age,
                            dateOfIssue,
                            designation,
                            height,
                            image,
                            identification,
                            phoneNo,
                            name,
                            hostName,
                            passN0,
                            relation,
                            rfidNo,
                            validity,
                            visitorType,
                            workStation)
                        addInfoViewModel.addUser(visitrosInfo)

                    }

//                    epcViewModel.readAllData.observe(this, Observer {
//                        if (it.isNotEmpty()) {
//                            for( i in it) {
//                                val allEpc = i.ept
//                                val time = i.time
//                                submit.add(SubmitInfoModel(allEpc, time.toString()))
//
//                            }
//
//                        }
//                    })
//
//                    visitorsViewModel.submitVisitorsInfo(submit)
//                    bindsubmitInfo()


                }
                is NetworkResult.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    Log.d("Error", "Error")
                    it.message?.let { it1 -> Log.d("Error", it1) }
                }
                is NetworkResult.Loading -> {
                    binding.imSyncProgress.isVisible = true
                    binding.imSynce.isVisible = false

                }
            }
        })

    }




    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


    override fun onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()

        } else {
            Toast.makeText(baseContext, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
        pressedTime = System.currentTimeMillis()
    }



    private fun createExcelSheet() {

        val hssfWorkbook = HSSFWorkbook()
        val hssfSheet = hssfWorkbook.createSheet()
        val row = hssfSheet.createRow(0)
        val row1 = hssfSheet.createRow(1)
        var cell = row.createCell(0)
        //        Row row = sheet.createRow(0);
        cell = row.createCell(0) as HSSFCell
        cell.setCellValue("RFID No")
        //        cell.setCellStyle(cellStyle);
        cell = row.createCell(1) as HSSFCell
        cell.setCellValue("Time")


        //Value Here
        cell = row1.createCell(0) as HSSFCell
        cell.setCellValue(RFIDNo)
        //        cell.setCellStyle(cellStyle);
        cell = row1.createCell(1) as HSSFCell
        cell.setCellValue(time)
        try {
            if (!filepath.exists()) {
                filepath.mkdirs()
                filepath.createNewFile()
            }
            val fileOutputStream: FileOutputStream = FileOutputStream(filepath)
            hssfWorkbook.write(fileOutputStream)
            if (fileOutputStream != null) {
                Toast.makeText(this, "" + filepath, Toast.LENGTH_SHORT).show()
                fileOutputStream.flush()
                fileOutputStream.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun UpdateExcelWrite() {
        val newStudents = arrayOf(arrayOf<Any>(RFIDNo, time))
        try {
            //Creating input stream
            val inputStream = FileInputStream(filepath)

            //Creating workbook from input stream
            val workbook = WorkbookFactory.create(inputStream)

            //Reading first sheet of excel file
            val sheet = workbook.getSheetAt(0)

            //Getting the count of existing records
            var rowCount = sheet.lastRowNum

            //Iterating new students to update
            for (student in newStudents) {

                //Creating new row from the next row count
                val row = sheet.createRow(++rowCount)

                //Iterating student informations
                for ((columnCount, info) in student.withIndex()) {

                    //Creating new cell and setting the value
                    val cell = row.createCell(columnCount)
                    if (info is String) {
                        cell.setCellValue(info)
                    } else if (info is Int) {
                        cell.setCellValue(info.toString())
                    }
                }
            }
            //Close input stream
            inputStream.close()

            //Crating output stream and writing the updated workbook
            val os = FileOutputStream(filepath)
            workbook.write(os)

            //Close the workbook and output stream
            workbook.close()
            os.close()
            Toast.makeText(this, "Excel file has been updated successfully.", Toast.LENGTH_SHORT).show()
            println("Excel file has been updated successfully.")
        } catch (e: EncryptedDocumentException) {
            System.err.println("Exception while updating an existing excel file.")
            Toast.makeText(this, "Exception while updating an existing excel file.", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        } catch (e: IOException) {
            System.err.println("Exception while updating an existing excel file.")
            Toast.makeText(this, "Exception while updating an existing excel file.", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }





}



