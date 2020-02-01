package app.itgungnir.kwa.common.util

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-29
 */
import android.app.Application
import app.itgungnir.kwa.common.util.config.*

class AppConfig private constructor() {

    companion object {
        val instance by lazy { AppConfig() }
    }

    fun init(application: Application) {
        listOf(
            BusConfig.instance,
            CacheConfig.instance,
            CrashDetectConfig.instance,
            DateTimeConfig.instance,
            LoggingConfig.instance,
            ScreenAdaptConfig.instance
        ).map {
            it.init(application)
        }
    }
}
