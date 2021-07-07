package it.mspc.vaccinedata

import android.os.Bundle
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import it.mspc.vaccinedata.data.lastupdate.LastUpdate
import it.mspc.vaccinedata.data.lastupdate.LastUpdateViewModel
import it.mspc.vaccinedata.data.vaccine.Vaccine
import it.mspc.vaccinedata.data.vaccine.VaccineViewModel
import it.mspc.vaccinedata.databinding.ActivityMainBinding
import org.json.JSONException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var lastupdateViewModel: LastUpdateViewModel
    private lateinit var vaccineViewModel: VaccineViewModel
    private var mQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        lastupdateViewModel = ViewModelProvider(this).get(LastUpdateViewModel::class.java)
        vaccineViewModel = ViewModelProvider(this).get(VaccineViewModel::class.java)
        mQueue = Volley.newRequestQueue(this)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        lastUpdateParse()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    private fun lastUpdateParse() {
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
                    var date = formatter.format(d)
                    var lastdate = LastUpdate(0, date)

                    if(lastupdateViewModel.getData().toString() != date){
                        lastupdateViewModel.delete()
                        lastupdateViewModel.addUpdate(lastdate)
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)

    }

    lateinit var anagraficaSummary: ArrayList<Vaccine>

    private fun jsonParse() {
        //parse del file json contenente il summary dell'anagrafica sui vaccini

        val request = JsonObjectRequest(
            Request.Method.GET, "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/anagrafica-vaccini-summary-latest.json", null,
            { response ->
                try {
                    val jsonArray = response.getJSONArray("data")

                    var gson = Gson()

                    val sType = object : TypeToken<ArrayList<Vaccine>>() {}.type
                    anagraficaSummary = gson.fromJson(jsonArray.toString(), sType)
                    var vaccine = Vaccine()
                    vaccineViewModel.addUpdate(anagraficaSummary)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }

}