package com.example.inskochi.data.AddEpc

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.inskochi.data.User
import com.example.inskochi.data.UserDao

@Database(entities = [AddEpc::class], version = 2, exportSchema = false)
abstract class EpcDataBase : RoomDatabase() {

    abstract fun epcDao(): EpcDAo

    companion object {
        @Volatile
        private var INSTANCE: EpcDataBase? = null

        fun getEpcDatabase(context: Context): EpcDataBase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EpcDataBase::class.java,
                    "Epc_Database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }


}