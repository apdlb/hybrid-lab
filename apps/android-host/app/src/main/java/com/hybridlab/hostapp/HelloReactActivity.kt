package com.hybridlab.hostapp

import android.os.Bundle
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultReactActivityDelegate

class HelloReactActivity : ReactActivity() {
    override fun getMainComponentName(): String = "HelloScreen"

    override fun createReactActivityDelegate(): ReactActivityDelegate =
        object : DefaultReactActivityDelegate(this, mainComponentName) {
            override fun getLaunchOptions(): Bundle = Bundle().apply {
                putString("source", intent.getStringExtra(EXTRA_SOURCE) ?: "unknown")
            }
        }

    companion object {
        const val EXTRA_SOURCE = "source"
    }
}