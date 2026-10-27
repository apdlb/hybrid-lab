package com.hybridlab.hostapp

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.facebook.react.ReactApplication
import com.facebook.react.runtime.ReactSurfaceImpl

@Composable
fun ReactView(moduleName: String, properties: Bundle, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val surface = remember(moduleName) {
        val reactHost = (context.applicationContext as ReactApplication).reactHost!!
        reactHost.createSurface(context, moduleName, properties).also { it.start() }
    }

    DisposableEffect(surface) {
        onDispose { surface.stop() }
    }

    AndroidView(
        modifier = modifier,
        factory = { surface.view!! },
        update = { (surface as? ReactSurfaceImpl)?.updateInitProps(properties) },
    )
}