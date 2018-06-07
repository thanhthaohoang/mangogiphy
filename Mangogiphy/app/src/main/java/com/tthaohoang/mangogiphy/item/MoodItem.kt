package com.tthaohoang.mangogiphy.item

import android.media.Image
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.tthaohoang.mangogiphy.R
import com.tthaohoang.mangogiphy.model.Mood

class MoodItem (var mood: Mood) : AbstractItem<MoodItem, MoodItem.MoodItemViewHolder>() {

    override fun getType(): Int {
        return R.id.labelMood
    }

    override fun getViewHolder(v: View?): MoodItem.MoodItemViewHolder {
        return MoodItem.MoodItemViewHolder(v)
    }

    override fun getLayoutRes(): Int {
        return R.layout.row_mood
    }

    fun getLabel(): String {
        return mood.label
    }

    class MoodItemViewHolder(itemView: View?) : FastAdapter.ViewHolder<MoodItem>(itemView) {
        private var labelMood: TextView?
        private var thumbMood: ImageView?

        override fun unbindView(item: MoodItem?) {
            labelMood?.text = null
            thumbMood?.setImageDrawable(null)

        }

        override fun bindView(item: MoodItem?, payloads: MutableList<Any>?) {
            val mood = item?.mood
            if (mood is Mood) {
                labelMood?.text = mood.label
                val currentThumbView =  thumbMood

                if (currentThumbView is ImageView) {
                    Glide
                            .with(itemView.context)
                            .load(mood.thumbnail)
                            .apply(RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .transforms(CenterCrop(), RoundedCorners(5))
                            )
                            .transition(DrawableTransitionOptions.withCrossFade( 300))
                            .into(currentThumbView)
                }
            }
        }

        init {
            labelMood = itemView?.findViewById<TextView>(R.id.labelMood)
            thumbMood = itemView?.findViewById<ImageView>(R.id.thumbMood)
        }
    }
}