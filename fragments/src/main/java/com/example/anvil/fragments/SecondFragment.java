package com.example.anvil.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static trikita.anvil.DSL.*;

import trikita.anvil.Anvil;
import trikita.anvil.RenderableView;

public class SecondFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {
		return new RenderableView(container.getContext()) {
			public void view() {
				textView(() -> {
					text("Second fragment");
				});
			}
		};
	}
}

