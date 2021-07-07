package it.mspc.vaccinedata.data.lastupdate


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import it.mspc.vaccinedata.data.VaccineDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

public class LastUpdateViewModel(application: Application): AndroidViewModel(application) {

    private val readAllData: LiveData<List<LastUpdate>>
    private val repository: LastUpdateRepository

    init {
        val lastupdateDao = VaccineDatabase.getDatabase(application).lastupdateDao()
        repository = LastUpdateRepository(lastupdateDao)
        readAllData = repository.readAllData
    }

    fun addUpdate(lastupdate: LastUpdate) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUpdate(lastupdate)
        }
    }

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getData()
        }
    }

    fun delete(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete()
        }
    }
}