package it.mspc.vaccinedata.data.lastupdate

import androidx.lifecycle.LiveData


class LastUpdateRepository(private val lastupdateDao: LastUpdateDao) {
    val readAllData: LiveData<List<LastUpdate>> = lastupdateDao.readAllData()

    suspend fun addUpdate(lastupdate: LastUpdate){
        lastupdateDao.addUpdate(lastupdate)
    }
    suspend fun getData(){
        lastupdateDao.getData()
    }

    suspend fun delete(){
        lastupdateDao.delete()
    }
}