package com.example.anvil.databinding

import android.content.Context
import android.widget.LinearLayout

import java.util.GregorianCalendar

import trikita.anvil.DSL.*
import trikita.anvil.RenderableView

class CalendarLayout(c: Context) : RenderableView(c) {

    var dateInMillis: Long = GregorianCalendar().timeInMillis

    override fun view() {
        linearLayout {
            size(FILL, FILL)
            orientation(LinearLayout.VERTICAL)

            calendar()
            calendar()
        }
    }

    private fun calendar() {
        calendarView {
            size(FILL, 0)
            weight(1f)

            date(dateInMillis)
            onDateChange { v, y, m, d -> dateInMillis = GregorianCalendar(y, m, d).timeInMillis }
        }
    }
}



