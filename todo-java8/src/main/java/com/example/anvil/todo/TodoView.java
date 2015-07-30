package com.example.anvil.todo;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import static trikita.anvil.v15.Attrs.*;

import trikita.anvil.RenderableAdapter;
import trikita.anvil.RenderableArrayAdapter;
import trikita.anvil.RenderableView;

public class TodoView extends RenderableView {
	private Todo todo = Todo.getInstance();

	private String message = "";
	private boolean clearText = false;

	public TodoView(Context c) {
		super(c);
	}

	private void onAddClicked(View v) {
		todo.add(message);
		message = "";
		clearText = true;
	}

	RenderableAdapter mTodoAdapter = new RenderableArrayAdapter<Todo.TodoItem>(todo.items()) {
		public ViewNode itemView(int pos, Todo.TodoItem value) {
			return
				v(LinearLayout.class,
					size(FILL, WRAP),
					minHeight(dip(72)),

					v(TextView.class,
						size(0, WRAP).weight(1).gravity(CENTER_VERTICAL),
						padding(dip(5)),
						text(value.getMessage())),
					v(CheckBox.class,
						size(WRAP, WRAP).margin(dip(5)).gravity(CENTER_VERTICAL),
						focusable(false),
						focusableInTouchMode(false),
						clickable(false),
						checked(value.isChecked())));
		}
	};

	public ViewNode view() {
		mTodoAdapter.notifyDataSetChanged();
		ViewNode layout = 
			v(LinearLayout.class,
				size(FILL, WRAP),
				orientation(LinearLayout.VERTICAL),

				v(LinearLayout.class,
					size(FILL, WRAP),

					v(EditText.class,
						size(0, WRAP).weight(1),
						clearText ? text("") : null,
						onTextChanged(text -> message = text)),

					v(Button.class,
						size(WRAP, WRAP).gravity(CENTER_VERTICAL),
						text("Add"),
						enabled(message.trim().length() != 0),
						onClick(this::onAddClicked))),

				v(Button.class,
					size(FILL, WRAP),
					padding(dip(5)),
					text("Clear checked tasks"),
					enabled(todo.hasChecked()),
					onClick(v -> todo.clear())),

				v(ListView.class,
					size(FILL, WRAP),
					itemsCanFocus(true),
					onItemClick((parent, v, pos, id) -> todo.toggle(pos)),
					adapter(mTodoAdapter)));
		clearText = false;
		return layout;
	}
}

