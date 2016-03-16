package com.example.anvil.databinding;

import android.content.Context;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import static trikita.anvil.DSL.*;
import trikita.anvil.RenderableView;

public class CheckBoxLayout extends RenderableView {

    boolean checked;

    public CheckBoxLayout(Context c) {
        super(c);
    }

    public void view() {
        linearLayout(() -> {
            size(FILL, FILL);
            orientation(LinearLayout.VERTICAL);
            padding(dip(20));

            checkBox(() -> {
                size(WRAP, WRAP);
                margin(0, dip(20));
                checked(checked);
                onCheckedChange((CompoundButton v, boolean isChecked) -> {
                    checked = isChecked;
                });
            });

            checkBox(() -> {
                size(WRAP, WRAP);
                margin(0, dip(20));
                checked(checked);
                onCheckedChange((CompoundButton v, boolean isChecked) -> {
                    checked = isChecked;
                });
            });
        });
    }
}
