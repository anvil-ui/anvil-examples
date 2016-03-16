package com.example.anvil.databinding;

import android.content.Context;
import android.widget.LinearLayout;

import static trikita.anvil.DSL.*;
import trikita.anvil.RenderableView;

public class EditTextLayout extends RenderableView {

    String value = "";

    public EditTextLayout(Context c) {
        super(c);
    }

    public void view() {
        linearLayout(() -> {
            size(FILL, FILL);
            orientation(LinearLayout.VERTICAL);

            input();
            input();
        });
    }

    private void input() {
        editText(() -> {
            size(FILL, WRAP);
            margin(dip(40));
            text(value);
            onTextChanged((s) -> {
                value = s.toString();
            });
            //onTextChanged(new TextWatcher() {
                //public void	afterTextChanged(Editable s) {}
                //public void	beforeTextChanged(CharSequence s, int from, int n, int after) {}
                //public void	onTextChanged(CharSequence s, int from, int before, int n) {
                    //System.out.println("s="+s);
                    //value = s.toString();
                //}
            //});
        });
    }
}


