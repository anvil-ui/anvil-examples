package com.example.anvil.countdone;

import org.immutables.value.Value;
import org.immutables.gson.Gson;

@Value.Immutable
@Gson.TypeAdapters
public abstract class Task {

	private final static long NEVER = -1;

	enum Status {
		NEW,
		STARTED,
		PAUSED,
		COMPLETED,
		FAILED
	}

	public abstract String name();
	public abstract long duration();
	public abstract Status status();
	public abstract long started();
	public abstract long stopped();
	public abstract long remainder();

	public static long now() {
		return System.currentTimeMillis();
	}

	public static Task newTask(String n, long d) {
		return ImmutableTask.builder()
			.name(n)
			.duration(d)
			.status(Status.NEW)
			.started(NEVER)
			.stopped(NEVER)
			.remainder(d)
			.build();
	}

	public Task newName(String n) {
		return ImmutableTask.builder()
			.from(this)
			.name(n)
			.build();
	}

	public Task newDuration(long d) {
		return ImmutableTask.builder()
			.from(this)
			.duration(d)
			.started(NEVER)
			.remainder(d)
			.build();
	}

	public Task start() {
		return ImmutableTask.builder()
			.from(this)
			.status(Status.STARTED)
			.started(now())
			.stopped(NEVER)
			.build();
	}

	public Task pause() {
		return ImmutableTask.builder()
			.from(this)
			.status(Status.PAUSED)
			.started(NEVER)
			.stopped(now())
			.remainder(this.started() + this.remainder() - now())
			.build();
	}

	public Task complete() {
		return ImmutableTask.builder()
			.from(this)
			.status(Status.COMPLETED)
			.started(NEVER)
			.stopped(now())
			.remainder(0)
			.build();
	}

	public Task fail() {
		return ImmutableTask.builder()
			.from(this)
			.status(Status.FAILED)
			.started(NEVER)
			.remainder(0)
			.build();
	}
}
