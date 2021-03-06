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

// A classic to-do list application
//
// You have a list of to-do items
// You can add to this list
// You can check an item in the list as "completed"
// "Completed" items still stay in the list
// You can actually remove all "completed" items from the list
public class TodoView extends RenderableView {
	private Todo todo = Todo.getInstance();

	private String message = "";

	public TodoView(Context c) {
		super(c);
	}

	View.OnClickListener mOnAddClicked = new View.OnClickListener() {
		public void onClick(View v) {
			todo.add(message);
			message = "";
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

    // Create an adapter with a list of to-do items
    // This is the adapter for the list of to-do items
    // It looks more complex than the simple-one-TextView adapter in the currency example
    // but it is still the analogy to the original list adapter
    // view() method is still the analogy to the original getView() method in original list adapter
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
					text(message),
					onTextChanged(new SimpleTextWatcher() {
					    public void onTextChanged(CharSequence s) {
						message = s.toString();
					    }
					})),

				o (button(),
					size(WRAP, WRAP),
					layoutGravity(CENTER_VERTICAL),
					text("Add"),
					enabled(message.trim().length() != 0),
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

