package com.example.inskochi.utils

class Cons {
    companion object {
        const val BASE_URL = "http://164.52.223.163:4561/api/"
         //const val BASE_URL = "http://192.168.1.10:82/api/"

         const val CAMERA_PERMISSION_CODE = 100
         const val STORAGE_PERMISSION_CODE = 101
        const val loaderMessage = "Please Wait...."
        const val START = "START"
        const val STOP = "STOP"
        const val ISCOMMINGFROM = "isCommingFrom"
        // below is variable for database name
        private val DATABASE_NAME = "Visitors_Mangement_Table"

        // below is the variable for database version
        private val DATABASE_VERSION = 1
        // below is the variable for table name
        val TABLE_NAME = "Visitors_Info"
        // below is the variable for id column
        val ID_COL = "id"
        // below is the variable for name column
        val NAME_COl = "name"
        // below is the variable for age column
        val AGE_COL = "age"
        val VISITORSTYPE_COL = "visitors_Type"
        val RFID = "rfid"



    }
}