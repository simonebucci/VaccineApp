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
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import it.mspc.vaccinedata.databinding.FragmentHomeBinding
import it.mspc.vaccinedata.utilities.ManageFile
import org.json.JSONException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private var mQueue: RequestQueue? = null
    private lateinit var homeViewModel: HomeViewModel

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

        var manage = ManageFile(requireContext())

        mQueue = Volley.newRequestQueue(requireContext())

        binding.ivFlower?.setOnClickListener{
            animationFlower()
        }

        animationFlower()
        //anagraficaParse()
        //plateaParse()
        //puntiParse()
        //puntiTipoParse()
        getDate()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var tot = 0
    var population = 0

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
/*
    private var ana = ArrayList<AnagraficaSummary>()
    private fun anagraficaParse() {
        //parse del file json contenente il summary dell'anagrafica sui vaccini
        val manage = ManageFile(requireContext())
        val out = manage.readFileAna()
        ana = manage.parseFileAna(out)
    }

   private var platea = ArrayList<Platea>()
    private fun plateaParse() {
        //parse del file json per ottenere la platea
        val manage = ManageFile(requireContext())
        val out = manage.readFilePlatea()
        platea = manage.parseFilePlatea(out)
    }

    private var punti = ArrayList<PuntiSomministrazione>()
    private fun puntiParse(){
        //parse del file json per ottenere i punti di somministrazione
        val manage = ManageFile(requireContext())
        val out = manage.readFilePunti()
        punti = manage.parseFilePunti(out)
    }

    private var puntiTipo = ArrayList<PuntiSommTipo>()
    private fun puntiTipoParse() {
        //parse del file json per ottenere la tipologia dei punti di somministrazione
        val manage = ManageFile(requireContext())
        val out = manage.readFilePuntiTipo()
        puntiTipo = manage.parseFilePuntiTipo(out)
    } */



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



