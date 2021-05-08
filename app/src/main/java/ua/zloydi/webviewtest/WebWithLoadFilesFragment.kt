package ua.zloydi.webviewtest

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.result.contract.ActivityResultContracts

class WebWithLoadFilesFragment : WebFragment(R.layout.fragment_web_with_load_files){

    companion object{
        val TAG: String = WebWithLoadFilesFragment::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView.loadUrl("https://jpg2pdf.com/")
        webView.settings.apply {
            domStorageEnabled = true
            allowContentAccess = true
            allowFileAccess = true
        }
        webView.webChromeClient = webChromeClient
    }

    private val webChromeClient = object : WebChromeClient(){
        override fun onPermissionRequest(request: PermissionRequest?) {
            super.onPermissionRequest(request)
            Log.d(TAG,"Required permissions")
        }

        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            filePathCallback?.onReceiveValue(null)

            Log.d(TAG,"Params: $fileChooserParams")

            registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()){result ->
                filePathCallback?.onReceiveValue(result.toTypedArray())
            }.launch(arrayOf("*/*"))

            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
        }
    }
}

