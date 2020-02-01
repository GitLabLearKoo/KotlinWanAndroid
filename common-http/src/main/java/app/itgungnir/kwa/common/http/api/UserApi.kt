package app.itgungnir.kwa.common.http.api

import app.itgungnir.kwa.common.http.dto.LoginResponse
import io.reactivex.Single
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-29
 */
interface UserApi {

    /**
     * 我的：登录
     */
    @POST("/user/login")
    fun login(
        @Query("username") userName: String,
        @Query("password") password: String
    ): Single<Result<LoginResponse>>

    /**
     * 我的：注册
     */
    @POST("/user/register")
    fun register(
        @Query("username") userName: String,
        @Query("password") password: String,
        @Query("repassword") confirmPwd: String
    ): Single<Result<Any>>
}
