package com.sid.sephoraproduct.requests

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sid.sephoraproduct.AppExecutor
import com.sid.sephoraproduct.models.SephoraData
import com.sid.sephoraproduct.requests.responses.SephoraDataResponse
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

class RecipeApiClient private constructor() {
    private val mSephoras: MutableLiveData<MutableList<SephoraData>?>

    private val mRecipeRequestTimeout = MutableLiveData<Boolean>()

    private var mRetrieveSephoraDataRunnable: RetrieveSephoraDataRunnable? = null

    val sephoras: LiveData<MutableList<SephoraData>?>
        get() = mSephoras


    val isRecipeRequestTimedOut: LiveData<Boolean>
        get() = mRecipeRequestTimeout

    fun searchSephoraDataApi(query: String?, pageNumber: Int) {
        if (mRetrieveSephoraDataRunnable != null) {
            mRetrieveSephoraDataRunnable = null
        }
        mRetrieveSephoraDataRunnable = RetrieveSephoraDataRunnable(query, pageNumber)
        val handller = AppExecutor.instance!!.networkIO().submit(mRetrieveSephoraDataRunnable)
        AppExecutor.instance!!.networkIO().schedule({ //let the user know its time out
            handller.cancel(true)
        }, 3000, TimeUnit.MILLISECONDS)
    }

    //retrive the Sephora data from webservice using retrofit
    private inner class RetrieveSephoraDataRunnable(private val query: String?, private val pageNumber: Int) : Runnable {
        private var cancelRequest = false
        override fun run() {
            try {
                val response: Response<SephoraDataResponse>? = getSephoraData()?.execute()
                if (cancelRequest) {
                    return
                }
                if (response?.code() == 200) {
                    val list: MutableList<SephoraData> = ArrayList((response?.body() as SephoraDataResponse?)!!.sephoraList)
                    Log.i(TAG, "Sephora list: ${list.toString()}")
                    if (pageNumber == 1) {
                        mSephoras.postValue(list)
                    } else {
                        val currentSephoraData = mSephoras.value
                        currentSephoraData!!.addAll(list)
                        mSephoras.postValue(currentSephoraData)
                    }
                } else {
                    val error = response?.errorBody()!!.string()
                    Log.d(TAG, "Error: $error")
                    mSephoras.postValue(null)
                }
            } catch (e: IOException) {
                Log.d(TAG, "Error: " + e.message)
                mSephoras.postValue(null)
                e.printStackTrace()
            }
        }

        private fun getSephoraData(): Call<SephoraDataResponse>? {
            val recipeAPI = ServiceGenerator.sephoraDataAPI
            return recipeAPI!!.getSephoraData()
        }

        fun cancelRequest() {
            Log.d(TAG, "Cancel Request : Canceling the search request")
            cancelRequest = true
        }
    }

    companion object {
        private const val TAG = "RecipeApiClient"
        private var mInstance: RecipeApiClient? = null
        @JvmStatic
        val instance: RecipeApiClient?
            get() {
                if (mInstance == null) {
                    mInstance = RecipeApiClient()
                }
                return mInstance
            }
    }

    init {
         mSephoras = MutableLiveData()
    }
}