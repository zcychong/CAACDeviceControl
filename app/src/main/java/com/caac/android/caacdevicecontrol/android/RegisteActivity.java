package com.caac.android.caacdevicecontrol.android;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.caac.android.caacdevicecontrol.R;
import com.caac.android.caacdevicecontrol.entity.User;
import com.caac.android.caacdevicecontrol.utils.StringUtils;

public class RegisteActivity extends BaseActivity {
    private Activity context;
    private AppCompatEditText etUsername, etRealName;
    private AppCompatSpinner spGroup;
    private AppCompatEditText etPhoneNumber, etPassword, etCommitPassword;
    private AppCompatButton btnCommit;

    private String userName, realName, phoneNumber, password, commitPassword;

    private User newUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);
        context = this;
        initView();
    }

    private void initView(){
        etUsername = (AppCompatEditText)findViewById(R.id.et_username);
        etRealName = (AppCompatEditText)findViewById(R.id.et_real_name);
        etPhoneNumber = (AppCompatEditText)findViewById(R.id.et_phone_number);
        etPassword = (AppCompatEditText)findViewById(R.id.et_password);
        etCommitPassword = (AppCompatEditText)findViewById(R.id.et_commit_password);
        spGroup = (AppCompatSpinner)findViewById(R.id.sp_group);
        btnCommit = (AppCompatButton)findViewById(R.id.acbtn_commit);
        newUser = new User();

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = etUsername.getText().toString().replaceAll(" ", "");
                if(StringUtils.isNotEmpty(userName)){
                    newUser.setUserName(userName);
                }else{
                    toast("用户名");
                }
            }
        });
    }

    private void toast(String str){
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
