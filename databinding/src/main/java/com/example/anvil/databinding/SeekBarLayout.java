package com.example.anvil.databinding;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import static trikita.anvil.DSL.*;
import trikita.anvil.RenderableView;

public class SeekBarLayout extends RenderableView {

    int value = 0;

    public SeekBarLayout(Context c) {
        super(c);
    }

    @Override
    public void view() {
        linearLayout(() -> {
            size(FILL, FILL);
            orientation(LinearLayout.VERTICAL);

            seekBarLayout();
            seekBarLayout();
        });
    }

    private void seekBarLayout() {
        seekBar(() -> {
            size(FILL, WRAP);
            margin(dip(40));
            progress(value);
            onSeekBarChange((sb, progress, fromUser) -> {
                if (fromUser) {
                    value = progress;
                }
            });
        });
    }
}


