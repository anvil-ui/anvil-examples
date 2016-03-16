package com.example.anvil.databinding

import android.content.Context

import trikita.anvil.DSL.*

import trikita.anvil.RenderableView

class MainLayout(c: Context) : RenderableView(c) {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onAttachedToWindow()
    }

    override fun view() {
        frameLayout {
            size(FILL, FILL)
            textView { text("Hello, Anvil!") }
        }
    }
}
