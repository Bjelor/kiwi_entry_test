package com.kiwi.flightoffers.dagger.module

import android.app.Application
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author Bjelis
 * @since 26. 1. 2018
 */
@Module
class OkHttpModule {

    companion object {
        private const val TIMEOUT_SECONDS : Long = 120
    }

    private fun getBaseBuilder(cache: Cache): OkHttpClient.Builder {
        return OkHttpClient.Builder()
                .cache(cache)
                .retryOnConnectionFailure(true)
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
    }

    private class CachingControlInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {

            val requestBuilder = chain.request().newBuilder()
            val cacheControl = CacheControl.Builder()
                    .maxStale(1, TimeUnit.MINUTES)
                    .maxAge(1, TimeUnit.MINUTES)
                    .build()

            requestBuilder.cacheControl(cacheControl)
            requestBuilder.header("Content-Type", "application/json")
            val request = requestBuilder.build()

            val originalResponse = chain.proceed(request)
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=604800")
                    .build()
        }
    }

    @Provides
    @Singleton
    fun providesOkHttpCache(pApplication: Application): Cache =
            Cache(pApplication.cacheDir, 10 * 1024 * 1024)

    @Provides
    @Singleton
    fun providesLogginInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Singleton
    fun providesOkHttp(cache: Cache, loggingInterceptor: HttpLoggingInterceptor) = getBaseBuilder(cache)
            .addNetworkInterceptor(CachingControlInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()
}