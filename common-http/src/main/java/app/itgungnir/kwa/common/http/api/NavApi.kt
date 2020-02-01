package app.itgungnir.kwa.common.http.api

import app.itgungnir.kwa.common.http.dto.BannerResponse
import app.itgungnir.kwa.common.http.dto.NavigationResponse
import app.itgungnir.kwa.common.http.dto.TabResponse
import app.itgungnir.kwa.common.http.dto.TagResponse
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-29
 */
interface NavApi {

    /**
     * 首页：banner
     */
    @GET("/banner/json")
    fun banners(): Single<Result<List<BannerResponse>>>

    /**
     * 搜索：热词列表
     */
    @GET("/hotkey/json")
    fun hotKeys(): Single<Result<List<TagResponse>>>

    /**
     * 知识体系：分类
     */
    @GET("/tree/json")
    fun hierarchyTabs(): Single<Result<List<TabResponse>>>

    /**
     * 常用网站
     */
    @GET("/friend/json")
    fun tools(): Single<Result<List<TagResponse>>>

    /**
     * 导航
     */
    @GET("/navi/json")
    fun navigation(): Single<Result<List<NavigationResponse>>>
}
