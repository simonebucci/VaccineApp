package it.mspc.vaccinedata.utilites

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import it.mspc.vaccinedata.data.AnagraficaSummary
import it.mspc.vaccinedata.data.Platea
import org.json.JSONObject
import java.io.File

public class manageFile(context: Context){

    var mycontext = context

    ///////////////////////LastUpdate///////////////////////////

    fun saveFileUpdate(jsonString: String): String {
        val data: String = "vaccine"
        val path =  mycontext.getExternalFilesDir(null)

        val folder = File(path, "jsondata")
        if (!folder.exists()) {
            folder.mkdirs()
        }

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
        val file3 = File(folder, "platea.txt")
        file.delete()
        file2.delete()
        file3.delete()
        println("files deleted!")

    }

    fun openFileUpdate(): File{
        val path = mycontext.getExternalFilesDir(null)
        val folder = File(path, "jsondata")
        val file = File(folder, "lastupdate.txt")
        return file
    }

    /////////////////Anagrafica Summary/////////////////////////

    fun saveFileAna(jsonString: String): String{
        val data: String = "vaccine"
        val path = mycontext.getExternalFilesDir(null)

        val folder = File(path, "jsondata")
        if (!folder.exists()) {
            folder.mkdirs()
        }

        println(folder.exists()) // you'll get true

        val file = File(folder, "anagrafica.txt")
        file.appendText(jsonString)
        println("Anagrafica File saved!")
        println(file.absolutePath)
        val file2 = File(folder, "anagrafica.txt")
        var out = ""
        out = file2.readText()
        println(out)
        return out
    }
    fun readFileAna(): String{
        val path = mycontext.getExternalFilesDir(null)
        val folder = File(path, "jsondata")
        val file2 = File(folder, "anagrafica.txt")
        var out = ""
        out = file2.readText()
        return out
    }
    fun parseFileAna(out: String): ArrayList<AnagraficaSummary>{
        val json =  JSONObject(out)
        val jsonArray = json.getJSONArray("data")
        var gson = Gson()

        val sType = object : TypeToken<ArrayList<AnagraficaSummary>>() {}.type
        var anagraficaSummary: ArrayList<AnagraficaSummary> = gson.fromJson(jsonArray.toString(), sType)
        return anagraficaSummary
    }

    ///////////////////////Platea////////////////////////////////////
    fun saveFilePlatea(jsonString: String): String{

        val path = mycontext.getExternalFilesDir(null)

        val folder = File(path, "jsondata")
        if (!folder.exists()) {
            folder.mkdirs()
        }

        println(folder.exists()) // you'll get true

        val file = File(folder, "platea.txt")
        file.appendText(jsonString)
        println("Platea File saved!")
        println(file.absolutePath)
        val file2 = File(folder, "platea.txt")
        var out = ""
        out = file2.readText()
        println(out)
        return out
    }
    fun readFilePlatea(): String{
        val path = mycontext.getExternalFilesDir(null)
        val folder = File(path, "jsondata")
        val file2 = File(folder, "platea.txt")
        var out = ""
        out = file2.readText()
        return out
    }
    fun parseFilePlatea(out: String): ArrayList<Platea> {
        val json = JSONObject(out)
        val jsonArray = json.getJSONArray("data")
        var gson = Gson()

        val sType = object : TypeToken<ArrayList<Platea>>() {}.type
        return gson.fromJson(jsonArray.toString(), sType)
    }

}

