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
interface WXArticleApi {

    /**
     * 公众号：分类
     */
    @GET("/wxarticle/chapters/json")
    fun weixinTabs(): Single<Result<List<TabResponse>>>

    /**
     * 公众号：文章列表
     */
    @GET("/wxarticle/list/{cid}/{page}/json")
    fun weixinArticles(
        @Path("page") page: Int,
        @Path("cid") cid: Int,
        @Query("k") k: String
    ): Single<Result<ArticleListResponse>>
}
