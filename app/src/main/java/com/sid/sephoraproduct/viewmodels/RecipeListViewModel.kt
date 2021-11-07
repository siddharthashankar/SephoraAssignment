package com.sid.sephoraproduct.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sid.sephoraproduct.models.SephoraData
import com.sid.sephoraproduct.repositories.ProductRepository

class RecipeListViewModel : ViewModel() {
    private val mProductRepository: ProductRepository
    var isViewingRecipies: Boolean
    var isPerformingQuery:Boolean = false
    val recipes: LiveData<List<SephoraData>>
        get() = mProductRepository.sephoraDatas
    val isQueryExhausted: LiveData<Boolean>
        get() = mProductRepository.isQueryExhausted

    fun searchSephoraDataApi(query: String? = "chicken", pageNumber: Int = 1) {
        isViewingRecipies = true
        isPerformingQuery = true
        mProductRepository.searchSephoraDataApi(query, pageNumber)
    }

    fun onBackPressed(): Boolean {
        if (isPerformingQuery) {
            isPerformingQuery = false
        } else if (isViewingRecipies) {
            isViewingRecipies = false
            return false
        }
        return true
    }

    init {
        mProductRepository = ProductRepository.instance!!
        isViewingRecipies = false
    }
}