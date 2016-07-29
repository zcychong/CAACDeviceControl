package com.caac.android.caacdevicecontrol.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.caac.android.caacdevicecontrol.R;
import com.caac.android.caacdevicecontrol.entity.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

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
//                User user = new User();
//                user.setUserName("11");
//                user.setPassword("11");
//                user.save(new SaveListener<String>() {
//                    @Override
//                    public void done(String s, BmobException e) {
//                        if(e==null){
//                            Toast.makeText(context, "添加数据成功，s=" + s , Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(context, "创建数据失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

            }
        });
    }
}
