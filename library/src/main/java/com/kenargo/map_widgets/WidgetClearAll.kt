package com.kenargo.map_widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout

class WidgetClearAll @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        initSubView(context)
    }

    private fun initSubView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.widget_map_clear_all, this, true)
    }

    // OnClick handler is part of the view class; I don't need to do anything to support it
}