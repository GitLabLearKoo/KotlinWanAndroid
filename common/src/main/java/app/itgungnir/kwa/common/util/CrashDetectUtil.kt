package app.itgungnir.kwa.common.util

import android.app.Application
import com.tencent.bugly.crashreport.CrashReport

class CrashDetectUtil private constructor() : Util {

    companion object {
        val instance by lazy { CrashDetectUtil() }
    }

    override fun init(application: Application) {

        CrashReport.initCrashReport(application)
    }
}