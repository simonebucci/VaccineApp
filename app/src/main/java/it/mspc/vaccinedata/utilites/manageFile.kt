package it.mspc.vaccinedata.utilites

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import it.mspc.vaccinedata.MainActivity
import it.mspc.vaccinedata.data.AnagraficaSummary
import org.json.JSONObject
import java.io.File

public class manageFile(context: Context){

    var mycontext = context

    fun saveFileUpdate(jsonString: String): String {
        val data: String = "vaccine"
        val path =  mycontext.getExternalFilesDir(null)

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

    fun readFileUpdate(): String{
        val path = mycontext.getExternalFilesDir(null)
        val folder = File(path, "jsondata")
        val file = File(folder, "lastupdate.txt")
        var out = file.readText()
        println(out)
        return out
    }
    fun deleteFiles(){
        val path = mycontext.getExternalFilesDir(null)
        val folder = File(path, "jsondata")
        val file = File(folder, "lastupdate.txt")
        val file2 = File(folder, "anagrafica.txt")
        file.delete()
        file2.delete()
        println("files deleted!")

    }

    fun openFileUpdate(): File{
        val path = mycontext.getExternalFilesDir(null)
        val folder = File(path, "jsondata")
        val file = File(folder, "lastupdate.txt")
        return file
    }

    fun saveFileAna(jsonString: String): String{
        val data: String = "vaccine"
        val path = mycontext.getExternalFilesDir(null)

        val folder = File(path, "jsondata")
        folder.mkdirs()

        println(folder.exists()) // you'll get true

        val file = File(folder, "anagrafica.txt")
        file.appendText(jsonString)
        println("File saved!")
        println(file.absolutePath)
        val file2 = File(folder, "anagrafica.txt")
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

    fun readFileAna(out: String): ArrayList<AnagraficaSummary>{
        val json =  JSONObject(out)
        val jsonArray = json.getJSONArray("data")
        var gson = Gson()

        val sType = object : TypeToken<ArrayList<AnagraficaSummary>>() {}.type
        var anagraficaSummary: ArrayList<AnagraficaSummary> = gson.fromJson(jsonArray.toString(), sType)
        return anagraficaSummary
    }

}

