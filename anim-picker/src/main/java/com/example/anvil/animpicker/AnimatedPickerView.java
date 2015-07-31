package com.example.anvil.animpicker;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import static trikita.anvil.v15.Attrs.*;
import static trikita.anvil.v15.Anim.*;

import trikita.anvil.State;
import trikita.anvil.Anvil;
import trikita.anvil.RenderableView;
import android.widget.FrameLayout;

// A simple picker component that looks like this:
//
//     <    foo    >
//
// When next button is pressed the item is moved to the right and
// the next item substitutes it like this:
//
//     < r   foo   >
//     < ar   foo  >
//     < bar   foo >
//     <  bar   fo >
//     <   bar   f >
//     <    bar    >
//
// Of course, if user clicks next button twice too fast - the animation
// should happen twice, or when the user presses next and previous buttons
// quickly - two animations should happen immidiately one after another.
//
// It may become too complicated when thinking of it in imperative style,
// but Anvil's declarative nature makes it really easy.
//
public class AnimatedPickerView extends RenderableView {

	public final static int ANIM_DURATION = 300; // 300ms for each animation

	//
	// State is a boolean variable which can be in either of two states and can
	// make a durable transition from one state to another
	//
	// This is handy to define animated events, like if we want next item animation
	// to happen we can set next state to true, then a few milliseconds later set
	// it back to false asking animation to stop.
	//
	// So, mNext state is on when the next item animation is happening,
	// mPrev state is on when the previous item animation is happening.
	// If both states are off - there is no animations and current item is
	// shown in the center.
	//
	private State mNext = Anvil.newState(false);
	private State mPrev = Anvil.newState(false);

	private int mIndex = 0; // Current item index
	private String[] mItems = { "Mercury", "Venus", "Earth", "Mars", "Jupiter",
		"Saturn", "Uranus", "Neptune", "Pluto"};

	// View width between next and prev buttons
	// We can't tell it beforehand (it depends on the screen size and
	// orientation), buf for animations we need to use real
	// offsets in pixels. So we use config() binding to measure the real View
	// instance after it's inflated and remember its width here.
	private int mWidth = 0;

	public AnimatedPickerView(Context c) {
		super(c);
	}

	// When next button is clicked - slowly change mNext state to true, then
	// increase the item index, then set mNext back to false
	private View.OnClickListener mOnNextClicked = (v) -> {
		mNext.set(true, ANIM_DURATION, () -> {
			mIndex = (mIndex + 1) % mItems.length;
		}).set(false, 0);
	};

	// When next button is clicked - slowly change mPrev state to true, then
	// decrease the item index, then set mPrev back to false
	private View.OnClickListener mOnPrevClicked = (v) -> {
		mPrev.set(true, ANIM_DURATION, () -> {
			mIndex = (mIndex + mItems.length - 1) % mItems.length;
		}).set(false, 0);
	};

	public ViewNode view() {
		return
			v(LinearLayout.class,
				size(FILL, WRAP),
				orientation(LinearLayout.HORIZONTAL),

				v(Button.class,
					text("<<<"),
					onClick(mOnPrevClicked)),

				// Container for two items at most (one is disappearing, another one
				// replaces it). Centered item takes 50% of the width in the center,
				// so it's left offset is 25% as well as its right offset
				v(LinearLayout.class,
					size(WRAP, FILL).weight(1),
					// here we measure container width after it's inflated
					config(v -> mWidth = v.getWidth()),
					orientation(LinearLayout.HORIZONTAL),

					// Primary view, displays the current item
					v(FrameLayout.class,
						// takes 50% of the container
						size(mWidth/2, FILL),
						// if it's replaced by the previous item - it's shifted full-width
						// to the left (from 25% to -25%) and fades out.
						anim(mPrev.isOn())
							.of("x", mWidth/4, -mWidth/4)
							.of("alpha", 1f, 0f)
							.duration(ANIM_DURATION),
						// if it's replaced by the next item - it's shifted full-width
						// to the right (from 25% to 75%) and fades out.
						anim(mNext.isOn())
							.of("x", mWidth/4, mWidth*3/4)
							.of("alpha", 1f, 0f)
							.duration(ANIM_DURATION),
						// if it's not animated at all - it's centered and is fully opaque
						mNext.isOff() && mPrev.isOff() ? x(mWidth/4) : null,
						mNext.isOff() && mPrev.isOff() ? alpha(1f) : null,

						// see below, custom method to render item content
						itemView(mIndex)),

					// Helper view - the "next" or the "previous" item
					// It is shown only during the animation phase
					v(FrameLayout.class,
						size(mWidth/2, FILL),
						// previous item is moving from 75% to 25% of the container width
						anim(mPrev.isOn())
							.of("x", mWidth*3/4, mWidth/4)
							.of("alpha", 0f, 1f)
							.duration(ANIM_DURATION),
						// next item is moving from -25% to 25% of the container width
						anim(mNext.isOn())
							.of("x", -mWidth/4, mWidth/4)
							.of("alpha", 0f, 1f)
							.duration(ANIM_DURATION),
						// if no animation happens now - hide this helper view
						visibility(mNext.isOn() || mPrev.isOn()),

						// display the contents of the next or previous item in the list
						itemView(mIndex + (mNext.isOn() ? 1 : -1)))),

				v(Button.class,
						text(">>>"),
						onClick(mOnNextClicked)));
	}

	//
	// Let's separate rendering of the individual item from the generic picker
	// component to make customizations easier
	//
	public ViewNode itemView(int index) {
		// Get text of the item in the list (using modulo, non-negative index)
		String item = mItems[(index + mItems.length) % mItems.length];
		return
			v(TextView.class,
				size(FILL, FILL),
				gravity(CENTER),
				text(item));
	}
}
