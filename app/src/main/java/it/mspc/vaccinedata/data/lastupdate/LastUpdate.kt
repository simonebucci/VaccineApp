package it.mspc.vaccinedata.data.lastupdate

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lastupdate_table")
data class LastUpdate (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val ultimo_aggiornamento: String
)