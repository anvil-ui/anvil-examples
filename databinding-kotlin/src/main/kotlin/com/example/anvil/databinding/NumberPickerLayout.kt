package com.example.anvil.databinding

import android.content.Context
import android.widget.LinearLayout

import trikita.anvil.DSL.*
import trikita.anvil.RenderableView

class NumberPickerLayout(c: Context) : RenderableView(c) {

    var n = 0

    override fun view() {
        linearLayout {
            size(FILL, FILL)
            orientation(LinearLayout.VERTICAL)

            numberPickerLayout()
            numberPickerLayout()
        }
    }

    private fun numberPickerLayout() {
        numberPicker {
            size(WRAP, WRAP)
            weight(1f)
            layoutGravity(CENTER)

            minValue(0)
            maxValue(9)

            value(n)
            onValueChanged { picker, old, value -> n = value }
        }
    }
}

