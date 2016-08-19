package com.caac.android.caacdevicecontrol.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.caac.android.caacdevicecontrol.R;
import com.caac.android.caacdevicecontrol.entity.User;
import com.caac.android.caacdevicecontrol.utils.ActivityController;
import com.caac.android.caacdevicecontrol.utils.SharedPreferencesUtils;
import com.caac.android.caacdevicecontrol.utils.StringUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    private Activity context;
    private TextInputEditText tetUserName, tetPassword;
    private String userName, password;
    private AppCompatButton acbtnLogin;
    private Toolbar toolbar;
    private User user;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        initView();

        initData();
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

        user = new User();


        tetUserName = (TextInputEditText)findViewById(R.id.tet_username);
        tetPassword = (TextInputEditText)findViewById(R.id.tet_password);

        acbtnLogin = (AppCompatButton)findViewById(R.id.btn_login);
        acbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userName = tetUserName.getText().toString().replaceAll(" ", "");
                password = tetPassword.getText().toString().replaceAll(" ", "");
                if(StringUtils.isNotEmpty(userName)){

                }else{
                    Toast.makeText(context,"用户名/手机号不能为空哦!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(StringUtils.isNotEmpty(password)){

                }else{
                    Toast.makeText(context,"密码不能为空哦!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = ProgressDialog.show(context, "", "登录中",true);
                User.loginByAccount(userName, password, new LogInListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        progressDialog.dismiss();
                        if(user!=null){
                            Log.i("smile","用户登陆成功");
                            ActivityController.finishAll();
                            SharedPreferencesUtils.setParam(context, "userId", user.getObjectId());
                            startActivity(new Intent(context, MainActivity.class));
                        }else{
                            Toast.makeText(context,e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });
    }

    private void initData(){

    }
}
