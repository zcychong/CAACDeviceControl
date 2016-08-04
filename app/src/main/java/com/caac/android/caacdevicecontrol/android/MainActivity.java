package com.caac.android.caacdevicecontrol.android;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.caac.android.caacdevicecontrol.R;
import com.caac.android.caacdevicecontrol.fragment.ExceptionDynamicsFragment;
import com.caac.android.caacdevicecontrol.fragment.GroupDynamicsFragment;
import com.caac.android.caacdevicecontrol.fragment.MyFragmentPgerAdapter;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

        //初始化按钮
        llException = (LinearLayout)findViewById(R.id.ll_exception);
        llException.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivException.setImageResource(R.mipmap.icon_exception_choosed);
                ivGroup.setImageResource(R.mipmap.icon_group_unchoose);
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
                ivGroup.setImageResource(R.mipmap.icon_group_choosed);
            }
        });

        ivException = (ImageView) findViewById(R.id.iv_exceptiom);
        ivGroup = (ImageView) findViewById(R.id.iv_group);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
