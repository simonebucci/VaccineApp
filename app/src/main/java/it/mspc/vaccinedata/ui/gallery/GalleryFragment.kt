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
import it.mspc.vaccinedata.data.VacciniSummary
import it.mspc.vaccinedata.databinding.FragmentGalleryBinding
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
        setBarChart()
        setPieChart()
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

    fun setBarChart(){

        //x axis values

        val xvalue = ArrayList<String>()
        xvalue.add("Inoculated Vaccine")


        //y axis values or bar data
        val yaxis = arrayOf<Float>(delivered.toFloat())
        val yaxis1 = arrayOf<Float>(shot.toFloat())
        // val yaxis2 = arrayOf<Float>(6.0f,1f,3.8f,2.4f,6f,5f,4.7f)

        //bar entry
        val barentries = ArrayList<BarEntry>()
        val barentries1 = ArrayList<BarEntry>()
        //val barentries2 = ArrayList<BarEntry>()

        for (i in 0..yaxis.size-1){
            barentries.add(BarEntry(yaxis[i],i))
        }
        for (i in 0..yaxis1.size-1){
            barentries1.add(BarEntry(yaxis1[i],i))
        }
        /*for (i in 0..yaxis2.size-1){
            barentries2.add(BarEntry(yaxis2[i],i))
        }*/

        /*barentries.add(BarEntry(delivered.toFloat(),0))
        barentries.add(BarEntry(3.5f,1))
        barentries.add(BarEntry(8.9f,2))
        barentries.add(BarEntry(5.6f,3))
        barentries.add(BarEntry(2f,4))
        barentries.add(BarEntry(6f,5))
        barentries.add(BarEntry(9f,6))
        */



        //bardata set
        val bardataset = BarDataSet(barentries,"Delivered")
        val bardataset1 = BarDataSet(barentries1,"Inoculated")
        // val bardataset2 = BarDataSet(barentries2,"Third")
        bardataset.color = resources.getColor(R.color.blue_500)
        bardataset1.color = resources.getColor(R.color.red)
        //bardataset2.color = resources.getColor(R.color.purple_700)


        val finalBarDataSet = ArrayList<BarDataSet>()
        finalBarDataSet.add(bardataset)
        finalBarDataSet.add(bardataset1)
        //finalBarDataSet.add(bardataset2)

        //make a bar data

        val data = BarData(xvalue,finalBarDataSet as List<IBarDataSet>?)

        binding.barChart.data = data

        binding.barChart.setBackgroundColor(resources.getColor(R.color.white))
        binding.barChart.animateXY(3000,3000)



    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}