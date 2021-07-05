package it.mspc.vaccinedata.data

data class VacciniSummary(
    val index: Int,
    val area: String,
    val dosi_somministrate: Int,
    val dosi_consegnate: Int,
    val percentuale_somministrazione: Number,
    val ultimo_aggiornamento: String,
    val codice_NUTS1: String,
    val codice_NUTS2: String,
    val codice_regione_ISTAT: Int,
    val nome_area: String
)
