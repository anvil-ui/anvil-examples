package com.example.anvil.databinding

import android.content.Context
import android.widget.LinearLayout

import trikita.anvil.DSL.*
import trikita.anvil.RenderableView

class TimePickerLayout(c: Context) : RenderableView(c) {

    var h = 0
    var m = 0

    override fun view() {
        linearLayout {
            size(FILL, FILL)
            orientation(LinearLayout.VERTICAL)

            timePickerLayout()
            timePickerLayout()
        }
    }

    private fun timePickerLayout() {
        timePicker {
            size(WRAP, WRAP)
            weight(1f)
            layoutGravity(CENTER)

            currentHour(h)
            currentMinute(m)

            onTimeChanged { picker, hr, min ->
                h = hr
                m = min
            }
        }
    }
}

