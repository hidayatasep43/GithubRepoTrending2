package com.example.githubrepotrending.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.githubrepotrending.R;

public class CircleView extends View {

    Paint p;
    int color;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // real work here
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Circle,
                0, 0
        );

        try {
            color = a.getColor(R.styleable.Circle_circleColor, 0xff000000);
        } finally {
            // release the TypedArray so that it can be reused.
            a.recycle();
        }
        init();
    }

    public void init() {
        p = new Paint();
        p.setColor(color);
    }

    public void setColor(String color) {
        if (color != null && !color.isEmpty()) {
            p.setColor(Color.parseColor(color));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas != null) {
            canvas.drawCircle(getHeight() / 2, getWidth() / 2, getWidth() / 2, p);
        }
    }

}
