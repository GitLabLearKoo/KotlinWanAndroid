package app.itgungnir.kwa.common.http.core

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-29
 */
data class Result<T>(
    val data: T,
    val errorCode: Int,
    val errorMsg: String
)
