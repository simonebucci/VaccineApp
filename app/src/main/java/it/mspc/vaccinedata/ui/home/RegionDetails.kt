package it.mspc.vaccinedata.ui.home

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import it.mspc.vaccinedata.databinding.RegionDetailsBinding

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


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}