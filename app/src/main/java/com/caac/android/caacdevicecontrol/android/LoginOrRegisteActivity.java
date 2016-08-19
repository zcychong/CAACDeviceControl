package com.caac.android.caacdevicecontrol.android;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;

import com.caac.android.caacdevicecontrol.R;
import com.caac.android.caacdevicecontrol.entity.User;
import com.caac.android.caacdevicecontrol.utils.SharedPreferencesUtils;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

public class LoginOrRegisteActivity extends BaseActivity {
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

        //初始化Bomb
        initBmob();

        initData();
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

    private void initBmob(){
        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config =new BmobConfig.Builder(context)
        //设置appkey
        .setApplicationId("a16b1f5be547a96723fdb27602fa6840")
        //请求超时时间（单位为秒）：默认15s
        .setConnectTimeout(20)
        //文件分片上传时每片的大小（单位字节），默认512*1024
        .setUploadBlockSize(1024*1024)
        //文件的过期时间(单位为秒)：默认1800s
        .setFileExpiration(2500)
        .build();
        Bmob.initialize(config);
    }


    public void registe(View v){
        startActivity(new Intent(this, RegisteActivity.class));
    }

    private void initData(){
        User user = User.getCurrentUser(User.class);
        if(user != null){
            finish();
            SharedPreferencesUtils.setParam(context, "userId", user.getObjectId());
            startActivity(new Intent(context, MainActivity.class));
        }else{
            //缓存用户对象为空时， 可打开用户注册界面…
        }
    }
}
