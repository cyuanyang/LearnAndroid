package com.xstudy.javathread;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by chenyuanyang on 2018/3/21.
 */

public class SyncActivity extends SimpleActivity implements View.OnClickListener {

    protected Button countDownLatchBtn;
    protected Button cyclicBarrierBtn;
    private Semaphore semaphore = new Semaphore(0, true);
    private List<Runnable> tasks = Collections.synchronizedList(new ArrayList<Runnable>());

    private Executor executor = Executors.newFixedThreadPool(10);

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
        } else if (view.getId() == R.id.countDownLatchBtn) {
            countDownLatchTest();
        } else if (view.getId() == R.id.cyclicBarrierBtn) {
            cyclicBarrierTest();
        }
    }

    private void initView() {
        semaphoreBtn = findViewById(R.id.semaphoreBtn);
        semaphoreBtn.setOnClickListener(SyncActivity.this);
        countDownLatchBtn = (Button) findViewById(R.id.countDownLatchBtn);
        countDownLatchBtn.setOnClickListener(SyncActivity.this);
        cyclicBarrierBtn = (Button) findViewById(R.id.cyclicBarrierBtn);
        cyclicBarrierBtn.setOnClickListener(SyncActivity.this);
    }


    private void semaphoreTest() {
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            tasks.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    outputMsgSafely("我是任务" + finalI);
                    semaphore.release();
                }
            });
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!tasks.isEmpty()) {
                        executor.execute(tasks.remove(0));
                        semaphore.acquire();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private CountDownLatch countDownLatch = new CountDownLatch(2);

    private void countDownLatchTest() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                outputMsgSafely("count task 1 begin");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outputMsgSafely("count task 1 end");
                countDownLatch.countDown();
                outputMsgSafely("剩下几个="+countDownLatch.getCount());
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                outputMsgSafely("count task 2 begin");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outputMsgSafely("count task 2 end");
                countDownLatch.countDown();
                outputMsgSafely("剩下几个="+countDownLatch.getCount());
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    outputMsgSafely("主线任务 等待任务1,2执行");
                    countDownLatch.await();
                    outputMsgSafely("任务1,2执行完毕");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
    private void cyclicBarrierTest() {
        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(finalI * 1000);
                        outputMsgSafely("玩家" + finalI + "等待游戏进入");
                        outputMsgSafely("cyclicBarrier.getParties=" + cyclicBarrier.getParties());
                        cyclicBarrier.await();
                        outputMsgSafely("玩家" + finalI + "进入游戏");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        outputMsgSafely("InterruptedException 玩家" + finalI );
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                        outputMsgSafely("BrokenBarrierException 玩家" + finalI);
                    }
                }
            });
        }
    }


}
