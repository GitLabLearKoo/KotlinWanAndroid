package app.itgungnir.kwa.common.util

import android.app.Application
import com.squareup.leakcanary.LeakCanary

class LeakDetectUtil private constructor() : Util {

    companion object {
        val instance by lazy { LeakDetectUtil() }
    }

    override fun init(application: Application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            return
        }
        LeakCanary.install(application)
    }
}