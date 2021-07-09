package it.mspc.vaccinedata.ui.appuntamento


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import it.mspc.vaccinedata.R
import it.mspc.vaccinedata.data.appuntamento.Appuntamento
import it.mspc.vaccinedata.databinding.FragmentListBinding
import kotlinx.android.synthetic.main.fragment_list.*
import org.json.JSONObject
import java.io.File


class AppuntamentoFragment: Fragment() {


    private var _binding: FragmentListBinding? = null
    private lateinit var appuntamentoViewModel: AppuntamentoViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var date: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appuntamentoViewModel = ViewModelProvider(this).get(AppuntamentoViewModel::class.java)

        _binding = FragmentListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        invisibleData()
        if(!openFile().exists()){
            saveFile("")
        }
        if(readFile().length > 1){
            visibleData()
            setInvisibility()
        }


        binding.addLocationEt.doAfterTextChanged { name ->
            loc = name.toString()
        }
        binding.addFirstDateEt.doAfterTextChanged { name ->
            fd = name.toString()
        }
        binding.addFirstHourEt.doAfterTextChanged { name ->
            fh = name.toString()
        }
        binding.addSecondDateEt.doAfterTextChanged { name ->
            sd = name.toString()
        }
        binding.addSecondHourEt.doAfterTextChanged { name ->
            sh = name.toString()
        }

        binding.addBtn.setOnClickListener {
            if (loc != "" && fd != "" && fh != "" && sd != "" && sh != "") {
                deleteFile()
                toJson(getApp())?.let { it1 ->
                    saveFile(it1)
                    setInvisibility()
                    visibleData()
                }}else{
                    Toast.makeText(requireContext(), "You must insert some data", Toast.LENGTH_LONG)
                        .show()
                }
            }

        binding.btnDelete.setOnClickListener {
            binding.addFirstDateEt.setText("")
            binding.addFirstHourEt.setText("")
            binding.addSecondDateEt.setText("")
            binding.addSecondHourEt.setText("")
            binding.addLocationEt.setText("")
        }

        binding.btnClear.setOnClickListener{

            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage(resources.getString(R.string.app_alert))
                .setCancelable(false)
                .setPositiveButton(R.string.app_yes) { dialog, id ->
                    // Delete selected note from database
                    deleteFile()
                    setVisibility()
                    invisibleData()
                    binding.btnClear.visibility = View.INVISIBLE

                }
                .setNegativeButton(R.string.app_no) { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
            return root
        }

    fun setInvisibility(){
        binding.addFirstDateEt.visibility = View.INVISIBLE
        binding.addFirstHourEt.visibility = View.INVISIBLE
        binding.addSecondDateEt.visibility = View.INVISIBLE
        binding.addSecondHourEt.visibility = View.INVISIBLE
        binding.addLocationEt.visibility = View.INVISIBLE
        binding.addBtn.visibility = View.INVISIBLE
        binding.btnDelete.visibility = View.INVISIBLE
    }

    fun setVisibility(){
        binding.addFirstDateEt.visibility = View.VISIBLE
        binding.addFirstHourEt.visibility = View.VISIBLE
        binding.addSecondDateEt.visibility = View.VISIBLE
        binding.addSecondHourEt.visibility = View.VISIBLE
        binding.addLocationEt.visibility = View.VISIBLE
        binding.addBtn.visibility = View.VISIBLE
        binding.btnDelete.visibility = View.VISIBLE
    }

    fun visibleData(){
        var app = parseFile(readFile())
        binding.tvFirstDateEt.visibility = View.VISIBLE
        binding.tvFirstDateEt.text = resources.getString(it.mspc.vaccinedata.R.string.app_datef) + app.data_prima_dose
        binding.tvFirstHourEt.visibility = View.VISIBLE
        binding.tvFirstHourEt.text = resources.getString(it.mspc.vaccinedata.R.string.app_hourf) + app.orario_prima_dose
        binding.tvSecondDateEt.visibility = View.VISIBLE
        binding.tvSecondDateEt.text = resources.getString(it.mspc.vaccinedata.R.string.app_dates) + app.data_seconda_dose
        binding.tvSecondHourEt.visibility = View.VISIBLE
        binding.tvSecondHourEt.text = resources.getString(it.mspc.vaccinedata.R.string.app_hours) + app.orario_seconda_dose
        binding.tvLocationEt.visibility = View.VISIBLE
        binding.tvLocationEt.text = resources.getString(it.mspc.vaccinedata.R.string.app_loc) + app.punto_vaccinazione
        binding.btnClear.visibility = View.VISIBLE
    }
    fun invisibleData(){
        binding.tvFirstDateEt.visibility = View.INVISIBLE
        binding.tvFirstHourEt.visibility = View.INVISIBLE
        binding.tvSecondDateEt.visibility = View.INVISIBLE
        binding.tvSecondHourEt.visibility = View.INVISIBLE
        binding.tvLocationEt.visibility = View.INVISIBLE
        binding.btnClear.visibility = View.INVISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var loc = ""
    var fd = ""
    var fh = ""
    var sd = ""
    var sh = ""

    //create a var of type Appuntamento
    fun getApp(): Appuntamento {
        var jstring = Appuntamento(0,fd,fh,sd,sh,loc)
        return jstring
    }

    //convert the Appuntamento data in a string
    fun toJson(app: Appuntamento): String? {
        val jsonString = Gson().toJson(app)
        return jsonString
    }



    fun saveFile(jsonString: String): String {
        val path = requireContext().getExternalFilesDir(null)

        val folder = File(path, "jsondata")
        if (!folder.exists()) {
            folder.mkdirs()
        }

        println(folder.exists()) // you'll get true

        val file = File(folder, "appuntamento.txt")
        file.appendText(jsonString)
        println("Appuntamento saved!")
        println(file.absolutePath)
        val file2 = File(folder, "appuntamento.txt")
        var out = ""
        out = file2.readText()
        println(out)
        return out
    }

    fun readFile(): String{
        val path =  requireContext().getExternalFilesDir(null)
        val folder = File(path, "jsondata")
        val file = File(folder, "appuntamento.txt")
        var out = file.readText()
        println(out)
        return out
    }
    fun deleteFile(){
        val path =  requireContext().getExternalFilesDir(null)
        val folder = File(path, "jsondata")
        val file = File(folder, "appuntamento.txt")
        file.delete()
        println("file deleted!")

    }

    fun openFile(): File {
        val path =  requireContext().getExternalFilesDir(null)
        val folder = File(path, "jsondata")
        val file = File(folder, "appuntamento.txt")
        return file
    }
    fun parseFile(out: String): Appuntamento {
        val json = JSONObject(out)
        var gson = Gson()

        val sType = object : TypeToken<Appuntamento>() {}.type
        return gson.fromJson(json.toString(), sType)
    }


}




