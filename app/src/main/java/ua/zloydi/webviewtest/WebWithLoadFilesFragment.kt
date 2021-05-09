package ua.zloydi.webviewtest

import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.webkit.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class WebWithLoadFilesFragment : WebFragment(R.layout.fragment_web_with_load_files) {

    companion object {
        val TAG: String = WebWithLoadFilesFragment::class.java.name
    }


    private lateinit var downloadCallback: (Boolean) -> Unit
    private val downloadLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            downloadCallback(it)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView.loadUrl("https://jpg2pdf.com/")
        webView.settings.apply {
            domStorageEnabled = true
            allowContentAccess = true
            allowFileAccess = true
        }

        webView.setDownloadListener { url, _, contentDisposition, mimetype, _ ->
            downloadCallback = {
                if (it) {
                    val request = DownloadManager.Request(Uri.parse(url))
                    request.apply {
                        setMimeType(mimetype)
                        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS,
                            URLUtil.guessFileName(url, contentDisposition, mimetype)
                        )
                    }
                    val downloadManager =
                        requireActivity().getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                    downloadManager.enqueue(request)
                }
            }

            downloadLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        }
        webView.webChromeClient = webChromeClient
    }

    private val webChromeClient = object : WebChromeClient() {
        override fun onPermissionRequest(request: PermissionRequest?) {
            super.onPermissionRequest(request)
            Log.d(TAG, "Required permissions")
        }

        private lateinit var launcherCallback: (List<Uri>) -> Unit
        private val launcher =
            registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { result ->
                Log.d(TAG, "Result : ${result.size}")
                launcherCallback(result)
            }

        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            launcherCallback = {
                filePathCallback?.onReceiveValue(it.toTypedArray())
            }

            launcher.launch(arrayOf("image/*"))

            return true
        }


    }
}

