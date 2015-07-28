package com.example.anvil.currency;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import trikita.anvil.Anvil;

//
// This is a simple currency exchange manager. It syncs with the backend
// on start and performs all the calculations.
//
// It could have been a service, but we just make it a singleton for
// simplicity.
//
// Using simple HTTP API from http://api.fixer.io/latest
//
public class CurrencyManager {

	private static CurrencyManager mInstance = new CurrencyManager();

	private OkHttpClient client = new OkHttpClient();
	private Gson gson = new Gson();

	private static class CurrencyAPIResponse {
		public String base;
		public String date;
		public Map<String, Float> rates;
	}

	private boolean mIsSyncing = false;

	private CurrencyAPIResponse mResponse = null;

	private CurrencyManager() {}

	public static CurrencyManager getInstance() {
		return mInstance;
	}

	// Get recent data from the prefs
	public void load(Context c) {
		SharedPreferences prefs = c.getSharedPreferences("rates",
				Context.MODE_PRIVATE);
		mResponse = gson.fromJson(prefs.getString("rates_response", "{}"),
				CurrencyAPIResponse.class);
	}

	// Save current rates
	public void save(Context c) {
		SharedPreferences prefs = c.getSharedPreferences("rates",
				Context.MODE_PRIVATE);
		if (!isSyncing() && mResponse != null) {
			prefs.edit().putString("rates_response", gson.toJson(mResponse)).commit();
		}
	}

	// Get recent data from the server
	public void sync() {
		mIsSyncing = true;
		Request request = new Request.Builder()
			.url("http://api.fixer.io/latest")
			.build();

		client.newCall(request).enqueue(new Callback() {
			public void onFailure(Request request, IOException e) {
				e.printStackTrace();
				mIsSyncing = false;
				Anvil.render();
			}
			public void onResponse(Response response) throws IOException {
				if (response.isSuccessful()) {
					mResponse = gson.fromJson(response.body().charStream(),
						CurrencyAPIResponse.class);

					mResponse.rates.put(mResponse.base, 1f);
					mResponse.rates = new TreeMap<>(mResponse.rates);
				}

				mIsSyncing = false;
				Anvil.render();
			}
		});
	}

	// Returns true if manager is now syncring with the server
	public boolean isSyncing() {
		return mIsSyncing;
	}

	// Returns last sync date as a string
	public String syncDate() {
		return mResponse.date;
	}

	// Returns last known list of currency names
	public List<String> currencies() {
		return Arrays.asList(mResponse.rates.keySet().toArray(new String[0]));
	}

	// Covert sum from currency at index from to currency at index to
	public float exchange(int from, int to, float sum) {
		Float r[] = mResponse.rates.values().toArray(new Float[0]);
		return sum / r[from] * r[to];
	}
}

