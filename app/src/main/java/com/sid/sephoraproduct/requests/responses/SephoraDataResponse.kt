package com.sid.sephoraproduct.requests.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.sid.sephoraproduct.models.SephoraData

class SephoraDataResponse {
    @SerializedName("data")
    @Expose
    val sephoraList: List<SephoraData>? = null
    override fun toString(): String {
        return "SephoraDataResponse{" +
                "sephoraList=" + sephoraList +
                '}'
    }
}