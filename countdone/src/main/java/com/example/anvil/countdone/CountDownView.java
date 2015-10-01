package com.example.anvil.countdone;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import static trikita.anvil.DSL.*;

import trikita.anvil.Anvil;
import trikita.anvil.RenderableView;

//
// A screen with some kind of an actionbar, a button to start/finish the task.
// Also shows a name and a countdown timer of the task.
//
public class CountDownView extends RenderableView {

	public final static int TASK_NAME_EDITTEXT_ID = 1;

	private final static int BACKGROUND_COLOR = 0xffcddc39;

	private Tasks.Task mCurrentTask;
	private boolean mTimeIsSet = false;
	private Backstack mBackstack;

	private Tasks mTasks = Tasks.getInstance();

	// Infinite periodic timer to update UI every second
	private CountDownTimer mTimer = new CountDownTimer(24*60*60*1000, 1000) {
		public void onTick(long millis) {
			Anvil.render();
		}
		public void onFinish() {
			// restart timer once it's finished to make it infinite
			this.start();
		}
	};

	private View.OnClickListener mStartClicked = (v) -> {
		// TODO: this should be declarative using a TextWatcher
		String s = ((EditText) findViewById(TASK_NAME_EDITTEXT_ID)).getText().toString();
		mCurrentTask.setName(s);
		mCurrentTask.start();
	};

	private View.OnClickListener mRestartClicked = (v) -> {
		Tasks.Task task = mTasks.create(mCurrentTask.getName(), mCurrentTask.getDuration());
		mCurrentTask = task;
		mCurrentTask.start();
	};

	private View.OnClickListener mDoneClicked = (v) -> {
		mCurrentTask.stop();
		finish();
	};

	private View.OnClickListener mTimeClicked = (v) -> {
		TimePickerDialog picker = new TimePickerDialog(v.getContext(), (p, hour, minute) -> {
			mCurrentTask.setDuration((hour * 60 + minute ) * 60 * 1000);
			mTimeIsSet = true;
			Anvil.render();
		}, (int) mCurrentTask.getRemainder()/60/60/1000,
		(int) (mCurrentTask.getRemainder()/60/1000)%60, true);
		picker.setTitle(R.string.set_time_title);
		picker.show();
	};

	public CountDownView(Context c) {
		super(c);
		mBackstack = ((MainActivity) c).getBackstack();
		mCurrentTask = mTasks.getCurrent();
		if (mCurrentTask == null) {
			mCurrentTask = mTasks.create("New task", 25 * 60 * 1000);
		} else if (mCurrentTask.isPaused()) {
			mCurrentTask.start();
		}
		mTimer.start();
	}

	public CountDownView withTask(Tasks.Task task) {
		mCurrentTask = task;
		return this;
	}

	private void finish() {
		mBackstack.back();
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		post(() -> {
			// Put cursor at the end of the edit text
			EditText editText = (EditText) findViewById(TASK_NAME_EDITTEXT_ID);
			editText.requestFocus();
			editText.setSelection(editText.getText().length());
		});
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mCurrentTask.isRunning()) {
			mCurrentTask.pause();
		}
		mTimer.cancel();
	}

	private String getCountDownText() {
		if (mCurrentTask.isNew() && !mTimeIsSet) {
			return getContext().getString(R.string.set_time);
		}
		long t = (mCurrentTask.isFinished() ? mCurrentTask.getDuration() : mCurrentTask.getRemainder());
		if (t < 0) {
			return getContext().getString(R.string.failed);
		}
		return String.format("%02d:%02d:%02d",
				t/1000/60/60,
				(t/1000/60)%60,
				(t/1000)%60);
	}

	private void materialIcon(String s) {
		size(dip(48), dip(48));
		weight(0);
		gravity(CENTER);
		textColor(Color.WHITE);
		clickable(true);
		typeface("Material-Design-Icons.ttf");
		textSize(36);
		backgroundResource(R.drawable.header_button);
		text(s);
	}

	@Override
	public void view() {
		linearLayout(() -> {
			size(FILL, FILL);
			orientation(LinearLayout.VERTICAL);
			backgroundColor(BACKGROUND_COLOR);

			linearLayout(() -> {
				size(FILL, WRAP);
				orientation(LinearLayout.HORIZONTAL);
				layoutGravity(CENTER_HORIZONTAL|TOP);
				padding(dip(4));

				textView(() -> {
					materialIcon("\ue893"); //Back
					onClick((v) -> finish());
				});

				v(View.class, () -> {
					size(WRAP, FILL);
					weight(1);
					gravity(CENTER_VERTICAL);
				});

				textView(() -> {
					materialIcon("\ue796"); // Edit
					visibility(mCurrentTask.isRunning());
					onClick((v) -> {
						mCurrentTask.pause();
						post(() -> {
							EditText editText = (EditText) findViewById(TASK_NAME_EDITTEXT_ID);
							editText.requestFocus();
						});
					});
				});

				textView(() -> {
					materialIcon("\ue620"); // Remove
					visibility(mCurrentTask.isFinished());
					onClick((v) -> {
						mCurrentTask.remove();
						finish();
					});
				});
			});

			frameLayout(() -> {
				size(FILL, WRAP);
				weight(1);

				linearLayout(() -> {
					orientation(LinearLayout.VERTICAL);
					size(FILL, WRAP);
					layoutGravity(CENTER_HORIZONTAL|BOTTOM);

					editText(() -> {
						id(TASK_NAME_EDITTEXT_ID);
						size(FILL, WRAP);
						gravity(CENTER);
						textColor(Color.WHITE);
						textSize(isPortrait() ? 42 : 36);
						singleLine(true);
						focusable(mCurrentTask.isNew() || mCurrentTask.isPaused());
						focusableInTouchMode(mCurrentTask.isNew() || mCurrentTask.isPaused());
						clickable(mCurrentTask.isNew() || mCurrentTask.isPaused());
						cursorVisible(mCurrentTask.isNew() || mCurrentTask.isPaused());
						backgroundDrawable(null);
						typeface("RobotoCondensed-Light.ttf");
						hint(R.string.task_name_hint);
						text(mCurrentTask.getName());
					});

					textView(() -> {
						size(FILL, WRAP);
						gravity(CENTER);
						textColor(Color.WHITE);
						textSize(isPortrait() ? 67 : 42);
						clickable(mCurrentTask.isNew() || mCurrentTask.isPaused());
						onClick(mTimeClicked);
						typeface("RobotoCondensed-Bold.ttf");
						text(getCountDownText());
					});
				});
			});

			frameLayout(() -> {
				size(FILL, WRAP);
				layoutGravity(CENTER);
				weight(1);

				textView(() -> {
					size(dip(StartView.BUTTON_SIZE), dip(StartView.BUTTON_SIZE));
					layoutGravity(CENTER);
					weight(1);
					gravity(CENTER);
					clickable(true);
					backgroundResource(R.drawable.black_button);
					textColor(Color.WHITE);
					textSize(24);
					enabled(!mCurrentTask.isNew() || mTimeIsSet);
					typeface("RobotoCondensed-Light.ttf");
					onClick(mCurrentTask.isRunning() ? mDoneClicked :
						(mCurrentTask.isFinished() ? mRestartClicked : mStartClicked));
					text(mCurrentTask.isNew() || mCurrentTask.isPaused() ? 
						R.string.start : (mCurrentTask.isFinished() ? R.string.again : R.string.done));
				});
			});
		});
	}

	public void onLoad(Bundle b) {
		if (b.getBoolean("new")) {
			mCurrentTask = mTasks.create(b.getString("newName"), b.getLong("newTime"));
			mTimeIsSet = b.getBoolean("newTimeSet");
			return;
		}
		if (b.getBoolean("current")) {
			mCurrentTask = mTasks.getCurrent();
			return;
		}
		boolean completed = b.getBoolean("completed");
		int index =	b.getInt("index");
		mCurrentTask = (completed ? mTasks.getCompleted() :
				mTasks.getFailed()).get(index);
	}

	public void onSave(Bundle b) {
		if (mCurrentTask.isNew()) {
			b.putBoolean("new", true);
			b.putBoolean("newTimeSet", mTimeIsSet);
			b.putString("newName", mCurrentTask.getName());
			b.putLong("newTime", mCurrentTask.getDuration());
		}
		if (mCurrentTask != mTasks.getCurrent()) {
			boolean completed = mTasks.getCompleted().contains(mCurrentTask);
			b.putBoolean("current", false);
			b.putBoolean("completed", completed);
			b.putInt("index", (completed ? mTasks.getCompleted() :
						mTasks.getFailed()).indexOf(mCurrentTask));
		} else {
			b.putBoolean("current", true);
		}
	}
}

