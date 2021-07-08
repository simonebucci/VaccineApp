package it.mspc.vaccinedata.utilities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import it.mspc.vaccinedata.R
import it.mspc.vaccinedata.databinding.ActivityMainBinding
import org.json.JSONException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DownloadManager(context: Context){

    var mycontext = context
    private var mQueue: RequestQueue? = null
    var manage = ManageFile(mycontext)

    fun lastUpdateParse() {
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
                            jsonAnagrafica()
                            jsonPlatea()
                            jsonConsegne()
                            jsonPunti()
                            jsonPuntiTipo()
                            jsonSommSumm()
                            jsonVacciniSumm()
                        }
                    }else{
                        manage.saveFileUpdate(date)
                        jsonAnagrafica()
                        jsonPlatea()
                        jsonConsegne()
                        jsonPunti()
                        jsonPuntiTipo()
                        jsonSommSumm()
                        jsonVacciniSumm()
                    }
                    ////////////////////////////////////////////////////////////
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue?.add(request)

    }


    /////////////////////////Functions for downloading files/////////////////////////
    private fun jsonAnagrafica() {
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
        mQueue?.add(request)
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
        mQueue?.add(request)
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
        mQueue?.add(request)
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
        mQueue?.add(request)
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
        mQueue?.add(request)
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
        mQueue?.add(request)
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
        mQueue?.add(request)
    }

}