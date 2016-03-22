package com.example.anvil.countdone;

import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import static trikita.anvil.DSL.*;

import trikita.anvil.Anvil;
import trikita.anvil.RenderableView;

//
// A screen with some kind of an actionbar, a button to start/finish the task.
// Also shows a name and a countdown timer of the task.
//
public class CountDownView extends RenderableView {

	private Runnable mUnsubscriber;

	public CountDownView(Context c) {
		super(c);
		mUnsubscriber = App.store().subscribe(() -> {
			Anvil.render();
		});
		if (App.state().currentTask() == null) {
			App.dispatch(new CountdoneAction(CountdoneAction.Type.NEW_TASK, new Pair("New task", 25*60*1000L)));
		}
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mUnsubscriber.run();
	}

	@Override
	public void view() {
		linearLayout(() -> {
			size(FILL, FILL);
			orientation(LinearLayout.VERTICAL);
			if (!App.state().isTaskFailed()) {
				Style.countdownBackground();
			} else {
				Style.failedBackground();
			}

			// action bar
			if (App.state().isTaskRunning()) {
				Style.actionBar(Style.ACTION_EDIT, this::onEdit);
			} else if (App.state().isTaskFailed()) {
				Style.actionBar(Style.ACTION_EDIT, this::onRenew);
			} else if (App.state().isTaskFinished()) {
				Style.actionBar(Style.ACTION_REMOVE, this::onRemove);
			} else {
				Style.actionBar(Style.ACTION_NONE, null);
			}

			frameLayout(() -> {
				size(FILL, WRAP);

				linearLayout(() -> {
					orientation(LinearLayout.VERTICAL);
					size(FILL, WRAP);
					gravity(CENTER_HORIZONTAL);

					Style.taskName(App.state().currentTask().name(),
						(App.state().isTaskNew() || App.state().isTaskPaused()),
						s -> App.dispatch(new CountdoneAction(CountdoneAction.Type.SET_NAME, s.toString())));

					Style.taskDuration(countdownText(),
						(App.state().isTaskNew() || App.state().isTaskPaused() ? this::onSetTime : null));

					Style.taskButton(taskButtonText(), (!App.state().isTaskNew() || App.state().durationSet()), taskButtonListener());
				});
			});
		});
	}

	public void onEdit(View v) {
		App.dispatch(new CountdoneAction(CountdoneAction.Type.PAUSE));
		post(() -> {
			EditText editText = (EditText) findViewById(Style.TASK_NAME_EDITTEXT_ID);
			editText.requestFocus();
			editText.setSelection(editText.getText().length());
		});
	}

	public void onRenew(View v) {
		App.dispatch(new CountdoneAction(CountdoneAction.Type.NEW_TASK, null));
	}

	public void onRemove(View v) {
		finish();
		// TODO a task should have an id. Otherwise removing one of the two identical tasks
		// (same values of task params) will remove both due to Immutables optimizations in
		// keeping a single instance for multiple identical objects
		App.dispatch(new CountdoneAction(CountdoneAction.Type.REMOVE_TASK));
	}

	public void onSetTime(View v) {
		TimePickerDialog picker = new TimePickerDialog(v.getContext(),
			TimePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, (p, hour, minute) -> {
				App.dispatch(new CountdoneAction(CountdoneAction.Type.SET_DURATION, (hour * 60 + minute ) * 60 * 1000L));
			}, (int) App.state().currentTask().remainder()/60/60/1000,
			(int) (App.state().currentTask().remainder()/60/1000)%60, true);
		picker.setTitle(R.string.set_time_title);
		picker.show();
	}

	private OnClickListener taskButtonListener() {
		return v -> {
			if (App.state().isTaskRunning() || App.state().isTaskFailed()) {
				finish();
				App.dispatch(new CountdoneAction(CountdoneAction.Type.STOP));
			} else if (App.state().isTaskFinished()) {
				String n = App.state().currentTask().name();
				long d = App.state().currentTask().duration();
				App.dispatch(new CountdoneAction(CountdoneAction.Type.NEW_TASK,
					new Pair<String, Long>(n, d)));
				App.dispatch(new CountdoneAction(CountdoneAction.Type.START));
			} else {
				App.dispatch(new CountdoneAction(CountdoneAction.Type.START));
			}
		};
	}

	private int taskButtonText() {
		if (App.state().isTaskNew() || App.state().isTaskPaused()) {
			return R.string.start;
		} else if (App.state().isTaskFinished()) {
			return R.string.again;
		} else {
			return R.string.done;
		}
	}

	private String countdownText() {
		if (App.state().isTaskNew() && !App.state().durationSet()) {
			return getContext().getString(R.string.set_time);
		}
		long t = (App.state().isTaskFinished() ?
				App.state().currentTask().duration() :
				App.state().getRemainder());
		t = (t < 0 ? 0 : t);
		return String.format("%02d:%02d:%02d",
				t/1000/60/60,
				(t/1000/60)%60,
				(t/1000)%60);
	}

	private void finish() {
		((MainActivity) getContext()).toStart();
	}
}

