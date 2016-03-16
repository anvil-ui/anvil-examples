package com.example.anvil.databinding

import android.content.Context
import android.widget.LinearLayout
import android.widget.SeekBar

import trikita.anvil.DSL.*
import trikita.anvil.RenderableView

class SeekBarLayout(c: Context) : RenderableView(c) {

    var value = 0

    override fun view() {
        linearLayout {
            size(FILL, FILL)
            orientation(LinearLayout.VERTICAL)

            seekBarLayout()
            seekBarLayout()
        }
    }

    private fun seekBarLayout() {
        seekBar {
            size(FILL, WRAP)
            margin(dip(40))

            progress(value)
            onSeekBarChange({ sb, progress, fromUser ->
                if (fromUser) {
                    value = progress.toInt()
                }
            })
        }
    }
}


