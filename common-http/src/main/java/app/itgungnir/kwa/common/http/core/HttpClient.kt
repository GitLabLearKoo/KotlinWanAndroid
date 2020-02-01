package app.itgungnir.kwa.common.http.core

/**
 * Description:
 *
 * Created by ITGungnir on 2020-01-29
 */
import android.util.Log
import app.itgungnir.kwa.common.http.BuildConfig
import app.itgungnir.kwa.common.http.api.VersionApi
import app.itgungnir.kwa.common.util.bus.AppBus
import app.itgungnir.kwa.common.util.bus.LocalizeCookies
import app.itgungnir.kwa.common.util.config.CacheConfig
import com.orhanobut.logger.Logger
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class HttpClient private constructor() {

    companion object {

        private val instance by lazy { HttpClient() }
    }

    private fun buildApi(clz: Class<*>) = Retrofit.Builder()
        .baseUrl(if (clz == VersionApi::class.java) BuildConfig.BASE_VERSION_URL else BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient())
        .build()
        .create(clz)

    private fun okHttpClient() = OkHttpClient.Builder()
        .connectTimeout(BuildConfig.HTTP_TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(BuildConfig.HTTP_TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor())
        .addInterceptor(cookieInterceptor())
        .cache(
            if (AppBus.instance.isAutoCache()) Cache(CacheConfig.instance.cacheFile, BuildConfig.HTTP_CACHE_SIZE)
            else null
        )
        .build()

    private fun loggingInterceptor() =
        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                if (message.startsWith("{") && message.endsWith("}") ||
                    message.startsWith("[") && message.endsWith("]")
                ) {
                    Logger.json(message)
                } else {
                    Log.d(app.itgungnir.kwa.common.util.BuildConfig.LOG_TAG, message)
                }
            }
        }).apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    private fun cookieInterceptor() = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            // Request
            val requestBuilder = chain.request().newBuilder()
            AppBus.instance.currState().cookies.forEach {
                requestBuilder.addHeader("Cookie", it)
            }
            // Response
            val response = chain.proceed(requestBuilder.build())
            val cookieList = response.headers("Set-Cookie")
            if (cookieList.isNotEmpty() && cookieList.any { it.startsWith("token_pass_wanandroid_com") }) {
                val cookieSet = mutableSetOf<String>()
                cookieList.forEach {
                    cookieSet.add(it)
                }
                AppBus.instance.dispatch(LocalizeCookies(cookieSet))
            }
            return response
        }
    }
}
