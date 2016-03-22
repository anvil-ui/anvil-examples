package com.example.anvil.countdone;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import org.immutables.value.Value;
import org.immutables.gson.Gson;

@Value.Immutable
@Gson.TypeAdapters
public abstract class State {

	@Nullable public abstract Task currentTask();
	public abstract List<Task> completed();
	public abstract List<Task> failed();
	public abstract boolean durationSet();
	public abstract boolean home();

	public boolean isTaskNew() {
		return currentTask() != null && currentTask().status() == Task.Status.NEW;
	}

	public boolean isTaskRunning() {
		return currentTask() != null && currentTask().status() == Task.Status.STARTED;
	}

	public boolean isTaskFailed() {
		return currentTask() != null && currentTask().status() == Task.Status.FAILED;
	}

	public boolean isTaskPaused() {
		return currentTask() != null && currentTask().status() == Task.Status.PAUSED;
	}

	public boolean isTaskFinished() {
		return currentTask() != null && currentTask().status() == Task.Status.COMPLETED;
	}

	public long getRemainder() {
		if (isTaskRunning()) {
			return currentTask().started() + currentTask().remainder() - Task.now();
		} else {
			return currentTask().remainder();
		}
	}

	public static State getDefault() {
		return ImmutableState.builder()
			.currentTask(null)
			.completed(initCompleted())
			.failed(initFailed())
			.durationSet(false)
			.home(true)
			.build();
	}

	public static List<Task> initCompleted() {
		List<Task> tasks = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			tasks.add(ImmutableTask.builder()
						.name("NEW COMPLETED TASK")
						.duration(15000)
						.status(Task.Status.COMPLETED)
						.started(Task.NEVER)
						.stopped(Task.now())
						.remainder(0)
						.build());
		}
		return tasks;
	}

	public static List<Task> initFailed() {
		List<Task> tasks = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			tasks.add(ImmutableTask.builder()
						.name("NEW FAILED TASK")
						.duration(15000)
						.status(Task.Status.COMPLETED)
						.started(Task.NEVER)
						.stopped(Task.now())
						.remainder(0)
						.build());
		}
		return tasks;
	}

}
