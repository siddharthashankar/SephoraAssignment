package com.sid.sephoraproduct.requests

import com.sid.sephoraproduct.requests.responses.SephoraDataResponse
import retrofit2.Call
import retrofit2.http.GET

interface SephoraProductAPI {

    //sephora data
    @GET("products")
    fun getSephoraData(): Call<SephoraDataResponse>?
}