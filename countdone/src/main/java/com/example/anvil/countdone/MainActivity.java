package com.example.anvil.countdone;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(new StartView(this));
	}

	@Override
	public void onPause() {
		super.onPause();
		// TODO
		//Tasks.getInstance().save(this);
		//if (Tasks.getInstance().getCurrent() != null &&
				//Tasks.getInstance().getCurrent().isRunning()) {
			//// Here's a good place to show notification informing that the task is
			//// still "ticking" in the background.
		//}
	}

	@Override
	public void onResume() {
		super.onPause();
		//Tasks.getInstance().load(this);
		//Tasks.Task t = Tasks.getInstance().getCurrent();
		//// Task may have been finished while the app was paused
		//if (t != null && t.isRunning() && t.getRemainder() < 0) {
			//t.stop();
		//}
	}

	@Override
	public void onBackPressed() {
		if (App.state().home()) {
			super.onBackPressed();
		} else {
			toStart();
		}
	}

	public void toStart() {
		App.dispatch(new CountdoneAction(CountdoneAction.Type.HOME, true));
		setContentView(new StartView(this));
	}

	public void toCountdown() {
		App.dispatch(new CountdoneAction(CountdoneAction.Type.HOME, false));
		setContentView(new CountDownView(this));
	}
}
