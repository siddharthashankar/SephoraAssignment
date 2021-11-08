package com.sid.sephoraproduct

import com.sid.sephoraproduct.adapter.OnSephoraProductListener
import com.sid.sephoraproduct.viewmodels.RecipeListViewModel
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.sid.sephoraproduct.util.VerticalSpacingItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.content.Intent
import android.util.Log
import android.view.View
import com.sid.sephoraproduct.adapter.SephoraDataRecyclerAdapter
import com.sid.sephoraproduct.models.SephoraData

class SephoraProductListActivity : BaseActivity(), OnSephoraProductListener {
    private var mRecipeListViewModel: RecipeListViewModel? = null
    private var recyclerView: RecyclerView? = null
   private var mAdapter: SephoraDataRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        recyclerView = findViewById<View>(R.id.rv) as RecyclerView
        //viewModel
        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel::class.java)
        initRecyclerView()
        subscribeObservers()
        if (!mRecipeListViewModel!!.isViewingRecipies) {
            displaySearchCategory()
        }
    }

    private fun subscribeObservers() {
        mRecipeListViewModel!!.recipes.observe(this, { recipes ->
            if (recipes != null) {
                if (mRecipeListViewModel!!.isViewingRecipies) {
                    mRecipeListViewModel!!.isPerformingQuery = false
                    mAdapter!!.setRecipe(recipes as MutableList<SephoraData>?)
                }
            }
        })
        mRecipeListViewModel!!.isQueryExhausted.observe(this, { aBoolean ->
            if (aBoolean) {
                Log.d(TAG, "On Changed: query is exhausted...")
                mAdapter!!.setQueryExhausted()
            }
        })
    }

    fun initRecyclerView() {
        mAdapter = SephoraDataRecyclerAdapter(this)
        val decoration = VerticalSpacingItemDecoration(5)
        recyclerView?.addItemDecoration(decoration)
        recyclerView?.adapter = mAdapter
        recyclerView?.layoutManager = LinearLayoutManager(this)
    }

    override fun onRecipeClick(position: Int) {
        val intent = Intent(this@SephoraProductListActivity, ProductDetailsActivity::class.java)
        Log.i(TAG,"position: $position")
        intent.putExtra("product_position", position)
        startActivity(intent)
    }

    override fun onCategoryClick(category: String?) {
        mAdapter!!.displayLoading()
        mRecipeListViewModel!!.searchSephoraDataApi(category, 1)
    }

    private fun displaySearchCategory() {
        mRecipeListViewModel?.isViewingRecipies = false
        mAdapter?.displaySearchCategory()
    }

    override fun onBackPressed() {
        if (mRecipeListViewModel!!.onBackPressed()) {
            super.onBackPressed()
        } else {
            displaySearchCategory()
        }
    }

    companion object {
        private const val TAG = "SephoraProductListActivity"
    }
}