package app.itgungnir.kwa.common.util.config

import android.app.Application
import app.itgungnir.kwa.common.util.Config
import net.danlew.android.joda.JodaTimeAndroid

class DateTimeConfig private constructor() : Config {

    companion object {
        val instance by lazy { DateTimeConfig() }
    }

    override fun init(application: Application) {
        JodaTimeAndroid.init(application)
    }
}