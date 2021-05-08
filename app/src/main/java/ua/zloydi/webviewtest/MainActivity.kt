package ua.zloydi.webviewtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupView()
    }

    private fun setupView(){
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragmentView,WebWithLoadFilesFragment())
            commit()
        }
    }


}