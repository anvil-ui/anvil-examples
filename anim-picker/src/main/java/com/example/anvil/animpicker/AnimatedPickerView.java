package com.example.anvil.animpicker;

import android.content.Context;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.LinearLayout;

import java.util.ArrayDeque;
import java.util.Deque;

import static trikita.anvil.DSL.*;

import trikita.anvil.Anvil;
import trikita.anvil.RenderableView;

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
public class AnimatedPickerView extends RenderableView {

	public final static int ANIM_DURATION = 3000; // 300ms for each animation

	public final static int ANIMS_PER_NAVIGATION = 2;

	enum Navigation { PREV, NEXT };

	private Deque<Navigation> mNavQueue = new ArrayDeque<>();
	private int mRunningAnims = 0;

	private int mIndex = 0; // Current item index
	private String[] mItems = { "Mercury", "Venus", "Earth", "Mars", "Jupiter",
		"Saturn", "Uranus", "Neptune", "Pluto"};

	// View width between next and prev buttons
	// We can't tell it beforehand (it depends on the screen size and
	// orientation), but for animations we need to use real
	// offsets in pixels.
	private int mWidth = 0;

	public AnimatedPickerView(Context c) {
		super(c);
	}

	public void view() {
		Navigation nav = mNavQueue.peek();
		linearLayout(() -> {
			size(FILL, WRAP);
			orientation(LinearLayout.HORIZONTAL);

			button(() -> {
				text("<<<");
				onClick(v -> {
					mNavQueue.offer(Navigation.PREV);
				});
			});

			// Container for two items at most (one is disappearing, another one
			// replaces it). Centered item takes 50% of the width in the center,
			// so it's left offset is 25% as well as its right offset
			linearLayout(() -> {
				size(WRAP, FILL);
				weight(1);
				orientation(LinearLayout.HORIZONTAL);

				// here we measure container width after it's inflated recurisvely
				// calling render() until the views have finished their layout
				if (mWidth <= 0) {
					if ((mWidth = Anvil.currentView().getWidth()) == 0) {
						post(Anvil::render);
					}
				}

				// Primary view, displays the current item
				frameLayout(() -> {
					// takes 50% of the container
					size(mWidth/2, FILL);

					if (nav == Navigation.PREV) {
						slide(mWidth/4, -mWidth/4, 1f, 0f);
					} else if (nav == Navigation.NEXT) {
						slide(mWidth/4, mWidth*3/4, 1f, 0f);
					} else {
						Anvil.currentView().setX(mWidth/4);
						Anvil.currentView().setAlpha(1f);
					}

					itemView(mIndex);
				});

				// Helper view - the "next" or the "previous" item
				// It is shown only during the animation phase
				frameLayout(() -> {
					size(mWidth/2, FILL);

					if (nav == Navigation.PREV) {
						visibility(true);
						slide(mWidth*3/4, mWidth/4, 0f, 1f);
						itemView(mIndex - 1);
					} else if (nav == Navigation.NEXT) {
						visibility(true);
						slide(-mWidth/4, mWidth/4, 0f, 1f);
						itemView(mIndex + 1);
					} else {
						visibility(false);
					}
				});
			});

			button(() -> {
				text(">>>");
				onClick(v -> {
					mNavQueue.offer(Navigation.NEXT);
				});
			});
		});
	}

	// Starts property animation for the current view
	// Uses ANIMS_PER_NAVIGATION limit to avoid starting new animations
	// on further render cycles
	private void slide(int from, int to, float fromAlpha, float toAlpha) {
		if (mRunningAnims == ANIMS_PER_NAVIGATION) {
			return;
		}
		mRunningAnims++;
		// Set inital values before animation and start animation
		Anvil.currentView().setX(from);
		Anvil.currentView().setAlpha(fromAlpha);
		Anvil.currentView().animate()
			.x(to)
			.alpha(toAlpha)
			.setDuration(ANIM_DURATION)
			.withEndAction(() -> {
				// When animation is finished - decrease the number of pending
				// animations and if coutned down to zero - pop current transition from
				// the stack
				mRunningAnims--;
				if (mRunningAnims == 0) {
					mIndex += (mNavQueue.poll() == Navigation.PREV ? -1 : +1);
					Anvil.render();
				}
			});
	}

	//
	// Let's separate rendering of the individual item from the generic picker
	// component to make customizations easier
	//
	public void itemView(int index) {
		// Get text of the item in the list (using modulo, non-negative index)
		String item = mItems[(index + mItems.length) % mItems.length];

		textView(() -> {
			size(FILL, FILL);
			gravity(CENTER);
			text(item);
		});
	}
}
