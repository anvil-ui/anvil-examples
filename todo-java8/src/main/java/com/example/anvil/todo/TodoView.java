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

public class TodoView extends RenderableView {
	private Todo todo = Todo.getInstance();

	private StringBuilder message = new StringBuilder();

	public TodoView(Context c) {
		super(c);
	}

	private void onAddClicked(View v) {
		todo.add(message.toString());
		message.delete(0, message.length());
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
				});

				button(() -> {
					size(WRAP, WRAP);
					layoutGravity(CENTER_VERTICAL);
					text("Add");
					enabled(message.toString().trim().length() != 0);
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

