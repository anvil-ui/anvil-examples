package com.example.anvil.databinding;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import static trikita.anvil.DSL.*;
import trikita.anvil.Anvil;
import trikita.anvil.RenderableView;

public class RadioGroupLayout extends RenderableView {

    int index;

    public RadioGroupLayout(Context c) {
        super(c);
    }

    public void view() {
        linearLayout(() -> {
            size(FILL, FILL);
            orientation(LinearLayout.VERTICAL);

            radioGroupLayout(100);
            radioGroupLayout(200);
        });
    }

    private void radioGroupLayout(final int startId) {
        radioGroup(() -> {
            margin(dip(20));
            onCheckedChange((RadioGroup rg, int id) -> {
                index = id - startId;
            });
            for (int i = 0; i < 3; i++) {
                final int index = i;
                radioButton(() -> {
                    id(startId + index);
                });
            }
            //final RadioGroup r = Anvil.currentView();
            //r.check(startId + index);
            check(startId + index);
        });
    }
}

