package it.mspc.vaccinedata.data.appuntamento


data class Appuntamento (
    var id: Int,
    var data_prima_dose: String,
    var orario_prima_dose: String,
    var data_seconda_dose: String,
    var orario_seconda_dose: String,
    var punto_vaccinazione: String
)
