package ua.zloydi.webviewtest

import android.app.Application
import com.onesignal.OneSignal

class App : Application(){
    companion object{
        private const val ONESIGNAL_APP_ID = "27d6bb4f-d638-454a-b976-99c7ff32f28d"
    }

    override fun onCreate() {
        super.onCreate()

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE,OneSignal.LOG_LEVEL.NONE)

        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }
}