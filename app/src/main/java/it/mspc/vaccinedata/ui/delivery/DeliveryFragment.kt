package it.mspc.vaccinedata.ui.delivery

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import it.mspc.vaccinedata.R
import it.mspc.vaccinedata.data.ConsegneVaccini
import it.mspc.vaccinedata.data.VacciniSummary
import it.mspc.vaccinedata.databinding.FragmentDeliveryBinding
import it.mspc.vaccinedata.utilities.ManageFile
import java.util.*
import kotlin.collections.ArrayList


class DeliveryFragment : Fragment() {

    private lateinit var deliveryViewModel: DeliveryViewModel
    private var _binding: FragmentDeliveryBinding? = null
    private var mQueue: RequestQueue? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        deliveryViewModel =
            ViewModelProvider(this).get(DeliveryViewModel::class.java)

        _binding = FragmentDeliveryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mQueue = Volley.newRequestQueue(requireContext())

        vacciniParse()
        consegneParse()
        getDealer()
        setChart()
        setPieChart()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var consegneSummary = ArrayList<ConsegneVaccini>()
    private fun consegneParse() {
        //parse del file json contenente le consegne dei vaccini
        val manage = ManageFile(requireContext())
        val out = manage.readFileConsegne()
        consegneSummary = manage.parseFileConsegne(out)
    }

    private var vacciniSummary = ArrayList<VacciniSummary>()
    private fun vacciniParse() {
        //parse del file json contenente il summary dei vaccini
        val manage = ManageFile(requireContext())
        val out = manage.readFileVacciniSumm()
        vacciniSummary = manage.parseFileVacciniSumm(out)
        getDelivered()
        getShotted()
    }


    private fun getDelivered(): Int {
        var delivered = 0
        for (i in 0 until 20) {
            delivered += vacciniSummary[i].dosi_consegnate
        }
        return delivered
    }



    private fun getShotted(): Int{
        var shot = 0
        for (i in 0 until 20){
            shot += vacciniSummary[i].dosi_somministrate
        }
        return shot
    }

    private fun setPieChart() {
        val xValues = ArrayList<String>()
        xValues.add(resources.getString(R.string.pie_delivered))
        xValues.add(resources.getString(R.string.pie_inoculated))

        var d = getDelivered()
        var s = getShotted()

        val pieChartEntry = ArrayList<Entry>()
        pieChartEntry.add(Entry(d.toFloat(),0))
        pieChartEntry.add(Entry(s.toFloat(),1))



        val dataSet = PieDataSet(pieChartEntry, "")
        dataSet.valueTextSize=0f
        val colors = java.util.ArrayList<Int>()
        colors.add(resources.getColor(R.color.blue_500))
        colors.add(resources.getColor(R.color.teal_200))

        dataSet.setColors(colors)
        val data = PieData(xValues,dataSet)
        data.setValueTextSize(10f)
        data.setValueTextColor(Color.BLACK)
        binding.pieChart2.data = data
        binding.pieChart2.centerTextRadiusPercent = 20f
        binding.pieChart2.isDrawHoleEnabled = true
        binding.pieChart2.legend.isEnabled = true
        binding.pieChart2.setDescription("")
        binding.pieChart2.animateY(3000)

        val legend: Legend = binding.pieChart2.legend
        legend.position = Legend.LegendPosition.LEFT_OF_CHART

    }

    var pfizer = 0
    var astra = 0
    var john = 0
    var moderna = 0

    private fun getDealer(){
        for (i in 0 until consegneSummary.size){
            if(consegneSummary[i].fornitore == "Vaxzevria (AstraZeneca)")
                astra += consegneSummary[i].numero_dosi
            else if(consegneSummary[i].fornitore == "Moderna"){
                moderna += consegneSummary[i].numero_dosi
            }else if(consegneSummary[i].fornitore == "Janssen"){
                john += consegneSummary[i].numero_dosi
            }else{
                pfizer += consegneSummary[i].numero_dosi
            }
        }
    }


    fun setChart(){

        //x axis values

        val xvalue = ArrayList<String>()
        xvalue.add("")


        //y axis values or bar data
        val yaxis = arrayOf<Float>(pfizer.toFloat())
        val yaxis1 = arrayOf<Float>(moderna.toFloat())
        val yaxis2 = arrayOf<Float>(astra.toFloat())
        val yaxis3 = arrayOf<Float>(john.toFloat())

        //bar entry
        val barentries = ArrayList<BarEntry>()
        val barentries1 = ArrayList<BarEntry>()
        val barentries2 = ArrayList<BarEntry>()
        val barentries3 = ArrayList<BarEntry>()

        for (i in 0..yaxis.size-1){
            barentries.add(BarEntry(yaxis[i],i))
        }
        for (i in 0..yaxis1.size-1){
            barentries1.add(BarEntry(yaxis1[i],i))
        }
        for (i in 0..yaxis2.size-1){
            barentries2.add(BarEntry(yaxis2[i],i))
        }
        for (i in 0..yaxis3.size-1){
            barentries3.add(BarEntry(yaxis3[i],i))
        }

        //bardata set
        val bardataset = BarDataSet(barentries,"Pfizer")
        val bardataset1 = BarDataSet(barentries1,"Moderna")
        val bardataset2 = BarDataSet(barentries2,"Astrazeneca")
        val bardataset3 = BarDataSet(barentries3,"Johnson&Johnson")
        bardataset.color = resources.getColor(R.color.blue_500)
        bardataset1.color = resources.getColor(R.color.colorAccent)
        bardataset2.color = resources.getColor(R.color.colorPrimaryDark)
        bardataset3.color = resources.getColor(R.color.teal_200)


        val finalBarDataSet = ArrayList<BarDataSet>()
        finalBarDataSet.add(bardataset)
        finalBarDataSet.add(bardataset1)
        finalBarDataSet.add(bardataset2)
        finalBarDataSet.add(bardataset3)

        //make a bar data

        val data = BarData(xvalue,finalBarDataSet as List<IBarDataSet>?)

        binding.barChart2.data = data
        binding.barChart2.setBackgroundColor(resources.getColor(R.color.white))
        binding.barChart2.animateXY(3000,3000)
        binding.barChart2.setDescription("")
    }


}