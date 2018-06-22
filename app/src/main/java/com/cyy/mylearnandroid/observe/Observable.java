package com.cyy.mylearnandroid.observe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyy on 2018/6/21.
 *
 */

public class Observable<T> {

    private List<Observer<T>> observers = new ArrayList<>();

    public void addObserver(Observer<T> observer){
        observers.add(observer);
    }

    public void notify(T value){
        for (Observer<T> o :observers) {
            o.update(value);
        }
    }

}


