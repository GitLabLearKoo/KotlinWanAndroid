package app.itgungnir.kwa.common.util

import android.app.Application
import app.itgungnir.kwa.common.redux.AppRedux

class ReduxUtil private constructor() : Util {

    companion object {
        val instance by lazy { ReduxUtil() }
    }

    override fun init(application: Application) {
        AppRedux.init(application)
    }
}