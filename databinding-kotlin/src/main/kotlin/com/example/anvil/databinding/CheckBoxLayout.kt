package com.example.anvil.databinding

import android.content.Context
import android.widget.LinearLayout

import trikita.anvil.DSL.*
import trikita.anvil.RenderableView

class CheckBoxLayout(c: Context) : RenderableView(c) {

    var checked: Boolean = false

    override fun view() {
        linearLayout {
            size(FILL, FILL)
            orientation(LinearLayout.VERTICAL)
            padding(dip(20))

            checkBoxLayout()
            checkBoxLayout()
        }
    }

    fun checkBoxLayout() {
        checkBox {
            size(WRAP, WRAP)
            margin(0, dip(20))

            checked(checked)
            onCheckedChange { v, isChecked: Boolean -> checked = isChecked }
        }
    }
}
