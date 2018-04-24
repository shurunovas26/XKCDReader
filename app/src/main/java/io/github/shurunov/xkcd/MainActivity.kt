package io.github.shurunov.xkcd

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Gravity
import android.widget.LinearLayout
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.padding

/**
 * Компонент для визуального взаимодействия с пользователем
 * отображает наши View на экране
 */
class MainActivity : AppCompatActivity() {

    var nextComicNum : Int? = null

    /**
     * Стартовый медот Activity
     * с него начинается жизненный цикл компонента
     * https://developer.android.com/guide/components/activities/activity-lifecycle.html
     */

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val comics = Comic.List()

        coordinatorLayout{
            val comicsView = RecyclerView(this.context).apply {
                // Устанавливаем LayoutManager
                // он отвечает за расположение View на экране
                layoutManager = LinearLayoutManager(this.context)
                addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
                // Устанавливание RecyclerView.Adapter
                // он преобразует список во View
                // для отображения внутри RecyclerView
                adapter = ComicsAdapter(comics)
            }

            // add new comic
            floatingActionButton {
                imageResource = R.drawable.ic_add_black_24dp
                backgroundTintList  = ColorStateList.valueOf(Color.GREEN)
                setOnClickListener {
                    launch(UI) {
                        nextComicNum?.let {
                            if (nextComicNum != 0) {
                                val cloudComicsJob = loadingComicsFromCloud(nextComicNum)
                                cloudComicsJob.start()
                                val cloudComic = cloudComicsJob.await()
                                saveComic(application as App, cloudComic)
                                comics.add(cloudComic)
                                comicsView.adapter.notifyDataSetChanged()
                                nextComicNum = nextComicNum!! - 1
                            }
                        } ?: run {
                            val cloudComicsJob = loadingComicsFromCloud()
                            cloudComicsJob.start()
                            val cloudComic = cloudComicsJob.await()
                            saveComic(application as App, cloudComic)
                            comics.add(cloudComic)
                            comicsView.adapter.notifyDataSetChanged()
                            nextComicNum =  cloudComic.num - 1
                        }
                    }
                }
            }.lparams {
                gravity = Gravity.BOTTOM or Gravity.END
                padding = 20
            }

            // clear comics
            floatingActionButton {
                imageResource = R.drawable.ic_clear_black_24dp
                backgroundTintList  = ColorStateList.valueOf(Color.RED)
                setOnClickListener {
                    launch(UI) {
                        comics.clear()
                        comicsView.adapter.notifyDataSetChanged()
                        deleteComics(application as App)
                        val cloudComicsJob = loadingLastComicNumberFromCloud()
                        cloudComicsJob.start()
                        nextComicNum = cloudComicsJob.await()
                    }
                }
            }.lparams {
                gravity = Gravity.BOTTOM or Gravity.START
                padding = 20
            }

            addView(comicsView)

            val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

                override fun onMove(
                        recyclerView: RecyclerView?,
                        viewHolder: RecyclerView.ViewHolder?,
                        target: RecyclerView.ViewHolder?
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                    val comicToDelete = comics[viewHolder!!.adapterPosition]
                    comics.remove(comicToDelete)
                    comicsView.adapter.notifyDataSetChanged()
                    deleteComic(application as App, comicToDelete)
                }

                override fun isItemViewSwipeEnabled(): Boolean {
                    return true
                }})

            itemTouchHelper.attachToRecyclerView(comicsView)

            launch(UI) {
                val cachedComics = loadingComicsFromCache(application as App).await()
                if (cachedComics.isNotEmpty()) {
                    comics.addAll(cachedComics)
                    comicsView.adapter.notifyDataSetChanged()
                    nextComicNum = cachedComics[cachedComics.size - 1].num - 1
                } else {
                    val cloudComicsJob = loadingLastComicNumberFromCloud()
                    cloudComicsJob.start()
                    nextComicNum = cloudComicsJob.await()
                }
            }
        }
    }
}