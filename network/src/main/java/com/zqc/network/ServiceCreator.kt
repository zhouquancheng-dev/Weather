package com.zqc.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.zqc.mdoel.weather.appcode
import com.zqc.mdoel.weather.authorization
import okhttp3.Headers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ServiceCreator {
    private const val CAI_YUN_URL = "https://api.caiyunapp.com"
    private const val MO_JI_URL = "https://aliv8.mojicb.com"
    private const val CONNECT_TIMEOUT = 30L // 连接超时时间
    private const val READ_TIMEOUT = 10L    // 读取超时时间

    /**
     * get ServiceApi
     * @param serviceClass Service接口所对应的Class类型
     */
    fun <T> createCaiYun(serviceClass: Class<T>): T = createCaiYun().create(serviceClass)
    fun <T> createMoJi(serviceClass: Class<T>): T = createMoJi().create(serviceClass)

    /**
     * reified get ServiceApi
     * @sample createCaiYunApi
     */
    inline fun <reified T> createCaiYunApi(): T = createCaiYun(T::class.java)
    inline fun <reified T> createMoJiApi(): T = createMoJi(T::class.java)

    private fun createCaiYun(): Retrofit {
        // 创建 okHttpClientBuilder
        val mOkHttpClient = OkHttpClient().newBuilder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        }
        return RetrofitBuild(
            url = CAI_YUN_URL,
            client = mOkHttpClient.build(),
            moshiConverterFactory = MoshiConverterFactory.create(moshi)
        ).retrofitCaiYun
    }

    private fun createMoJi(): Retrofit {
        val mOkHttpClient = OkHttpClient().newBuilder().apply {
            addInterceptor { chain ->
                val request = chain.request()
                val builder = request
                    .newBuilder()
                    .headers(Headers.of(getMoJiHeaders()))
                chain.proceed(builder.build())
            }
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        }
        return RetrofitBuild(
            url = MO_JI_URL,
            client = mOkHttpClient.build(),
            moshiConverterFactory = MoshiConverterFactory.create(moshi)
        ).retrofitMoJi
    }
}

private class RetrofitBuild(
    url: String,
    client: OkHttpClient,
    moshiConverterFactory: MoshiConverterFactory
) {
    val retrofitCaiYun: Retrofit = Retrofit.Builder().apply {
        baseUrl(url)
        client(client)
        addConverterFactory(moshiConverterFactory)
    }.build()

    val retrofitMoJi: Retrofit = Retrofit.Builder().apply {
        baseUrl(url)
        client(client)
        addConverterFactory(moshiConverterFactory)
    }.build()
}

/**
 * 墨迹天气接口全局请求头
 */
private fun getMoJiHeaders(): Map<String, String> {
    return mapOf(
       "appcode" to appcode,
       "Authorization" to authorization,
       "Content-Type" to "application/x-www-form-urlencoded; charset=utf-8"
   )
}

// 添加 KotlinJsonAdapterFactory以支持 Kotlin 数据类的解析
// 使用 Moshi 解析 Kotlin 数据类需要添加 KotlinJsonAdapterFactory() 以支持数据类中定义的泛型
private val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
