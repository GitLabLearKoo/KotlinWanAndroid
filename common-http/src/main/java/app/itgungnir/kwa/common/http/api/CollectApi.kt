package app.itgungnir.kwa.common.http.api

import app.itgungnir.kwa.common.http.dto.ArticleListResponse
import app.itgungnir.kwa.common.http.dto.ArticleResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-29
 */
interface CollectApi {

    /**
     * 收藏：收藏列表
     */
    @GET("/lg/collect/list/{page}/json")
    fun mineCollections(
        @Path("page") page: Int
    ): Single<Result<ArticleListResponse>>

    /**
     * 收藏：站内收藏
     */
    @POST("/lg/collect/{id}/json")
    fun innerCollect(
        @Path("id") id: Int
    ): Single<Result<Any>>

    /**
     * 收藏：站外收藏
     */
    @POST("/lg/collect/add/json")
    fun outerCollect(
        @Query("title") title: String,
        @Query("author") author: String,
        @Query("link") link: String
    ): Single<Result<ArticleResponse>>

    /**
     * 收藏：取消站内收藏
     */
    @POST("/lg/uncollect_originId/{id}/json")
    fun innerDisCollect(
        @Path("id") id: Int
    ): Single<Result<Any>>

    /**
     * 收藏：取消站外收藏
     */
    @POST("/lg/uncollect/{id}/json")
    fun outerDisCollect(
        @Path("id") id: Int,
        @Query("originId") originId: Int
    ): Single<Result<Any>>
}
