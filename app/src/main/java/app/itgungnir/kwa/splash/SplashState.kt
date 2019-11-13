package app.itgungnir.kwa.splash

import my.itgungnir.rxmvvm.core.mvvm.State

/**
 * Description:
 *
 * Created by ITGungnir on 2019-11-13
 */
data class SplashState(
    val versionInfo: VersionVO? = null,
    val error: Throwable? = null
) : State {

    data class VersionVO(
        val upgradeUrl: String,
        val upgradeVersionCode: Int,
        val upgradeVersionName: String,
        val upgradeDesc: String
    )
}