package com.example.anvil.databinding

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout

import trikita.anvil.DSL.*
import trikita.anvil.Anvil
import trikita.anvil.RenderableView

class SpinnerLayout(c: Context) : RenderableView(c) {

    var index: Int = 0
    val ITEMS = arrayOf("One", "Two", "Three", "Four")

    override fun view() {
        linearLayout {
            size(FILL, FILL)
            orientation(LinearLayout.VERTICAL)
            padding(dip(20))

            spinnerLayout()
            spinnerLayout()
        }
    }

    fun spinnerLayout() {
        spinner {
            size(FILL, WRAP)
            margin(dip(50), dip(20))
            init({
                val adp = ArrayAdapter<String>(
                        this@SpinnerLayout.context,
                        android.R.layout.simple_spinner_item,
                        ITEMS)
                adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                adapter(adp)
            })
            selection(index)
            onItemSelected({ parent, v, pos, id -> index = pos })
        }
    }
}

