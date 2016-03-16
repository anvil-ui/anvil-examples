package com.example.anvil.databinding;

import android.content.Context;
import android.widget.LinearLayout;

import static trikita.anvil.DSL.*;
import trikita.anvil.RenderableView;

public class NumberPickerLayout extends RenderableView {

    int n = 0;

    public NumberPickerLayout(Context c) {
        super(c);
    }

    @Override
    public void view() {
        linearLayout(() -> {
            size(FILL, FILL);
            orientation(LinearLayout.VERTICAL);

            numberPickerLayout();
            numberPickerLayout();
        });
    }

    private void numberPickerLayout() {
        numberPicker(() -> {
            size(WRAP, WRAP);
            weight(1);
            layoutGravity(CENTER);

            minValue(0);
            maxValue(9);

            value(n);

            onValueChanged((picker, old, value) -> {
                n = value;
            });
        });
    }
}

