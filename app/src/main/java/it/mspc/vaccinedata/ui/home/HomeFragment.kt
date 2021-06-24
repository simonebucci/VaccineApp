package it.mspc.vaccinedata.ui.home

import android.content.res.AssetManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import it.mspc.vaccinedata.R
import it.mspc.vaccinedata.databinding.FragmentHomeBinding
import it.mspc.vaccinedata.models.Data
import java.io.FileReader

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var progr = 10

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

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

        val json = """{"title": "Kotlin Tutorial #1", "author": "bezkoder", "categories" : ["Kotlin","Basic"]}"""
        val gson = Gson()

        val tutorial_1: Data = gson.fromJson(json, Data::class.java)
        binding.tvJson.text = tutorial_1


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun AssetManager.readFile(fileName: String) = open(fileName)
        .bufferedReader()
        .use { it.readText() }

    val jsonString = context?.assets?.readFile("https://github.com/italia/covid19-opendata-vaccini/blob/master/dati/anagrafica-vaccini-summary-latest.json")

    private fun updateProgressBar(){
        binding.progressBar.progress = progr
        binding.tvProgress.text= "$progr%"
    }




}