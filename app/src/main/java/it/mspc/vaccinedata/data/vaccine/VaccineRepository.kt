package it.mspc.vaccinedata.data.vaccine

import androidx.lifecycle.LiveData
import it.mspc.vaccinedata.data.vaccine.Vaccine
import it.mspc.vaccinedata.data.vaccine.VaccineDao

class VaccineRepository(private val vaccineDao: VaccineDao) {
    val readAllData: LiveData<List<Vaccine>> = vaccineDao.readAllData()

    suspend fun addUpdate(vaccine: Vaccine){
        vaccineDao.addUpdate(vaccine)
    }
}