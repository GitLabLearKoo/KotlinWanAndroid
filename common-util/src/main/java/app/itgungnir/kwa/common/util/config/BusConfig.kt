package app.itgungnir.kwa.common.util.config

import android.app.Application
import app.itgungnir.kwa.common.util.Config
import app.itgungnir.kwa.common.util.bus.AppBus

class BusConfig private constructor() : Config {

    companion object {
        val instance by lazy { BusConfig() }
    }

    override fun init(application: Application) {
        AppBus.init(application)
    }
}