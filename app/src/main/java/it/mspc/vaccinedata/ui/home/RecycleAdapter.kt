package it.mspc.vaccinedata.ui.home


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import it.mspc.vaccinedata.R

class RecyclerAdapter(private val region: Array<String>, val images: Array<String>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardTitle: TextView = itemView.findViewById(R.id.cardTitle)
        val cardImage: ImageView = itemView.findViewById(R.id.cardImage)
        val view = itemView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardTitle.text = region[position]
        Picasso.get().load(images[position]).into(holder.cardImage)

 //
        // intent per activity RegionDetails
        holder.view.setOnClickListener {
            val intent = Intent(it.context, RegionDetails::class.java)
            intent.putExtra("Region", region[position])
            intent.putExtra("Image", images[position])
            holder.view.context.startActivity(intent)
        }
    }
//
    override fun getItemCount() = region.size
}
    //1
    /*class RegionHolder (v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        //2
        private var view: View = v
        private var region: String? = null

        //3
        init {
            v.setOnClickListener(this)
        }

        //4
        override fun onClick(v: View) {
            val context = itemView.context
            val showRegionIntent = Intent(context, HomeFragment::class.java)
            showRegionIntent.putExtra(REGION_KEY, region)
            context.startActivity(showRegionIntent)
        }

        companion object {
            //5
            private val REGION_KEY = "REGION"
        }
        /*fun bindPhoto(region: Region) {
            this.region = region
            Picasso.with(view.context).load(region.url).into(view.itemImage)
            view.itemDate.text = photo.humanDate
            view.itemDescription.text = photo.explanation
        }*/
        fun bindRegion(region: String) {
            this.region = region

        }
    }*/
/*
Make the class extend RecyclerView.ViewHolder, allowing the adapter to use it as as a ViewHolder.
Add a reference to the view you’ve inflated to allow the ViewHolder to access the ImageView and TextView
as an extension property. Kotlin Android Extensions plugin adds hidden caching functions and fields to prevent
the constant querying of views.
Initialize the View.OnClickListener.
Implement the required method for View.OnClickListener since ViewHolders are responsible for their own event handling.
Add a key for easy reference to the item launching the RecyclerView.
*/


