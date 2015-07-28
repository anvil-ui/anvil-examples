package com.example.anvil.currency;

public class Mutable<T> {
	private T value;
	private boolean fromUser = false;

	public Mutable() {}

	public Mutable(T value) {
		this.set(value);
	}

	public void set(T value) {
		set(value, false);
	}

	public void set(T value, boolean fromUser) {
		this.value = value;
		this.fromUser = fromUser;
	}

	public T get() {
		return get(false);
	}

	public T get(boolean clearFromUser) {
		if (clearFromUser) {
			this.fromUser = false;
		}
		return this.value;
	}

	public boolean isModifiedByUser() {
		return this.fromUser;
	}
}

