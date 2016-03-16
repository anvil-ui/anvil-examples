package com.example.anvil.databinding;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import static trikita.anvil.DSL.*;
import trikita.anvil.Anvil;
import trikita.anvil.RenderableView;

public class SpinnerLayout extends RenderableView {
    private final static String[] ITEMS = {
        "One", "Two", "Three", "Four"
    };

    int index;

    public SpinnerLayout(Context c) {
        super(c);
    }

    public void view() {
        linearLayout(() -> {
            size(FILL, FILL);
            orientation(LinearLayout.VERTICAL);
            padding(dip(20));

            spinner(() -> {
                size(FILL, WRAP);
                margin(dip(50), dip(20));
                init(() -> {
                    ArrayAdapter<String> adp = new ArrayAdapter<>(
                        SpinnerLayout.this.getContext(),
                        android.R.layout.simple_spinner_item,
                        ITEMS);
                    adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    adapter(adp);
                });
                selection(index);
                onItemSelected(this::onSpinnerSelected);
            });

            spinner(() -> {
                size(FILL, WRAP);
                margin(dip(50), dip(20));
                init(() -> {
                    ArrayAdapter<String> adp = new ArrayAdapter<>(
                        SpinnerLayout.this.getContext(),
                        android.R.layout.simple_spinner_item,
                        ITEMS);
                    adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    adapter(adp);
                });
                selection(index);
                onItemSelected(this::onSpinnerSelected);
            });
        });
    }

    public void onSpinnerSelected(AdapterView<?> parent, View v, int pos, long id) {
        index = pos;
    }
}

