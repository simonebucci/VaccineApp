package it.mspc.vaccinedata.data.vaccine

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.mspc.vaccinedata.data.vaccine.Vaccine

@Dao
interface VaccineDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUpdate(vaccine: Vaccine)

    @Query("SELECT * FROM vaccine_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Vaccine>>

}
