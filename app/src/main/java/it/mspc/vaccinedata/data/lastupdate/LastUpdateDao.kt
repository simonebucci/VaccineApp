package it.mspc.vaccinedata.data.lastupdate

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.mspc.vaccinedata.data.lastupdate.LastUpdate

@Dao
interface LastUpdateDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUpdate(lastupdate: LastUpdate)

    @Query("SELECT * FROM lastupdate_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<LastUpdate>>

    @Query("SELECT ultimo_aggiornamento FROM lastupdate_table")
    fun getData(): String

    @Query("DELETE FROM lastupdate_table")
    suspend fun delete()
}