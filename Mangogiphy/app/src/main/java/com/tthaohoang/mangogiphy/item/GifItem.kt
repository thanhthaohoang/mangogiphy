@file:Suppress("UNREACHABLE_CODE")

package com.tthaohoang.mangogiphy.item

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.tthaohoang.mangogiphy.R
import com.tthaohoang.mangogiphy.model.Gif

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

    class GifViewHolder(view: View?) : FastAdapter.ViewHolder<GifItem>(view) {

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
                            .with(currentImageView)
                            .load(gif.urlImg)
                            .into(currentImageView)
                }
            }
        }

        init {
            gifImage = view?.findViewById<ImageView>(R.id.urlImageView)
        }

    }
}