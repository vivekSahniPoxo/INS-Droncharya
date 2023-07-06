package com.example.inskochi

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.*
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.inskochi.data.AddEpc.AddEpc
import com.example.inskochi.data.AddEpc.EpcDataBase
import com.example.inskochi.data.AddEpc.EpcViewModel
import com.example.inskochi.data.User
import com.example.inskochi.data.UserViewModel
import com.example.inskochi.databinding.ActivityMainBinding
import com.example.inskochi.ui.SubmitInfoModel
import com.example.inskochi.ui.VisitorsViewModel
import com.example.inskochi.utils.NetworkResult
import com.google.android.material.snackbar.Snackbar
import com.speedata.libuhf.IUHFService
import com.speedata.libuhf.UHFManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.poi.EncryptedDocumentException
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Name
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.*
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val visitorsViewModel: VisitorsViewModel by viewModels()
    private val addInfoViewModel: UserViewModel by viewModels()
    private val roomViewModel: UserViewModel by viewModels()
    private val epcViewModel:EpcViewModel by viewModels()
    lateinit var binding: ActivityMainBinding
    lateinit var submit:MutableList<SubmitInfoModel>
    lateinit var progressBar: ProgressDialog
   // var iuhfService: IUHFService? = null
    lateinit var iuhfService: IUHFService
    var RfidNo: String? = null
    var stringList = arrayListOf<String>()

    var filepath = File(Environment.getExternalStorageDirectory().toString() + "/entryDetails.xls")


    var isConnected = false
     var isWiFi = false
    var name = ""
    var PassNo = ""
    var AddharNo = ""
    var Address = ""
    var Validity = ""
    var NameOfHost = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        submit = arrayListOf()

        progressBar = ProgressDialog(this@MainActivity)


        Thread {
            Handler(Looper.getMainLooper()).post {
                try {
                    iuhfService = UHFManager.getUHFService(this)
                    iuhfService.openDev()
                    iuhfService.antennaPower = 30
                    iuhfService.inventoryStart()
                } catch (e:Exception){

                }

            }

        }.start()







        binding.btnTest.setOnClickListener {
           // CoroutineScope(Dispatchers.Main).launch {
                roomViewModel.getVisitInfo("E28011721792AF12F5")
                bindItemFromRoomDb()

            //}
        }

        binding.btnTestTwo.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                roomViewModel.getVisitInfo("E2004701F3006022EEE2010B")
                bindItemFromRoomDb()

            }
        }

        binding.btnTestThree.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                roomViewModel.getVisitInfo("E200470168706022E6390111")
                bindItemFromRoomDb()

            }
        }

        binding.btnTestFour.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                roomViewModel.getVisitInfo("E2004701F3006022EEE2010B")
                bindItemFromRoomDb()

            }
        }

//        val cm = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork = cm.activeNetworkInfo
//        if (activeNetwork != null) {
//            isWiFi = activeNetwork.type == ConnectivityManager.TYPE_WIFI
//            isConnected = activeNetwork.isConnectedOrConnecting
//        }

//        epcViewModel.readAllData.observe(this, Observer {
//            for( i in it) {
//                Log.d("directDb", i.ept)
//                Log.d("directDb", i.time)
//            }
//        })

//        if (isConnected) {
//            if (isWiFi) {
//                Toast.makeText(this, "Yes, WiFi", Toast.LENGTH_SHORT).show()
//                if (isConnectedToThisServer(Cons.BASE_URL)) {
//                    epcViewModel.readAllData.observe(this, Observer {
//                        if (it.isNotEmpty()) {
//                            for( i in  it) {
//                                val allEpc = i.ept
//                                val time = i.time
//                                Log.d("directDb",i.ept)
//                                Log.d("directDb",i.time)
//                                submit.add(SubmitInfoModel(allEpc, time.toString()))
//                                Log.d("submitList",submit.toString())
//
//                            }
//                            visitorsViewModel.submitVisitorsInfo(submit)
//                            bindsubmitInfo()
//                        }
//                    })
//
//                    //Toast.makeText(this, "Yes, Connected to Google", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "The device is not in network range", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }


        binding.btnErase.setOnClickListener {
            binding.apply {
                   tvName.text = ""
                        tvAadhar.text = ""
                        tvAddress.text = ""
                        tvPassno.text = ""
                        tvAddress.text = ""
                        tvValidity.text = ""
                        tvVisitos.text = ""
                        imEmpImg.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.priest))
                        ll3.setBackgroundResource(R.drawable.bg_box)
                        imTick.isVisible = false
            }
        }


//        binding.btnSumbit.setOnClickListener {
//            epcViewModel.readAllData.observe(this, Observer {
////                Log.d("epc",it[0].ept)
//                if (it.isNotEmpty()) {
//                    for( i in  it) {
//                        val allEpc = i.ept
//                        val time = i.time
//                        Log.d("directDb",i.ept)
//                        Log.d("directDb",i.time)
//                        submit.add(SubmitInfoModel(allEpc, time.toString()))
//                        Log.d("submitList",submit.toString())
//                    }
//
//                    visitorsViewModel.submitVisitorsInfo(submit)
//                    bindsubmitInfo()
//                } else{
//                    visitorsViewModel.getVisitorsInfo()
//                     bindObserverToGetVisitorsInfo()
//
//                }
//            })
//
//
//        }


//        binding.imSynce.setOnClickListener {
//            if (checkForInternet(this)) {
//                epcViewModel.readAllData.observe(this, Observer {
//                    if (it.isNotEmpty()) {
//                       for( i in it) {
//                            val allEpc = i.ept
//                            val time = i.time
//                            submit.add(SubmitInfoModel(allEpc, time))
//                       }
//                        visitorsViewModel.submitVisitorsInfo(submit)
//                        bindsubmitInfo()
//                    } else{
//                        visitorsViewModel.getVisitorsInfo()
//                            bindObserverToGetVisitorsInfo()
//                    }
//
//
//                })
//
//                 //Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
//
//////
////            } else {
////                Toast.makeText(this, "The device is not in network range", Toast.LENGTH_SHORT).show()
////            }
//        }

//        binding.imSynce.setOnClickListener {
//            val db = DBHelper(this, null)
//            val cursor = db.getName()
//            cursor!!.moveToFirst()
//            Name.append(cursor.getString(cursor.getColumnIndex(DBHelper.RFIDNO_COl)) + "\n")
//            Age.append(cursor.getString(cursor.getColumnIndex(DBHelper.TIME_COL)) + "\n")
//            while(cursor.moveToNext()){
//                Name.append(cursor.getString(cursor.getColumnIndex(DBHelper.RFIDNO_COl)) + "\n")
//                Age.append(cursor.getString(cursor.getColumnIndex(DBHelper.RFIDNO_COl)) + "\n")
//            }
//            cursor.close()
//        }
//
   }





    private fun bindObserverToGetVisitorsInfo() {
        visitorsViewModel.getVisitorsInfo.observe(this, Observer {
            binding.imSyncProgress.isVisible = false
            binding.imSynce.isVisible = true
            //progressBar.hide()

            when (it) {
                is NetworkResult.Success -> {
                    roomViewModel.deleteAllRfid()
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
                     //binding.progressBar.isVisible = true
                    binding.imSyncProgress.isVisible = true
                    binding.imSynce.isVisible = false
//                    progressBar.show()
//                    progressBar.setMessage("Please wait........")
//                    progressBar.setCancelable(false)
                    Log.d("Error", "Loading")

                }
            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindItemFromRoomDb() {
        roomViewModel.getResponseFromDb.observe(this, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    binding.imEmpImg.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.priest))
                    binding.ll3.setBackgroundResource(R.drawable.bg_box)
                    try {
                         binding.tvNameHost.text = it.data?.nameofHost.toString()
                        binding.tvName.text = it.data?.name
                        binding.tvAadhar.text = it.data?.adhaarNo
                        binding.tvAddress.text = it.data?.address
                        binding.tvPassno.text = it.data?.passnoo
                        binding.tvVisitos.text = it.data?.visitorType
                        if (it.data?.validity!=null) {
                            binding.tvValidity.text = it.data.validity.toString()
                        }
                        val rfid = it.data?.rfidno.toString()
                        val current = LocalDateTime.now()
                        val epc = AddEpc(0, rfid, current.toString())
                        epcViewModel.addEpc(epc)

                        if (it.data?.validity!! < current.toString()){
                            binding.imTick.isVisible = true
                            binding.imTick.setBackgroundResource(R.drawable.cross_icon)
                            // binding.tvValidity.setTextColor(ContextCompat.getColor(this, R.color.green))
                        } else if(it.data.validity >=current.toString()){
                            binding.imTick.setBackgroundResource(R.drawable.greenicon)
                            binding.imTick.isVisible = true
                            // binding.tvValidity.setTextColor(ContextCompat.getColor(this, R.color.red))
                        }
                        val getValid = it.data?.validity


                            val string = it.data?.idPhoto
                            val imageBytes = android.util.Base64.decode(string, 0)
                            val img = convertCompressedByteArrayToBitmap(imageBytes)
                            binding.imEmpImg.setImageBitmap(img)
                        if (it.data?.visitorType == "Guest Pass" || it.data?.visitorType == "guest pass") {
                            binding.ll3.setBackgroundResource(R.drawable.green_bg_box)
                            binding.tvAddressHeading.text = "Address:"

                        } else if (it.data?.visitorType == "Servant Pass" || it.data?.visitorType == "servant pass") {
                            binding.ll3.setBackgroundResource(R.drawable.yellow_bg)
                            binding.tvAddressHeading.text = "Location:"
                        } else if(it.data?.visitorType == "Officer Guest Pass"){
                            binding.ll3.setBackgroundResource(R.drawable.red_bg)
                            binding.tvAddressHeading.text = "Address:"
                        }


//                            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
//                            val formatter = SimpleDateFormat("dd.MM.yyyy")
//                            val output = parser.parse(it.data?.validity.toString())

                            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                            val formatter = SimpleDateFormat("MM/dd/yyyy")

                            val output = formatter.format(parser.parse(getValid.toString()))
//                            binding.tvValidity.text = it.data?.validity.toString()
                            if (output != null) {
                                Log.d("outPutValidity",output.toString())
                            }

                            //Toast.makeText(this@MainActivity,output,Toast.LENGTH_SHORT).show()


//                        val rfid = it.data?.rfidno.toString()
//                        val current = LocalDateTime.now()
//                        val db = DBHelper(this, null)
//                        db.addName(rfid, current.toString())



                        if (filepath.exists()) {
                            UpdateExcelWrite()
                        } else {
                            createExcelSheet()
                        }

                    }  catch (e:Exception){

                    }
                }
                is NetworkResult.Error->{

                }
                is NetworkResult.Loading->{
                    binding.progressBar.isVisible = true

                }
            }
        })


    }



    fun convertCompressedByteArrayToBitmap(src: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(src, 0, src.size)
    }




    private fun bindsubmitInfo() {
        visitorsViewModel.binOutRepairResponseLiveData.observe(this, Observer {
            //binding.progressBar.isVisible = false
            binding.imSyncProgress.isVisible = false
            binding.imSynce.isVisible = true
            //progressBar.hide()

            when (it) {
                is NetworkResult.Success -> {
                    visitorsViewModel.getVisitorsInfo()
                    bindObserverToGetVisitorsInfo()
                    //Toast.makeText(this,"SuccessFully Submitted",Toast.LENGTH_SHORT).show()
                    epcViewModel.deleteAllEPC()
                    //roomViewModel.deleteAllRfid()
                    Thread {
                        EpcDataBase.getEpcDatabase(this).clearAllTables()
                    }.start() //clear all rows from database


                    Log.d("Submit", it.data.toString())
                   // Toast.makeText(this,"SuccessFully Submitted",Toast.LENGTH_SHORT).show()
                    Snackbar.make(binding.rootLayout,"SuccessFully Submitted",Snackbar.LENGTH_SHORT).show()

                    binding.apply {
                        tvName.text = ""
                        tvAadhar.text = ""
                        tvAddress.text = ""
                        tvPassno.text = ""
                        tvAddress.text = ""
                        tvValidity.text = ""
                        tvVisitos.text = ""
                        imEmpImg.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.priest))
                        ll3.setBackgroundResource(R.drawable.bg_box)
                        imTick.isVisible = false
                    }




                }is NetworkResult.Error -> {
                    //Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    Log.d("SubmitAPIERROR Response", it.message.toString())
                    it.message?.let { it1 -> Log.d("Error", it1)
                    }
                }
                is NetworkResult.Loading -> {
                     //binding.progressBar.isVisible = true
                    binding.imSyncProgress.isVisible = true
                    binding.imSynce.isVisible = false
//                    progressBar.show()
//                    progressBar.setMessage("Please wait........")
//                    progressBar.setCancelable(false)

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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_F1) {

            RfidNo = iuhfService.read_area(1, "2", "6", "00000000").toString()

            Toast.makeText(this,RfidNo,Toast.LENGTH_SHORT).show()
            Log.d("ScannedRfid",RfidNo.toString())
            try {


                CoroutineScope(Dispatchers.Main).launch {
                roomViewModel.getVisitInfo(RfidNo.toString())
                 bindItemFromRoomDb()




                }

//                epcViewModel.readAllData.removeObservers(this)
            } catch (e:Exception){
                Log.d("Error",e.toString())
            }
            return true
        }
        else {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           // startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        }
        return super.onKeyUp(keyCode, event)
    }

//    private fun byteArrayToBitmap(data: ByteArray): Bitmap {
//        return BitmapFactory.decodeByteArray(data, 0, data.size)
//    }


    fun  isConnectedToThisServer(host:String ): Boolean{
        val runtime = Runtime.getRuntime()
        try {
            val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8" + host);
            val exitValue = ipProcess.waitFor()
            return (exitValue == 0)
        } catch ( e: IOException) {
            e.printStackTrace()
        } catch ( e:InterruptedException) {
            e.printStackTrace()
        }
        return false
    }


    private fun createExcelSheet() {
        val hssfWorkbook = HSSFWorkbook()
        val hssfSheet = hssfWorkbook.createSheet()
        val row = hssfSheet.createRow(0)
        val row1 = hssfSheet.createRow(1)
        var cell = row.createCell(0)
        //        Row row = sheet.createRow(0);
        cell = row.createCell(0) as HSSFCell
        cell.setCellValue("name")
        //        cell.setCellStyle(cellStyle);
        cell = row.createCell(1) as HSSFCell
        cell.setCellValue("PassNo")
        //        cell.setCellStyle(cellStyle);
        cell = row.createCell(2) as HSSFCell
        cell.setCellValue("AddharNo")
        //        cell.setCellStyle(cellStyle);
        cell = row.createCell(3) as HSSFCell
        cell.setCellValue("Address")
        cell = row.createCell(4) as HSSFCell
        cell.setCellValue("Name Of Host")
        cell = row.createCell(5) as HSSFCell
        cell.setCellValue("Validity")

        //Value Here
        cell = row1.createCell(0) as HSSFCell
        cell.setCellValue(binding.tvName.text.toString())
        //        cell.setCellStyle(cellStyle);
        cell = row1.createCell(1) as HSSFCell
        cell.setCellValue( binding.tvPassno.text.toString())
        //        cell.setCellStyle(cellStyle);
        cell = row1.createCell(2) as HSSFCell
        cell.setCellValue(binding.tvAadhar.text.toString())

        cell = row1.createCell(3) as HSSFCell
        cell.setCellValue( binding.tvAddress.text.toString())
        cell = row1.createCell(4) as HSSFCell
        cell.setCellValue(binding.tvNameHost.text.toString())
        cell = row1.createCell(5) as HSSFCell
        cell.setCellValue(binding.tvValidity.text.toString())

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
        val newStudents = arrayOf(arrayOf<Any>(name, PassNo,AddharNo,Address,NameOfHost,Validity))
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
                var columnCount = 0

                //Iterating student informations
                for (info in student) {

                    //Creating new cell and setting the value
                    val cell = row.createCell(columnCount++)
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


    override fun onBackPressed() {

        val i = Intent(applicationContext, HomeActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }




}





