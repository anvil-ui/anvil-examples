package com.example.anvil.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static trikita.anvil.DSL.*;

import trikita.anvil.Anvil;
import trikita.anvil.RenderableView;

public class FirstFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {
		System.out.println("onCreateView()");
		return new RenderableView(container.getContext()) {
			public void view() {
				linearLayout(() -> {
					textView(() -> {
						text("First fragment");
					});
					button(() -> {
						text("Go to second fragment");
						onClick(v -> {
							((MainActivity)(v.getContext())).showSecondFragment();
						});
					});
				});
			}
		};
	}
}
