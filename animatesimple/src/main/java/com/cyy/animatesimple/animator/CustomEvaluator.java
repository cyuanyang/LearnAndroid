package com.cyy.animatesimple.animator;


 import android.animation.ObjectAnimator;
 import android.animation.TypeEvaluator;
 import android.animation.ValueAnimator;

 import java.util.ArrayList;
 import java.util.Random;

 import android.app.Activity;
 import android.content.Context;
 import android.graphics.Canvas;
 import android.graphics.Color;
 import android.graphics.Paint;
 import android.graphics.Path;
 import android.graphics.PointF;
 import android.os.Bundle;
 import android.view.View;
 import android.widget.Button;
 import android.widget.LinearLayout;

 import com.cyy.animatesimple.R;

public class CustomEvaluator extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_evaluator);
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        final MyAnimationView animView = new MyAnimationView(this);
        container.addView(animView);

        Button starter = (Button) findViewById(R.id.startButton);
        starter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                animView.startAnimation();
            }
        });
    }

    public class XYHolder {
        private float mX;
        private float mY;

        public XYHolder(float x, float y) {
            mX = x;
            mY = y;
        }

        public float getX() {
            return mX;
        }

        public void setX(float x) {
            mX = x;
        }

        public float getY() {
            return mY;
        }

        public void setY(float y) {
            mY = y;
        }
    }


    public class BezierEvaluator implements TypeEvaluator<XYHolder> {


        private PointF pointF1;
        private PointF pointF2;
        public BezierEvaluator(PointF pointF1,PointF pointF2){
            this.pointF1 = pointF1;
            this.pointF2 = pointF2;
        }
        @Override
        public XYHolder evaluate(float time, XYHolder startValue,
                                 XYHolder endValue) {

            float timeLeft = 1.0f - time;

            float x = timeLeft * timeLeft * timeLeft * (startValue.getX())
                    + 3 * timeLeft * timeLeft * time * (pointF1.x)
                    + 3 * timeLeft * time * time * (pointF2.x)
                    + time * time * time * (endValue.getX());

             float y = timeLeft * timeLeft * timeLeft * (startValue.getY())
                    + 3 * timeLeft * timeLeft * time * (pointF1.y)
                    + 3 * timeLeft * time * time * (pointF2.y)
                    + time * time * time * (endValue.getY());
            return new XYHolder(x , y);
        }
    }


    public class MyAnimationView extends View  {
        ValueAnimator bounceAnim = null;
        Paint pathPaint;
        Paint controllPaint = new Paint();
        Path path = new Path();
        Random random = new Random();

        PointF controllPosint1 , controllPosint2;

        public MyAnimationView(Context context) {
            super(context);

            pathPaint = new Paint();
            pathPaint.setColor(Color.RED);
            pathPaint.setStrokeWidth(5);
            pathPaint.setStyle(Paint.Style.STROKE);

            controllPaint.setColor(Color.GREEN);
            controllPaint.setStyle(Paint.Style.FILL);
            controllPaint.setStrokeWidth(20);

            path.moveTo(0 , 0);
        }

        private void createAnimation() {
            if (bounceAnim!=null){
                bounceAnim.cancel();
            }
            XYHolder startXY = new XYHolder(0f, 0f);
            XYHolder endXY = new XYHolder(300f, 500f);
            controllPosint1 = getControllPoint(2);
            controllPosint2 = getControllPoint(1);
            bounceAnim = ObjectAnimator.ofObject(this, "xY",
                    new BezierEvaluator(controllPosint1 , controllPosint2),startXY, endXY);
            bounceAnim.setDuration(1500);
        }

        private PointF getControllPoint(int i){
            float x = random.nextInt(300);
            float y = random.nextInt(500)/i ;
            return new PointF(x, y);
        }

        public void setXY(XYHolder xyHolder){
            path.lineTo(xyHolder.getX() , xyHolder .getY());
            invalidate();
        }

        public void startAnimation() {
            path = new Path();
            path.moveTo(0,0);
            createAnimation();
            bounceAnim.start();
        }


        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath( path, pathPaint);

            if (controllPosint1!=null){
                canvas.drawPoint(controllPosint1.x , controllPosint1.y , controllPaint);
                canvas.drawPoint(controllPosint2.x , controllPosint2.y , controllPaint);
            }

        }

    }
}
