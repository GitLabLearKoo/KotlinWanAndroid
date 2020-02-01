package app.itgungnir.kwa.common.http.api

import app.itgungnir.kwa.common.http.dto.ArticleListResponse
import app.itgungnir.kwa.common.http.dto.TabResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-29
 */
interface ProjectApi {

    /**
     * 项目：分类
     */
    @GET("/project/tree/json")
    fun projectTabs(): Single<Result<List<TabResponse>>>

    /**
     * 项目：文章列表
     */
    @GET("/project/list/{page}/json")
    fun projectArticles(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): Single<Result<ArticleListResponse>>
}
