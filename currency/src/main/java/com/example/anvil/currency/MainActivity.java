package com.example.anvil.currency;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CurrencyManager.getInstance().load(this);
		CurrencyManager.getInstance().sync();
		setContentView(new CurrencyView(this));
	}

	@Override
	public void onPause() {
		CurrencyManager.getInstance().save(this);
		super.onPause();
	}
}
