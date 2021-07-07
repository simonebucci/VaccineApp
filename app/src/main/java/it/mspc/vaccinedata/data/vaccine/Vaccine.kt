package it.mspc.vaccinedata.data.vaccine

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "vaccine_table")
data class Vaccine (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val index: Int,
    val fascia_anagrafica: String,
    val prima_dose: Int,
    val seconda_dose: Int,
    val sesso_femminile: Int,
    val sesso_maschile: Int,
    val ultimo_aggiornamento: String,
    val totale: Int
    )

