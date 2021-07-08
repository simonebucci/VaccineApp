package it.mspc.vaccinedata.ui.gallery

import android.graphics.Color
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
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import it.mspc.vaccinedata.R
import it.mspc.vaccinedata.data.AnagraficaSummary
import it.mspc.vaccinedata.data.Platea
import it.mspc.vaccinedata.data.VacciniSummary
import it.mspc.vaccinedata.databinding.FragmentGalleryBinding
import it.mspc.vaccinedata.utilities.ManageFile
import org.json.JSONException


class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null
    private var mQueue: RequestQueue? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mQueue = Volley.newRequestQueue(requireContext())
        jsonParse()
        setPieChart()
        plateaParse()
        anagraficaParse()
        updateProgressBar()
        return root
    }


    private fun setPieChart() {
        val xValues = ArrayList<String>()
        xValues.add("Pfizer")
        xValues.add("Moderna")
        xValues.add("Astrazeneca")
        xValues.add("Johnson&Johnson")

        val pieChartEntry = ArrayList<Entry>()
        pieChartEntry.add(Entry(23f,0))
        pieChartEntry.add(Entry(11f,0))
        pieChartEntry.add(Entry(33f,0))


        val dataSet = PieDataSet(pieChartEntry, "")
        dataSet.valueTextSize=0f
        val colors = java.util.ArrayList<Int>()
        colors.add(Color.BLUE)
        colors.add(Color.GREEN)
        colors.add(Color.MAGENTA)

        dataSet.setColors(colors)
        val data = PieData(xValues,dataSet)
        binding.pieChart.data = data
        binding.pieChart.centerTextRadiusPercent = 0f
        binding.pieChart.isDrawHoleEnabled = false
        binding.pieChart.legend.isEnabled = true
        binding.pieChart.setDescription("Vaccine Dealers")
        binding.pieChart.animateY(3000)

        val legend: Legend = binding.pieChart.legend
        legend.position = Legend.LegendPosition.LEFT_OF_CHART

    }
    lateinit var vacciniSummary: ArrayList<VacciniSummary>
    private fun jsonParse() {

        val request = JsonObjectRequest(
            Request.Method.GET, "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/vaccini-summary-latest.json", null,
            { response ->
                try {
                    val jsonArray = response.getJSONArray("data")

                    var gsonVaccine = Gson()

                    val sType = object : TypeToken<ArrayList<VacciniSummary>>() {}.type
                    vacciniSummary = gsonVaccine.fromJson(jsonArray.toString(), sType)
                    getDelivered()
                    getShotted()
                    setPieChart()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }

    var population = 0
    var tot = 0
    private var anagraficaSummary = ArrayList<AnagraficaSummary>()
    private fun anagraficaParse() {
        //parse del file json contenente il summary dell'anagrafica sui vaccini
        val manage = ManageFile(requireContext())
        val out = manage.readFileAna()
        anagraficaSummary = manage.parseFileAna(out)
    }

    private var platea = ArrayList<Platea>()
    private fun plateaParse() {
        //parse del file json per ottenere la platea
        val manage = ManageFile(requireContext())
        val out = manage.readFilePlatea()
        platea = manage.parseFilePlatea(out)
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
        val vaccinated = (100 * tot) / population

        binding.progressBar2.progress = vaccinated
        binding.tvProgress.text = "$vaccinated%"
    }


    var delivered = 0

    private fun getDelivered() {
        for (i in 0 until 20) {
            delivered += vacciniSummary[i].dosi_consegnate
        }
    }

    var shot = 0

    private fun getShotted(){
        for (i in 0 until 20){
            shot += vacciniSummary[i].dosi_somministrate
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}