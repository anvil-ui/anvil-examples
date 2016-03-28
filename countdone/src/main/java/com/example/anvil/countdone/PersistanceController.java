package com.example.anvil.countdone;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import trikita.jedux.Store;

public class PersistanceController implements Store.Middleware<CountdoneAction<?>, State> {

	private final SharedPreferences mPreferences;
	private final Gson mGson;

	public PersistanceController(Context context) {
		mPreferences = context.getSharedPreferences("data", 0);
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapterFactory(new GsonAdaptersState());
		gsonBuilder.registerTypeAdapterFactory(new GsonAdaptersTask());
		mGson = gsonBuilder.create();
	}

	public State getSavedState() {
		if (mPreferences.contains("data")) {
			String json = mPreferences.getString("data", "");
			return mGson.fromJson(json, ImmutableState.class);
		}
		return null;
	}

	@Override
	public void dispatch(Store<CountdoneAction<?>, State> store, CountdoneAction<?> action, Store.NextDispatcher<CountdoneAction<?>> next) {
		next.dispatch(action);
		String json = mGson.toJson(store.getState());
		mPreferences.edit().putString("data", json).apply();
	}
}
