package com.sid.sephoraproduct.adapter

interface OnSephoraProductListener {
    fun onRecipeClick(position: Int)
    fun onCategoryClick(category: String?)
}