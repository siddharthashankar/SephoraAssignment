package com.sid.sephoraproduct

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sid.sephoraproduct.models.SephoraData
import com.sid.sephoraproduct.viewmodels.RecipeListViewModel
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener


class ProductDetailsActivity : BaseActivity() {
    // UI components
    private var mRecipeImage: ImageView? = null
    private var mRecipeTitle: TextView? = null
    private var mRecipeRank: TextView? = null
    private var rBar: RatingBar? = null
    private var mDescription: TextView? = null
    private var mBenefits: TextView? = null
    private var mRecipeIngredientsContainer: LinearLayout? = null
    private var mScrollView: ScrollView? = null
    private var mRecipeListViewModel: RecipeListViewModel? = null
    private var selectedItem: Int = -1
    var carouselView: CarouselView? = null
    var sephoraDatas: List<SephoraData>? = null
    val imageUrls: ArrayList<String>? = null
    var arraylist = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        mRecipeImage = findViewById(R.id.recipe_image)
        mRecipeTitle = findViewById(R.id.recipe_title)
        mRecipeRank = findViewById(R.id.recipe_social_score)
        rBar = findViewById<View>(R.id.rBar) as RatingBar
        mDescription = findViewById(R.id.description)
        mBenefits = findViewById(R.id.benefits)
        mRecipeIngredientsContainer = findViewById(R.id.ingredients_container)
        mScrollView = findViewById(R.id.parent)
        //viewModel
        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel::class.java)
        showProgressBar(true)
        subscribeObservers()
        incomingIntent
        var size = sephoraDatas?.get(selectedItem)?.attributes?.imageUrls?.size
        Log.i(TAG,"size urls: $size")
        val carouselView = findViewById(R.id.image_carousel) as CarouselView;
        if (size != null) {
            carouselView.setPageCount(size)
        };
        carouselView.setImageListener(imageListener);
    }

    private fun subscribeObservers() {
        mRecipeListViewModel!!.recipes.observe(this, { recipes ->
            if (recipes != null) {
                setRecipeProperties(recipes)
            }
        })
    }

    private val incomingIntent: Unit
        private get() {
                selectedItem = intent.getIntExtra("product_position", 0);
                Log.i(TAG, "Selected id:$selectedItem")
        }


    private fun displayErrorScreen(errorMessage: String) {
        mRecipeTitle!!.text = "Error retrieveing recipe..."
        mRecipeRank!!.text = ""
        val textView = TextView(this)
        if (errorMessage != "") {
            textView.text = errorMessage
        } else {
            textView.text = "Error"
        }
        textView.textSize = 15f
        textView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mRecipeIngredientsContainer!!.addView(textView)
        val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(R.drawable.ic_launcher_background)
                .into(mRecipeImage!!)
        showParent()
        showProgressBar(false)
    }

    private fun setRecipeProperties(sephoraDatas: List<SephoraData>) {
        this.sephoraDatas = sephoraDatas
        Log.i(TAG, "setRecipeProperties: ${sephoraDatas.toString()}")
        val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)
        mRecipeTitle!!.text = "Name: "+sephoraDatas.get(selectedItem).attributes?.name
        mRecipeRank!!.text = "Price: "+sephoraDatas.get(selectedItem).attributes?.price.toString()
        rBar?.rating = sephoraDatas.get(selectedItem).attributes?.rating?.toFloat()!!
        mDescription!!.text = "Description: "+sephoraDatas.get(selectedItem).attributes?.description
        mBenefits!!.text = "Benefits: "+sephoraDatas.get(selectedItem).attributes?.benefits.toString()

        var size = sephoraDatas?.get(selectedItem)?.attributes?.imageUrls?.size
        Log.i(TAG,"size urls: ${size?.toInt()}")
        val carouselView = findViewById(R.id.image_carousel) as CarouselView;
        if (size != null) { carouselView.setPageCount(size) };
        carouselView.setImageListener(imageListener);
        for(i in sephoraDatas.get(selectedItem).attributes?.imageUrls!!){
            arraylist.add(i)
            Log.i(TAG,"imageUrls: $i ${arraylist.size}")
        }
        mRecipeIngredientsContainer!!.removeAllViews()
        showParent()
        showProgressBar(false)
    }

    private fun showParent() {
        mScrollView!!.visibility = View.VISIBLE
    }

    companion object {
        private const val TAG = "ProductDetailsActivity"
    }

    var imageListener: ImageListener = object : ImageListener {
        override fun setImageForPosition(position: Int, imageView: ImageView) {
            val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)
            for(item in arraylist){
                Glide.with(this@ProductDetailsActivity)
                        .setDefaultRequestOptions(requestOptions)
                        .load(item)
                        .into(imageView!!)
            }

        }
    }
}