@file:Suppress("UNREACHABLE_CODE")

package com.tthaohoang.mangogiphy.item

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.tthaohoang.mangogiphy.R
import com.tthaohoang.mangogiphy.model.Gif
import java.util.*

class GifItem(var gif: Gif) : AbstractItem<GifItem, GifItem.GifViewHolder>() {
    override fun getType(): Int {
        return R.id.urlImageView
    }

    override fun getViewHolder(v: View?): GifViewHolder {
        return GifViewHolder(v)
    }

    override fun getLayoutRes(): Int {
        return R.layout.row_gif
    }


    // recycler view

    class GifViewHolder(itemView: View?) : FastAdapter.ViewHolder<GifItem>(itemView) {

        private var gifImage: ImageView?

        override fun unbindView(item: GifItem?) {
            // Nettoyage de la cellule avant réutilisation
            gifImage?.setImageDrawable(null)
        }

        override fun bindView(item: GifItem?, payloads: MutableList<Any>?) {
            val gif = item?.gif
            if (gif is Gif) {

                val currentImageView = gifImage

                if (currentImageView is ImageView) {
                    Glide
                            .with(itemView.context)
                            .asGif()
                            .load(gif.urlImg)
                            .apply(RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .transforms(CenterCrop(), RoundedCorners(10))
//                                    .placeholder(R.drawable.ic_launcher_background)
                            )
                            .transition(DrawableTransitionOptions.withCrossFade( 300))
                           .into(currentImageView)
                }
            }
        }

        init {
            gifImage = itemView?.findViewById<ImageView>(R.id.urlImageView)
        }

    }
}