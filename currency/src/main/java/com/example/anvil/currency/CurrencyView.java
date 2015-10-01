package com.example.anvil.currency;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import static trikita.anvil.DSL.*;

import trikita.anvil.Anvil;
import trikita.anvil.RenderableAdapter;
import trikita.anvil.RenderableView;

public class CurrencyView extends RenderableView {

	private CurrencyManager mCurrencyManager = CurrencyManager.getInstance();

	private Mutable<Integer> mFirstIndex = new Mutable<>(-1);
	private Mutable<Integer> mSecondIndex = new Mutable<>(-1);

	private Mutable<Float> mFirstSum = new Mutable<>(100.0f);
	private Mutable<Float> mSecondSum = new Mutable<>(0.f);

	private TextWatcher mFirstWatcher = bindValue(mFirstSum);
	private TextWatcher mSecondWatcher = bindValue(mSecondSum);

	private RenderableAdapter mCurrencyAdapter = new RenderableAdapter() {
		public int getCount() {
			if (mCurrencyManager.isSyncing()) {
				return 0;
			}
			return mCurrencyManager.currencies().size();
		}
		public String getItem(int index) {
			if (mCurrencyManager.isSyncing()) {
				return "";
			}
			return mCurrencyManager.currencies().get(index);
		}
		public void view(int index) {
			textView(() -> {
				size(FILL, dip(48));
				gravity(CENTER);
				text(getItem(index));
			});
		}
	};

	public CurrencyView(Context c) {
		super(c);
	}

	private void setupAdapters() {
		if (mCurrencyManager.isSyncing() == false &&
				mCurrencyManager.currencies().size() >= 2) {
			if (mFirstIndex.get() == -1 && mSecondIndex.get() == -1) {
				// Looks like the first render after data was synced
				int i = mCurrencyManager.currencies().indexOf("USD");
				int j = mCurrencyManager.currencies().indexOf("EUR");
				if (i != -1 && j != -1) {
					// Try to select USD <-> EUR by default
					mFirstIndex.set(i);
					mSecondIndex.set(j);
				} else {
					// If either of the currencies is missing - use the first two
					mFirstIndex.set(0);
					mSecondIndex.set(1);
				}
				mCurrencyAdapter.notifyDataSetChanged();
			}
		}
	}

	private TextWatcher bindValue(final Mutable<Float> value) {
		return new TextWatcher() {
			public void afterTextChanged(Editable s) {
				try {
					float n = Float.valueOf(s.toString());
					if (n != value.get()) {
						value.set(n, true);
						update(value);
					}
				} catch (NumberFormatException e) {
					value.set(0f, true);
					update(value);
				}
			}
			public void	beforeTextChanged(CharSequence s, int from, int n, int after) {}
			public void	onTextChanged(CharSequence s, int from, int before, int n) {}
		};
	}

	private void update(Mutable<Float> value) {
		if (value == mFirstSum) {
			mSecondSum.set(mCurrencyManager.
				exchange(mFirstIndex.get(), mSecondIndex.get(), value.get()));
		} else {
			mFirstSum.set(mCurrencyManager.
				exchange(mSecondIndex.get(), mFirstIndex.get(), value.get()));
		}
		Anvil.render();
	}

	public void view() {
		setupAdapters();

		linearLayout(() -> {
			size(FILL, FILL);
			orientation(LinearLayout.VERTICAL);

			linearLayout(() -> {
				size(FILL, WRAP);

				spinner(() -> {
					spinnerStyle();
					selection(mFirstIndex.get());
					onItemSelected((av, v, i, id) -> {
						mFirstIndex.set(i);
						update(mFirstSum);
					});
					adapter(mCurrencyAdapter);
				});

				editText(() -> {
					inputStyle();
					onTextChanged(mFirstWatcher);
					if (mFirstSum.isModifiedByUser() == false) {
						text(mFirstSum.get(true).toString());
					}
				});
			});

			linearLayout(() -> {
				size(FILL, WRAP);

				spinner(() -> {
					spinnerStyle();
					selection(mSecondIndex.get());
					onItemSelected((av, v, i, id) -> {
						mSecondIndex.set(i);
						update(mSecondSum);
					});
					adapter(mCurrencyAdapter);
				});

				editText(() -> {
					inputStyle();
					onTextChanged(mSecondWatcher);
					if (mSecondSum.isModifiedByUser() == false) {
						text(mSecondSum.get(true).toString());
					}
				});
			});
		});
	}

	private void spinnerStyle() {
		size(0, FILL);
		weight(0.4f);
	}

	private void inputStyle() {
		size(0, FILL);
		weight(0.6f);
		inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		selectAllOnFocus(true);
	}
}

