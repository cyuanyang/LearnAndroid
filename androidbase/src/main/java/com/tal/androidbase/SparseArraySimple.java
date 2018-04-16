package com.tal.androidbase;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import android.util.SparseArray;

/**
 * Created by shawn on 2018/4/13.
 *
 */

public class SparseArraySimple extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_sparse_simple);

        SparseArrayCompat<Person> sparseArray = new SparseArrayCompat<>();

        Person person = new Person();
        person.name = "append";
        sparseArray.append(100 , person);
        person.name = "put";
        sparseArray.put(200 , person);

        int key = sparseArray.keyAt(0);
        Log.e("TAG" , "key ="+key);
        Person value = sparseArray.get(key);
        Log.e("TAG" , "value ="+value.name);


        int index = sparseArray.indexOfKey(100);
        person.name = "修改的1";
        sparseArray.setValueAt(index ,person );
        person.name = "修改的2";
        sparseArray.put(200 , person);
        Log.e("TAG" , sparseArray.get(100).name);
        Log.e("TAG" , sparseArray.get(200).name);
        person.name = "修改的3";
        sparseArray.append(200 , person);
        Log.e("TAG" , sparseArray.get(200).name);


        sparseArray.put(1000 , person);
        sparseArray.put(1001 , person);
        sparseArray.put(1002 , person);
        sparseArray.put(1003 , person);

//        sparseArray.delete(100);
        sparseArray.remove(200);


        sparseArray.delete(1000);

        sparseArray.put(1000 , person);

        Log.e("TAG" , "size ==" + sparseArray.size());
    }


    class Person{
        String name;
    }
}
