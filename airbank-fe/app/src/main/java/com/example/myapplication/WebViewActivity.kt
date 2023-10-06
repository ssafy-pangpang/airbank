package com.example.myapplication

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview) // Corrected layout file name

        val webView = findViewById<WebView>(R.id.web_view) // Define the "web_view" variable

        webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
        }

        webView.loadUrl("https://m.naver.com")
    }
}