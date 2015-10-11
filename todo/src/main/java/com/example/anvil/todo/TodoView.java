package com.example.anvil.todo;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import static trikita.anvil.DSL.*;

import trikita.anvil.RenderableAdapter;
import trikita.anvil.RenderableView;

public class TodoView extends RenderableView {
	private Todo todo = Todo.getInstance();

	private StringBuilder message = new StringBuilder("");

	public TodoView(Context c) {
		super(c);
	}

	View.OnClickListener mOnAddClicked = new View.OnClickListener() {
		public void onClick(View v) {
			todo.add(message.toString());
			message.delete(0, message.length());
		}
	};

	View.OnClickListener mOnClearClicked = new View.OnClickListener() {
		public void onClick(View v) {
			todo.clear();
		}
	};

	AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView parent, View v, int pos, long id) {
			todo.toggle(pos);
		}
	};

	RenderableAdapter mTodoAdapter = RenderableAdapter.withItems(todo.items(),
			new RenderableAdapter.Item<Todo.TodoItem>() {
				public void view(int pos, Todo.TodoItem value) {
					o (linearLayout(),
						size(FILL, WRAP),
						minHeight(dip(72)),

						o (textView(),
							size(0, WRAP),
							weight(1),
							layoutGravity(CENTER_VERTICAL),
							padding(dip(5)),
							text(value.getMessage())),

						o (checkBox(),
							size(WRAP, WRAP),
							margin(dip(5)),
							layoutGravity(CENTER_VERTICAL),
							focusable(false),
							focusableInTouchMode(false),
							clickable(false),
							checked(value.isChecked())));
				}
			});

	public void view() {
		mTodoAdapter.notifyDataSetChanged();

		o (linearLayout(),
			size(FILL, WRAP),
			orientation(LinearLayout.VERTICAL),

			o (linearLayout(),
				size(FILL, WRAP),

				o (editText(),
					size(0, WRAP),
					weight(1),
					text(message)),

				o (button(),
					size(WRAP, WRAP),
					layoutGravity(CENTER_VERTICAL),
					text("Add"),
					enabled(message.toString().trim().length() != 0),
					onClick(mOnAddClicked))),

				o (button(),
					size(FILL, WRAP),
					padding(dip(5)),
					text("Clear checked tasks"),
					enabled(todo.hasChecked()),
					onClick(mOnClearClicked)),

				o (listView(),
					size(FILL, WRAP),
					itemsCanFocus(true),
					onItemClick(mOnItemClickListener),
					adapter(mTodoAdapter)));
	}
}

