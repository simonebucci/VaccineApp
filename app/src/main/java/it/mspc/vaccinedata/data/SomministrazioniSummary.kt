package it.mspc.vaccinedata.data

data class SomministrazioniSummary(
    val index: Int,
    val data_somministrazione: String,
    val area: String,
    val totale: Int,
    val sesso_maschile: Int,
    val sesso_femminile: Int,
    val prima_dose: String,
    val seconda_dose: String,
    val pregressa_infezione: Int,
    val codice_NUTS1: String,
    val codice_NUTS2: String,
    val codice_regione_ISTAT: Int,
    val nome_area: String
)
