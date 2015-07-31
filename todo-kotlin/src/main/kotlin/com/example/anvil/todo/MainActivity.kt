package com.example.anvil.todo

import android.app.Activity
import android.os.Bundle

class MainActivity : Activity() {
	public override fun onCreate(b: Bundle?) {
		super.onCreate(b)
		setContentView(TodoView(this))
	}
}


