package it.mspc.vaccinedata.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import it.mspc.vaccinedata.R
import it.mspc.vaccinedata.data.vaccine.Vaccine
import it.mspc.vaccinedata.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private var _binding: FragmentGalleryBinding? = null
    private var vaccineList = ArrayList<Vaccine>()

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
        setBarChart()
        return root
    }
    fun setBarChart(){

        //x axis values

        val xvalue = ArrayList<String>()
        xvalue.add("f.a.")//"fascia_anagrafica")
        xvalue.add("p.d.")
        xvalue.add("s.d.")
        xvalue.add("s.f.")
        xvalue.add("s.m.")
        xvalue.add("u.a.")
        xvalue.add("totale")

        //y axis values or bar data
        val yaxis = arrayOf<Float>(2.0f,6f,7.8f,3.4f,8f,5f,9.7f)
        //val yaxis1 = arrayOf<Float>(1.0f,3f,8.8f,3.4f,1f,5f,2.7f)
       // val yaxis2 = arrayOf<Float>(6.0f,1f,3.8f,2.4f,6f,5f,4.7f)

        //bar entry
        val barentries = ArrayList<BarEntry>()
        //val barentries1 = ArrayList<BarEntry>()
        //val barentries2 = ArrayList<BarEntry>()

        for (i in 0..yaxis.size-1){
            barentries.add(BarEntry(yaxis[i],i))
        }
        /*for (i in 0..yaxis1.size-1){
            barentries1.add(BarEntry(yaxis1[i],i))
        }
        for (i in 0..yaxis2.size-1){
            barentries2.add(BarEntry(yaxis2[i],i))
        }*/

        /*barentries.add(BarEntry(4f,0))
        barentries.add(BarEntry(3.5f,1))
        barentries.add(BarEntry(8.9f,2))
        barentries.add(BarEntry(5.6f,3))
        barentries.add(BarEntry(2f,4))
        barentries.add(BarEntry(6f,5))
        barentries.add(BarEntry(9f,6))
        */



        //bardata set
        val bardataset = BarDataSet(barentries,"First")
        //val bardataset1 = BarDataSet(barentries1,"Second")
       // val bardataset2 = BarDataSet(barentries2,"Third")
        bardataset.color = resources.getColor(R.color.green)
        //bardataset1.color = resources.getColor(R.color.red)
        //bardataset2.color = resources.getColor(R.color.purple_700)


        val finalBarDataSet = ArrayList<BarDataSet>()
        finalBarDataSet.add(bardataset)
        //finalBarDataSet.add(bardataset1)
        //finalBarDataSet.add(bardataset2)

        //make a bar data

        val data = BarData(xvalue,finalBarDataSet as List<IBarDataSet>?)

        binding.barChart.data = data

        binding.barChart.setBackgroundColor(resources.getColor(R.color.darkorange))
        binding.barChart.animateXY(3000,3000)



    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}