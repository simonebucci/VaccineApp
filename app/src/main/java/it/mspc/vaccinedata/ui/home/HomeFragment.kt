package it.mspc.vaccinedata.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import it.mspc.vaccinedata.R
import it.mspc.vaccinedata.databinding.FragmentHomeBinding
import it.mspc.vaccinedata.utilities.FileManager
import org.json.JSONException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private var mQueue: RequestQueue? = null
    private lateinit var homeViewModel: HomeViewModel
    var region = arrayOf<String>()
    var images = arrayOf<String>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var date: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        region = resources.getStringArray(R.array.regionName)
        images = resources.getStringArray(R.array.regionImages)

        mQueue = Volley.newRequestQueue(requireContext())

        binding.ivFlower?.setOnClickListener{
            animationFlower()
        }

        val adapter = RecyclerAdapter(region,images)
        binding.recycleView?.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleView?.adapter = adapter

        animationFlower()
        getDate()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun animationFlower(){
        val rotate = RotateAnimation(
            0F,
            180F,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 5000
        rotate.interpolator = LinearInterpolator()
        rotate.fillAfter = true

        val image: ImageView? = binding.ivFlower

        if (image != null) {
            image.startAnimation(rotate)
        }
    }


    fun getDate() {
        //parse del file json contenente la data dell'ultimo aggiornamento effettuato sui dati
        val url = "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/last-update-dataset.json"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val json = response.get("ultimo_aggiornamento")
                    val d: ZonedDateTime = ZonedDateTime.parse(json.toString())
                    val formatter: DateTimeFormatter =
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    date = formatter.format(d)
                    binding.tvJson.append(date)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }

}



