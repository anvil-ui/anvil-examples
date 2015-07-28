package com.example.anvil.currency;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import static trikita.anvil.v15.Attrs.*;

import trikita.anvil.Anvil;
import trikita.anvil.RenderableAdapter;
import trikita.anvil.SimpleAttrNode;
import trikita.anvil.RenderableView;
import android.view.View;
import android.widget.TextView;
import java.util.Map;
import java.util.HashMap;

public class CurrencyView extends RenderableView {

	private CurrencyManager mCurrencyManager = CurrencyManager.getInstance();

	private Mutable<Integer> mFirstIndex = new Mutable<>(-1);
	private Mutable<Integer> mSecondIndex = new Mutable<>(-1);

	private Mutable<Float> mFirstSum = new Mutable<>(100.0f);
	private Mutable<Float> mSecondSum = new Mutable<>(0.f);

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
		public ViewNode itemView(int index) {
			return
				v(TextView.class,
					size(FILL, dip(48)),
					gravity(CENTER),
					text(getItem(index)));
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

	public ViewNode view() {
		setupAdapters();
		return
			v(LinearLayout.class,
				size(FILL, FILL),
				orientation(LinearLayout.VERTICAL),
				
				v(LinearLayout.class,
					size(FILL, WRAP),
					
					v(Spinner.class,
						size(WRAP, WRAP).weight(1f),
						spinnerStyle(),
						selection(mFirstIndex.get()),
						onItemSelected((av, v, i, id) -> {
							mFirstIndex.set(i);
							update(mFirstSum);
						}),
						adapter(mCurrencyAdapter)),
					
					v(EditText.class,
						size(WRAP, WRAP).weight(1.6f),
						inputStyle(),
						bindValue(mFirstSum))),

				v(LinearLayout.class,
					size(FILL, WRAP),
					
					v(Spinner.class,
						size(WRAP, WRAP).weight(1),
						spinnerStyle(),
						selection(mSecondIndex.get()),
						onItemSelected((av, v, i, id) -> {
							mSecondIndex.set(i);
							update(mSecondSum);
						}),
						adapter(mCurrencyAdapter)),
					
					v(EditText.class,
						size(WRAP, WRAP).weight(1.6f),
						inputStyle(),
						bindValue(mSecondSum))));
	}

	private AttrNode spinnerStyle() {
		return null;
	}

	private AttrNode inputStyle() {
		return attrs(
				inputType(InputType.TYPE_CLASS_NUMBER |
					InputType.TYPE_NUMBER_FLAG_DECIMAL),
				null);
	}

	private AttrNode bindValue(Mutable<Float> value) {
		return attrs(
			selectAllOnFocus(true),
			!value.isModifiedByUser() ? text(value.get(true).toString()) : null,
			onTextChanged(s -> {
				try {
					float n = Float.valueOf(s);
					if (n != value.get()) {
						value.set(n, true);
						update(value);
					}
				} catch (NumberFormatException e) {
					value.set(0f, true);
					update(value);
				}
			}));
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
}

