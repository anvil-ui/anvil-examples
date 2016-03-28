package com.example.anvil.countdone;

import trikita.jedux.Action;

public class CountdoneAction<V> extends Action<CountdoneAction.Type, V> {
	public static enum Type {
		NEW_TASK,
		POP_TASK,
		REMOVE_TASK,
		SET_NAME,
		SET_DURATION,
		START,
		PAUSE,
		STOP,
		FINISH,
		TICK,
		HOME,
	}

	public CountdoneAction(Type type) {
		super(type, null);
	}

	public CountdoneAction(Type type, V value) {
		super(type, value);
	}	
}
