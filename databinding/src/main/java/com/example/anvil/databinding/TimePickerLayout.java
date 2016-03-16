package trikita.anvil.listenertest;

import android.content.Context;
import android.widget.LinearLayout;

import static trikita.anvil.DSL.*;
import trikita.anvil.RenderableView;

public class TimePickerLayout extends RenderableView {

    int h = 0;
    int m = 0;

    public TimePickerLayout(Context c) {
        super(c);
    }

    @Override
    public void view() {
        linearLayout(() -> {
            size(FILL, FILL);
            orientation(LinearLayout.VERTICAL);

            timePickerLayout();
            timePickerLayout();
        });
    }

    private void timePickerLayout() {
        timePicker(() -> {
            size(WRAP, WRAP);
            weight(1);
            layoutGravity(CENTER);

            currentHour(h);
            currentMinute(m);

            onTimeChanged((picker, hr, min) -> {
                h = hr;
                m = min;
            });
        });
    }
}

