package com.arabam.android.assigment.util

import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import com.arabam.android.assigment.R
import com.arabam.android.assigment.data.model.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

fun ImageView.downloadImage(
    url1: String,
    url2: String
) {
    Glide.with(this)
        .load(url1)
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(R.drawable.ic_error)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                Handler(Looper.getMainLooper()).post {
                    Glide.with(this@downloadImage)
                        .load(url2)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .fitCenter()
                        .error(R.drawable.ic_error)
                        .into(this@downloadImage)
                }
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }
        })
        .into(this)
}

fun Car.toEntity(): CarEntity {
    return CarEntity(
        id,
        title,
        location.cityName,
        location.townName,
        category.id,
        category.name,
        priceFormatted,
        photo
    )
}

fun CarDetail.toEntity(): CarDetailEntity {
    return CarDetailEntity(
        id,
        title,
        location.cityName,
        location.townName,
        category.id,
        category.name,
        modelName,
        priceFormatted,
        dateFormatted,
        photos,
        properties,
        text,
        userInfo.id,
        userInfo.nameSurname,
        userInfo.phoneFormatted,
        userInfo.phone,
        System.currentTimeMillis()
    )
}

fun CarDetailEntity.toUserInfo(): UserInfo {
    return UserInfo(userId,nameSurname,phoneFormatted,phone)
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.dismiss() {
    this.visibility = View.GONE
}