package it.mspc.vaccinedata.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import it.mspc.vaccinedata.MainActivity
import it.mspc.vaccinedata.data.AnagraficaSummary
import it.mspc.vaccinedata.data.Platea
import it.mspc.vaccinedata.data.VacciniSummary
import it.mspc.vaccinedata.databinding.FragmentHomeBinding
import it.mspc.vaccinedata.utilites.manageFile
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.lang.Thread.sleep
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


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

        binding.btnRefresh.setOnClickListener {
            getPopulation()
            getFullVaccine()
        }

        mQueue = Volley.newRequestQueue(requireContext())

        jsonParse()
        jsonParsePopulation()
        lastUpdateParse()
        file()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    lateinit var anagraficaSummary: ArrayList<AnagraficaSummary>
    var tot = 0
    var population = 0
    var one = 0

    private fun jsonParse() {
        //parse del file json contenente il summary dell'anagrafica sui vaccini

        val request = JsonObjectRequest(
            Request.Method.GET,
            "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/anagrafica-vaccini-summary-latest.json",
            null,
            { response ->
                try {
                    val jsonArray = response.getJSONArray("data")

                    var gson = Gson()

                    val sType = object : TypeToken<ArrayList<AnagraficaSummary>>() {}.type
                    anagraficaSummary = gson.fromJson(jsonArray.toString(), sType)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }

    lateinit var platea: ArrayList<Platea>
    private fun jsonParsePopulation() {
        //parse del file json per ottenere il totale della popolazione italiana aggiornata
        val request = JsonObjectRequest(
            Request.Method.GET,
            "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/platea.json",
            null,
            { response ->
                try {
                    val jsonArray = response.getJSONArray("data")

                    var gsonPop = Gson()

                    val sType = object : TypeToken<ArrayList<Platea>>() {}.type
                    platea = gsonPop.fromJson(jsonArray.toString(), sType)
                    updateProgressBar()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)

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
            tot += anagraficaSummary[i].seconda_dose
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


    private fun lastUpdateParse() {
        //parse del file json contenente la data dell'ultimo aggiornamento effettuato sui dati

        val url =
            "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/last-update-dataset.json"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val json = response.get("ultimo_aggiornamento")
                    val d: ZonedDateTime = ZonedDateTime.parse(json.toString())
                    val formatter: DateTimeFormatter =
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    date = formatter.format(d)
                    binding.tvJson.append(date)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }

    val manage = manageFile(requireContext())
    //private lateinit var gianfredo: MainActivity
    fun file() {
    val path = requireContext().getExternalFilesDir(null)
    val folder = File(path, "jsondata")
    val file2 = File(folder, "file_name.txt")
        var out = ""
        out = file2.readText()
        manage.readFileUpdate()
        println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
    //gianfredo.readFile(out)
      //  binding.tvJson.text = anagraficaSummary[0].seconda_dose.toString()
    }


  //  https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/somministrazioni-vaccini-summary-latest.json

}



