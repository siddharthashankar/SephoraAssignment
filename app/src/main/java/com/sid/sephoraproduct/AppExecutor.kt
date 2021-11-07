package com.sid.sephoraproduct

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class AppExecutor {
    private val mNetworkIO = Executors.newScheduledThreadPool(3)
    fun networkIO(): ScheduledExecutorService {
        return mNetworkIO
    }

    companion object {
        private var mInstance: AppExecutor? = null
        @JvmStatic
        val instance: AppExecutor?
            get() {
                if (mInstance == null) {
                    mInstance = AppExecutor()
                }
                return mInstance
            }
    }
}