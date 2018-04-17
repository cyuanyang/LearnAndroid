package com.tal.androidbase;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by shawn on 2018/4/13.
 *
 */

public class SparseArraySimple extends Activity implements View.OnClickListener {

    protected Button num100Btn;
    protected TextView s100View;
    protected TextView h100View;
    protected EditText numEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.layout_sparse_simple);

        SparseArrayCompat<Person> sparseArray = new SparseArrayCompat<>();

        Person person = new Person();
        person.name = "append";
        sparseArray.append(100, person);
        person.name = "put";
        sparseArray.put(200, person);

        int key = sparseArray.keyAt(0);
        Log.e("TAG", "key =" + key);
        Person value = sparseArray.get(key);
        Log.e("TAG", "value =" + value.name);


        int index = sparseArray.indexOfKey(100);
        person.name = "修改的1";
        sparseArray.setValueAt(index, person);
        person.name = "修改的2";
        sparseArray.put(200, person);
        Log.e("TAG", sparseArray.get(100).name);
        Log.e("TAG", sparseArray.get(200).name);
        person.name = "修改的3";
        sparseArray.append(200, person);
        Log.e("TAG", sparseArray.get(200).name);


        sparseArray.put(1000, person);
        sparseArray.put(1001, person);
        sparseArray.put(1002, person);
        sparseArray.put(1003, person);

//        sparseArray.delete(100);
        sparseArray.remove(200);


        sparseArray.delete(1000);

        sparseArray.put(1000, person);

        Log.e("TAG", "size ==" + sparseArray.size());
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.num100Btn) {
//            putPk();
            getPk();
        }
    }

    private void initView() {
        num100Btn = findViewById(R.id.num100Btn);
        num100Btn.setOnClickListener(SparseArraySimple.this);
        s100View = findViewById(R.id.s100View);
        h100View = findViewById(R.id.h100View);
        numEditText = (EditText) findViewById(R.id.numEditText);
    }

    private void updateS100(long s) {
        s100View.setText(s + "");
    }

    private void updateH100(long h) {
        h100View.setText(h + "");
    }

    private int MAX = 0;

    private void putPk() {
        MAX = Integer.parseInt(numEditText.getText()+"");
        Log.e("TAG" , "num="+MAX);
        final Person p = new Person();
        new Thread(new Runnable() {
            @Override
            public void run() {
                long before = System.currentTimeMillis();
                SparseArray<Person> sparseArray = new SparseArray<>();
                for (int i = 0; i < MAX; i++) {
                    sparseArray.put(i, p);
                }
                final long useTime = System.currentTimeMillis() - before;

                Log.e("TAG", "sparseArray use itme = " + useTime);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                long before = System.currentTimeMillis();
                HashMap<Integer, Person> map = new HashMap<>();
                for (int i = 0; i < MAX; i++) {
                    map.put(i, p);
                }
                final long useTime = System.currentTimeMillis() - before;
                Log.e("TAG", "HashMap use itme = " + useTime);

            }
        }).start();
    }

    private void getPk(){
        MAX = Integer.parseInt(numEditText.getText()+"");
        Log.e("TAG" , "num="+MAX);
        SparseArray<Person> sparseArray = new SparseArray<>();
        HashMap<Integer , Person> hashMap = new HashMap<>();
        Person p = new Person();
        for (int i = 0; i < MAX; i++) {
            sparseArray.put(i , p);
            hashMap.put(i , p);
        }
        Log.e("TAG" , "sparseArray.size = "+sparseArray.size());
        Log.e("TAG" , "HashMap.size ="+hashMap.size());

        long before = System.currentTimeMillis();
        Person getP1 = sparseArray.get(MAX >>> 1);
        Log.e("TAG" , "sparseArray.get="+(System.currentTimeMillis() - before));
        before = System.currentTimeMillis();
        Person getP2 = hashMap.get(MAX >>> 1);
        Log.e("TAG" , "hashMap.get="+(System.currentTimeMillis() - before));
    }

    class Person {
        String name;
    }
}
