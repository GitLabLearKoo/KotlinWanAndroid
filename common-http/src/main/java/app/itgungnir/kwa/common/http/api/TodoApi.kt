package app.itgungnir.kwa.common.http.api

import app.itgungnir.kwa.common.http.dto.ScheduleListResponse
import app.itgungnir.kwa.common.http.dto.ScheduleResponse
import io.reactivex.Single
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-29
 */
interface TodoApi {

    /**
     * 日程：日程列表
     */
    @POST("/lg/todo/v2/list/{page}/json")
    fun scheduleList(
        @Path("page") page: Int,
        @Query("status") status: Int,
        @Query("type") type: Int?,
        @Query("priority") priority: Int?,
        @Query("orderby") orderBy: Int
    ): Single<Result<ScheduleListResponse>>

    /**
     * 日程：添加日程
     */
    @POST("/lg/todo/add/json")
    fun addSchedule(
        @Query("title") title: String,
        @Query("content") content: String,
        @Query("date") date: String,
        @Query("type") type: Int,
        @Query("priority") priority: Int
    ): Single<Result<ScheduleResponse>>

    /**
     * 日程：编辑日程
     */
    @POST("/lg/todo/update/{id}/json")
    fun editSchedule(
        @Path("id") id: Int,
        @Query("title") title: String,
        @Query("content") content: String,
        @Query("date") date: String,
        @Query("status") status: Int,
        @Query("type") type: Int,
        @Query("priority") priority: Int
    ): Single<Result<ScheduleResponse>>

    /**
     * 日程：完成日程
     */
    @POST("/lg/todo/done/{id}/json")
    fun finishSchedule(
        @Path("id") id: Int,
        @Query("status") status: Int
    ): Single<Result<ScheduleResponse>>

    /**
     * 日程：删除日程
     */
    @POST("/lg/todo/delete/{id}/json")
    fun deleteSchedule(
        @Path("id") id: Int
    ): Single<Result<Any>>
}
