package it.mspc.vaccinedata.utilites

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import it.mspc.vaccinedata.MainActivity
import java.io.File

public class manageFile(context: Context){

    fun saveFileUpdate(jsonString: String): String {
        val data: String = "vaccine"
        val path =  requireContext().getExternalFilesDir(null)

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

    fun readFileUpdate(){
        val path = context.getExternalFilesDir(null)
        val folder = File(path, "jsondata")
        val file = File(folder, "lastupdate.txt")
        var out = file.readText()
        println(out)
    }
    fun deleteFileUpdate(){
        val path = context.getExternalFilesDir(null)
        val folder = File(path, "jsondata")
        val file = File(folder, "lastupdate.txt")
        file.delete()
        println("file deleted!")

    }

    fun openFileUpdate(): File{
        val path = context.getExternalFilesDir(null)
        val folder = File(path, "jsondata")
        val file = File(folder, "lastupdate.txt")
        return file
    }

}

