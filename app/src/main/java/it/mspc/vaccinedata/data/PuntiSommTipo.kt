package it.mspc.vaccinedata.data

data class PuntiSommTipo(
    val index: Int,
    val area: String,
    val denominazione_struttura: String,
    val tipologia: String,
    val codice_NUTS1: String,
    val codice_NUTS2: String,
    val codice_regione_ISTAT: Int,
    val nome_area: String
)