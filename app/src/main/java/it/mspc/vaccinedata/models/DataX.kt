package it.mspc.vaccinedata.models

data class DataX(
    val index: Int,
    val fascia_anagrafica: String,
    val prima_dose: Int,
    val seconda_dose: Int,
    val sesso_femminile: Int,
    val sesso_maschile: Int,
    val totale: Int,
    val ultimo_aggiornamento: String
)