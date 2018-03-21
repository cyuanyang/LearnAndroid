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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected Button reentrantLockBtn;
    protected LinearLayout contentLayout;
    protected Button clearBtn;
    protected ScrollView scrollView;
    protected Button writeLockBtn;
    protected Button readLockBtn;
    protected Button readWriteLockBtn;
    private Lock lock = new ReentrantLock();
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private List<String> cacheList = new ArrayList<>();

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

    private void writeLockTest() {

        final Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                writeSomething();
            }
        }, "t1");
        t1.start();


        final Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                writeSomething();
            }
        }, "t2");
        t2.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    t1.join();
                    t2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendMessage("写完了");
                for (String s : cacheList) {
                    sendMessage(s);
                }
            }
        }).start();
    }

    private void readLockTest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                readSomething();
            }
        }, "t1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                readSomething();
            }
        }, "t2").start();
    }

    private void readWriteLockTest(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeSomething();
            }
        }, "t1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                readSomething();
            }
        }, "t2").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                writeSomething();
            }
        }, "t3").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                readSomething();
            }
        }, "t4").start();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.reentrantLockBtn) {
            lockTest();
        } else if (view.getId() == R.id.clearBtn) {
            contentLayout.removeAllViews();
        } else if (view.getId() == R.id.readLockBtn) {
            readLockTest();
        } else if (view.getId() == R.id.writeLockBtn) {
            writeLockTest();
        } else if (view.getId() == R.id.readWriteLockBtn) {
            readWriteLockTest();
        }
    }

    private void initView() {
        reentrantLockBtn = findViewById(R.id.reentrantLockBtn);
        reentrantLockBtn.setOnClickListener(MainActivity.this);
        contentLayout = findViewById(R.id.contentLayout);
        clearBtn = findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(MainActivity.this);
        scrollView = findViewById(R.id.scrollView);
        writeLockBtn = findViewById(R.id.writeLockBtn);
        writeLockBtn.setOnClickListener(MainActivity.this);
        readLockBtn = findViewById(R.id.readLockBtn);
        readLockBtn.setOnClickListener(MainActivity.this);
        readWriteLockBtn = (Button) findViewById(R.id.readWriteLockBtn);
        readWriteLockBtn.setOnClickListener(MainActivity.this);
    }

    //一次访问这个函数
    private void doSomething() {
        lock.lock();

        try {
            Thread.sleep(100);
            sendMessage(Thread.currentThread().getName() + " doSomething");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    private void readSomething() {
        readWriteLock.readLock().lock();
        try {
            Iterator<String> iterator = cacheList.iterator();
            while (iterator.hasNext()) {
                Thread.sleep(100);
                sendMessage(Thread.currentThread().getName() + "读取 -- "+ iterator.next());
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            readWriteLock.readLock().unlock();
        }
    }

    private void writeSomething() {
        readWriteLock.writeLock().lock();
        try{
            for (int i = 0; i < 10; i++) {
                Thread.sleep(100);
                cacheList.add("这是第" + i + "元素");
                sendMessage(Thread.currentThread().getName() + "写入---这是第" + i + "元素");
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            readWriteLock.writeLock().unlock();
        }
    }


    private void sendMessage(String content) {
        Message message = mHandler.obtainMessage();
        message.obj = content;
        message.sendToTarget();
    }
}
