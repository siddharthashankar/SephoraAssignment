package com.sid.sephoraproduct.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sid.sephoraproduct.R
import com.sid.sephoraproduct.models.SephoraData
import com.sid.sephoraproduct.util.Constants
import java.util.*

class SephoraDataRecyclerAdapter(private val onSephoraProductListener: OnSephoraProductListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mRecipe: MutableList<SephoraData>? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View? = null
        return when (viewType) {
            SEPHORAPRODUCT_TYPE -> {
                view = LayoutInflater.from(viewGroup.context).inflate(R.layout.layout_product_list_item, viewGroup, false)
                SephoraProductViewHolder(view, onSephoraProductListener)
            }
            LOADING_TYPE -> {
                view = LayoutInflater.from(viewGroup.context).inflate(R.layout.layout_loading_list_item, viewGroup, false)
                LoadingViewHolder(view)
            }
            EXHAUSTED_TYPE -> {
                view = LayoutInflater.from(viewGroup.context).inflate(R.layout.layout_search_exhausted, viewGroup, false)
                SearchExhaustedViewHolder(view)
            }
            CATEGORY_TYPE -> {
                view = LayoutInflater.from(viewGroup.context).inflate(R.layout.layout_category_list_item, viewGroup, false)
                CategoryViewHolder(view, onSephoraProductListener)
            }
            else -> {
                view = LayoutInflater.from(viewGroup.context).inflate(R.layout.layout_product_list_item, viewGroup, false)
                SephoraProductViewHolder(view, onSephoraProductListener)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val itemViewType = getItemViewType(position)
        if (itemViewType == SEPHORAPRODUCT_TYPE) {
            val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)
            Glide.with(viewHolder.itemView.context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(mRecipe!![position].attributes?.imageUrls?.get(0))
                    .into((viewHolder as SephoraProductViewHolder).img)
            viewHolder.title.text = mRecipe!![position].attributes?.name
            viewHolder.publisher.text = mRecipe!![position].attributes?.price.toString()
            viewHolder.rBar.rating = mRecipe!![position].attributes?.rating?.toFloat()!!
        } else if (itemViewType == CATEGORY_TYPE) {
            val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)
            val path = Uri.parse("android.resource://com.sid.foodrecipes/drawable/" + mRecipe!![position].image_url)
            Glide.with(viewHolder.itemView.context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(path)
                    .into((viewHolder as CategoryViewHolder).circleImageView)
            viewHolder.categoryTitle.text = mRecipe!![position].title
        }
    }

    override fun getItemCount(): Int {
        return if (mRecipe != null) {
            mRecipe!!.size
        } else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (mRecipe!![position].social_rank == -1f) {
            CATEGORY_TYPE
        } else if (mRecipe!![position].id == "LOADING...") {
            LOADING_TYPE
        } else if (mRecipe!![position].type == "EXHAUSTED...") {
            EXHAUSTED_TYPE
        } else if (position == mRecipe!!.size - 1 && position != 0 && mRecipe!![position].id != "EXHAUSTED...") {
            LOADING_TYPE
        } else {
            SEPHORAPRODUCT_TYPE
        }
    }

    fun setQueryExhausted() {
        hideLoading()
        val exhaustedRecipe = SephoraData()
        exhaustedRecipe.id = "EXHAUSTED..."
        mRecipe!!.add(exhaustedRecipe)
        notifyDataSetChanged()
    }

    private fun hideLoading() {
        if (isLoading) {
            for (recipe in mRecipe!!) {
                if (recipe.id == "LOADING...") {
                    mRecipe!!.remove(recipe)
                }
            }
            notifyDataSetChanged()
        }
    }

    fun displayLoading() {
        if (!isLoading) {
            val recipe = SephoraData()
            recipe.id = "LOADING..."
            val loadingList: MutableList<SephoraData> = ArrayList()
            loadingList.add(recipe)
            mRecipe = loadingList
            notifyDataSetChanged()
        }
    }

    private val isLoading: Boolean
        private get() {
            if (mRecipe != null) {
                if (mRecipe!!.size > 0) {
                    if (mRecipe!![mRecipe!!.size - 1].id == "LOADING...") {
                        return true
                    }
                }
            }
            return false
        }

    fun displaySearchCategory() {
        val categories: MutableList<SephoraData> = ArrayList()
        for (i in 0 until Constants.DEFAULT_SEARCH_CATEGORIES.size) {
            val recipe = SephoraData()
            recipe.title = Constants.DEFAULT_SEARCH_CATEGORIES[i]
            recipe.image_url = Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]
            recipe.social_rank = -1f
            categories.add(recipe)
        }
        mRecipe = categories
        notifyDataSetChanged()
    }

    fun setRecipe(recipe: MutableList<SephoraData>?) {
        mRecipe = recipe
        notifyDataSetChanged()
    }

    fun getSelectedRecipe(position: Int = 1): SephoraData? {
        if (mRecipe != null) {
            if (mRecipe!!.size > 0) {
                return mRecipe!![position]
            }
        }
        return null
    }

    companion object {
        private const val SEPHORAPRODUCT_TYPE = 1
        private const val LOADING_TYPE = 2
        private const val CATEGORY_TYPE = 3
        private const val EXHAUSTED_TYPE = 4
    }
}