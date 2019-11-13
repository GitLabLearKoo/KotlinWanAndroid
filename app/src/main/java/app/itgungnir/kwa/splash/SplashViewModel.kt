package app.itgungnir.kwa.splash

import android.annotation.SuppressLint
import app.itgungnir.kwa.common.http.HttpClient
import app.itgungnir.kwa.common.http.handleResult
import app.itgungnir.kwa.common.http.io2Main
import my.itgungnir.rxmvvm.core.mvvm.BaseViewModel

/**
 * Description:
 *
 * Created by ITGungnir on 2019-11-13
 */
class SplashViewModel : BaseViewModel<SplashState>(initialState = SplashState()) {

    @SuppressLint("CheckResult")
    fun checkForUpdates() {
        HttpClient.api2.versionInfo()
            .handleResult()
            .io2Main()
            .map {
                SplashState.VersionVO(
                    upgradeUrl = it.downloadUrl,
                    upgradeVersionCode = it.versionCode,
                    upgradeVersionName = it.versionName,
                    upgradeDesc = it.versionDesc
                )
            }
            .subscribe({
                setState {
                    copy(
                        versionInfo = it,
                        error = null
                    )
                }
            }, {
                setState {
                    copy(
                        versionInfo = null,
                        error = it
                    )
                }
            })
    }
}