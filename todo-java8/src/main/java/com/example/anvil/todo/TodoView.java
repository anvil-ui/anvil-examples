package com.example.anvil.todo;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import static trikita.anvil.DSL.*;

import trikita.anvil.Anvil;
import trikita.anvil.RenderableAdapter;
import trikita.anvil.RenderableView;

// A classic to-do list application
//
// You have a list of to-do items
// You can add to this list
// You can check an item in the list as "completed"
// "Completed" items still stay in the list
// You can actually remove all "completed" items from the list
//
// Major difference from Java-7 version:
// Lambda expressions are used
// In render() method Anvil DSL's are used
public class TodoView extends RenderableView {
	private Todo todo = Todo.getInstance();

	private String message = "";

	public TodoView(Context c) {
		super(c);
	}

	private void onAddClicked(View v) {
		todo.add(message);
		message = "";
	}

	RenderableAdapter mTodoAdapter =
		RenderableAdapter.withItems(todo.items(), (i, value) -> {
			linearLayout(() -> {
				size(FILL, WRAP);
				minHeight(dip(72));

				textView(() -> {
					size(0, WRAP);
					weight(1);
					layoutGravity(CENTER_VERTICAL);
					padding(dip(5));
					text(value.getMessage());
				});

				checkBox(() -> {
					size(WRAP, WRAP);
					margin(dip(5));
					layoutGravity(CENTER_VERTICAL);
					focusable(false);
					focusableInTouchMode(false);
					clickable(false);
					checked(value.isChecked());
				});
			});
		});

	public void view() {
		mTodoAdapter.notifyDataSetChanged();

		linearLayout(() -> {
			size(FILL, WRAP);
			orientation(LinearLayout.VERTICAL);

			linearLayout(() -> {
				size(FILL, WRAP);

				editText(() -> {
					size(0, WRAP);
					weight(1);
					text(message);
					onTextChanged(s -> {
						message = s.toString();
					});
				});

				button(() -> {
					size(WRAP, WRAP);
					layoutGravity(CENTER_VERTICAL);
					text("Add");
					enabled(message.trim().length() != 0);
					onClick(this::onAddClicked);
				});

			});

			button(() -> {
				size(FILL, WRAP);
				padding(dip(5));
				text("Clear checked tasks");
				enabled(todo.hasChecked());
				onClick(v -> todo.clear());
			});

			listView(() -> {
				size(FILL, WRAP);
				itemsCanFocus(true);
				onItemClick((parent, v, pos, id) -> todo.toggle(pos));
				adapter(mTodoAdapter);
			});
		});
	}
}

