package com.sid.sephoraproduct.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.sid.sephoraproduct.models.SephoraData
import com.sid.sephoraproduct.requests.RecipeApiClient

class ProductRepository private constructor() {
    private val mRecipeApiClient: RecipeApiClient?
    private var mQuery: String? = null
    private var mPageNumber = 0
    private val mIsQueryExhausted = MutableLiveData<Boolean>()
    private val mSephoraData = MediatorLiveData<List<SephoraData>>()
    private fun initMediators() {
        val recipeListApiSource: LiveData<MutableList<SephoraData>?> = mRecipeApiClient!!.sephoras
        mSephoraData.addSource(recipeListApiSource) { recipes ->
            if (recipes != null) {
                mSephoraData.setValue(recipes)
                doneQuery(recipes)
            } else {
                // search database cache
                doneQuery(null)
            }
        }
    }

    private fun doneQuery(list: List<SephoraData>?) {
        if (list != null) {
            if (list.size % 30 != 0) {
                mIsQueryExhausted.value = true
            }
        } else {
            mIsQueryExhausted.setValue(true)
        }
    }

    val isQueryExhausted: LiveData<Boolean>
        get() = mIsQueryExhausted

    val sephoraDatas: LiveData<List<SephoraData>>
        get() = mSephoraData


    fun searchRecipeApi(query: String?, pageNumber: Int) {
        var pageNumber = pageNumber
        if (pageNumber == 0) {
            pageNumber = 1
        }
        mQuery = query
        mPageNumber = pageNumber
        mIsQueryExhausted.value = false
    }

    fun searchSephoraDataApi(query: String?, pageNumber: Int) {
        mRecipeApiClient!!.searchSephoraDataApi(query, pageNumber)
    }

    companion object {
        private var mInstance: ProductRepository? = null
        val instance: ProductRepository?
            get() {
                if (mInstance == null) {
                    mInstance = ProductRepository()
                }
                return mInstance
            }
    }

    init {
        mRecipeApiClient = RecipeApiClient.instance
        initMediators()
    }
}