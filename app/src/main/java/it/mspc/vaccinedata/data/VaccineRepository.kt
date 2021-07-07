package it.mspc.vaccinedata.data

import androidx.lifecycle.LiveData

class VaccineRepository(private val vaccineDao: VaccineDao) {
    val readAllData: LiveData<List<Vaccine>> = vaccineDao.readAllData()

    suspend fun addUpdate(vaccine: Vaccine){
        vaccineDao.addUpdate(vaccine)
    }
}