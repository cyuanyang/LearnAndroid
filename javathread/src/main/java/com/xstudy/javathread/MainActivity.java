package com.xstudy.javathread;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected Button reentrantLockBtn;
    protected LinearLayout contentLayout;
    protected Button clearBtn;
    protected ScrollView scrollView;
    private Lock lock = new ReentrantLock();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            TextView textView = new TextView(MainActivity.this);
            textView.setText((String) msg.obj);
            contentLayout.addView(textView);

            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);

        initView();
    }

    private void lockTest() {
        sendMessage("lockTest begin");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    doSomething();
                }
                sendMessage("thread-1 end");
            }
        }, "thread-1").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    doSomething();
                }
                sendMessage("thread-2 end");
            }
        }, "thread-2").start();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.reentrantLockBtn) {
            lockTest();
        } else if (view.getId() == R.id.clearBtn) {
            contentLayout.removeAllViews();
        }
    }

    private void initView() {
        reentrantLockBtn = findViewById(R.id.reentrantLockBtn);
        reentrantLockBtn.setOnClickListener(MainActivity.this);
        contentLayout = findViewById(R.id.contentLayout);
        clearBtn = findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(MainActivity.this);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
    }

    //一次访问这个函数
    private void doSomething() {
        lock.lock();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendMessage(Thread.currentThread().getName() + " doSomething");

        lock.unlock();
    }

    private void sendMessage(String content) {
        Message message = mHandler.obtainMessage();
        message.obj = content;
        message.sendToTarget();
    }
}
