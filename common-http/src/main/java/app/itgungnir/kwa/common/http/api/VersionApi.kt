package app.itgungnir.kwa.common.http.api

import app.itgungnir.kwa.common.http.dto.VersionResponse
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-29
 */
interface VersionApi {

    /**
     * 版本检查
     */
    @GET("/ITGungnir/KotlinWanAndroid/dev/version.json")
    fun versionInfo(): Single<Result<VersionResponse>>
}
