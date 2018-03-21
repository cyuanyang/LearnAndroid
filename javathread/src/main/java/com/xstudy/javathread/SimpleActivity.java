package com.xstudy.javathread;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by chenyuanyang on 2018/3/21.
 *
 * 统一的带输出的activity
 */

public abstract class SimpleActivity extends AppCompatActivity implements View.OnClickListener {

    protected LinearLayout operationLayout;
    protected LinearLayout contentLayout;
    protected ScrollView scrollView;
    protected Button clearBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_simple);
        initView();

        int layoutRes = getOperationLayout();
        if (layoutRes != 0){
            LayoutInflater.from(this).inflate(layoutRes , operationLayout);
        }
    }

    protected @LayoutRes int getOperationLayout(){
        return  0;
    }

    public void outputMsg(String msg){
        TextView textView = new TextView(this);
        textView.setText(msg);
        contentLayout.addView(textView);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    public void clearOutput(){
        contentLayout.removeAllViews();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.clearBtn) {
            clearOutput();
        }
    }

    private void initView() {
        operationLayout = findViewById(R.id.operationLayout);
        contentLayout = findViewById(R.id.contentLayout);
        scrollView = findViewById(R.id.scrollView);
        clearBtn = findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(SimpleActivity.this);
    }


    private void goActivity(Class clazz){
        startActivity(new Intent(this , clazz));
    }
}
