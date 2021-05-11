package ua.zloydi.webviewtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

open class WebFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId){
    protected lateinit var webView: WebView
    protected val viewModel: WebViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(requireActivity())
        webView.apply {
            settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportZoom(true)
        }
        savedInstanceState?.let { webView.restoreState(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return webView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    override fun onDetach() {
        super.onDetach()
        if(webView.parent is ViewGroup){
            (webView.parent as ViewGroup).removeView(webView)
        }
    }


}