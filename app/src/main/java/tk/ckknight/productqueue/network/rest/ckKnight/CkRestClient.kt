package tk.ckknight.productqueue.network.rest.ckKnight

import android.text.TextUtils
import retrofit2.Retrofit
import tk.ckknight.productqueue.network.GeneralRestClient

/**
 * Created by sauyee on 13/1/18.
 */
class CkRestClient private constructor() : GeneralRestClient() {

    private val retrofit: Retrofit
    val `interface`: CkRestInterface

    init {
        val client = getOkHttpClient(0, 0, null)
        retrofit = getRetrofit(client)
        `interface` = retrofit.create(CkRestInterface::class.java)
    }

    object SingletonHolder {
        val INSTANCE = CkRestClient()
    }

    override fun getBaseUrl(): String {
        var domain = BASE_PRODUCTION_URL
        if (!TextUtils.isEmpty(API_VERSION)) {
            domain += "/${API_VERSION}/"
        }
        return domain
    }

    companion object {
        private val BASE_PRODUCTION_URL = "";

        private val API_VERSION = ""
        private val CACHE_SIZE = 10 * 1024 * 1024
        private val serverOption = ServerOption.TEST;

        val instance: CkRestClient
            get() = SingletonHolder.INSTANCE
    }
}
