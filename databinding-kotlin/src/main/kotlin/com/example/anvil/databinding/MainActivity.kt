package com.example.anvil.databinding

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager

import java.lang.reflect.Constructor

class MainActivity : Activity() {

    var home = true

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(MainLayout(this))
    }

    override fun onBackPressed() {
        if (home) {
            super.onBackPressed()
        } else {
            home = true
            setContentView(MainLayout(this))
        }
    }

    fun route(pos: Int) {
        try {
            val cls = Class.forName(getPackageName() + "." + LAYOUTS[pos])
            val constructor = cls.getConstructor(Context::class.java)
            setContentView(constructor.newInstance(this) as View)

            home = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
