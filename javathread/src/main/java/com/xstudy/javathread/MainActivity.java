package com.xstudy.javathread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.locks.Condition;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(100*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();


    }

}
