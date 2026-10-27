package com.hybridlab.hostapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import com.facebook.react.ReactApplication
import com.hybridlab.hostapp.ui.theme.HostAppTheme

private const val TAG = "HostLifecycle"

class MainActivity : ComponentActivity() {
    
    private val reactHost get() = (application as ReactApplication).reactHost!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate (restored=${savedInstanceState != null})")
        enableEdgeToEdge()
        setContent {
            HostAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CounterScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    override fun onStart() { super.onStart(); Log.d(TAG, "onStart") }
    override fun onResume() { super.onResume(); Log.d(TAG, "onResume"); reactHost.onHostResume(this) }
    override fun onPause() { super.onPause(); Log.d(TAG, "onPause"); reactHost.onHostPause(this) }
    override fun onStop() { super.onStop(); Log.d(TAG, "onStop") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy"); reactHost.onHostDestroy(this) }
}

@Composable
fun CounterScreen(modifier: Modifier = Modifier) {
    var remembered by remember { mutableIntStateOf(0) }
    var saved by rememberSaveable { mutableIntStateOf(0) }
    val context = LocalContext.current
    Log.d(TAG, "CounterScreen recomposition")

    DisposableEffect(Unit) {
        Log.d(TAG, "CounterScreen enters composition")
        onDispose { Log.d(TAG, "CounterScreen leaves composition") }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("remember: $remembered", style = MaterialTheme.typography.headlineSmall)
        Text("rememberSaveable: $saved", style = MaterialTheme.typography.headlineSmall)
        ReactView(
            moduleName = "InlineCard",
            properties = bundleOf("title" to "Card inside Compose (saved: $saved)"),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 16.dp),
        )
        Button(onClick = { remembered++; saved++ }) { Text("Increment") }
        Button(onClick = {
            val intent = Intent(context, DetailActivity::class.java)
                .putExtra(DetailActivity.EXTRA_COUNT, saved)
            context.startActivity(intent)
        }) { Text("Open detail") }
        Button(onClick = {
            val intent = Intent(context, HelloReactActivity::class.java)
                .putExtra(HelloReactActivity.EXTRA_SOURCE, "Compose Home (saved: $saved)")
            context.startActivity(intent)
        }) { Text("Open RN screen") }
    }
}

@Preview(showBackground = true)
@Composable
fun CounterScreenPreview() {
    HostAppTheme {
        CounterScreen()
    }
}