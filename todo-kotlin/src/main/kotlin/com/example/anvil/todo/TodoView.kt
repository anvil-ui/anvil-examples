package com.example.anvil.todo

import android.content.Context
import android.widget.LinearLayout

import trikita.anvil.RenderableView
import trikita.anvil.RenderableAdapter

// Kotlin has a bug with importing static methods
// so we have to import whole hierarchy of classes instead
import trikita.anvil.DSL.*

// A classic to-do list application
//
// You have a list of to-do items
// You can add to this list
// You can check an item in the list as "completed"
// "Completed" items still stay in the list
// You can actually remove all "completed" items from the list
class TodoView(c: Context) : RenderableView(c) {
	val message = StringBuilder("")

	var todoAdapter = RenderableAdapter.withItems(Todo.items) { pos, value ->
		linearLayout {
			size(FILL, WRAP)
			minHeight(dip(72))

			textView {
				size(0, WRAP)
				weight(1f)
				layoutGravity(CENTER_VERTICAL)
				padding(dip(5))
				text(value.message)
			}

			checkBox {
				size(WRAP, WRAP)
				margin(dip(5))
				layoutGravity(CENTER_VERTICAL)
				focusable(false)
				focusableInTouchMode(false)
				clickable(false)
				checked(value.checked)
			}
		}
	}

	override fun view() {
		todoAdapter.notifyDataSetChanged()

		linearLayout {
			size(FILL, WRAP)
			orientation(LinearLayout.VERTICAL)

			linearLayout {
				size(FILL, WRAP)

				editText {
					size(0, WRAP)
					weight(1f)
					text(message)
				}

				button {
					size(WRAP, WRAP)
					layoutGravity(CENTER_VERTICAL)
					text("Add")
					enabled(message.toString().trim().length != 0)
					onClick {
						Todo.add(message.toString())
						message.delete(0, message.length)
					}
				}
			}

			button {
				size(FILL, WRAP)
				padding(dip(5))
				text("Clear checked tasks")
				enabled(Todo.hasChecked())
				onClick {
					Todo.clear()
				}
			}

			listView {
				size(FILL, WRAP)
				itemsCanFocus(true)
				onItemClick { parent, view, pos, id ->
					Todo.toggle(pos)
				}
				adapter(todoAdapter)
			}
		}
	}
}
