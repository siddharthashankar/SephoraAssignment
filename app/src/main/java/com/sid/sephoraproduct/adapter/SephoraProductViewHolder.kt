package com.sid.sephoraproduct.adapter

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sid.sephoraproduct.R

class SephoraProductViewHolder(itemView: View, var onSephoraProductListener: OnSephoraProductListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var title: TextView
    var publisher: TextView
    var rBar : RatingBar
    var img: ImageView
    override fun onClick(v: View) {
        onSephoraProductListener.onRecipeClick(adapterPosition)
    }

    init {
        title = itemView.findViewById<View>(R.id.recipe_title) as TextView
        publisher = itemView.findViewById<View>(R.id.recipe_publisher) as TextView
        rBar = itemView.findViewById<View>(R.id.rBar) as RatingBar
        img = itemView.findViewById<View>(R.id.recipe_image) as ImageView
        itemView.setOnClickListener(this)
    }
}