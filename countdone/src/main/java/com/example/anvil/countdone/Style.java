package com.example.anvil.countdone;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import static trikita.anvil.DSL.*;

import trikita.anvil.Anvil;

public class Style {
	private final static String ACTION_BACK = "\ue893";

	public final static String ACTION_EDIT = "\ue796";
	public final static String ACTION_REMOVE = "\ue620";
	public final static String ACTION_NONE = "";

	public final static int TASK_NAME_EDITTEXT_ID = 1;

	public final static int MIN_TOP_HEIGHT = 120;
	public final static int MAX_TOP_HEIGHT = 260;

	private final static int START_BACKGROUND_COLOR = 0xff3f51b5;
	private final static int COUNTDOWN_BACKGROUND_COLOR = 0xffcddc39;
	private final static int FAILED_BACKGROUND_COLOR = 0xffff3b30;

	private final static int BUTTON_SIZE = (int) (MIN_TOP_HEIGHT * 0.8);

	public static void startBackground() {
		backgroundColor(START_BACKGROUND_COLOR);
	}

	public static void countdownBackground() {
		backgroundColor(COUNTDOWN_BACKGROUND_COLOR);
	}

	public static void failedBackground() {
		backgroundColor(FAILED_BACKGROUND_COLOR);
	}

	public static int listHeaderColor(int coef) {
		float[] hsv = new float[3];
		Color.colorToHSV(START_BACKGROUND_COLOR, hsv);
		hsv[2] = 0.5f + (coef-MIN_TOP_HEIGHT)*(hsv[2]-0.5f)/(MAX_TOP_HEIGHT-MIN_TOP_HEIGHT);
		return Color.HSVToColor(255, hsv);
	}

	public static void blackButton(int resId) {
		button(() -> {
			size(dip(BUTTON_SIZE), dip(BUTTON_SIZE));
			layoutGravity(Gravity.CENTER);
			gravity(Gravity.CENTER);
			backgroundResource(R.drawable.black_button);
			text(resId);
			textSize(sip(24));
			textColor(Color.WHITE);
			typeface("RobotoCondensed-Light.ttf");
			onClick(v -> ((MainActivity) v.getContext()).toCountdown());
		});
	}

	public static void taskItem(String itemText, String itemDate) {
		linearLayout(() -> {
			size(FILL, WRAP);

			textView(() -> {
				size(WRAP, dip(48));
				weight(1);
				gravity(CENTER_VERTICAL);
				typeface("RobotoCondensed-Bold.ttf");
				padding(dip(8));
				textSize(sip(24));
				textColor(itemDate == null ? Color.WHITE: 0x90ffffff);
				text(itemText);
			});

			textView(() -> {
				size(WRAP, dip(48));
				weight(0);
				gravity(CENTER_VERTICAL);
				typeface("RobotoCondensed-Bold.ttf");
				padding(dip(8));
				textSize(sip(24));
				textColor(Color.WHITE);
				text(itemDate);
			});
		});
	}

	public static void actionBar(String action, View.OnClickListener listener) {
		linearLayout(() -> {
			size(FILL, WRAP);
			orientation(LinearLayout.HORIZONTAL);
			layoutGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP);
			padding(dip(4));

			textView(() -> {
				materialIcon(ACTION_BACK);
				onClick(v -> ((MainActivity) v.getContext()).toStart());
			});

			v(View.class, () -> {
				size(WRAP, FILL);
				weight(1);
				gravity(Gravity.CENTER_VERTICAL);
			});

			textView(() -> {
				materialIcon(action == null ? "" : action);
				visibility(action != null);
				onClick(listener);
			});
		});
	}

	public static void taskName(String name, boolean editable, SimpleTextWatcher watcher) {
		editText(() -> {
			id(TASK_NAME_EDITTEXT_ID);
			size(FILL, WRAP);
			margin(0, dip(20));
			gravity(Gravity.CENTER);
			focusable(editable);
			focusableInTouchMode(editable);
			clickable(editable);
			cursorVisible(editable);
			backgroundDrawable(null);
			hint(R.string.task_name_hint);
			text(name);
			textColor(Color.WHITE);
			textSize(sip(isPortrait() ? 42 : 36));
			typeface("RobotoCondensed-Light.ttf");
			singleLine(true);
			onTextChanged(watcher);
			init(() -> {
				((EditText) Anvil.currentView()).requestFocus();
			});
		});
	}

	public static void taskDuration(String s, View.OnClickListener listener) {
		textView(() -> {
			size(FILL, WRAP);
			margin(0, dip(20));
			gravity(Gravity.CENTER);
			textColor(Color.WHITE);
			textSize(sip(isPortrait() ? 67 : 42));
			clickable(listener != null);
			onClick(listener != null ? listener : v -> {});
			typeface("RobotoCondensed-Bold.ttf");
			text(s);
		});
	}

	public static void taskButton(int resId, boolean on, View.OnClickListener listener) {
		button(() -> {
			size(dip(BUTTON_SIZE), dip(BUTTON_SIZE));
			margin(0, dip(50));
			gravity(Gravity.CENTER);
			backgroundResource(R.drawable.black_button);
			text(resId);
			textSize(sip(24));
			textColor(Color.WHITE);
			typeface("RobotoCondensed-Light.ttf");
			enabled(on);
			onClick(listener);
		});
	}

	private static void materialIcon(String s) {
		size(dip(48), dip(48));
		weight(0);
		gravity(Gravity.CENTER);
		textColor(Color.WHITE);
		clickable(true);
		typeface("Material-Design-Icons.ttf");
		textSize(sip(36));
		backgroundResource(R.drawable.header_button);
		text(s);
	}
}
