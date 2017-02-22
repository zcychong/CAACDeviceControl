package com.caac.android.caacdevicecontrol.android;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.caac.android.caacdevicecontrol.R;
import com.caac.android.caacdevicecontrol.entity.Dynamic;
import com.caac.android.caacdevicecontrol.entity.User;
import com.caac.android.caacdevicecontrol.utils.ImageUtils;
import com.caac.android.caacdevicecontrol.utils.StringUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ReleaseDynamicActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    private Activity context;
    private EditText etMeessage;
    private ImageView ivAddVideo;
    private ImageView ivAddImage;
    private LinearLayout llAddChoose;
    private AppCompatButton acbtnCommit;
    private AppCompatSpinner aspinGroup;
    private GridView glImageList;
    private static int REQUEST_CODE_CAPTURE_CAMEIA = 0;
    private static int REQUEST_CODE_PICK_IMAGE = 1;
    private View root;
    private List<Bitmap> bitmapList = new ArrayList<>();
    private ReleaseDynamicAdapter adapter;
    private Bitmap addBitmap;
    private List<String> imagePathList = new ArrayList<>();

    private User user;

    private String group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_dynamic);
        context = this;

        initView();

        initData();
        user = User.getCurrentUser(User.class);

    }

    private void initView() {
        glImageList = (GridView) findViewById(R.id.gv_image_list);
        ivAddImage = (ImageView) findViewById(R.id.iv_add_image);
        llAddChoose = (LinearLayout)findViewById(R.id.ll_add_choose);
        etMeessage = (EditText)findViewById(R.id.et_message);
        aspinGroup = (AppCompatSpinner)findViewById(R.id.sp_group);
        acbtnCommit = (AppCompatButton)findViewById(R.id.acbtn_commit);

        adapter = new ReleaseDynamicAdapter();
        glImageList.setAdapter(adapter);

        glImageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(position == bitmapList.size()-1){
                    showChooseDialog();
                }
            }
        });

        ivAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChooseDialog();
            }
        });

        aspinGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                group = getResources().getStringArray(R.array.group_list_no_hint)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        acbtnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkInfo();

            }
        });
    }

    private void initData(){
        addBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_add_image);
    }

    private void checkInfo(){
        if(etMeessage.getText() != null){
            if(etMeessage.getText().toString().length() <= 0){
                toast("请输入故障信息!");
                return;
            }
        }

        if(StringUtils.isEmpty(group)){
            toast("请选择上报科室!");
            return;
        }

        Dynamic dy = new Dynamic();
        dy.setUserId(user.getObjectId());
        dy.setAvatar(user.getAvatar());
        dy.setUserName(user.getUsername());
        dy.setGroup(user.getGroup());
        dy.setImages(imagePathList);
        dy.setLeaveMsgCount(0);
        dy.setMessage(etMeessage.getText().toString());

        dy.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){ // 发布成功
                    toast("发布成功!");
                    setResult(1);
                    finish();
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
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
                    llAddChoose.setVisibility(View.GONE);
                    Uri uri = data.getData();
                    Log.e(TAG, "uri=" + uri);
//                    Bitmap bitmap = ImageUtils.getimage(uri + ".jpg");
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    saveImage(bitmap, Environment.getExternalStorageDirectory()+"/image.jpg");
                    if(bitmapList.size() != 0){
                        bitmapList.remove( bitmapList.size() - 1);
                    }
                    bitmapList.add(bitmap);
                    bitmapList.add(addBitmap);
                    adapter.notifyDataSetChanged();

                    final BmobFile bmobFile = new BmobFile(new File(Environment.getExternalStorageDirectory()+"/image.jpg"));
                    bmobFile.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                Log.e("图片上传路径",bmobFile.getFileUrl());
                                imagePathList.add(bmobFile.getFileUrl());
                            }else{
                                Toast.makeText(context,"文件上传失败!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
                llAddChoose.setVisibility(View.GONE);
                Bitmap bitmap = ImageUtils.getimage(Environment.getExternalStorageDirectory()+"/image.jpg");
                saveImage(bitmap, Environment.getExternalStorageDirectory()+"/image.jpg");
                if(bitmapList.size() != 0){
                    bitmapList.remove( bitmapList.size() - 1);
                }
                bitmapList.add(bitmap);

                bitmapList.add(addBitmap);
                adapter.notifyDataSetChanged();

                final BmobFile bmobFile = new BmobFile(new File(Environment.getExternalStorageDirectory()+"/image.jpg"));
                bmobFile.upload(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e == null){
                            Log.e("图片上传路径",bmobFile.getFileUrl());
                            imagePathList.add(bmobFile.getFileUrl());
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

    class ReleaseDynamicAdapter extends BaseAdapter{
        private ViewHolder viewHolder;

        @Override
        public int getCount() {
            return bitmapList.size();
        }

        @Override
        public Object getItem(int i) {
            return bitmapList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int posotion, View view, ViewGroup viewGroup) {
            Log.e("posotion", posotion + "");
            if(view == null){
                view = LayoutInflater.from(context).inflate(R.layout.dynamic_item, null);
                viewHolder = new ViewHolder();
                viewHolder.ivImage = (ImageView)view.findViewById(R.id.iv_image);

                view.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)view.getTag();
            }

            if(posotion != bitmapList.size() - 1){
                viewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("提示!");
                        builder.setMessage("确定删除这张图片吗?");
                        builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                bitmapList.remove(posotion);
                                notifyDataSetChanged();
                                checkHasImage();
                            }
                        });
                        builder.create().show();
                    }
                });

            }else {
                viewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          showChooseDialog();
                      }
                });

            }

            viewHolder.ivImage.setImageBitmap(bitmapList.get(posotion));

            return view;
        }

        class ViewHolder{
            ImageView ivImage;
        }
    }

    private void checkHasImage(){
        if(bitmapList.size() != 1){
            llAddChoose.setVisibility(View.GONE);
        }else{
            llAddChoose.setVisibility(View.VISIBLE);
        }
    }

    private void toast(String str){
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

}
