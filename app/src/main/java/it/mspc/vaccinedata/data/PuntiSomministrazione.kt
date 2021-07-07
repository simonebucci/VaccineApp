package it.mspc.vaccinedata.data

data class PuntiSomministrazione (
    val index: Int,
    val area: String,
    val provincia: String,
    val comune: String,
    val presidio_ospedaliero: String,
    val codice_NUTS1: String,
    val codice_NUTS2: String,
    val codice_regione_ISTAT: Int,
    val nome_area: String
)