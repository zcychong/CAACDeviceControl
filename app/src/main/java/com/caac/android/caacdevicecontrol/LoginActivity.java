package com.caac.android.caacdevicecontrol;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class LoginActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    private Activity context;

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        initView();
    }

    private void initView(){

        toolbar = (Toolbar)findViewById(R.id.toolbar);
    }
}
