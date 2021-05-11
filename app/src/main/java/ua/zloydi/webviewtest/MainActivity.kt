package ua.zloydi.webviewtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupView()
    }

    private fun setupView(){
        val config = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(5)
            .build()
        config.setConfigSettingsAsync(configSettings)
        config.fetchAndActivate().addOnCompleteListener {
            supportFragmentManager.beginTransaction().apply {
                if(config.getBoolean("IsLogin")) {
                    add(R.id.fragmentView, WebWithLoginFragment())
                }else {
                    add(R.id.fragmentView, WebWithLoadFilesFragment())
                }
                commit()
            }
        }

    }


}