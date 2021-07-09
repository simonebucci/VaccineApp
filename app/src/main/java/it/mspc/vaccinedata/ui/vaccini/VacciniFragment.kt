package it.mspc.vaccinedata.ui.vaccini

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import it.mspc.vaccinedata.R
import it.mspc.vaccinedata.data.AnagraficaSummary
import it.mspc.vaccinedata.data.Platea
import it.mspc.vaccinedata.databinding.FragmentVacciniBinding
import it.mspc.vaccinedata.utilities.ManageFile


class VacciniFragment : Fragment() {

    private lateinit var vacciniViewModel: VacciniViewModel
    private var _binding: FragmentVacciniBinding? = null
    private var mQueue: RequestQueue? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vacciniViewModel =
            ViewModelProvider(this).get(VacciniViewModel::class.java)

        _binding = FragmentVacciniBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mQueue = Volley.newRequestQueue(requireContext())

        plateaParse()
        anagraficaParse()
        getOneVaccine()
        updateProgressBar()
        setPieChart()
        return root
    }

    //var population = 0
    //var tot = 0
   // var one = 0

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


    private fun getPopulation(): Int {
        //funzione per ottenere il totale della popolazione italiana
        var population = 0
        for (i in 0 until platea.size) {
            population += platea[i].totale_popolazione
        }
        return population
    }

    private fun getFullVaccine(): Int {
        //funzione per ottenere il totale delle persone vaccinate con entrambe le dosi
        var tot = 0
        for (i in 0 until 8) {
            tot += anagraficaSummary[i].seconda_dose
        }
        return tot
    }

    private fun getOneVaccine(): Int {
        //function that find the number of people vaccinated with one inoculation
        var one = 0
        for(i in 0 until 8){
            one += anagraficaSummary[i].prima_dose
        }
        return one
    }


    private fun updateProgressBar() {
        //funzione che calcola la percentuale della popolazione italiana vaccinata e che aggiorna la progress bar
        var population = getPopulation()
        var tot = getFullVaccine()
        val vaccinated = (100 * tot) / population

        binding.progressBar2.progress = vaccinated
        binding.tvProgress.text = "$vaccinated%"
    }


    private fun setPieChart() {
        val xValues = ArrayList<String>()
        xValues.add(resources.getString(R.string.pie_one))
        xValues.add(resources.getString(R.string.pie_two))

        var one = getOneVaccine()
        var tot = getFullVaccine()

        val pieChartEntry = ArrayList<Entry>()
        pieChartEntry.add(Entry(one.toFloat(),0))
        pieChartEntry.add(Entry(tot.toFloat(),1))



        val dataSet = PieDataSet(pieChartEntry, "")
        dataSet.valueTextSize=0f
        val colors = java.util.ArrayList<Int>()
        colors.add(resources.getColor(R.color.blue_A200))
        colors.add(resources.getColor(R.color.blue_700))

        dataSet.setColors(colors)
        val data = PieData(xValues,dataSet)
        data.setValueTextSize(10f)
        data.setValueTextColor(Color.BLACK)
        binding.pieChart.data = data
        binding.pieChart.centerTextRadiusPercent = 20f
        binding.pieChart.isDrawHoleEnabled = true
        binding.pieChart.legend.isEnabled = true
        binding.pieChart.setDescription("")
        binding.pieChart.animateY(3000)
        binding.pieChart.setDrawSliceText(true)

        val legend: Legend = binding.pieChart.legend
        legend.position = Legend.LegendPosition.BELOW_CHART_RIGHT

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}