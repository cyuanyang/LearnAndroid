package com.xstudy.javathread;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by chenyuanyang on 2018/3/21.
 *
 *
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    protected Button lockBtn;
    protected Button syncBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_home);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.lockBtn) {
            goActivity(MainActivity.class);
        } else if (view.getId() == R.id.syncBtn) {
            goActivity(SyncActivity.class);
        }
    }

    private void initView() {
        lockBtn = findViewById(R.id.lockBtn);
        lockBtn.setOnClickListener(HomeActivity.this);
        syncBtn = findViewById(R.id.syncBtn);
        syncBtn.setOnClickListener(HomeActivity.this);
    }

    private void goActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
    }
}
