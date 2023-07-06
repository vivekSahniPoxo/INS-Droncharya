package com.example.inskochi.ragistration

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.inskochi.data.UserViewModel
import com.example.inskochi.HomeActivity
import com.example.inskochi.R
import com.example.inskochi.data.AddEpc.AddEpc
import com.example.inskochi.data.AddEpc.EpcViewModel
import com.example.inskochi.data.RegistrationModel
import com.example.inskochi.data.User
import com.example.inskochi.databinding.ActivityRaginstrationBinding
import com.example.inskochi.ui.VisitorsViewModel
import com.example.inskochi.utils.NetworkResult
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.speedata.libuhf.IUHFService
import com.speedata.libuhf.UHFManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


class RaginstrationActivity : AppCompatActivity() {
     lateinit var binding: ActivityRaginstrationBinding
    private var selectedFirstAidType: Int = 0
    private var selectType: String = "Guest Pass"
    var imageUri: Uri?=null
    lateinit var iuhfService: IUHFService
    var RfidNo = ""
    var issueDate =  ""
    var validDate = ""
    lateinit var dialog:Dialog
    private val roomViewModel: UserViewModel by viewModels()
    private val visitorsViewModel:VisitorsViewModel by viewModels()
    private val epcViewModel: EpcViewModel by viewModels()
    private val addInfoViewModel: UserViewModel by viewModels()

    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
        binding.btnImage.setImageURI(null)
        Glide.with(this).load(imageUri).centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE)
            .apply(RequestOptions.skipMemoryCacheOf(true)).into(binding.btnImage)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRaginstrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTypeSpinner()









        binding.btnImage.setOnClickListener {
            onClickRequestPermission(binding.root)
        }

        imageUri = createImageUri()!!
        binding.addBtn.setOnClickListener {
                if (validation()) {
                    binding.apply {
                        val name = etName.text.toString()
                        val passNo = etPassNo.text.toString()
                        //val rfidNo = tvRfidNo.text.toString()
                         val rfidNo = "E28011721792AF12F5"
                        val nameOFHost = etNameOfHost.text.toString()
                        val relation = etRelation.text.toString()
                        val age = etAge.text.toString()
                        val height = etHeight.text.toString()
                        val workStation = etWorkStation.text.toString()
                        val address = etAddress.text.toString()
                        val identification = etIdent.text.toString()
                        val aadhar = etAadhar.text.toString()
                        val mobile = etMobile.text.toString()
                        issueDate = LocalDateTime.now().toString()

                    val bitmap = (binding.btnImage.drawable as BitmapDrawable).bitmap
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                    val image = stream.toByteArray()
                    val registData = RegistrationModel(0, address,aadhar,age,issueDate,height,image.toBase64(),identification,
                    mobile,name,nameOFHost,passNo,relation,rfidNo,validDate,selectType,workStation)

                        val visitrosInfo = User(
                            0,
                            address,
                            aadhar,
                            age,
                            issueDate,
                            "",
                            height,
                            image.toBase64(),
                            identification,
                            mobile,
                            name,
                            nameOFHost,
                            passNo,
                            relation,
                            rfidNo,
                            validDate,
                            selectType,
                            workStation)
                        addInfoViewModel.addUser(visitrosInfo)

                        CoroutineScope(Dispatchers.IO).launch {

                            val rfidFromGetVisitorsTable = roomViewModel.findRfid(rfidNo)
                            val rfidFromRegisteredTable =
                                roomViewModel.findRfidFromRegisTable(rfidNo)
                            Handler(Looper.getMainLooper()).post {

                                try {
                                    if (rfidNo == rfidFromGetVisitorsTable.rfidno || rfidNo == rfidFromRegisteredTable.rfidno) {
                                        dialog = Dialog(this@RaginstrationActivity)
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                        dialog.setContentView(R.layout.rest_alert)
                                        dialog.setCancelable(false)
                                        dialog.show()

                                        val cancel: MaterialButton = dialog.findViewById(R.id.btn_cancel)
                                        cancel.setOnClickListener {
                                            dialog.dismiss()

                                        }

                                        val reset: MaterialButton? = dialog.findViewById(R.id.btn_ok)
                                        reset?.setOnClickListener {
                                            roomViewModel.updateRegistration(registData)
                                            roomViewModel.registerUser(registData)
                                            val current = LocalDateTime.now()
                                            val epc = AddEpc(0, rfidNo, current.toString())
                                             epcViewModel.addEpc(epc)
                                            Toast.makeText(this@RaginstrationActivity, "Successfully Updated!", Toast.LENGTH_LONG).show()
                                             val intent = Intent(this@RaginstrationActivity,HomeActivity::class.java)
                                              startActivity(intent)
                                              finish()

                                        }


                                    }

//                                    else {
//                                        roomViewModel.registerUser(registData)
//                                    }
                                } catch (e: Exception) {
                                    Log.d("exception", e.toString())
                                    roomViewModel.registerUser(registData)
                                    val current = LocalDateTime.now()
                                    val epc = AddEpc(0, rfidNo, current.toString())
                                     epcViewModel.addEpc(epc)
                                    Toast.makeText(this@RaginstrationActivity, "Successfully Registered!", Toast.LENGTH_LONG).show()
                                    val intent = Intent(this@RaginstrationActivity,HomeActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                        }



//                        // Update Log Info Table
//                        val current = LocalDateTime.now()
//                        val epc = AddEpc(0, rfidNo, current.toString())
//                        epcViewModel.addEpc(epc)
//
//
//                        Toast.makeText(this@RaginstrationActivity, "Successfully Registered!", Toast.LENGTH_LONG).show()
//                        val intent = Intent(this@RaginstrationActivity,HomeActivity::class.java)
//                        startActivity(intent)
                }
            }
        }

        binding.idBtnPickDate.setOnClickListener {
            pickDateTime()
        }


    }


    private fun createImageUri(): Uri? {
        val cImage = File(this.filesDir,"camera_photos.png")
        return FileProvider.getUriForFile(this,"com.example.inskochi.fileProvider",cImage)
    }








    private fun validation(): Boolean {

        if (binding.spType.selectedItem.toString().trim { it <= ' ' } == "Choose Visitors Type") {
            Snackbar.make(binding.root, "Please Choose Visitors Type", Snackbar.LENGTH_SHORT).show()
             return false

        } else if (binding.etName.text?.isEmpty() == true){
            binding.etName.error = "Name field should not be empty"
            return false

        } else if (binding.etPassNo.text?.isEmpty() == true){
            binding.etPassNo.error = "Pass No field should not be empty"
            return false



        } else if (binding.etNameOfHost.text?.isEmpty()==true){
            binding.etNameOfHost.error = "this field should not be empty"
            return false

        } else if (binding.etWorkStation.text?.isEmpty()==true){
            binding.etWorkStation.error= "this field should not be empty"
            return true

        } else if (binding.etRelation.text?.isEmpty()==true){
            binding.etRelation.error = "Relation field should not be empty"
            return false


        } else if (binding.etAge.text?.isEmpty()==true){
            binding.etAge.error = "Age field should not be empty"
            return false
        } else if (binding.etHeight.text?.isEmpty()==true){
            binding.etHeight.error = "Height field should not be empty"
            return false

        } else if(binding.etAddress.text?.isEmpty()==true){
            binding.etAddress.error = "Address field should not be empty"
            return false

        } else if (binding.etMobile.text?.isEmpty()==true && binding.etMobile.text!!.length<10){
            binding.etMobile.error = "Please provide a valid mobile no"
            return false

        } else if (binding.etIdent.text?.isEmpty()==true){
            binding.etIdent.error = "Identification Mark field should not be empty"
            return false

        } else if (binding.etAadhar.text?.isEmpty()==true){
            binding.etAadhar.error = "Aadhar field should not be empty"
            return false



//        } else if (binding.etDesignation.text?.isEmpty()==true){
//            binding.etDesignation.error = "Designation field should not be empty"
//            return false


        } else if (binding.tvRfidNo.text?.isEmpty()==true){
            binding.etPassNo.error=  "Rfid No field should not be empty"
            return false


        } else if(binding.idTVSelectedDate.text?.isEmpty()==true){
            binding.idTVSelectedDate.error = "Please select valid date"
            return false
        } else if(binding.btnImage.drawable==null){
            Snackbar.make(binding.root,"Please click image",Snackbar.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun setTypeSpinner() {
        val typeList = arrayOf("Choose Visitors Type", "Guest Pass", "Servant Pass","Officer Guest Pass")
        val adapter: ArrayAdapter<String> = object :
            ArrayAdapter<String>(this, androidx.appcompat.R.layout.select_dialog_item_material, typeList) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView =
                    super.getDropDownView(position, convertView, parent) as TextView

                if (position == binding.spType.selectedItemPosition && position != 0) {
                    view.setTextColor(Color.parseColor("#000000"))
                }
                if (position == 0) {
                    view.setTextColor(Color.parseColor("#999999"))
                }


                return view
            }

            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }
        }
        binding.spType.adapter = adapter
        binding.spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if (binding.spType.selectedItem.toString().trim { it <= ' ' } == "Choose Visitors Type") {

                }  else if (binding.spType.selectedItem.toString().trim { it <= ' ' } == "Servant Pass") {
                    binding
                        .apply {
                            etNameOfHost.hint = "Employed as"
                            //etWorkStation.hint = "Location"
                            etRelation.hint = "Name of Employer"
                            etWorkStation.isVisible = true
                            tvWorkStation.isVisible = true
                        }
                } else if (binding.spType.selectedItem.toString().trim { it <= ' ' } == "Guest Pass" || binding.spType.selectedItem.toString().trim { it <= ' ' } == "Officer Guest Pass") {

                    binding.apply {
                            etNameOfHost.hint = "Enter name of host"
                            etRelation.hint = "Enter relation"
                            tvWorkStation.isVisible = false
                            etWorkStation.isVisible = false

                        }
                }


                if (position > 0) {
                    selectedFirstAidType = if (position == 1) 1 else 0
                }
                selectType = parent?.getItemAtPosition(position).toString().toLowerCase()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }



    fun View.showSnackbar(view: View, msg: String, length: Int, actionMessage: CharSequence?, action: (View) -> Unit) {
        val snackbar = Snackbar.make(view, msg, length)
        if (actionMessage != null) {
            snackbar.setAction(actionMessage) {
                action(this)
            }.show()
        } else {
            snackbar.show()
        }
    }

    private fun onClickRequestPermission(view: View) {
        when {
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {

                contract.launch(imageUri)
//                binding.root.showSnackbar(view, getString(R.string.permission_granted),
//                    Snackbar.LENGTH_INDEFINITE, null) {
//                }
            }

            ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA) -> {
                binding.root.showSnackbar(view, getString(R.string.permission_required), Snackbar.LENGTH_INDEFINITE, getString(R.string.ok)) {
                    requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                }
            }

            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }

    fun ByteArray.toBase64(): String = String(Base64.getEncoder().encode(this))

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_F1) {
            Thread {
                Handler(Looper.getMainLooper()).post {
                    iuhfService = UHFManager.getUHFService(this@RaginstrationActivity)
                    iuhfService.openDev()
                    iuhfService.antennaPower = 30
                    iuhfService.inventoryStart()
                    RfidNo = iuhfService.read_area(1, "2", "6", "00000000").toString()

                }
            }.start()

            Toast.makeText(this,RfidNo,Toast.LENGTH_SHORT).show()
                binding.tvRfidNo.text = RfidNo

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


    private fun pickDateTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)



        DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                val gc = GregorianCalendar(year, month, day, hour, minute)
                val ldt = gc.toZonedDateTime().toLocalDateTime()
                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
                val formatter = SimpleDateFormat("dd-MM-yyyy")
                val output = parser.parse(ldt.toString())?.let { formatter.format(it) }
                binding.idTVSelectedDate.text = output.toString()
                 validDate = ldt.toString()
                Log.d("dateTime",output.toString())
            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
    }



    private fun bindSubmitRegistrationForm(){
        visitorsViewModel.submitRegistrationFormResponseLiveData.observe(this, androidx.lifecycle.Observer {
           binding.imSyncProgress.isVisible=false
            when(it){
               is NetworkResult.Success->{
                   Snackbar.make(binding.root,"Successfully submitted all registered record",Snackbar.LENGTH_SHORT).show()
               }
               is NetworkResult.Error ->{
                   Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
               }
                is NetworkResult.Loading ->{
                    binding.imSyncProgress.isVisible = false
                }
            }
        })
    }

    fun convertCompressedByteArrayToBitmap(src: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(src, 0, src.size)
    }

}