package it.mspc.vaccinedata.data.vaccine

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import it.mspc.vaccinedata.data.VaccineDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

public class VaccineViewModel(application: Application): AndroidViewModel(application) {

    private val readAllData: LiveData<List<Vaccine>>
    private val repository: VaccineRepository

    init{
        val vaccineDao = VaccineDatabase.getDatabase(application).vaccineDao()
        repository = VaccineRepository(vaccineDao)
        readAllData = repository.readAllData
    }

    fun addUpdate(vaccine: Vaccine){
        viewModelScope.launch(Dispatchers.IO){
            repository.addUpdate(vaccine)
        }
    }
}