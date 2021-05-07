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

    private fun chooseFile() {
        val pickImage = Intent(Intent.ACTION_PICK)
        pickImage.type = "image/*"
        startActivityForResult(pickImage,SELECT_PHOTO)
        Log.d("FILE","You choose file")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.data != null){
            val uri = data.data!!
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
    }
}