package app.itgungnir.kwa.common.util

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid

class DateTimeUtil private constructor() : Util {

    companion object {
        val instance by lazy { DateTimeUtil() }
    }

    override fun init(application: Application) {
        JodaTimeAndroid.init(application)
    }
}