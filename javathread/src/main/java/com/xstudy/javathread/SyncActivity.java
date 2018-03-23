package com.xstudy.javathread;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by chenyuanyang on 2018/3/21.
 *
 *
 */

public class SyncActivity extends SimpleActivity implements View.OnClickListener {

    protected Button semaphoreBtn;

    @Override
    protected int getOperationLayout() {
        return R.layout.activity_sync;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.semaphoreBtn) {
            semaphoreTest();
        }
    }

    private void initView() {
        semaphoreBtn = findViewById(R.id.semaphoreBtn);
        semaphoreBtn.setOnClickListener(SyncActivity.this);
    }
    private Semaphore semaphore = new Semaphore(0,true);
    private List<Runnable> tasks = Collections.synchronizedList(new ArrayList<Runnable>());

    private Executor executor = Executors.newFixedThreadPool(10);

    private void semaphoreTest(){

        for (int i = 0; i < 30; i++) {

            final int finalI = i;
            tasks.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    outputMsgSafely("我是任务"+ finalI);
                    semaphore.release();
                }
            });
        }

        executor.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    while (!tasks.isEmpty()){
                        executor.execute(tasks.remove(0));
                        semaphore.acquire();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private CountDownLatch countDownLatch = new CountDownLatch(1);

}
