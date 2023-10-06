package com.example.myapplication.screens

import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.myapplication.model.POSTLoginResponse
import com.google.gson.Gson

@Composable
fun NaverScreen(navController: NavController){
    val url = "file:///android_asset/login.html"
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {

                settings.javaScriptEnabled = true
                settings.cacheMode = WebSettings.LOAD_NO_CACHE
                webChromeClient = WebChromeClient()
                addJavascriptInterface(JavaScriptInterface(this), "AIRBANK")
                loadUrl("file:///android_asset/login.html")
            }
        }
    )
}


class JavaScriptInterface(private val webView: WebView) {
    @JavascriptInterface
    fun showToast(toast:String){
        val TAG:String = "showToast : "
        Log.d(TAG, "showToast: $toast")
        Toast.makeText(webView.context, toast, Toast.LENGTH_SHORT).show()
    }
    @JavascriptInterface
    fun onData(response:String) {
        val TAG:String = "onData : "
        Log.d(TAG,"data: $response")
        val gson = Gson()
        val data = gson.fromJson(response,POSTLoginResponse::class.java)
        Log.d(TAG,"code: ${data.code}")
        Log.d(TAG,"data: ${data.data}")
        Log.d(TAG,"message: ${data.message}")
    }
}

