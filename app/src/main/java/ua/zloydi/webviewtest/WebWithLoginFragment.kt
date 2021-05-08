package ua.zloydi.webviewtest

import android.os.Bundle

class WebWithLoginFragment : WebFragment(R.layout.fragment_web_with_login){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView.loadUrl("https://jpg2pdf.com/")
    }
}