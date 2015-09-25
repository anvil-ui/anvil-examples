package com.example.anvil.counter;

import android.widget.Button;
import android.widget.LinearLayout;
import android.view.View;

import static trikita.anvil.v15.Attrs.*;
import trikita.anvil.RenderableView;
import android.content.Context;
import android.widget.TextView;

// Counter component contains a text and a button.
// Text displays how many times the button was clicked.
// Component is updated automatically due to its reactive nature.
public class CounterView extends RenderableView {

	public CounterView(Context c) {
		super(c);
	}

	// State of our component 
	int mClicks = 0;

	// View listeners modify the state
	View.OnClickListener mOnButtonClicked = new View.OnClickListener() {
		public void onClick(View v) {
			// Update the component state.
			// There is nothing more to do here. UI will be update automatically.
			mClicks++;
		}
	};

	// Virtual layout will be created on each render cycle.
	// If new virtual layout is different - then the actual views will be
	// updated. For example, if mClicks value has been changed - 
	// TextView's setText() will be called. Other attributes and views will
	// remain untouched.
	public void view() {
		o (linearLayout(),
			size(FILL, WRAP),
			orientation(LinearLayout.VERTICAL),

			o (textView(),
				text("Clicks: " + mClicks)),

			o (button(),
				size(FILL, WRAP),
				text("Click me"),
				// Component rendering happens automatically in every standard
				// listener binding, like onClick(), onTouch, onScroll etc.
				onClick(mOnButtonClicked)));
	}
}

