package app.itgungnir.kwa.common.util.config

import android.app.Application
import app.itgungnir.kwa.common.util.BuildConfig
import app.itgungnir.kwa.common.util.Config
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

class LoggingConfig private constructor() : Config {

    companion object {
        val instance by lazy { LoggingConfig() }
    }

    override fun init(application: Application) {

        val logStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)
            .methodOffset(2)
            .tag(BuildConfig.LOG_TAG)
            .build()

        Logger.addLogAdapter(object : AndroidLogAdapter(logStrategy) {
            override fun isLoggable(priority: Int, tag: String?) = BuildConfig.DEBUG
        })
    }
}