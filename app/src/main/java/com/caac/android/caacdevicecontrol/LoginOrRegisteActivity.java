package com.caac.android.caacdevicecontrol;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class LoginOrRegisteActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private Activity context;
    private AppCompatButton btnLogin;
    private Button btnRegiste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_registe);

        context = this;
        initView();
    }

    private void initView(){
        btnLogin = (AppCompatButton)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, LoginActivity.class));
            }
        });

        btnRegiste = (Button)findViewById(R.id.btn_registe);
    }


    public void registe(View v){
        startActivity(new Intent(this, RegisteActivity.class));
    }
}
