package it.mspc.vaccinedata.data

data class ConsegneVaccini(
    val index: Int,
    val area: String,
    val fornitore: String,
    val numero_dosi: Int,
    val data_consegna: String,
    val codice_NUTS1: String,
    val codice_NUTS2: String,
    val codice_regione_ISTAT: Int,
    val nome_area: String
)


