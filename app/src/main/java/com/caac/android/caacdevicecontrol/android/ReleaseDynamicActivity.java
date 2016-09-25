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
import com.caac.android.caacdevicecontrol.utils.ImageUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

public class ReleaseDynamicActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    private Activity context;
    private EditText etMeessage;
    private ImageView ivAddVideo;
    private ImageView ivAddImage;
    private LinearLayout llAddChoose;
    private AppCompatSpinner aspinGroup;
    private GridView glImageList;
    private static int REQUEST_CODE_CAPTURE_CAMEIA = 0;
    private static int REQUEST_CODE_PICK_IMAGE = 1;
    private View root;
    private List<Bitmap> bitmapList = new ArrayList<>();
    private ReleaseDynamicAdapter adapter;
    private Bitmap addBitmap;
    private List<String> imagePathList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_dynamic);
        context = this;

        initView();

    }

    private void initView() {
        glImageList = (GridView) findViewById(R.id.gv_image_list);
        ivAddImage = (ImageView) findViewById(R.id.iv_add_image);
        llAddChoose = (LinearLayout)findViewById(R.id.ll_add_choose);

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
    }

    private void showChooseDialog(){
        String items[] = new String[]{"拍摄照片", "从相册里选"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("请选择:");
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    getImageFromCamera();
                    dialogInterface.dismiss();
                } else if (i == 1) {
                    getImageFromAlbum();
                    dialogInterface.dismiss();
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
                    Bitmap bitmap = ImageUtils.getimage(uri + ".jpg");
                    saveImage(bitmap, Environment.getExternalStorageDirectory()+"/image.jpg");
                    if(bitmapList.size() != 0){
                        bitmapList.remove( bitmapList.size() - 1);
                    }
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
                if(bitmapList.size() > 1){
                    bitmapList.remove(bitmapList.size() -1);
                }
                addBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_add_image);
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
        public View getView(final int positoon, View view, ViewGroup viewGroup) {
            Log.e("positoon", positoon + "");
            if(view == null){
                view = LayoutInflater.from(context).inflate(R.layout.dynamic_item, null);
                viewHolder = new ViewHolder();
                viewHolder.ivImage = (ImageView)view.findViewById(R.id.iv_image);

                viewHolder.ivImage.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("提示!");
                        builder.setMessage("确定删除这张图片吗?");
                        builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                bitmapList.remove(positoon);
                                notifyDataSetChanged();
                                checkHasImage();
                            }
                        });
                        builder.create().show();
                        return true;
                    }
                });

                view.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)view.getTag();
            }



            viewHolder.ivImage.setImageBitmap(bitmapList.get(positoon));

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

}
