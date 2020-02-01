package app.itgungnir.kwa.common.http.dto

data class ScheduleListResponse(
    val curPage: Int,
    val datas: List<ScheduleResponse>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)