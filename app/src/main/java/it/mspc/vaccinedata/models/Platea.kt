package it.mspc.vaccinedata.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "platea_table")
data class Platea(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val index: Int,
    val area: String,
    val nome_area: String,
    val fascia_anagrafica: String,
    val totale_popolazione: Int,
)