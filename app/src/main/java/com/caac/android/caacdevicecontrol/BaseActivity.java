package com.caac.android.caacdevicecontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * activity的基类
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
}
