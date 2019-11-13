package app.itgungnir.kwa

import androidx.multidex.MultiDexApplication
import app.itgungnir.kwa.common.util.AppConfig

class App : MultiDexApplication() {

    companion object {
        // 是否是第一次运行项目，通过判断决定是否跳过SplashActivity，该值的生命周期即应用的生命周期
        var isFirstRun = true
    }

    override fun onCreate() {
        super.onCreate()
        AppConfig.instance.init(this)
    }
}