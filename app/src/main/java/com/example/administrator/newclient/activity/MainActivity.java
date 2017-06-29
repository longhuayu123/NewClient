package com.example.administrator.newclient.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;

import com.example.administrator.newclient.R;
import com.example.administrator.newclient.base.BaseActivity;
import com.example.administrator.newclient.fragment.Fragmentfive;
import com.example.administrator.newclient.fragment.Fragmentfour;
import com.example.administrator.newclient.fragment.Fragmentone;
import com.example.administrator.newclient.fragment.Fragmentthree;
import com.example.administrator.newclient.fragment.Fragmenttwo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private RadioGroup radioGroup;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;


    @Override
    public void initView() {
        radioGroup = (RadioGroup) findViewById(R.id.rg_01);
        initViewPager();
        initNavigationView();
        initToolbar();
        initDrawerLayout();
    }

    private void initDrawerLayout() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,0,0 );

        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();     // 同步drawerLayout和toolbar的状态
    }

    /*使用ToolBar需要去掉Activity原有标题栏，否则会报错。
       * 去掉ActionBar的方式有如下3种方式：
       * 代码调用：
       *   Activity           requestWindowFeature(Window.FEATURE_NO_TITLE)
       *   AppCompActivity    supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
       * 使用没有ActionBar的主题, 如：Theme.AppCompat.Light.NoActionBar
       * 在主题中通过windowNoTitle属性去掉ActionBar
           <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
           <item name="android:windowNoTitle">true</item>
           </style>
       * */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_01) {
            showToast("item_01");
            return true;
        } else if (item.getItemId() == R.id.item_02) {
            showToast("item_02");
            return true;
        } else if (item.getItemId() == R.id.item_03) {
            showToast("item_03");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);  // 使用toolbar代替ActionBar

        //toolbar.setLogo(R.drawable.selected_home_tab_05);
        toolbar.setTitle("");   // 通过代码设置才生效：app:title="toolbar"

        toolbar.setSubtitle("");
        toolbar.setTitleTextColor(Color.RED);
        toolbar.setSubtitleTextColor(Color.YELLOW);

        // 点击toolbar导航栏左上角的图标后，退出当前界面
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.vp);

        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(new Fragmentone());
        fragments.add(new Fragmenttwo());
        fragments.add(new Fragmentthree());
        fragments.add(new Fragmentfour());
        fragments.add(new Fragmentfive());

        //getSupportFragmentManager需要继承fragmentactivity
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);//显示哪个framgent
            }

            @Override
            public int getCount() {
                return fragments.size();//有多少个framgent
            }
        });
    }

    @Override
    public void initListener() {


        /**侧滑菜单点击监听*/
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 点击侧滑菜单item时，通过DrawerLayout关闭侧滑菜单
                showToast("" + item.getTitle());
                drawerLayout.closeDrawers();

                return false;
            }
        });


        /**当点击RadioButton时，ViewPager显示的子界面工切换*/

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_01:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.rb_02:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.rb_03:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.rb_04:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.rb_05:
                        viewPager.setCurrentItem(4);
                        break;
                }
            }
        });


        /**当ViewPager子界面发生了改变时，要选中并高亮不同的RadioButton选项卡*/
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageSelected(int position) {
                switch (position) {  // 当ViewPager子界面切换后，选中某个RadioButton
                    case 0:
                        radioGroup.check(R.id.rb_01);
                        break;
                    case 1:
                        radioGroup.check(R.id.rb_02);
                        break;
                    case 2:
                        radioGroup.check(R.id.rb_03);
                        break;
                    case 3:
                        radioGroup.check(R.id.rb_04);
                        break;
                    case 4:
                        radioGroup.check(R.id.rb_05);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {

    }
}
