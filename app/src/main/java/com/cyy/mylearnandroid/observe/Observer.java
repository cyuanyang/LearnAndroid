package com.cyy.mylearnandroid.observe;

/**
 * Created by cyy on 2018/6/21.
 *
 *
 */

public interface Observer<T> {
    void update(T value);

    void onError(Exception e);
}
