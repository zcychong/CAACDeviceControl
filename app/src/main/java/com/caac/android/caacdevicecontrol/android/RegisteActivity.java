package com.caac.android.caacdevicecontrol.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.caac.android.caacdevicecontrol.R;
import com.caac.android.caacdevicecontrol.entity.User;
import com.caac.android.caacdevicecontrol.utils.ActivityController;
import com.caac.android.caacdevicecontrol.utils.SharedPreferencesUtils;
import com.caac.android.caacdevicecontrol.utils.StringUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisteActivity extends BaseActivity {
    private Activity context;
    private AppCompatEditText etUsername, etRealName;
    private AppCompatSpinner spGroup;
    private AppCompatEditText etPhoneNumber, etPassword, etCommitPassword;
    private AppCompatButton btnCommit;

    private String userName, realName, phoneNumber, password, commitPassword;
    private String strGroup;
    private int groupPosition = 0;
    private User newUser;
    private ProgressDialog progressDialog;
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

        spGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                groupPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//        spGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                groupPosition = position;
//            }
//        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = etUsername.getText().toString().replaceAll(" ", "");
                realName = etRealName.getText().toString().replaceAll(" ", "");
                phoneNumber = etPhoneNumber.getText().toString().replaceAll(" ", "");
                password = etPassword.getText().toString().replaceAll(" ", "");
                commitPassword = etCommitPassword.getText().toString().replaceAll(" ", "");
                strGroup = getResources().getStringArray(R.array.group_list)[groupPosition];

                if(StringUtils.isNotEmpty(userName)){
                    newUser.setUsername(userName);
                }else{
                    toast(getResources().getString(R.string.username_not_null));
                    return ;
                }

                if(StringUtils.isNotEmpty(realName)){
                    newUser.setRealName(realName);
                }else{
                    toast(getResources().getString(R.string.realname_not_null));
                    return ;
                }

                if(StringUtils.isNotEmpty(phoneNumber)){
                    newUser.setMobilePhoneNumber(phoneNumber);
                }else{
                    toast(getResources().getString(R.string.phonenumber_not_null));
                    return ;
                }

                if(StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(password)){
                    if(password.equals(commitPassword)){
                        newUser.setPassword(password);
                    }else{
                        toast(getResources().getString(R.string.password_not_equal));
                        return ;
                    }
                }else{
                    toast(getResources().getString(R.string.password_not_null));
                    return ;
                }


                if(groupPosition != 0){
                    newUser.setGroup(strGroup);
                }else{
                    toast(getResources().getString(R.string.group_not_null));
                    return ;
                }

                addNewUser();

            }
        });
    }

    private boolean addNewUser(){
        progressDialog = ProgressDialog.show(context, "11", "22",true);
        newUser.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                progressDialog.dismiss();
                if(e != null){
                    Log.e("error", e.toString());
                }else{
                    ActivityController.finishAll();
                    SharedPreferencesUtils.setParam(context, "userId", user.getObjectId());
                    startActivity(new Intent(context,MainActivity.class));
                }
            }
        });
        return true;
    }

    private void toast(String str){
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
