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
import it.mspc.vaccinedata.data.Vaccine
import it.mspc.vaccinedata.data.VaccineViewModel
//import it.mspc.vaccinedata.data.VaccineViewModel
import it.mspc.vaccinedata.databinding.FragmentHomeBinding
import it.mspc.vaccinedata.models.DataX
import org.json.JSONException


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private var mQueue: RequestQueue? = null
    private lateinit var mVaccineViewModel: VaccineViewModel
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var json: Any = "0"
    private var progr = 10

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        mVaccineViewModel = ViewModelProvider(requireActivity()).get(VaccineViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        updateProgressBar()

        binding.btnIncr.setOnClickListener{
            if(progr <= 90){
                progr += 10
                updateProgressBar()
            }
        }
        binding.btnDecr.setOnClickListener{
            if(progr >= 10){
                progr -= 10
                updateProgressBar()
            }
        }
        mQueue = Volley.newRequestQueue(requireContext())
        jsonParse()
        //lastUpdateParse()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun updateProgressBar(){
        binding.progressBar.progress = progr
        binding.tvProgress.text= "$progr%"
    }


    private fun jsonParse() {
        val url = "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/anagrafica-vaccini-summary-latest.json"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val jsonArray = response.getJSONArray("data")
                    for (i in 0 until jsonArray.length()) {
                        val update = jsonArray.getJSONObject(i)
                        val index = update.getInt("index")
                        val fascia = update.getString("fascia_anagrafica")
                        val totale = update.getInt("totale")
                        val sesso_femminile = update.getInt("sesso_femminile")
                        val sesso_maschile = update.getInt("sesso_maschile")
                        val prima_dose = update.getInt("prima_dose")
                        val seconda_dose = update.getInt("seconda_dose")
                        val ultimo_aggiornamento = update.getString("ultimo_aggiornamento")
                        var vaccine = Vaccine(0,index,fascia,prima_dose,seconda_dose,sesso_femminile,sesso_maschile,ultimo_aggiornamento,totale)
                        mVaccineViewModel.addUpdate(vaccine)
                        if(index == 1) {
                           binding.tvJson.text = ultimo_aggiornamento
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }}
    /*private fun lastUpdateParse() {
        val url = "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/last-update-dataset.json"


        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    json = response.get("ultimo_aggiornamento")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }

     */


