package io.github.shurunov.xkcd

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

/**
 * Класс для хранения ссылки на [View]
 * нужен для того что-бы [RecyclerView]
 * мог переиспользвать [View]
 * для эфективного использования памяти
 * @param view Публичная ссыла на [TextView] для обновления данных
 */
class ComicViewHolder(
    val view: TextView
) : RecyclerView.ViewHolder(view) {

}