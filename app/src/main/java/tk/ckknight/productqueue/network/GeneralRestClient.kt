package tk.ckknight.productqueue.network

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import tk.ckknight.productqueue.ProductQueue
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by sauyee on 13/1/18.
 */
abstract class GeneralRestClient {

    fun getRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build()
    }

    fun getOkHttpClient(connectTimeoutSec: Long, readTimeoutSec: Long, userAgent: String?): OkHttpClient {
        val builder = OkHttpClient.Builder()

        builder.addInterceptor { chain ->
            val original = chain.request()

            // Request customization: add request headers
            val requestBuilder = original.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json,text/html")
                    .method(original.method(), original.body())

//            userAgent?.isNotEmpty().let {
//                requestBuilder.addHeader("User-Agent", userAgent)
//            }
            val request = requestBuilder.build()
            chain.proceed(request)
        }

        builder.addNetworkInterceptor(StethoInterceptor())

        val cacheFilePath = ProductQueue.getInstance().cacheDir
        if (cacheFilePath != null) {
            builder.cache(Cache(File(cacheFilePath.toString()), CACHE_SIZE.toLong()))
        }

        if (connectTimeoutSec > 0) {
            builder.connectTimeout(connectTimeoutSec, TimeUnit.SECONDS)
        }
        if (readTimeoutSec > 0) {
            builder.readTimeout(readTimeoutSec, TimeUnit.SECONDS)
        }
        return builder.build()
    }

    abstract fun getBaseUrl(): String

    enum class ServerOption {
        PRODUCTION, STAGING, NIGHTLY, TEST
    }

    companion object {
        private val CACHE_SIZE = 10 * 1024 * 1024
    }
}