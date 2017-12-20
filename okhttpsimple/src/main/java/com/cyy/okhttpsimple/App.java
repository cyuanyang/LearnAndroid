package com.cyy.okhttpsimple;

import android.app.Application;

/**
 * Created by study on 17/11/21.
 */

public class App extends Application {

    static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }


    public static App getApp() {
        return app;
    }
}
