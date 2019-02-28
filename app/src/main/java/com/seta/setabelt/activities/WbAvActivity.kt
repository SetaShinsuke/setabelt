package com.seta.setabelt.activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.*
import com.seta.common.extensions.dumpExtras
import com.seta.common.extensions.logD
import com.seta.setabelt.R
import kotlinx.android.synthetic.main.activity_wb_av.*

class WbAvActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wb_av)

        mWebView.settings.javaScriptEnabled = true
        mWebView.settings.builtInZoomControls = false
        mWebView.settings.javaScriptCanOpenWindowsAutomatically = true
        WebView.setWebContentsDebuggingEnabled(true)
        if (Build.VERSION.SDK_INT >= 21) {
            mWebView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW//允许混合内容，5.0之后的api
        }
        intent.dumpExtras()
        if (intent.hasExtra(android.content.Intent.EXTRA_TEXT)) {
            val url = intent.getStringExtra(android.content.Intent.EXTRA_TEXT)
            mEtUrl.setText(url)
            startUrl(url)
//        }
//        if (intent.hasExtra("typ")) {
//            val url = when (intent.getStringExtra("typ")) {
//                "text/plain" ->
//                    intent.getStringExtra("clip")
//                else -> {
//                    logD("type unknown")
//                    null
//                }
//            }
//            mEtUrl.setText(url)
//            url?.let { startUrl(it) }
        } else if (intent.dataString != null) {
            mEtUrl.setText(intent.dataString)
            startUrl(intent.dataString)
        }

    }

    fun onStartClick(view: View) {
        val url = mEtUrl.text.toString()
        startUrl(url)
    }

    fun startUrl(urlParam: String) {
        val url = urlParam.replaceBefore("http", "")
        logD("WebView load url: $url")
        mWebView.clearCache(true)
        mWebView.loadUrl(url)
        mWebView.webChromeClient = WebChromeClient()
        mWebView.webViewClient = object : WebViewClient() {

            override fun onLoadResource(view: WebView?, url: String?) {
                logD("onLoadResource: $url")
                url?.let {
                    if (it.contains(".mp4") || it.contains(".flv")) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }
                }
                super.onLoadResource(view, url)
            }

//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                logD("WebView shouldOverrideUrlLoading (Old) : $url")
//                if (handleUrl(url.toString())) {
//                    return true
//                }
//                return super.shouldOverrideUrlLoading(view, url)
//            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                logD("WebView shouldOverrideUrlLoading: $url")
                val jumpUrl = request?.url.toString()
                if (handleUrl(jumpUrl)) {
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            private fun handleUrl(url: String): Boolean {
                logD("WebView handle: $url")
                return false
            }
        }
    }
}
