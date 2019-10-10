package com.tal.androidbase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class DrawableView extends View {

    private Bitmap bitmap;

    public DrawableView(Context context) {
        this(context , null);
    }

    public DrawableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public DrawableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        bitmap = BitmapFactory.decodeResource(getResources() , R.mipmap.ic_launcher);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setColorFilter(new LightingColorFilter(Color.RED , Color.BLACK));

        bitmap = BitmapFactory.decodeResource(getResources() , R.drawable.ic_launcher12);
        canvas.drawBitmap(bitmap , 0 , 0,paint  ) ;
//        bitmap.recycle();
    }
}
