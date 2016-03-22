package com.example.anvil.countdone;

import android.app.Application;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import trikita.jedux.Store;

public class App extends Application {

	private static App sInstance;

	private Store<CountdoneAction<?>, State> store;

	public void onCreate() {
		sInstance = this;

		PersistanceController persistanceController = new PersistanceController(this);
		State initialState = persistanceController.getSavedState();
		if (initialState == null) {
			initialState = State.getDefault();
		}

		store = new Store<CountdoneAction<?>, State>(this::reduce,
				initialState,
				new Timer(),
				persistanceController);

		super.onCreate();
	}

	private static App getInstance() {
		return sInstance;
	}

	public static Store store() {
		return getInstance().store;
	}

	public static State dispatch(CountdoneAction action) {
		return getInstance().store.dispatch(action);
	}

	public static State state() {
		return getInstance().store.getState();
	}

	public State reduce(CountdoneAction<?> action, State old) {
		switch (action.type) {
			case NEW_TASK:
				if (action.value != null) {
					Pair p = (Pair) action.value;
					return ImmutableState.builder()
						.from(old)
						.currentTask(Task.newTask((String) p.first, (Long) p.second))
						.durationSet(false)
						.build();
				} else {
					return ImmutableState.builder()
						.from(old)
						.currentTask(Task.newTask(old.currentTask().name(), old.currentTask().duration()))
						.build();
				}
			case POP_TASK:
				int pos = (Integer) action.value;
				boolean isCompleted = pos < old.completed().size();
				Task oldTask = isCompleted ? 
					old.completed().get(pos) :
					old.failed().get(pos - old.completed().size());
				return ImmutableState.builder()
					.from(old)
					.currentTask(oldTask)
					.durationSet(true)
					.build();
			case SET_NAME:
				return ImmutableState.builder()
					.from(old)
					.currentTask(old.currentTask().newName((String) action.value))
					.build();
			case SET_DURATION: return ImmutableState.builder()
					.from(old)
					.currentTask(old.currentTask().newDuration((Long) action.value))
					.durationSet(true)
					.build();
			case START:
				return ImmutableState.builder()
					.from(old)
					.currentTask(old.currentTask().start())
					.build();
			case PAUSE:
				return ImmutableState.builder()
					.from(old)
					.currentTask(old.currentTask().pause())
					.build();
			case STOP:
				if (old.isTaskFailed()) {
					return ImmutableState.builder()
						.from(old)
						.currentTask(null)
						.addFailed(old.currentTask().complete())
						.build();
				} else {
					return ImmutableState.builder()
						.from(old)
						.currentTask(null)
						.addCompleted(old.currentTask().complete())
						.build();
				}
			case FINISH:
				return ImmutableState.builder()
					.from(old)
					.currentTask(old.currentTask().fail())
					.build();
			case REMOVE_TASK:
				List<Task> completed = new ArrayList<>(old.completed());
				System.out.println("REMOVE_TASK: completed size="+completed.size());
				boolean done = completed.remove(old.currentTask());
				System.out.println("REMOVE_TASK: completed removed " + done);
				List<Task> failed = new ArrayList<>(old.failed());
				System.out.println("REMOVE_TASK: failed size="+failed.size());
				done = failed.remove(old.currentTask());
				System.out.println("REMOVE_TASK: failed removed " + done);
				return ImmutableState.builder()
					.from(old)
					.completed(completed)
					.failed(failed)
					.currentTask(null)
					.durationSet(false)
					.build();
			case HOME:
				return ImmutableState.builder()
					.from(old)
					.home((Boolean) action.value)
					.build();
		}
		return old;
	}
}
