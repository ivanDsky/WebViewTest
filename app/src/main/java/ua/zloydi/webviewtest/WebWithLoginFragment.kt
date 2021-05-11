package ua.zloydi.webviewtest

import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient

class WebWithLoginFragment : WebFragment(R.layout.fragment_web_with_login) {
    companion object {
        private const val URL = "http://codeforces.com/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?) =
                !(url?.startsWith(URL) ?: false)

        }

        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
        webView.settings.useWideViewPort = true
        webView.setInitialScale(1)
        webView.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (webView.canGoBack()) {
                        webView.goBack()
                        return@setOnKeyListener true
                    }
                }
            }
            return@setOnKeyListener false
        }
        webView.loadUrl(URL)
    }


}