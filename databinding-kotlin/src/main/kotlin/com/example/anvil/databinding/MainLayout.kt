package com.example.anvil.databinding

import android.content.Context
import android.widget.ArrayAdapter

import trikita.anvil.DSL.*

import trikita.anvil.RenderableView

val LAYOUTS = arrayOf<String>("CalendarLayout", "CheckBoxLayout", "EditTextLayout",
        "ExpandableListLayout", "NumberPickerLayout", "RadioGroupLayout", "RatingBarLayout",
        "SeekBarLayout", "SpinnerLayout", "TimePickerLayout")

class MainLayout(c: Context) : RenderableView(c) {

    val mAdapter: ArrayAdapter<String>

    init {
        mAdapter = ArrayAdapter(c, android.R.layout.simple_list_item_1, LAYOUTS)
    }

    override fun view() {
        listView {
            size(FILL, WRAP)
            adapter(mAdapter)
            onItemClick { parent, view, pos, id -> (getContext() as MainActivity).route(pos) }
        }
    }
}
