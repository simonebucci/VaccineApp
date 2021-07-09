package it.mspc.vaccinedata.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.squareup.picasso.Picasso
import it.mspc.vaccinedata.R
import it.mspc.vaccinedata.data.VacciniSummary
import it.mspc.vaccinedata.databinding.RegionDetailsBinding
import it.mspc.vaccinedata.utilities.FileManager

class RegionDetails: AppCompatActivity() {
    private lateinit var binding: RegionDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = RegionDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sTitle = intent.getStringExtra("Region")
        val sImage = intent.getStringExtra("Image")

        supportActionBar?.title = sTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Picasso.get().load(sImage).into(binding.storyFeatureImage)

        vacciniParse()
        if (sTitle != null) {
            getRegion(sTitle)
        }

    }
    private var del = 0
    private var ino = 0
    private var per = 0
    var del1 = 0
    var ino1 = 0
    var per1 = 0

    fun getRegion(region: String){
        for(i in 0 until vacciniSummary.size){
            if(region == vacciniSummary[i].nome_area){
                binding.tvName.text = resources.getString(R.string.name_region) + vacciniSummary[i].nome_area
                binding.tvDel.text = resources.getString(R.string.del_region) + vacciniSummary[i].dosi_consegnate.toString()
                binding.tvIno.text = resources.getString(R.string.ino_region) + vacciniSummary[i].dosi_somministrate.toString()
                binding.tvPerc.text = resources.getString(R.string.perc_region) + vacciniSummary[i].percentuale_somministrazione.toString()
                del = vacciniSummary[i].dosi_consegnate
                ino = vacciniSummary[i].dosi_somministrate
                per = vacciniSummary[i].percentuale_somministrazione.toInt()
                setPieChart()
                updateProgressBar()
            }else if(region == "Valle d'Aosta"){
                for(j in 0 until vacciniSummary.size){
                    if(vacciniSummary[i].index == 19) {
                        binding.tvName.text = resources.getString(R.string.name_region) + vacciniSummary[i].nome_area
                        binding.tvDel.text = resources.getString(R.string.del_region) + vacciniSummary[i].dosi_consegnate.toString()
                        binding.tvIno.text = resources.getString(R.string.ino_region) + vacciniSummary[i].dosi_somministrate.toString()
                        binding.tvPerc.text = resources.getString(R.string.perc_region) + vacciniSummary[i].percentuale_somministrazione.toString()
                        del = vacciniSummary[i].dosi_consegnate
                        ino = vacciniSummary[i].dosi_somministrate
                        per = vacciniSummary[i].percentuale_somministrazione.toInt()
                        setPieChart()
                        updateProgressBar()
                    }
                }
            }else if(region == "Trentino Alto Adige"){
                for(j in 0 until vacciniSummary.size){
                    if(vacciniSummary[i].index == 12) {
                        binding.tvName.text = resources.getString(R.string.name_region) + "Trentino Alto Adige"
                        binding.tvDel.text = resources.getString(R.string.del_region) + vacciniSummary[i].dosi_consegnate.toString()
                        binding.tvIno.text = resources.getString(R.string.ino_region) + vacciniSummary[i].dosi_somministrate.toString()
                        binding.tvPerc.text = resources.getString(R.string.perc_region) + vacciniSummary[i].percentuale_somministrazione.toString()
                        var del1 = vacciniSummary[i].dosi_consegnate
                        var ino1 = vacciniSummary[i].dosi_somministrate
                        var per1 = vacciniSummary[i].percentuale_somministrazione.toInt()
                    }
                    if(vacciniSummary[i].index == 11) {
                        binding.tvDel.text = resources.getString(R.string.del_region) + vacciniSummary[i].dosi_consegnate.toString()
                        binding.tvIno.text = resources.getString(R.string.ino_region) + vacciniSummary[i].dosi_somministrate.toString()
                        binding.tvPerc.text = resources.getString(R.string.perc_region) + vacciniSummary[i].percentuale_somministrazione.toString()
                        var del2 = vacciniSummary[i].dosi_consegnate
                        var ino2 = vacciniSummary[i].dosi_somministrate
                        del = del1 + del2
                        ino = ino1 + ino2
                        per = (ino*100)/del

                        setPieChart()
                        updateProgressBar()
                    }
                }
            }
        }
    }

    private var vacciniSummary = ArrayList<VacciniSummary>()
    private fun vacciniParse() {
        //parse del file json contenente il summary dei vaccini
        val manage = FileManager(this)
        val out = manage.readFileVacciniSumm()
        vacciniSummary = manage.parseFileVacciniSumm(out)
    }

    private fun setPieChart() {
        val xValues = ArrayList<String>()
        xValues.add(resources.getString(R.string.pie_delivered))
        xValues.add(resources.getString(R.string.pie_inoculated))



        val pieChartEntry = ArrayList<Entry>()
        pieChartEntry.add(Entry(del.toFloat(),0))
        pieChartEntry.add(Entry(ino.toFloat(),1))



        val dataSet = PieDataSet(pieChartEntry, "")
        dataSet.valueTextSize=0f
        val colors = java.util.ArrayList<Int>()
        colors.add(resources.getColor(R.color.blue_A200))
        colors.add(resources.getColor(R.color.blue_700))

        dataSet.setColors(colors)
        val data = PieData(xValues,dataSet)
        binding.pieChart.data = data
        binding.pieChart.centerTextRadiusPercent = 20f
        binding.pieChart.isDrawHoleEnabled = true
        binding.pieChart.legend.isEnabled = true
        binding.pieChart.setDescription("")
        binding.pieChart.animateY(3000)
        binding.pieChart.setDrawSliceText(true)

        val legend: Legend = binding.pieChart.legend
        legend.position = Legend.LegendPosition.BELOW_CHART_RIGHT

    }

    private fun updateProgressBar() {
        //funzione che aggiorna la progress bar
        binding.progressBar2.progress = per
        binding.tvProgress.text = "$per%"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}