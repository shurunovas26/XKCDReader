package io.github.shurunov.xkcd

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface ComicsDao {

    @Query("SELECT * FROM Comic ORDER BY num DESC")
    fun getAll(): List<Comic>

    @Insert
    fun insert(comic: Comic)

    @Query("DELETE FROM Comic")
    fun deleteAll()

    @Delete
    fun delete(comic: Comic)
}