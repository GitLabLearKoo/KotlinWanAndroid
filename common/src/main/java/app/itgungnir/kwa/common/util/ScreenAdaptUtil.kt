package app.itgungnir.kwa.common.util

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.os.Bundle
import app.itgungnir.kwa.common.BuildConfig

class ScreenAdaptUtil private constructor() : Util {

    companion object {
        val instance by lazy { ScreenAdaptUtil() }

        var sNonCompatDensity: Float = 0F
        var sNonCompatScaledDensity: Float = 0F
    }

    override fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(AppActivityLifecycleCallback(application))
    }

    inner class AppActivityLifecycleCallback(private val application: Application) :
        Application.ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            activity?.let { act ->
                val appDisplayMetrics = application.resources.displayMetrics

                if (sNonCompatDensity == 0F) {
                    sNonCompatDensity = appDisplayMetrics.density
                    sNonCompatScaledDensity = appDisplayMetrics.scaledDensity
                    application.registerComponentCallbacks(object : ComponentCallbacks {
                        override fun onConfigurationChanged(newConfig: Configuration?) {
                            newConfig?.let {
                                if (it.fontScale > 0) {
                                    sNonCompatScaledDensity = application.resources.displayMetrics.scaledDensity
                                }
                            }
                        }

                        override fun onLowMemory() {
                        }
                    })
                }

                val targetDensity = appDisplayMetrics.widthPixels / BuildConfig.SCREEN_WIDTH
                val targetScaledDensity = when (BuildConfig.USE_SYSTEM_SP) {
                    true -> targetDensity * (sNonCompatScaledDensity / sNonCompatDensity)
                    else -> targetDensity
                }
                val targetDensityDpi = (160 * targetDensity).toInt()

                appDisplayMetrics.density = targetDensity
                appDisplayMetrics.scaledDensity = targetScaledDensity
                appDisplayMetrics.densityDpi = targetDensityDpi

                val activityDisplayMetrics = act.resources.displayMetrics
                activityDisplayMetrics.density = targetDensity
                activityDisplayMetrics.scaledDensity = targetScaledDensity
                activityDisplayMetrics.densityDpi = targetDensityDpi
            }
        }

        override fun onActivityStarted(activity: Activity?) {}

        override fun onActivityResumed(activity: Activity?) {}

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

        override fun onActivityPaused(activity: Activity?) {}

        override fun onActivityStopped(activity: Activity?) {}

        override fun onActivityDestroyed(activity: Activity?) {}
    }
}