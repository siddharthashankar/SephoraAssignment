package com.sid.sephoraproduct.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sid.sephoraproduct.R
import de.hdodenhof.circleimageview.CircleImageView

class CategoryViewHolder(itemView: View, var listener: OnSephoraProductListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var circleImageView: CircleImageView
    var categoryTitle: TextView
    override fun onClick(v: View) {
        listener.onCategoryClick(categoryTitle.text.toString())
    }

    init {
        circleImageView = itemView.findViewById(R.id.category_image)
        categoryTitle = itemView.findViewById(R.id.category_title)
        itemView.setOnClickListener(this)
    }
}