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

// A component that shows and converts values of two currencies
//
// UI looks like this:
//
// [currency 1 spinner] [currency 1 input]
// [currency 2 spinner] [currency 2 input]
//
// Users can change currency in one of the spinners
// Changing one currency will trigger change in value of another currency
//
// e.g. currently we have
//
// [USD] [100.0]
// [EUR] [91.10787]
//
// We change USD to CAD, and now we have
//
// [CAD] [100.0]
// [EUR] [69.41552]
//
// Notice the value of EUR has been changed
//
// Users can also change value in one of the inputs
// Changing one value will trigger change in value of another currency
//
// e.g. currently we have
//
// [USD] [100.0]
// [EUR] [91.10787]
//
// We change 100 to 200, and now we have
//
// [USD] [200.0]
// [EUR] [182.21574]
//
// Notice the value of EUR has been changed
//
// In a word, the UI is always expected to update
// such that value of currency 1 is equivalent to value of currency 2, according to exchange rate
public class CurrencyView extends RenderableView {

	private CurrencyManager mCurrencyManager = CurrencyManager.getInstance();

    // Explanations on Mutable wrappers of currency option and currency value
    // The Mutable wrapper is a collection of a value and a flag indicating
    // whether the recent change of the value is made by user (=fromUser)
    // By default fromUser is false
    //
    // In this scenario, every operation works as usual
    // e.g. setting and getting values without setting fromUser flag to true
    //
    // The only moment this flag is set to true is when the change is directly triggered by a user
    // For example:
    // If a user manually changes the value in input 1, the fromUser flag in value 1 is set to true
    // The value of input 2 is ought to be changed by code logic but its fromUser flag remains false
    //
    // The purpose of distinguishing such two types of operations (by user/by code logic) is to
    //
    // 1. If the input is changed by user, then for every render cycle we don't want to re-render the value
    // because it's going to interfere with user's editing action
    // e.g. in EditText if you are editing a value, you would expect the cursor to always point to last position
    // but re-rendering every cycle would cause the cursor to reset to first position often
    //
    // 2. If the input is not changed by user, then for every render cycle the value will be updated by code logic
    //
    // We can refer value with fromUser flag true as "dummy" value
    // We can refer value with fromUser flag false as "reactive" value

	// Index of currency spinners
	private Mutable<Integer> mFirstIndex = new Mutable<>(-1);
	private Mutable<Integer> mSecondIndex = new Mutable<>(-1);

	// Values of currencies
	private Mutable<Float> mFirstSum = new Mutable<>(100.0f);
	private Mutable<Float> mSecondSum = new Mutable<>(0.f);

	// TextWatcher's in which afterTextChanged will be called after the texts of inputs are changed
	private TextWatcher mFirstWatcher = bindValue(mFirstSum);
	private TextWatcher mSecondWatcher = bindValue(mSecondSum);

	// Adapter of currency spinners
	// It's conceptually equivalent to a ListView adapter implementation
	//
	// The view(int) method returns a view for each item in the spinner
	// It's conceptually equivalent to getView() method in a ListView adapter implementation
	// In this case the returned view is just a simple TextView
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

	// This method is called every render cycle
	// but it's major logic (try to set up default currencies to be USD and EUR)
	// is only executed once
	private void setupAdapters() {
		if (mCurrencyManager.isSyncing() == false &&
				mCurrencyManager.currencies().size() >= 2) {
			// If the manager is not doing network request and there are more than 2 currencies for fallback
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

	// Create a new TextWatch, given a value
    // This method applies to values of two inputs
	private TextWatcher bindValue(final Mutable<Float> value) {
		return new TextWatcher() {
			public void afterTextChanged(Editable s) {
                // After the text of the binding input is changed
				try {
                    // Try to cast the string input into float
					float n = Float.valueOf(s.toString());
                    // If the pre-edit value is different from the post-edit value
                    if (n != value.get()) {
                        // Set the value with post-edit value, and indicate this value is now "dummy"
						value.set(n, true);
                        // Update another value
						update(value);
					}
				} catch (NumberFormatException e) {
                    // If failed casting just assume post-edit value to 0
					value.set(0f, true);
                    // Update another value
					update(value);
				}
			}
			public void	beforeTextChanged(CharSequence s, int from, int n, int after) {}
			public void	onTextChanged(CharSequence s, int from, int before, int n) {}
		};
	}

	private void update(Mutable<Float> value) {
        // If "dummy" value is the first sum,
        // then set second sum's value,
        // and set second sum as "reactive" value
		if (value == mFirstSum) {
			mSecondSum.set(mCurrencyManager.
				exchange(mFirstIndex.get(), mSecondIndex.get(), value.get()));
        // Vice versa
		} else {
			mFirstSum.set(mCurrencyManager.
				exchange(mSecondIndex.get(), mFirstIndex.get(), value.get()));
		}
        // Re-render now
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
                    // If first sum is "reactive" then re-render
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
                    // If second sum is "reactive" then re-render
					if (mSecondSum.isModifiedByUser() == false) {
						text(mSecondSum.get(true).toString());
					}
				});
			});
		});
	}

	// Separate spinner style from logic
	private void spinnerStyle() {
		size(0, FILL);
		weight(0.4f);
	}

	// Separate input style from logic
	private void inputStyle() {
		size(0, FILL);
		weight(0.6f);
		inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		selectAllOnFocus(true);
	}
}

