package io.github.shurunov.xkcd

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.github.chrisbanes.photoview.PhotoView
import org.jetbrains.anko.*

class ComicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val requestOptions = RequestOptions()
        requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA)

        val intentThatStartedThisActivity = intent

        if (intentThatStartedThisActivity.hasExtra("comic")) {
            val comic: Comic = intentThatStartedThisActivity.getParcelableExtra("comic")

            val photoView = PhotoView (this)
            Glide.with(this).load(comic.img).apply(requestOptions).into(photoView)

            relativeLayout {
            }.addView(photoView)

            photoView.adjustViewBounds = true
            photoView.layoutParams.height = MATCH_PARENT
            photoView.layoutParams.width = MATCH_PARENT
        }
    }
}

