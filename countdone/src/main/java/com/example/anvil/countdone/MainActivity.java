package com.example.anvil.countdone;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(new StartView(this));
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
