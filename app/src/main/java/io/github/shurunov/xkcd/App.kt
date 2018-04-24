package io.github.shurunov.xkcd

import android.app.Application
import android.arch.persistence.room.Room

class App : Application() {

    lateinit var database: ComicsDatabase

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, ComicsDatabase::class.java, "comics").build()
    }
}