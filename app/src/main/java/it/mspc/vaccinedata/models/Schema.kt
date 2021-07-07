package it.mspc.vaccinedata.models

data class Schema(
    val fields: List<Field>,
    val pandas_version: String,
    val primaryKey: List<String>
)