package com.caac.android.caacdevicecontrol.android;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.caac.android.caacdevicecontrol.R;
import com.caac.android.caacdevicecontrol.entity.User;
import com.caac.android.caacdevicecontrol.fragment.ExceptionDynamicsFragment;
import com.caac.android.caacdevicecontrol.fragment.GroupDynamicsFragment;
import com.caac.android.caacdevicecontrol.fragment.MyFragmentPgerAdapter;
import com.caac.android.caacdevicecontrol.utils.ActivityController;
import com.caac.android.caacdevicecontrol.utils.ImageUtils;
import com.caac.android.caacdevicecontrol.utils.StringUtils;
import com.caac.android.caacdevicecontrol.view.CircleImageView;
import com.caac.android.caacdevicecontrol.view.ExchangeColorView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Activity context;
    private ViewPager vpList;
    private MyFragmentPgerAdapter myFragmentPgerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private ExceptionDynamicsFragment exceptionDynamicsFragment;
    private GroupDynamicsFragment groupDynamicsFragment;

    private LinearLayout llException, llGroup, llAdd;
    private ImageView ivException, ivGroup;
    private CircleImageView civAvatar;
    private TextView tvTabException, tvTabGroup;
    private View heardView;
    private User user;

    private ExchangeColorView ecExcwption, ecGroup;
    private List<ExchangeColorView> exchangeColorViewList = new ArrayList<>();

    private TextView tvUserName, tvGroup, tvPhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initView();

    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        heardView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        civAvatar = (CircleImageView)heardView.findViewById(R.id.civ_avatar);
        tvUserName = (TextView)heardView.findViewById(R.id.tv_name);
        tvPhoneNumber = (TextView)heardView.findViewById(R.id.tv_phone);
        tvGroup = (TextView)heardView.findViewById(R.id.tv_group);

        ecExcwption = (ExchangeColorView)findViewById(R.id.ec_exception_dynamics);
        ecGroup = (ExchangeColorView)findViewById(R.id.ec_group_dynamics);
        exchangeColorViewList.add(ecExcwption);
        exchangeColorViewList.add(ecGroup);
        ecExcwption.setIconAlpha(1.0f);

        ecExcwption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vpList.setCurrentItem(0);
            }
        });

        ecGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vpList.setCurrentItem(1);
            }
        });

        user = User.getCurrentUser(User.class);
        tvUserName.setText(user.getUsername());
        tvPhoneNumber.setText(user.getMobilePhoneNumber());
        tvGroup.setText(user.getGroup());

        if(StringUtils.isNotEmpty(user.getAvatar())){
            Glide.with(context).load(user.getAvatar()).into(civAvatar);
        }


        // 初始化Fragment
        exceptionDynamicsFragment = new ExceptionDynamicsFragment();
        groupDynamicsFragment = new GroupDynamicsFragment();

        if(exceptionDynamicsFragment == null){
            Log.e(TAG, "null");
        }
        fragments.add(exceptionDynamicsFragment);
        fragments.add(groupDynamicsFragment);



        vpList = (ViewPager)findViewById(R.id.vp_list);
        myFragmentPgerAdapter = new MyFragmentPgerAdapter(getSupportFragmentManager(), fragments);
        vpList.setAdapter(myFragmentPgerAdapter);
        vpList.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //监听滑动事件, 滑动的位置--->自定义控件的颜色透明度
                if(positionOffset > 0){
                    ExchangeColorView left =  exchangeColorViewList.get(position);
                    ExchangeColorView right =  exchangeColorViewList.get(position+1);
                    //positionOffset:划出去的偏移量(0~1)
                    left.setIconAlpha(1-positionOffset);
                    right.setIconAlpha(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    ivException.setImageResource(R.mipmap.icon_exception_choosed);
                    tvTabException.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    ivGroup.setImageResource(R.mipmap.icon_group_unchoose);
                    tvTabGroup.setTextColor(getResources().getColor(R.color.image_gray));
                }else if(position == 1){
                    vpList.setCurrentItem(1);
                    ivException.setImageResource(R.mipmap.icon_exception_unchoose);
                    tvTabException.setTextColor(getResources().getColor(R.color.image_gray));
                    ivGroup.setImageResource(R.mipmap.icon_group_choosed);
                    tvTabGroup.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //初始化按钮
        llException = (LinearLayout)findViewById(R.id.ll_exception);
        llException.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivException.setImageResource(R.mipmap.icon_exception_choosed);
                tvTabException.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                ivGroup.setImageResource(R.mipmap.icon_group_unchoose);
                tvTabGroup.setTextColor(getResources().getColor(R.color.image_gray));
                vpList.setCurrentItem(0);
            }
        });
        llAdd = (LinearLayout)findViewById(R.id.ll_add);
        llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(context, ReleaseDynamicActivity.class), 0);
            }
        });
        llGroup = (LinearLayout)findViewById(R.id.ll_group);
        llGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vpList.setCurrentItem(1);
                ivException.setImageResource(R.mipmap.icon_exception_unchoose);
                tvTabException.setTextColor(getResources().getColor(R.color.image_gray));
                ivGroup.setImageResource(R.mipmap.icon_group_choosed);
                tvTabGroup.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            }
        });

        ivException = (ImageView) findViewById(R.id.iv_exception);
        ivGroup = (ImageView) findViewById(R.id.iv_group);

        tvTabException = (TextView)findViewById(R.id.tv_exception);
        tvTabGroup = (TextView)findViewById(R.id.tv_tab_group);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if(resultCode == 1){
                Log.e(TAG, "新数据-刷新");
                if(exceptionDynamicsFragment != null){
                    exceptionDynamicsFragment.onRefresh();
                }
                if(groupDynamicsFragment != null){
                    groupDynamicsFragment.onRefresh();
                }
            }
        }
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user_info) {
            startActivity(new Intent(context, UserInfoActivity.class));
        } else if (id == R.id.nav_trouble_count) {

        } else if (id == R.id.nav_sign_out) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 退出登录
     */
    private void signOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示:");
        builder.setMessage("退出该账号?");
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //清除缓存用户对象
                User.logOut();
                ActivityController.finishAll();
                startActivity(new Intent(context, LoginOrRegisteActivity.class));

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
}
