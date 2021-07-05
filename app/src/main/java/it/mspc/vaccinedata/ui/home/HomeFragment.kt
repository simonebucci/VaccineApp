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
import it.mspc.vaccinedata.databinding.FragmentHomeBinding
import org.json.JSONException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private var mQueue: RequestQueue? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var date: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnRefresh.setOnClickListener{
            getPopulation()
            getFullVaccine()
        }

        mQueue = Volley.newRequestQueue(requireContext())
        jsonParsePopulation()
        jsonParse()
        lastUpdateParse()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    lateinit var anagraficaSummary: ArrayList<AnagraficaSummary>
    var tot = 0
    var population = 0

    private fun jsonParse() {

        val request = JsonObjectRequest(
            Request.Method.GET, "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/anagrafica-vaccini-summary-latest.json", null,
            { response ->
                try {
                    val jsonArray = response.getJSONArray("data")

                    var gson = Gson()

                    val sType = object : TypeToken<ArrayList<AnagraficaSummary>>() {}.type
                    anagraficaSummary = gson.fromJson(jsonArray.toString(), sType)
                    getFullVaccine()
                    updateProgressBar()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }

    lateinit var platea: ArrayList<Platea>
    private fun jsonParsePopulation() {

        val request = JsonObjectRequest(
            Request.Method.GET, "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/platea.json", null,
            { response ->
                try {
                    val jsonArray = response.getJSONArray("data")

                    var gsonPop = Gson()

                    val sType = object : TypeToken<ArrayList<Platea>>() {}.type
                    platea = gsonPop.fromJson(jsonArray.toString(), sType)
                    getPopulation()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }

    private fun getPopulation(){
        for (i in 0 until 167) {
            population += platea[i].totale_popolazione
        }
    }

    private fun getFullVaccine() {
        for (i in 0 until 8) {
            tot += anagraficaSummary[i].seconda_dose
        }
    }

    private fun updateProgressBar(){

        var vaccinated = (100*tot)/population

        binding.progressBar.progress = vaccinated
        binding.tvProgress.text= "$vaccinated%"
    }

    private fun lastUpdateParse() {
        val url = "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/last-update-dataset.json"


        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val json = response.get("ultimo_aggiornamento")
                    val d: ZonedDateTime = ZonedDateTime.parse(json.toString())
                    val formatter: DateTimeFormatter =
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    date =  formatter.format(d)
                    binding.tvJson.append(date)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }

}



