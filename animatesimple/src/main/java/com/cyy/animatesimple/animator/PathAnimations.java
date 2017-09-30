
package com.cyy.animatesimple.animator;

import android.animation.ObjectAnimator;
import android.animation.TypeConverter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.cyy.animatesimple.R;

public class PathAnimations extends Activity implements
        RadioGroup.OnCheckedChangeListener, View.OnLayoutChangeListener {

    final static Path sTraversalPath = new Path();
    final static float TRAVERSE_PATH_SIZE = 7.0f;

    final static Property<PathAnimations, Point> POINT_PROPERTY
            = new Property<PathAnimations, Point>(Point.class, "point") {
        @Override
        public Point get(PathAnimations object) {
            View v = object.findViewById(R.id.moved_item);
            return new Point(Math.round(v.getX()), Math.round(v.getY()));
        }

        @Override
        public void set(PathAnimations object, Point value) {
            object.setCoordinates(value.x, value.y);
        }
    };

    static {

        sTraversalPath.moveTo(100,100);
        sTraversalPath.lineTo(500,100);
        sTraversalPath.lineTo(500 ,600);
        sTraversalPath.lineTo(100,600);
        sTraversalPath.close();
    }

    private CanvasView mCanvasView;

    private ObjectAnimator mAnimator;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.path_animations);
        mCanvasView = (CanvasView) findViewById(R.id.canvas);
        mCanvasView.addOnLayoutChangeListener(this);
        ((RadioGroup) findViewById(R.id.path_animation_type)).setOnCheckedChangeListener(this);
    }

    public void setCoordinates(int x, int y) {
        changeCoordinates((float) x, (float) y);
    }

    public void changeCoordinates(float x, float y) {
        View v = findViewById(R.id.moved_item);
        v.setX(x);
        v.setY(y);
    }

    public void setPoint(PointF point) {
        changeCoordinates(point.x, point.y);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        startAnimator(checkedId);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
                               int oldTop, int oldRight, int oldBottom) {
        int checkedId = ((RadioGroup)findViewById(R.id.path_animation_type)).getCheckedRadioButtonId();
        if (checkedId != RadioGroup.NO_ID) {
            startAnimator(checkedId);
        }
    }

    private void startAnimator(int checkedId) {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }

        View view = findViewById(R.id.moved_item);
        Path path = mCanvasView.getPath();
        if (path.isEmpty()) {
            return;
        }

        switch (checkedId) {
            case R.id.named_components:
                // Use the named "x" and "y" properties for individual (x, y)
                // coordinates of the Path and set them on the view object.
                // The setX(float) and setY(float) methods are called on view.
                // An int version of this method also exists for animating
                // int Properties.
                mAnimator = ObjectAnimator.ofFloat(view, "x", "y", path);
                break;
            case R.id.property_components:
                // Use two Properties for individual (x, y) coordinates of the Path
                // and set them on the view object.
                // An int version of this method also exists for animating
                // int Properties.
                mAnimator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);
                break;
            case R.id.multi_int:
                // Use a multi-int setter to animate along a Path. The method
                // setCoordinates(int x, int y) is called on this during the animation.
                // Either "setCoordinates" or "coordinates" are acceptable parameters
                // because the "set" can be implied.
                mAnimator = ObjectAnimator.ofMultiInt(this, "setCoordinates", path);
                break;
            case R.id.multi_float:
                // Use a multi-float setter to animate along a Path. The method
                // changeCoordinates(float x, float y) is called on this during the animation.
                mAnimator = ObjectAnimator.ofMultiFloat(this, "changeCoordinates", path);
                break;
            case R.id.named_setter:
                // Use the named "point" property to animate along the Path.
                // There must be a method setPoint(PointF) on the animated object.
                // Because setPoint takes a PointF parameter, no TypeConverter is necessary.
                // In this case, the animated object is PathAnimations.
                mAnimator = ObjectAnimator.ofObject(this, "point", null, path);
                break;
            case R.id.property_setter:
                // Use the POINT_PROPERTY property to animate along the Path.
                // POINT_PROPERTY takes a Point, not a PointF, so the TypeConverter
                // PointFToPointConverter is necessary.
                mAnimator = ObjectAnimator.ofObject(this, POINT_PROPERTY,
                        new PointFToPointConverter(), path);
                break;
        }

        mAnimator.setDuration(10000);
//        mAnimator.setRepeatMode(Animation.RESTART);
//        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.start();
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Object o = animation.getAnimatedValue();
                Log.i("onAnimationUpdate" , "o="+o);

            }
        });
    }

    public static class CanvasView extends FrameLayout {

        Paint mPathPaint = new Paint();

        public CanvasView(Context context) {
            super(context);
            init();
        }

        public CanvasView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public CanvasView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }

        private void init() {
            setWillNotDraw(false);
            mPathPaint.setColor(0xFFFF0000);
            mPathPaint.setStrokeWidth(2.0f);
            mPathPaint.setStyle(Paint.Style.STROKE);
        }

        public Path getPath() {
            return sTraversalPath;
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawPath(sTraversalPath, mPathPaint);
            super.draw(canvas);
        }
    }

    private static class PointFToPointConverter extends TypeConverter<PointF, Point> {
        Point mPoint = new Point();

        public PointFToPointConverter() {
            super(PointF.class, Point.class);
        }

        @Override
        public Point convert(PointF value) {
            mPoint.set(Math.round(value.x), Math.round(value.y));
            return mPoint;
        }
    }
}