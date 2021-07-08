package it.mspc.vaccinedata.ui.delivery

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
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import it.mspc.vaccinedata.R
import it.mspc.vaccinedata.data.ConsegneVaccini
import it.mspc.vaccinedata.data.VacciniSummary
import it.mspc.vaccinedata.databinding.FragmentDeliveryBinding
import org.json.JSONException

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
        jsonParse()
        vaccineParse()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    lateinit var vacciniSummary: ArrayList<VacciniSummary>
    lateinit var consegneSummary: ArrayList<ConsegneVaccini>

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


    private fun vaccineParse() {

        val request = JsonObjectRequest(
            Request.Method.GET, "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/consegne-vaccini-latest.json", null,
            { response ->
                try {
                    val jsonArray = response.getJSONArray("data")

                    var vaccine = Gson()

                    val sType = object : TypeToken<ArrayList<ConsegneVaccini>>() {}.type
                    consegneSummary = vaccine.fromJson(jsonArray.toString(), sType)
                    getDealer()
                    setChart()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }

    var pfizer = 0
    var astra = 0
    var john = 0
    var moderna = 0

    private fun getDealer(){
        for (i in 0 until 3103){
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
        xvalue.add("Vaccine Delivered from Providers")


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

        /*barentries.add(BarEntry(delivered.toFloat(),0))
        barentries.add(BarEntry(3.5f,1))
        barentries.add(BarEntry(8.9f,2))
        barentries.add(BarEntry(5.6f,3))
        barentries.add(BarEntry(2f,4))
        barentries.add(BarEntry(6f,5))
        barentries.add(BarEntry(9f,6))
        */



        //bardata set
        val bardataset = BarDataSet(barentries,"Pfizer")
        val bardataset1 = BarDataSet(barentries1,"Moderna")
        val bardataset2 = BarDataSet(barentries2,"Astrazeneca")
        val bardataset3 = BarDataSet(barentries3,"Johnson&Johnson")
        bardataset.color = resources.getColor(R.color.blue_500)
        bardataset1.color = resources.getColor(R.color.red)
        bardataset2.color = resources.getColor(R.color.purple_700)
        bardataset3.color = resources.getColor(R.color.blue_A200)


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

    }



}