package com.example.anvil.databinding

import android.content.Context
import android.widget.LinearLayout
import android.widget.RadioGroup

import trikita.anvil.DSL.*
import trikita.anvil.Anvil
import trikita.anvil.RenderableView

class RadioGroupLayout(c: Context) : RenderableView(c) {

    var index: Int = 0

    override fun view() {
        linearLayout {
            size(FILL, FILL)
            orientation(LinearLayout.VERTICAL)

            radioGroupLayout(100)
            radioGroupLayout(200)
        }
    }

    private fun radioGroupLayout(startId: Int) {
        radioGroup {
            margin(dip(20))
            for (i in 0..2) {
                val index = i
                radioButton { id(startId + index) }
            }
            check(startId + index)
            onCheckedChange { rg: RadioGroup, id: Int -> index = id - startId }
        }
    }
}

