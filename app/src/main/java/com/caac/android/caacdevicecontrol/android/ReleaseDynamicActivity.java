package com.caac.android.caacdevicecontrol.android;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.Image;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import com.caac.android.caacdevicecontrol.R;

public class ReleaseDynamicActivity extends AppCompatActivity {
    private Activity context;
    private EditText etMeessage;
    private ImageView ivAddVideo;
    private ImageView ivAddImage;
    private AppCompatSpinner aspinGroup;
    private GridLayout glImageList;

    private View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_dynamic);
        context = this;

        initView();

    }

    private void initView(){
        glImageList = (GridLayout)findViewById(R.id.gl_image_list);
        ivAddImage = (ImageView)findViewById(R.id.iv_add_image);
        ivAddVideo = (ImageView)findViewById(R.id.iv_add_video);

        ivAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String items[] = new String[]{"拍摄照片", "从相册里选"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("请选择:");
                builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            Snackbar.make(null,0 + items[i], Snackbar.LENGTH_SHORT).show();
                        }else if(i == 1){
                            Snackbar.make(null,1 + items[i], Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
    }
}
