package app.itgungnir.kwa.common.util

import android.app.Application

class AppConfig private constructor() {

    companion object {
        val instance by lazy { AppConfig() }
    }

    fun init(application: Application) {
        listOf(
            CacheUtil.instance,
            CrashDetectUtil.instance,
            DateTimeUtil.instance,
            LeakDetectUtil.instance,
            LoggingUtil.instance,
            ReduxUtil.instance,
            ScreenAdaptUtil.instance,
            ThemeUtil.instance,
            RefreshUtil.instance
        ).map {
            it.init(application)
        }
    }
}