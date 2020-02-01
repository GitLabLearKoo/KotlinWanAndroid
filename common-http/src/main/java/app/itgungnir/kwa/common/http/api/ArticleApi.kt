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
interface ArticleApi {

    /**
     * 首页：置顶列表
     */
    @GET("/article/top/json")
    fun topArticles(): Single<Result<List<ArticleResponse>>>

    /**
     * 首页：文章列表
     */
    @GET("/article/list/{page}/json")
    fun homeArticles(
        @Path("page") page: Int
    ): Single<Result<ArticleListResponse>>

    /**
     * 搜索：结果列表
     */
    @POST("/article/query/{page}/json")
    fun searchResult(
        @Path("page") page: Int, @Query("k") k: String
    ): Single<Result<ArticleListResponse>>

    /**
     * 知识体系：文章列表
     */
    @GET("/article/list/{page}/json")
    fun hierarchyArticles(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): Single<Result<ArticleListResponse>>
}
