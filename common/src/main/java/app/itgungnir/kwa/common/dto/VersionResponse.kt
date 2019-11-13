package app.itgungnir.kwa.common.dto

data class VersionResponse(
    val downloadUrl: String,
    val versionCode: Int,
    val versionName: String,
    val versionDesc: String
)