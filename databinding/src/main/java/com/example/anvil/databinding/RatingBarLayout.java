package com.example.anvil.databinding;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import static trikita.anvil.DSL.*;
import trikita.anvil.RenderableView;

public class RatingBarLayout extends RenderableView {
    float value;

    public RatingBarLayout(Context c) {
        super(c);
    }

    public void view() {
        linearLayout(() -> {
            size(FILL, FILL);
            orientation(LinearLayout.VERTICAL);
            padding(dip(20));

            ratingView();
            ratingView();
        });
    }

    private void ratingView() {
        ratingBar(() -> {
            size(WRAP, WRAP);
            margin(dip(20));
            rating(value);
            onRatingBarChange(this::onRatingChanged);
        });
    }

    public void onRatingChanged(RatingBar bar, float rating, boolean fromUser) {
        if (fromUser) {
            value = rating;
        }
    }
}


