package com.example.inskochi.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     suspend fun addUser(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun registerUser(registrationModel: RegistrationModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
   // @Query(("UPDATE registration_table SET price=:price WHERE id = :id"))
    suspend fun updateRegistration(registrationModel: RegistrationModel)

     @Query("SELECT * FROM user_table ORDER BY id ASC")
      fun readAllData(): LiveData<List<User>>


    @Query("SELECT * FROM registration_table ORDER BY id ASC")
    fun readAllRegisteredData(): LiveData<List<RegistrationModel>>

    @Query("SELECT * FROM registration_table WHERE rfidno = :rfidno")
    fun getRfidFromResigTable(rfidno: String):RegistrationModel


    @Query("SELECT * FROM user_table WHERE rfidno = :rfidno")
    suspend fun getUserDetails(rfidno: String): User


    @Query("DELETE FROM user_table")
    suspend fun deleteAllRfid()

    @Query("DELETE FROM registration_table")
    suspend fun deleteRegisteredData()




}