package app.itgungnir.kwa.common.util.config

import android.app.Application
import app.itgungnir.kwa.common.util.Config
import com.tencent.bugly.crashreport.CrashReport

class CrashDetectConfig private constructor() : Config {

    companion object {
        val instance by lazy { CrashDetectConfig() }
    }

    override fun init(application: Application) {

        CrashReport.initCrashReport(application)
    }
}