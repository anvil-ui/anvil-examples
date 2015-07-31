package com.example.anvil.todo

import java.util.ArrayList

object Todo {
	val items = ArrayList<TodoItem>()

	public fun add(msg: String) {
		items.add(TodoItem(msg, false))
	}

	public fun toggle(index: Int) {
		val item = items.get(index)
		item.checked = !item.checked
	}

	public fun clear() {
		val iter = items.iterator()
		while (iter.hasNext()) {
			if (iter.next().checked) {
				iter.remove()
			}
		}
	}

	public fun hasChecked() : Boolean {
		for (item: TodoItem in items) {
			if (item.checked)
				return true
		}
		return false
	}

	public data class TodoItem(val message: String, var checked: Boolean)
}



