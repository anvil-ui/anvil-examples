package com.example.anvil.databinding;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Constructor;

public class MainActivity extends Activity {

    private boolean home = true;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(new MainLayout(this));
    }

    @Override
    public void onBackPressed() {
        if (home) {
            super.onBackPressed();
        } else {
            home = true;
            setContentView(new MainLayout(this));
        }
    }

    public void route(int pos) {
        try {
            Class cls = Class.forName(getPackageName()+"."+MainLayout.LAYOUTS[pos]);
            Constructor constructor = cls.getConstructor(Context.class);
            setContentView((View) constructor.newInstance(this));

            home = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
