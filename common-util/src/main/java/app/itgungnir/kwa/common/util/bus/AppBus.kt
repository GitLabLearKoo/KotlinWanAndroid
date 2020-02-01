package app.itgungnir.kwa.common.util.bus

import android.app.Application
import com.google.gson.Gson
import my.itgungnir.rxmvvm.core.redux.BaseRedux

class AppBus(context: Application) : BaseRedux<AppState>(
    context = context,
    initialState = AppState(),
    reducer = AppReducer()
) {

    companion object {

        lateinit var instance: AppBus

        fun init(context: Application) {
            instance = AppBus(context)
        }
    }

    override fun deserializeToCurrState(json: String): AppState? =
        Gson().fromJson(json, AppState::class.java)

    // 判断用户是否已登录
    fun isUserIn() = !currState().userName.isNullOrBlank()

    // 判断用户是否已收藏了某篇文章
    fun isCollected(articleId: Int) = currState().collectIds.contains(articleId)

    // 判断当前是否自动缓存
    fun isAutoCache() = currState().autoCache

    // 判断当前是否处于无图模式
    fun isNoImage() = currState().noImage
}