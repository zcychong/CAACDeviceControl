package com.caac.android.caacdevicecontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class LoginActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    private Activity context;

    private AppCompatButton acbtnLogin;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        initView();
    }

    private void initView(){

//        toolbar = (Toolbar)findViewById(R.id.toolbar);
////        toolbar.setTitle(R.string.login);
//        toolbar.setNavigationIcon(R.mipmap.icon_back);
//        toolbar.setNavigationContentDescription("标题");
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        acbtnLogin = (AppCompatButton)findViewById(R.id.btn_login);
        acbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, MainActivity.class));
            }
        });
    }
}
