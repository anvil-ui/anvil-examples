package com.example.anvil.todo

object Todo {
	val items = mutableListOf<TodoItem>()

	fun add(msg: String) {
		items.add(TodoItem(msg, false))
	}

	fun toggle(index: Int) {
		val item = items[index]
		item.checked = !item.checked
	}

	fun clear() {
		items.filter { it.checked }.forEach { todoItem -> items.remove(todoItem) }
	}

	fun hasChecked() : Boolean = items.any { it.checked }

	data class TodoItem(val message: String, var checked: Boolean)
}



