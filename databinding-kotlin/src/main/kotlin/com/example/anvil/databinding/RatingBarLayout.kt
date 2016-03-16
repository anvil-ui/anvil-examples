package com.example.anvil.databinding

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.RatingBar

import trikita.anvil.DSL.*
import trikita.anvil.RenderableView

class RatingBarLayout(c: Context) : RenderableView(c) {
    internal var value: Float = 0.toFloat()

    override fun view() {
        linearLayout {
            size(FILL, FILL)
            orientation(LinearLayout.VERTICAL)
            padding(dip(20))

            ratingView()
            ratingView()
        }
    }

    private fun ratingView() {
        ratingBar {
            size(WRAP, WRAP)
            margin(dip(20))

            rating(value)
            onRatingBarChange { bar, rating, fromUser ->
                if (fromUser) {
                    value = rating
                }
            }
        }
    }
}


