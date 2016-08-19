package com.caac.android.caacdevicecontrol.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.caac.android.caacdevicecontrol.R;
import com.caac.android.caacdevicecontrol.utils.ActivityController;

/**
 * activity的基类
 */
public class BaseActivity extends AppCompatActivity {
    public final String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        ActivityController.addActivity(this);
    }
}
