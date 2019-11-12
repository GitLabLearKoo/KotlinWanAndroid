package app.itgungnir.kwa.common.util

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import app.itgungnir.kwa.common.redux.AppRedux

class ThemeUtil private constructor() : Util {

    companion object {
        val instance by lazy { ThemeUtil() }
    }

    override fun init(application: Application) {

        when (AppRedux.instance.isDarkMode()) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}