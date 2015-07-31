package com.example.anvil.todo

import android.content.Context
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView

import trikita.anvil.RenderableView

// Kotlin has a bug with importing static methods
// so we have to import whole hierarchy of classes instead
import trikita.anvil.Nodes.*
import trikita.anvil.BaseAttrs.*
import trikita.anvil.v15.Attrs.*
import trikita.anvil.RenderableView
import trikita.anvil.RenderableArrayAdapter

class TodoView(c: Context) : RenderableView(c) {
	var message = ""
	var clearText = false

	var todoAdapter = object : RenderableArrayAdapter<Todo.TodoItem>(Todo.items) {
		public override fun itemView(pos: Int, value: Todo.TodoItem) = 
			v<LinearLayout> {
				- size(FILL, WRAP)
				- minHeight(dip(72))

				v<TextView> {
					- size(0, WRAP).weight(1f).gravity(CENTER_VERTICAL)
					- padding(dip(5))
					- text(value.message)
				}

				v<CheckBox> {
					- size(WRAP, WRAP).margin(dip(5)).gravity(CENTER_VERTICAL)
					- focusable(false)
					- focusableInTouchMode(false)
					- clickable(false)
					- checked(value.checked)
				}
			}
	}

	public override fun view() =
		v<LinearLayout> {
			- size(FILL, WRAP)
			- orientation(LinearLayout.VERTICAL)

			v<LinearLayout> {
				- size(FILL, WRAP)

				v<EditText> {
					- size(0, WRAP).weight(1f)
					if (clearText) {
						- text("")
					}
					clearText = false
					- onTextChanged { text ->
						message = text
					}
				}

				v<Button> {
					- size(WRAP, WRAP).gravity(CENTER_VERTICAL)
					- text("Add")
					- enabled(message.trim().length() != 0)
					- onClick {
						Todo.add(message)
						message = ""
						clearText = true
					}
				}
			}

			v<Button> {
				- size(FILL, WRAP)
				- padding(dip(5))
				- text("Clear checked tasks")
				- enabled(Todo.hasChecked())
				- onClick {
					Todo.clear()
				}
			}
			v<ListView> {
				- size(FILL, WRAP)
				- itemsCanFocus(true)
				- onItemClick { parent, view, pos, id ->
					Todo.toggle(pos)
				}
				- adapter(todoAdapter)
				todoAdapter.notifyDataSetChanged()
			}
		}
}


