package com.xstudy.javathread;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SynchronizedTest {

    @Test
    public void testSynchronizedMethod() throws Exception {

        final View view = new View();

        final View view1 = new View();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 3; i++) {

            final int finalI = i;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (finalI == 0){
                        view.say1(Thread.currentThread().getName());
                    }else if (finalI == 1){
                        view.say2(Thread.currentThread().getName());
                    }else {
                        view1.say3(Thread.currentThread().getName());
                    }

                }
            } , "t-"+i);
            threads.add(t);
        }

        for (Thread thread: threads) {
            thread.start();
        }

        synchronized (this){
            wait();
        }
    }

    @Test
    public void testSynchronizedStaticMethod() throws Exception {
        final View view = new View();

        final View view1 = new View();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 3; i++) {

            final int finalI = i;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (finalI == 0){
                        view.say4(Thread.currentThread().getName());
                    }else if (finalI == 1){
                        view.say2(Thread.currentThread().getName());
                    }else {
                        view1.say4(Thread.currentThread().getName());
                    }

                }
            } , "t-"+i);
            threads.add(t);
        }

        for (Thread thread: threads) {
            thread.start();
        }

        synchronized (this){
            wait();
        }

    }

}

class View {

    int i=0;

    static int j =0;

    public synchronized void say1(String name) {
        i = 0;
        while (i<10) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("11111111111 来自thread =" + name);
            i++;
        }
    }


    public void say2(String name) {
        while (true) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("22222222222 来自thread =" + name);
        }
    }

    public synchronized void say3(String name) {
        i = 0;
        while (i<10) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("33333333333 来自thread =" + name);
            i++;
        }
    }

    public static synchronized void say4(String name) {
        j = 0;
        while (j<10) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("4444444444 来自thread =" + name);
            j++;
        }
    }
}

