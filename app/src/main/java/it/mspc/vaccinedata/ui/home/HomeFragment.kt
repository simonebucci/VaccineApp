package it.mspc.vaccinedata.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import it.mspc.vaccinedata.data.AnagraficaSummary
import it.mspc.vaccinedata.data.Platea
import it.mspc.vaccinedata.data.PuntiSommTipo
import it.mspc.vaccinedata.databinding.FragmentHomeBinding
import it.mspc.vaccinedata.utilites.manageFile
import org.json.JSONException
import java.io.File


class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private var mQueue: RequestQueue? = null
    private lateinit var homeViewModel: HomeViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var date: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var manage = manageFile(requireContext())

        mQueue = Volley.newRequestQueue(requireContext())

        //anagraficaParse()
        //plateaParse()
        //updateProgressBar()
        //puntiPars()
        //puntiTipoPars()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private var ana = ArrayList<AnagraficaSummary>()
    var tot = 0
    var population = 0

    private fun anagraficaParse() {
        //parse del file json contenente il summary dell'anagrafica sui vaccini
        var manage = manageFile(requireContext())
        var out = manage.readFileAna()
        ana = manage.parseFileAna(out)
    }

    private var platea = ArrayList<Platea>()
    private fun plateaParse() {
        //parse del file json per ottenere il totale della popolazione italiana aggiornata
        println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
        var manage = manageFile(requireContext())
        var out = manage.readFilePlatea()
        platea = manage.parseFilePlatea(out)
    }
    private var puntiTipo = ArrayList<PuntiSommTipo>()
    private fun puntiTipoPars() {
        //parse del file json per ottenere il totale della popolazione italiana aggiornata
        println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
        var manage = manageFile(requireContext())
        var out = manage.readFilePuntiTipo()
        puntiTipo = manage.parseFilePuntiTipo(out)
    }

    private fun getPopulation() {
        //funzione per ottenere il totale della popolazione italiana
        for (i in 0 until 167) {
            population += platea[i].totale_popolazione
        }
    }

    private fun getFullVaccine() {
        //funzione per ottenere il totale delle persone vaccinate con entrambe le dosi
        for (i in 0 until 8) {
            tot += ana[i].seconda_dose
        }
    }

    private fun updateProgressBar() {
        //funzione che calcola la percentuale della popolazione italiana vaccinata e che aggiorna la progress bar
        getPopulation()
        getFullVaccine()
        var vaccinated = (100 * tot) / population

        binding.progressBar.progress = vaccinated
        binding.tvProgress.text = "$vaccinated%"
    }




  //  https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/somministrazioni-vaccini-summary-latest.json

}



