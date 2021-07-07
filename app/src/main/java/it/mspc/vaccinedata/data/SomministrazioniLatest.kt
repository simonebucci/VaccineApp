package it.mspc.vaccinedata.data

class SomministrazioniLatest (
    val index: Int,
    val data_somministrazione: String,
    val fornitore: String,
    val area: String,
    val fascia_anagrafica: String,
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
