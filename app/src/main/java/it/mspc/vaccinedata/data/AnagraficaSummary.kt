package it.mspc.vaccinedata.data

data class AnagraficaSummary(
    val index: Int,
    val fascia_anagrafica: String,
    val totale: Int,
    val sesso_maschile: Int,
    val sesso_femminile: Int,
    val prima_dose: Int,
    val seconda_dose: Int,
    val pregressa_infezione: Int,
    val ultimo_aggiornamento: String
)