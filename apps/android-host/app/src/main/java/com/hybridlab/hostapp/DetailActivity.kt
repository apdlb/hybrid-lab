package com.hybridlab.hostapp

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import androidx.activity.ComponentActivity

private const val TAG = "HostLifecycle"

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "[Detail] onCreate")
        val count = intent.getIntExtra(EXTRA_COUNT, -1)
        setContentView(TextView(this).apply {
            text = "Count received: $count"
            textSize = 24f
            gravity = Gravity.CENTER
        })
    }

    override fun onStart() { super.onStart(); Log.d(TAG, "[Detail] onStart") }
    override fun onResume() { super.onResume(); Log.d(TAG, "[Detail] onResume") }
    override fun onPause() { super.onPause(); Log.d(TAG, "[Detail] onPause") }
    override fun onStop() { super.onStop(); Log.d(TAG, "[Detail] onStop") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "[Detail] onDestroy") }

    companion object {
        const val EXTRA_COUNT = "count"
    }
}