package com.example.anvil.databinding

import android.content.Context
import android.view.View
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.LinearLayout
import android.widget.SimpleExpandableListAdapter

import java.util.*

import trikita.anvil.DSL.*

import trikita.anvil.Anvil
import trikita.anvil.RenderableView

class ExpandableListLayout(c: Context) : RenderableView(c) {

    private val mAdapter: ExpandableListAdapter

    internal var expanded: BooleanArray

    init {

        expanded = BooleanArray(GROUP.size)
        val groupData = ArrayList<Map<String, String>>()
        val childData = ArrayList<List<Map<String, String>>>()
        for (i in GROUP.indices) {
            val map = HashMap<String, String>()
            groupData.add(map)
            map.put(NAME, GROUP[i])

            val children = ArrayList<Map<String, String>>()
            for (j in 0..CHILD[i].size - 1) {
                val childMap = HashMap<String, String>()
                children.add(childMap)
                childMap.put(NAME, CHILD[i][j])
            }
            childData.add(children)
            expanded[i] = false
        }

        mAdapter = SimpleExpandableListAdapter(c, groupData,
                android.R.layout.simple_expandable_list_item_1,
                arrayOf<String>(NAME), intArrayOf(android.R.id.text1),
                childData, android.R.layout.simple_expandable_list_item_2,
                arrayOf<String>(NAME), intArrayOf(android.R.id.text1))
    }

    override fun view() {
        linearLayout {
            size(FILL, FILL)

            list()
            list()
        }
    }

    private fun list() {
        expandableListView {
            size(0, FILL)
            weight(1f)
            adapter(mAdapter)
            for (i in GROUP.indices) {
                if (expanded[i]) {
                    (Anvil.currentView<View>() as ExpandableListView).expandGroup(i)
                } else {
                    (Anvil.currentView<View>() as ExpandableListView).collapseGroup(i)
                }
            }
            onGroupCollapse { expanded[it] = false }
            onGroupExpand { expanded[it] = true }
        }
    }

    companion object {
        private val NAME = "NAME"
        private val GROUP = arrayOf<String>("Veggies", "Fruits")
        private val CHILD = arrayOf<Array<String>>(arrayOf<String>("Carrot", "Cucumber", "Beet"), arrayOf<String>("Orange", "Apple", "Cherry"))
    }
}




