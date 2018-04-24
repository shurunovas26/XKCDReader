package io.github.shurunov.xkcd

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlin.coroutines.experimental.CoroutineContext

/**
    Запросы на сохранение и удаление комиксов
 */

fun saveComic(
        app: App,
        comic: Comic,
        coroutineContext: CoroutineContext = CommonPool
): Deferred<Unit> = async(coroutineContext) {
    app.database.comicsDao().insert(comic)
}

fun deleteComics(
        app: App,
        coroutineContext: CoroutineContext = CommonPool
): Deferred<Unit> = async(coroutineContext) {
    app.database.comicsDao().deleteAll()
}

fun deleteComic(
        app: App,
        comic: Comic,
        coroutineContext: CoroutineContext = CommonPool
): Deferred<Unit> = async(coroutineContext) {
    app.database.comicsDao().delete(comic)
}