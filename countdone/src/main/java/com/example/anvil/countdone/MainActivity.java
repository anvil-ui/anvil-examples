package com.example.anvil.countdone;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	private Backstack mBackstack = new Backstack(this, new Backstack.Listener() {
		public void setContentView(View v) {
			MainActivity.this.setContentView(v);
		}
	});

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		if (b != null) {
			mBackstack.load(b);
		} else {
			mBackstack.navigate(new StartView(this));
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		Tasks.getInstance().save(this);
		if (Tasks.getInstance().getCurrent() != null &&
				Tasks.getInstance().getCurrent().isRunning()) {
			// Here's a good place to show notification informing that the task is
			// still "ticking" in the background.
		}
	}

	@Override
	public void onResume() {
		super.onPause();
		Tasks.getInstance().load(this);
		Tasks.Task t = Tasks.getInstance().getCurrent();
		// Task may have been finished while the app was paused
		if (t != null && t.isRunning() && t.getRemainder() < 0) {
			t.stop();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle b) {
		mBackstack.save(b);
		super.onSaveInstanceState(b);
	}

	@Override
	public void onBackPressed() {
		if (!mBackstack.back()) {
			finish();
		}
	}

	public Backstack getBackstack() {
		return mBackstack;
	}
}
