package com.example.anvil.databinding;

import android.content.Context;
import android.widget.ArrayAdapter;

import static trikita.anvil.DSL.*;

import trikita.anvil.RenderableView;

public class MainLayout extends RenderableView {

    public final static String[] LAYOUTS = {
        "CalendarLayout", "CheckBoxLayout", "EditTextLayout", "ExpandableListLayout",
        "NumberPickerLayout", "RadioGroupLayout", "RatingBarLayout", "SeekBarLayout",
        "SpinnerLayout", "TimePicker"
    };

    private ArrayAdapter<String> mAdapter;

    public MainLayout(Context c) {
        super(c);
        mAdapter = new ArrayAdapter<>(c, android.R.layout.simple_list_item_1, LAYOUTS);
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
        listView(() -> {
            size(FILL, WRAP);
            adapter(mAdapter);
            onItemClick((parent, view, pos, id) -> {
                ((MainActivity) getContext()).route(pos);
            });
        });
    }
}
