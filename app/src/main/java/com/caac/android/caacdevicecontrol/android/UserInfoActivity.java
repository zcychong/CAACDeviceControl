package com.caac.android.caacdevicecontrol.android;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caac.android.caacdevicecontrol.R;
import com.caac.android.caacdevicecontrol.entity.User;
import com.caac.android.caacdevicecontrol.utils.StringUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener{
    private final String TAG = getClass().getSimpleName();
    private Activity context;
    private LinearLayout llNickName, llRelaName, llGroup, llPhoneNumber, llPassWord;
    private TextView tvNickName, tvRealName, tvGroup, tvPhone;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        context = this;

        initView();

        initData();
    }

    private void initView(){
        llNickName = (LinearLayout)findViewById(R.id.ll_nick_name);
        llRelaName = (LinearLayout)findViewById(R.id.ll_user_name);
        llGroup = (LinearLayout)findViewById(R.id.ll_group);
        llPhoneNumber = (LinearLayout)findViewById(R.id.ll_phone);
        llPassWord = (LinearLayout)findViewById(R.id.ll_pass_word);

        tvNickName = (TextView)findViewById(R.id.tv_nick_name);
        tvRealName = (TextView)findViewById(R.id.tv_user_name);
        tvGroup = (TextView)findViewById(R.id.tv_group);
        tvPhone = (TextView)findViewById(R.id.tv_phone);

        llNickName.setOnClickListener(this);
        llRelaName.setOnClickListener(this);
        llGroup.setOnClickListener(this);
        llPhoneNumber.setOnClickListener(this);
        llPassWord.setOnClickListener(this);

    }

    private void initData(){
        user = User.getCurrentUser(User.class);
        if(user != null){
            tvNickName.setText(user.getUsername());
            tvRealName.setText(user.getRealName());
            tvGroup.setText(user.getGroup());
            tvPhone.setText(user.getMobilePhoneNumber());
        }else{
            Log.e(TAG, "没有缓存用户信息");
        }

    }

    @Override
    public void onClick(View view){
        int type;
        switch (view.getId()){
            case R.id.ll_nick_name:
                type = 0;
                showUpdateDialog(type);
                break;
            case R.id.ll_user_name:
                type = 1;
                showUpdateDialog(type);
                break;
            case R.id.ll_group:
                type = 3;
                showChangeGroupDialog(type);
                break;
            case R.id.ll_phone:
                type = 2;
                showUpdateDialog(type);
                break;
            case R.id.ll_pass_word:
                type = 4;
                showUpdatePasswordDialog(type);
                break;
        }
    }

    private void showUpdateDialog(final int type){

        String info = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(type == 0){
            info = "昵称";
        }else if(type == 1){
            info = "姓名";
        }else if(type == 2){
            info = "手机号码";
        }
        builder.setTitle("修改" + info);

        View root = LayoutInflater.from(context).inflate(R.layout.dialog_change_info,null);
        TextView tvHint = (TextView)root.findViewById(R.id.tv_hint);
        tvHint.setText("请输入新的" + info);
        final EditText etInfo = (EditText)root.findViewById(R.id.et_info);
        if(type == 0){
            etInfo.setText(user.getUsername());
        }else if(type == 1){
            etInfo.setText(user.getRealName());
        }else if(type == 2){
            etInfo.setText(user.getMobilePhoneNumber());
        }

        builder.setView(root);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(StringUtils.isEmpty(etInfo.getText().toString())){
                    Toast.makeText(context, "新的用户信息不能为空!", Toast.LENGTH_SHORT).show();
                }else{
                    upDateUserInfo(type, etInfo.getText().toString());
                }

                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    private void showChangeGroupDialog(final int type){
        final int[] index = new int[1];

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("修改科室");
        builder.setSingleChoiceItems(R.array.group_list_no_hint, 0,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                index[0] = i;
//                Log.e(TAG, getResources().getStringArray(R.array.group_list)[i]);
//                upDateUserInfo(type, getResources().getStringArray(R.array.group_list)[i]);
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                upDateUserInfo(type, getResources().getStringArray(R.array.group_list)[index[0]]);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
//        AlertDialog dialog = builder.create();
//
//        dialog.show();
//        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundResource(R.color.gary_bg);
    }

    private void showUpdatePasswordDialog(final int type){
        String info = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("修改密码" + info);

        View root = LayoutInflater.from(context).inflate(R.layout.dialog_change_password, null);
        final EditText etOldPassword = (EditText)root.findViewById(R.id.et_old_password);
        final EditText etNewPassword = (EditText)root.findViewById(R.id.et_new_password);

        builder.setView(root);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(StringUtils.isEmpty(etOldPassword.getText().toString()) || StringUtils.isEmpty(etNewPassword.getText().toString())){
                    Toast.makeText(context, "新旧密码都不能为空哦!", Toast.LENGTH_SHORT).show();
                }else{
                    upDateUserInfo(type, etOldPassword.getText().toString(), etNewPassword.getText().toString());
                }

                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    private void upDateUserInfo(int type, String info){
        User newUser = new User();
        if(type == 0){
            newUser.setUsername(info);
        }else if(type == 1){
            newUser.setRealName(info);
        }else if(type == 2){
            newUser.setMobilePhoneNumber(info);
        }else if(type == 3){
            newUser.setGroup(info);
        }

        newUser.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    initData();
                    Toast.makeText(context, "修改成功!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "修改失败!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void upDateUserInfo(int type, String oldPassword, String newPassword){
        if(type == 4){
            User.updateCurrentUserPassword(oldPassword, newPassword, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e == null){
                        Toast.makeText(context, "用户登录密码修改成功!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "用户登录密码修改失败!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}
