package com.example.anvil.databinding;

import android.content.Context;

import static trikita.anvil.DSL.*;

import trikita.anvil.RenderableView;

public class MainLayout extends RenderableView {

    public MainLayout(Context c) {
        super(c);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void view() {
        frameLayout(() -> {
            size(FILL, FILL);
            textView(() -> {
                text("Hello, Anvil!");
            });
        });
    }
}
