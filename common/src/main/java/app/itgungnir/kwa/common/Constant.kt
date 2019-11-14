package app.itgungnir.kwa.common

/**
 * 路由表
 */
// 闪屏页
const val SplashActivity = "splash"
// 主页
const val MainActivity = "main"
// 搜索结果页
const val SearchResultActivity = "searchResult"
// H5页面
const val WebActivity = "web"
// 知识体系页
const val HierarchyActivity = "hierarchy"
// 登录页
const val LoginActivity = "login"
// 注册页
const val RegisterActivity = "register"
// 设置页
const val SettingActivity = "setting"
// 待办列表页
const val ScheduleActivity = "schedule"
// 待办列表-已完成列表页
const val ScheduleDoneActivity = "scheduleDone"

/**
 * HTTP相关
 */
// BaseUrl，用于请求WanAndroid相关的接口
const val HTTP_BASE_URL = "https://www.wanandroid.com"
// BaseUrl，用于请求检查版本更新接口
const val HTTP_VERSION_URL = "https://raw.githubusercontent.com"
// 网络请求超时时间
const val HTTP_TIME_OUT = 5L
// 网络请求中产生的日志的TAG
const val HTTP_LOG_TAG = "kotlin-wan-android"
// 自动缓存的大小
const val MAX_CACHE_SIZE = 1024 * 1024 * 50L
