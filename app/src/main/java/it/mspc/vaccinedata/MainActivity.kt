package it.mspc.vaccinedata

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import it.mspc.vaccinedata.databinding.ActivityMainBinding
import it.mspc.vaccinedata.utilites.manageFile
import org.json.JSONException
import java.io.File
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var mQueue: RequestQueue? = null
    var manage = manageFile(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        mQueue = Volley.newRequestQueue(this)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_delivery, R.id.nav_settings
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
                    //if file exists check for updates, if first time save data//
                    if(manage.openFileUpdate().exists()) {
                        if (manage.readFileUpdate() != date) {
                            manage.deleteFiles()
                            manage.saveFileUpdate(date)
                            jsonParse()
                            jsonPlatea()
                            jsonConsegne()
                            jsonPunti()
                            jsonPuntiTipo()
                            jsonSommLatest()
                            jsonSommSumm()
                            jsonVacciniSumm()
                        }
                    }else{
                        manage.saveFileUpdate(date)
                        jsonParse()
                        jsonPlatea()
                        jsonConsegne()
                        jsonPunti()
                        jsonPuntiTipo()
                        jsonSommLatest()
                        jsonSommSumm()
                        jsonVacciniSumm()
                    }
                    ////////////////////////////////////////////////////////////
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }

    private fun jsonParse() {
        //parse del file json contenente il summary dell'anagrafica sui vaccini

        val request = JsonObjectRequest(
            Request.Method.GET, "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/anagrafica-vaccini-summary-latest.json", null,
            { response ->
                try {
                    var file = response.toString()

                    manage.saveFileAna(file)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }

    private fun jsonPlatea(){
        //parse del file json contenente la platea

        val request = JsonObjectRequest(
            Request.Method.GET, "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/platea.json", null,
            { response ->
                try {
                    var file = response.toString()

                    manage.saveFilePlatea(file)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }

    private fun jsonConsegne(){
        //parse del file json contenente le consegne dei vaccini

        val request = JsonObjectRequest(
            Request.Method.GET, "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/consegne-vaccini-latest.json", null,
            { response ->
                try {
                    var file = response.toString()

                    manage.saveFileConsegne(file)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }

    private fun jsonPunti(){
        //parse del file json contenente i punti di somministrazione

        val request = JsonObjectRequest(
            Request.Method.GET, "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/punti-somministrazione-latest.json", null,
            { response ->
                try {
                    var file = response.toString()

                    manage.saveFilePunti(file)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }

    private fun jsonPuntiTipo(){
        //parse del file json contenente le tipologie di punti di somministrazione

        val request = JsonObjectRequest(
            Request.Method.GET, "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/punti-somministrazione-tipologia.json", null,
            { response ->
                try {
                    var file = response.toString()

                    manage.saveFilePuntiTipo(file)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }

    private fun jsonSommLatest(){
        //parse del file json contenente le ultime info sulle somministrazioni

        val request = JsonObjectRequest(
            Request.Method.GET, "https://github.com/italia/covid19-opendata-vaccini/blob/master/dati/somministrazioni-vaccini-latest.json", null,
            { response ->
                try {
                    var file = response.toString()

                    manage.saveFileSommLatest(file)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }

    private fun jsonSommSumm(){
        //parse del file json contenente le summary info sulle somministrazioni

        val request = JsonObjectRequest(
            Request.Method.GET, "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/somministrazioni-vaccini-summary-latest.json", null,
            { response ->
                try {
                    var file = response.toString()

                    manage.saveFileSommSumm(file)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }

    private fun jsonVacciniSumm(){
        //parse del file json contenente le summary info sulle somministrazioni

        val request = JsonObjectRequest(
            Request.Method.GET, "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/vaccini-summary-latest.json", null,
            { response ->
                try {
                    var file = response.toString()

                    manage.saveFileVacciniSumm(file)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }


}