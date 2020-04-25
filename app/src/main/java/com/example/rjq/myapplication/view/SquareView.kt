package com.example.rjq.myapplication.view

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView

class SquareView : View {
    constructor(context: Context): super(context)
    constructor(context: Context, attributes: AttributeSet): super(context, attributes)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        setMeasuredDimension(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))
//        val spec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        setMeasuredDimension(getDefaultSize(suggestedMinimumHeight, heightMeasureSpec), getDefaultSize(suggestedMinimumHeight, heightMeasureSpec))
        var size = 0

        var width = getMeasuredWidth()
        var height = getMeasuredHeight()

        if (width > height) {
            size = height
        } else {
            size = width
        }

        setMeasuredDimension(size, size)
    }
}