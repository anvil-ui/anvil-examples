package com.example.anvil.todo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Todo {
	private final static Todo sInstance = new Todo();

	private Todo() {}

	public static Todo getInstance() {
		return sInstance;
	}

	private List<TodoItem> items = new ArrayList<>();

	public List<TodoItem> items() {
		return items;
	}

	public void add(String msg) {
		items.add(new TodoItem(msg));
	}

	public void toggle(int index) {
		items.get(index).toggle();
	}

	public void clear() {
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			if (((TodoItem) iter.next()).isChecked()) {
				iter.remove();
			}
		}
	}

	public boolean hasChecked() {
		for (TodoItem item : items()) {
			if (item.isChecked()) {
				return true;
			}
		}
		return false;
	}

	public class TodoItem {
		private String message;
		private boolean checked;

		TodoItem(String msg) {
			message = msg;
		}

		public String getMessage() {
			return message;
		}

		public boolean isChecked() {
			return checked;
		}

		void toggle() {
			checked = !checked;
		}
	}
}

