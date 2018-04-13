package com.tal.androidbase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected Button sparseArraySimple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sparseArraySimple) {
            startActivity(new Intent(this , SparseArraySimple.class));
        }
    }

    private void initView() {
        sparseArraySimple = (Button) findViewById(R.id.sparseArraySimple);
        sparseArraySimple.setOnClickListener(MainActivity.this);
    }
}
