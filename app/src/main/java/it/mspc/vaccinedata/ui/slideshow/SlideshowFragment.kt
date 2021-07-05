package it.mspc.vaccinedata.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import it.mspc.vaccinedata.R
import it.mspc.vaccinedata.data.AnagraficaSummary
import it.mspc.vaccinedata.data.VacciniSummary
import it.mspc.vaccinedata.databinding.FragmentSlideshowBinding
import org.json.JSONException

class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel
    private var _binding: FragmentSlideshowBinding? = null
    private var mQueue: RequestQueue? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSlideshow
        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        mQueue = Volley.newRequestQueue(requireContext())
        jsonParse()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    lateinit var vacciniSummary: ArrayList<VacciniSummary>

    private fun jsonParse() {

        val request = JsonObjectRequest(
            Request.Method.GET, "https://github.com/italia/covid19-opendata-vaccini/blob/master/dati/vaccini-summary-latest.json", null,
            { response ->
                try {
                    val jsonArray = response.getJSONArray("data")

                    var gsonVaccine = Gson()

                    val sType = object : TypeToken<ArrayList<AnagraficaSummary>>() {}.type
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
        binding.tvDelivered.text = delivered.toString()
    }

    var shot = 0

    private fun getShotted(){
        for (i in 0 until 20){
            shot += vacciniSummary[i].dosi_somministrate
        }
        binding.tvShotted.text = shot.toString()
    }
}