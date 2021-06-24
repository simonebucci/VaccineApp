package it.mspc.vaccinedata.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
/*
@Dao
interface VaccineDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUpdate(vaccine: Vaccine)

    @Query("SELECT * FROM vaccine_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Vaccine>>

}
*/