package com.example.anvil.hello;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

// Attrs is a mixin for all View bindings for the given API level
import static trikita.anvil.v15.Attrs.*;

// RenderableView is a reactive Anvil component
import trikita.anvil.RenderableView;

public class HelloView extends RenderableView {

	public HelloView(Context c) {
		super(c);
	}

	//
	// view() returns the component layout. It's a virtual layout,
	// no views are created at this time. Actual views will be created
	// only when the component is first rendered on the screen.
	//
	// Layout structure is nested, as if you wrote it in XML
	// Attribute bindings have the similar names to View setters,
	// e.g. text() -> setText(), gravity() -> setGravity()
	//
	// size() is a shortcut builder for LayoutParams.
	//
	// So the following code corresponds to an XML like this:
	//
	// <FrameLayout
	//   android:layout_width="fill_parent"
	//   android:layout_height="fill_parent" >
	//   <TextView
	//      android:layout_width="wrap_content"
	//      android:layout_height="wrap_content"
	//      android:layout_gravity="center"
	//      android:test="@string/hello" />
	// </FrameLayout>
	//
	public ViewNode view() {
		return
			v(FrameLayout.class,
				size(FILL, FILL),

				v(TextView.class,
					size(WRAP, WRAP)
						.gravity(CENTER),
					text(R.string.hello)));
	}
}
