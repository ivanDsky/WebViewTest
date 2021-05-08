package ua.zloydi.webviewtest

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    val SELECT_PHOTO = 1
    private lateinit var imageView: ImageView
    private lateinit var chooseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        chooseButton = findViewById(R.id.chooseButton)

        chooseButton.setOnClickListener {
            chooseFile()
        }
    }

    private val activityLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()){uri ->
        try {
            val bitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val imageDecoder = ImageDecoder.createSource(contentResolver,uri)
                ImageDecoder.decodeBitmap(imageDecoder)
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver,uri)
            }
            imageView.setImageBitmap(bitmap)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun chooseFile() {
        activityLauncher.launch(arrayOf("image/*"))
        Log.d("FILE","You choose file")
    }

}