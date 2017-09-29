package com.cyy.animatesimple.svganimate;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;

import com.cyy.animatesimple.R;

/**
 * Created by study on 17/9/28.
 *
 */

public class SVGAnimations extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.svg_animation_layout);

        appCompatImageView();
        imageView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            api21();
        }else {
            View api21Layout = findViewById(R.id.api21Layout);
            api21Layout.setVisibility(View.GONE);
        }
        simple1();


    }

    private void simple1(){
        final ImageView simple1View = (ImageView) findViewById(R.id.simple1View);
        final AnimatedVectorDrawableCompat cancel2Search =
                AnimatedVectorDrawableCompat.create(this , R.drawable.cancel_to_search);
        final AnimatedVectorDrawableCompat search2Cancel =
                AnimatedVectorDrawableCompat.create(this , R.drawable.search_to_cancel);

        simple1View.setImageDrawable(search2Cancel);
        simple1View.setTag(1);
        search2Cancel.start();

        findViewById(R.id.simple1StartBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatedVectorDrawableCompat d;
                int tag = (int) simple1View.getTag();
                if (tag == 1){
                    d = cancel2Search;
                    simple1View.setTag(0);
                }else {
                    d = search2Cancel;
                    simple1View.setTag(1);
                }
                simple1View.setImageDrawable(d);
                d.start();
            }
        });
    }

    /**
     * 21以上或者21
     */
    private void api21(){
        final AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) ActivityCompat.getDrawable(this , R.drawable.animate_mouse);
        ImageView iv = (ImageView) findViewById(R.id.imageView21);
        iv.setImageDrawable(animatedVectorDrawable);
        animatedVectorDrawable.start();
        findViewById(R.id.imageViewStartBtn21).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatedVectorDrawable.start();
            }
        });

    }

    private void appCompatImageView(){
        AppCompatImageView iv = (AppCompatImageView) findViewById(R.id.imageView);
        final AnimatedVectorDrawableCompat animatedVectorDrawableCompat =
                AnimatedVectorDrawableCompat.create(this , R.drawable.animate_mouse);
        iv.setImageDrawable(animatedVectorDrawableCompat);
        animatedVectorDrawableCompat.start();

        findViewById(R.id.appCompatImageViewStartBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatedVectorDrawableCompat.start();
            }
        });
    }

    private void imageView(){
        ImageView iv = (ImageView) findViewById(R.id.imageView2);
        final AnimatedVectorDrawableCompat animatedVectorDrawableCompat =
                AnimatedVectorDrawableCompat.create(this , R.drawable.animate_mouse);
        iv.setImageDrawable(animatedVectorDrawableCompat);
        animatedVectorDrawableCompat.start();
        findViewById(R.id.imageViewStartBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatedVectorDrawableCompat.start();
            }
        });
    }
}
