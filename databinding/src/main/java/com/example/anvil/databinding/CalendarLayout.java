package com.example.anvil.databinding;

import android.content.Context;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static trikita.anvil.DSL.*;
import trikita.anvil.RenderableView;

public class CalendarLayout extends RenderableView {

    long dateInMillis;

    public CalendarLayout(Context c) {
        super(c);
        Calendar calendar = new GregorianCalendar();
        dateInMillis = calendar.getTimeInMillis();
    }

    public void view() {
        linearLayout(() -> {
            size(FILL, FILL);
            orientation(LinearLayout.VERTICAL);

            calendar();
            calendar();
        });
    }

    private void calendar() {
        calendarView(() -> {
            size(FILL, 0);
            weight(1);
            margin(dip(20), 0);
            date(dateInMillis);
            onDateChange(this::onDateChanged);
        });
    }

    public void onDateChanged(CalendarView v, int y, int m, int d) {
        dateInMillis = new GregorianCalendar(y, m, d).getTimeInMillis();
    }
}



