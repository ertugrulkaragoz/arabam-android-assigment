package com.arabam.android.assigment.ui.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import com.arabam.android.assigment.R
import com.arabam.android.assigment.util.downloadImage
import com.stfalcon.imageviewer.StfalconImageViewer
import com.stfalcon.imageviewer.loader.ImageLoader

class ImageSlideViewPagerAdapter(
    private val context: Context,
    private val photos: List<String>
) : PagerAdapter() {

    private lateinit var inflater: LayoutInflater

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_image_slider, container, false)
        val image = view.findViewById<ImageView>(R.id.custom_slider_image_view)

        image.setOnClickListener {
            StfalconImageViewer.Builder(
                context,
                photos,
                ImageLoader<String> { imageView: ImageView, image: String ->
                    imageView.downloadImage(
                        image.replace("{0}", "1920x1080"),
                        image.replace("{0}", "800x600")
                    )
                })
                .withStartPosition(position)
                .withHiddenStatusBar(true)
                .show(true)
        }

        image.downloadImage(
            photos[position].replace("{0}", "1920x1080"),
            photos[position].replace("{0}", "800x600")
        )

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean =
        view == `object` as RelativeLayout

    override fun getCount() = photos.size

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }
}