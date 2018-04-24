package io.github.shurunov.xkcd

import com.google.gson.Gson
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.coroutines.experimental.CoroutineContext

/**
    Загрузка комиксов с сервера и из кэша
 */

fun loadingComicsFromCloud(
    coroutineContext: CoroutineContext = CommonPool
): Deferred<Comic> = async(coroutineContext) {
    // Создать клиент для HTTP запросов
    val httpClient = OkHttpClient()

    // Создать запрос
    val request = Request.Builder()
        .url("http://xkcd.com/info.0.json")
        .build()

    httpClient.newCall(request).execute().use {
        Gson().fromJson(it.body()!!.string(), Comic::class.java)
    }
}

fun loadingLastComicNumberFromCloud(
        coroutineContext: CoroutineContext = CommonPool
): Deferred<Int?> = async(coroutineContext) {
    // Создать клиент для HTTP запросов
    val httpClient = OkHttpClient()

    // Создать запрос
    val request = Request.Builder()
            .url("http://xkcd.com/info.0.json")
            .build()

    httpClient.newCall(request).execute().use {
        Gson().fromJson(it.body()!!.string(), Comic::class.java).num
    }
}

fun loadingComicsFromCloud(
        num : Int?,
        coroutineContext: CoroutineContext = CommonPool
): Deferred<Comic> = async(coroutineContext) {
    // Создать клиент для HTTP запросов
    val httpClient = OkHttpClient()

    // Создать запрос
    val request = Request.Builder()
            .url("http://xkcd.com/" + num.toString() + "/info.0.json")
            .build()

    httpClient.newCall(request).execute().use {
        Gson().fromJson(it.body()!!.string(), Comic::class.java)
    }
}

fun loadingComicsFromCache(
        app: App,
        coroutineContext: CoroutineContext = CommonPool
): Deferred<List<Comic>> = async(coroutineContext) {
    app.database.comicsDao().getAll()
}