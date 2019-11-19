package app.itgungnir.kwa.main

import my.itgungnir.rxmvvm.core.mvvm.State

/**
 * Description:
 *
 * Created by ITGungnir on 2019-11-13
 */
data class MainState(
    val versionInfo: VersionVO? = null,
    val error: Throwable? = null
) : State {

    data class TabItem(
        val title: String,
        val unselectedIcon: String,
        val selectedIcon: String
    )

    data class VersionVO(
        val upgradeUrl: String,
        val upgradeVersionCode: Int,
        val upgradeVersionName: String,
        val upgradeDesc: String
    )
}
