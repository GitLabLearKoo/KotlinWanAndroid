package app.itgungnir.kwa.common.http

import android.util.Log
import app.itgungnir.kwa.common.*
import app.itgungnir.kwa.common.redux.AppRedux
import app.itgungnir.kwa.common.redux.LocalizeCookies
import app.itgungnir.kwa.common.util.CacheUtil
import com.orhanobut.logger.Logger
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class HttpClient private constructor() {

    companion object {

        private val instance by lazy { HttpClient() }

        val api: HttpApi by lazy { HttpClient.instance.buildApi() }

        val api2: HttpApi by lazy { HttpClient.instance.buildApi2() }
    }

    private fun buildApi() = Retrofit.Builder()
        .baseUrl(HTTP_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient())
        .build()
        .create(HttpApi::class.java)

    private fun buildApi2() = Retrofit.Builder()
        .baseUrl(HTTP_VERSION_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient())
        .build()
        .create(HttpApi::class.java)

    private fun okHttpClient() = OkHttpClient.Builder()
        .connectTimeout(HTTP_TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(HTTP_TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor())
        .addInterceptor(cookieInterceptor())
        .cache(if (AppRedux.instance.isAutoCache()) Cache(CacheUtil.instance.cacheFile, MAX_CACHE_SIZE) else null)
        .build()

    private fun loggingInterceptor() =
        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            if (message.startsWith("{") && message.endsWith("}") ||
                message.startsWith("[") && message.endsWith("]")
            ) {
                Logger.json(message)
            } else {
                Log.d(HTTP_LOG_TAG, message)
            }
        }).apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    private fun cookieInterceptor() = Interceptor { chain: Interceptor.Chain ->
        // Request
        val requestBuilder = chain.request().newBuilder()
        AppRedux.instance.currState().cookies.forEach {
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
            AppRedux.instance.dispatch(LocalizeCookies(cookieSet))
        }
        response
    }
}