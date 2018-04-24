package io.github.shurunov.xkcd

import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.dip
import org.jetbrains.anko.padding
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.textColor

/**
 * Адаптер для [RecyclerView]
 * Преобразует список объектов [Comic], [Comic.List]
 * в наследников [View] для отрисовки
 */
class ComicsAdapter(
    private val comics: Comic.List
) : RecyclerView.Adapter<ComicViewHolder>() {

    override fun getItemCount() = comics.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {

        val textView = TextView(parent!!.context).apply {
            textSize = 26f
            textColor = Color.BLACK
            padding = dip(16)
        }

        val holder = ComicViewHolder(textView)
        return holder
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        val comicView = holder.view
        val comic = comics[position]
        comicView.text = (comic.num.toString() + ". " + comic.title)

        comicView.onClick {
            val context = it?.context
            val showComicIntent = Intent(context, ComicActivity::class.java)
            showComicIntent.putExtra("comic", comic)
            context?.startActivity(showComicIntent)
        }
    }
}