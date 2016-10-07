package com.caac.android.caacdevicecontrol.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
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
import com.caac.android.caacdevicecontrol.utils.ImageUtils;
import com.caac.android.caacdevicecontrol.utils.SharedPreferencesUtils;
import com.caac.android.caacdevicecontrol.utils.StringUtils;
import com.caac.android.caacdevicecontrol.view.CircleImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class RegisteActivity extends BaseActivity {
    private Activity context;
    private CircleImageView civAvatar;
    private AppCompatEditText etUsername, etRealName;
    private AppCompatSpinner spGroup;
    private AppCompatEditText etPhoneNumber, etPassword, etCommitPassword;
    private AppCompatButton btnCommit;

    private String userName, realName, phoneNumber, password, commitPassword;
    private String strGroup, strAvatar;
    private int groupPosition = 0;
    private User newUser;
    private ProgressDialog progressDialog;

    private static int REQUEST_CODE_CAPTURE_CAMEIA = 0;
    private static int REQUEST_CODE_PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);
        context = this;
        initView();
    }

    private void initView(){
        civAvatar = (CircleImageView)findViewById(R.id.civ_avatar);
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


        civAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChooseDialog();
            }
        });

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

    private void showChooseDialog(){
        String items[] = new String[]{"拍摄照片", "从相册里选"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("请选择:");
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                if (i == 0) {
                    getImageFromCamera();

                } else if (i == 1) {
                    getImageFromAlbum();

                }
            }
        });
        builder.create().show();
    }

    protected void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
            Log.e("url", imageUri.toString());
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);
        }
    }

    protected void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("requestCode", requestCode + "");
        if(resultCode == RESULT_OK){
            if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                if(data != null ){
                    Uri uri = data.getData();
                    Log.e(TAG, "uri=" + uri);
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    saveImage(bitmap, Environment.getExternalStorageDirectory()+"/image.jpg");

                    civAvatar.setImageBitmap(bitmap);

                    final BmobFile bmobFile = new BmobFile(new File(Environment.getExternalStorageDirectory()+"/image.jpg"));
                    bmobFile.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                Log.e("图片上传路径",bmobFile.getFileUrl());
                                strAvatar = bmobFile.getFileUrl();
                                newUser.setAvatar(strAvatar);
                            }else{
                                Toast.makeText(context,"文件上传失败!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
                Bitmap bitmap = ImageUtils.getimage(Environment.getExternalStorageDirectory()+"/image.jpg");
                saveImage(bitmap, Environment.getExternalStorageDirectory()+"/image.jpg");

                civAvatar.setImageBitmap(bitmap);

                final BmobFile bmobFile = new BmobFile(new File(Environment.getExternalStorageDirectory()+"/image.jpg"));
                bmobFile.upload(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e == null){
                            strAvatar = bmobFile.getFileUrl();
                            newUser.setAvatar(strAvatar);
                        }else{
                            Toast.makeText(context,"文件上传失败!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

    }

    public static boolean saveImage(Bitmap photo, String spath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(spath, false));
            photo.compress(Bitmap.CompressFormat.JPEG, 20, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean addNewUser(){
        progressDialog = ProgressDialog.show(context, "提示;", "注册中",true);
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
