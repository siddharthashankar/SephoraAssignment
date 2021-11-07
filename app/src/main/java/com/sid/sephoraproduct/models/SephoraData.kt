package com.sid.sephoraproduct.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SephoraData(
        var id: String? = null,
        var type: String? = null,
        var attributes: Attributes? = null,
        var image_url: String? = null,
        var social_rank: Float = 0f,
        var title: String? = null,
) : Parcelable

@Parcelize
data class Attributes(
        val name: String? = null,
        val price: Int? = null,
        val description: String? = null,
        val benefits: String? = null,
        @SerializedName("image-urls")
        val imageUrls: List<String>,
        val rating: Float? = null
): Parcelable

