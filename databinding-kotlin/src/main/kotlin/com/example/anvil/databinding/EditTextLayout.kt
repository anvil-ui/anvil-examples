package com.example.anvil.databinding

import android.content.Context
import android.widget.LinearLayout

import trikita.anvil.DSL.*
import trikita.anvil.RenderableView

class EditTextLayout(c: Context) : RenderableView(c) {

    var value = ""

    override fun view() {
        linearLayout {
            size(FILL, FILL)
            orientation(LinearLayout.VERTICAL)

            input()
            input()
        }
    }

    private fun input() {
        editText {
            size(FILL, WRAP)
            margin(dip(40))

            text(value)
            onTextChanged({ s -> value = s.toString() })
        }
    }
}


