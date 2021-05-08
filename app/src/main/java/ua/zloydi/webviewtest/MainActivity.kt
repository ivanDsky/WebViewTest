package ua.zloydi.webviewtest

import android.Manifest
import android.app.DownloadManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var valueCallback: ValueCallback<Array<Uri>>

    companion object {
        const val SELECT_PHOTO = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        haveStoragePermission()
        webView = findViewById(R.id.webview)
        webView.apply {
            settings.cacheMode = WebSettings.LOAD_DEFAULT
            settings.javaScriptEnabled = true
            settings.allowFileAccess = true
            setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
                Log.d("Download", "$url -> $contentLength")
                val request = DownloadManager.Request(Uri.parse(url))
                request.allowScanningByMediaScanner()
                request.setMimeType(mimetype)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    URLUtil.guessFileName(url, contentDisposition, mimetype)
                )
                if (haveStoragePermission()) {
                    val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                    downloadManager.enqueue(request)
                }

            }
            webChromeClient = this@MainActivity.webChromeClient
            loadUrl("https://jpg2pdf.com/")
        }
    }

    private val webChromeClient = object : WebChromeClient() {
        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            filePathCallback?.let { valueCallback = it }
            Log.d("File choose", "You wan't choose file")
            imagePick()
            return true
        }
    }

    private fun imagePick() {
        val pickImage = Intent(Intent.ACTION_GET_CONTENT)
        pickImage.type = "image/*"
        pickImage.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(pickImage, "Select pictures "), SELECT_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null) {
            val uriArray = arrayListOf<Uri>()
            data.clipData?.let {
                for (i in 0 until it.itemCount) {
                    uriArray.add(it.getItemAt(i).uri)
                }
            }
            data.data?.let {
                uriArray.add(it)
            }
            valueCallback.onReceiveValue(uriArray.toTypedArray())
        }
    }

    private fun haveStoragePermission(): Boolean =
        if (Build.VERSION.SDK_INT >= 23 &&
            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("Permission error", "You have asked for permission")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
            false
        } else {
            true
        }


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        webView.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        webView.restoreState(savedInstanceState)
    }
}