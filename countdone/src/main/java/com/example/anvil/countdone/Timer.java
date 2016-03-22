package com.example.anvil.countdone;

import android.os.CountDownTimer;

import trikita.jedux.Store;

public class Timer implements Store.Middleware<CountdoneAction<?>, State> {

	private final static long TICK_DURATION = 1000;		// 1s

	private CountDownTimer mTimer;
	
	@Override
	public void dispatch(Store<CountdoneAction<?>, State> store, CountdoneAction<?> action, Store.NextDispatcher<CountdoneAction<?>> next) {
		switch (action.type) {
			case START:
				long d = store.getState().isTaskPaused() ?
					store.getState().currentTask().remainder() :
					store.getState().currentTask().duration();
				start(d);
				break;
			case PAUSE:
			case STOP:
				cancel();
				break;
		}
		next.dispatch(action);
	}

	private void start(long duration) {
		cancel();

		mTimer = new CountDownTimer(duration, TICK_DURATION) {
			public void onTick(long millis) {
				App.dispatch(new CountdoneAction(CountdoneAction.Type.TICK));
			}
			public void onFinish() {
				App.dispatch(new CountdoneAction(CountdoneAction.Type.FINISH));
			}
		};
		mTimer.start();
	}

	private void cancel() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}
}
