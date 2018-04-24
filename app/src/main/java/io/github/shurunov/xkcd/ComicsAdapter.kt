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

    /**
     * @return количество элементов для отображения
     */
    override fun getItemCount() = comics.size

    /**
     * Создаёт [RecyclerView.ViewHolder] который хранит в себе [View] для переиспользования
     * @param parent ссылка на контейнер в котором будут хранится [View]
     * @param viewType тип создаваемого контента, определяется в методе [RecyclerView.Adapter.getItemViewType]
     * @return ViewHolder с PhotoView внутри
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {

        val textView = TextView(parent!!.context).apply {
            textSize = 26f
            textColor = Color.BLACK
            padding = dip(16)
        }

        // Создаём и возвращаем объект хранителя View
        val holder = ComicViewHolder(textView)
        return holder
    }

    /**
     * Выставляем данные в [View]
     * либо обновляем в случае если [View] уже был создан
     * это нужно обязательно иметь ввиду т.к. [RecyclerView.ViewHolder]
     * хранит в себе [View] в последнем её состоянии
     * @param holder объект который хранит в себе [View]
     * @param position позиция элемента в списке
     */
    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        // Получаем объект PhotoView из PhotoViewHolder
        val comicView = holder.view
        // Получем объект Photo из списка по позиции
        val comic = comics[position]
        // Устанавливаем данные с нашим представлением (View)
        comicView.text = (comic.num.toString() + ". " + comic.title)

        comicView.onClick {
            val context = it?.context
            val showComicIntent = Intent(context, ComicActivity::class.java)
            showComicIntent.putExtra("comic", comic)
            context?.startActivity(showComicIntent)
        }
    }
}