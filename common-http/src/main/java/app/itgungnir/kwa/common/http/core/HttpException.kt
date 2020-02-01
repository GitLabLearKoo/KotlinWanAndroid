package app.itgungnir.kwa.common.http.core

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-29
 */
class HttpException(errorMsg: String) : Throwable(message = errorMsg)
