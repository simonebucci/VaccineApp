package it.mspc.vaccinedata

import android.content.Context
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import it.mspc.vaccinedata.data.AnagraficaSummary
import it.mspc.vaccinedata.databinding.ActivityMainBinding
import it.mspc.vaccinedata.utilites.manageFile
import org.json.JSONException
import org.json.JSONObject
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

        jsonParse()
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


    private fun jsonParse() {
        //parse del file json contenente il summary dell'anagrafica sui vaccini

        val request = JsonObjectRequest(
            Request.Method.GET, "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/anagrafica-vaccini-summary-latest.json", null,
            { response ->
                try {
                    var file = response.toString()

                    //saveFile(file)
                    manage.readFileAna(manage.saveFileAna(file))
                    /*val jsonArray = response.getJSONArray("data")

                    var gson = Gson()

                    val sType = object : TypeToken<ArrayList<AnagraficaSummary>>() {}.type
                    anagraficaSummary = gson.fromJson(jsonArray.toString(), sType)
                    */
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }

    private fun lastUpdateParse() {
        //parse del file json contenente la data dell'ultimo aggiornamento effettuato sui dati

        val url =
            "https://raw.githubusercontent.com/italia/covid19-opendata-vaccini/master/dati/last-update-dataset.json"

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
                        }
                    }else{
                        manage.saveFileUpdate(date)
                    }
                    ////////////////////////////////////////////////////////////
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> error.printStackTrace() }
        mQueue!!.add(request)
    }
/*
    fun saveFile(jsonString: String): String{
        val data: String = "vaccine"
        val path = this.getExternalFilesDir(null)

        val folder = File(path, "jsondata")
        folder.mkdirs()

        println(folder.exists()) // you'll get true

        val file = File(folder, "file_name.txt")
        file.appendText(jsonString)
        println("File saved!")
        println(file.absolutePath)
        val file2 = File(folder, "file_name.txt")
        var out = ""
        out = file2.readText()
        println(out)
        return out
        /*val json =  JSONObject(out)
        val jsonArray = json.getJSONArray("data")
        var gson = Gson()

        val sType = object : TypeToken<ArrayList<AnagraficaSummary>>() {}.type
        anagraficaSummary = gson.fromJson(jsonArray.toString(), sType)
        println("daniele ti amo"+anagraficaSummary[0].prima_dose)*/
    }

    fun saveFileUpdate(jsonString: String): String {
        val data: String = "vaccine"
        val path = this.getExternalFilesDir(null)

        val folder = File(path, "jsondata")
        folder.mkdirs()

        println(folder.exists()) // you'll get true

        val file = File(folder, "lastupdate.txt")
        file.appendText(jsonString)
        println("File saved!")
        println(file.absolutePath)
        val file2 = File(folder, "lastupdate.txt")
        var out = ""
        out = file2.readText()
        println(out)
        return out
    }

    fun readFile(out: String){
        val json =  JSONObject(out)
        val jsonArray = json.getJSONArray("data")
        var gson = Gson()

        val sType = object : TypeToken<ArrayList<AnagraficaSummary>>() {}.type
        anagraficaSummary = gson.fromJson(jsonArray.toString(), sType)
        println("daniele ti amo"+anagraficaSummary[0].prima_dose)
    }

    fun readFileUpdate(){
        val path = this.getExternalFilesDir(null)
        val folder = File(path, "jsondata")
        val file = File(folder, "lastupdate.txt")
        var out = file.readText()
        println(out)
    }
    fun deleteFileUpdate(){
        val path = this.getExternalFilesDir(null)
        val folder = File(path, "jsondata")
        val file = File(folder, "lastupdate.txt")
        file.delete()
        println("file deleted!")

    }

    fun openFileUpdate(): File{
        val path = this.getExternalFilesDir(null)
        val folder = File(path, "jsondata")
        val file = File(folder, "lastupdate.txt")
        return file
    }
*/

}