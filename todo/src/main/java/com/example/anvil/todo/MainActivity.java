package com.example.anvil.todo;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new TodoView(this));
	}
}
