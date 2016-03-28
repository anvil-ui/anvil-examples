package com.example.anvil.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_container);
		getFragmentManager().beginTransaction().add(R.id.fragment_container, new FirstFragment()).commit();
	}

	public void showSecondFragment() {
		getFragmentManager().beginTransaction()
			.replace(R.id.fragment_container, new SecondFragment())
			.addToBackStack(null)
			.commit();
	}
}
