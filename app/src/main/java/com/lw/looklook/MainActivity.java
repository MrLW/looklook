package com.lw.looklook;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lw.looklook.activity.BaseActivity;
import com.lw.looklook.fragment.MeiziFragment;
import com.lw.looklook.fragment.TopNewsFragment;
import com.lw.looklook.fragment.ZhihuFragment;
import com.lw.looklook.utils.SharePrefenceUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity {

    MenuItem currentMenuItem;
    Fragment currentFragment;

    @InjectView(R.id.toolbar)
    Toolbar toolbar; // 不可以私有或者静态
    @InjectView(R.id.nav_view)
    NavigationView navView;
    @InjectView(R.id.drawer)
    DrawerLayout drawer;
    // 替代了HashMap，性能更好
    SimpleArrayMap<Integer, String> mTitleMap = new SimpleArrayMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 注入
        ButterKnife.inject(this);
        // 设置toolbar
        setSupportActionBar(toolbar);
        // 给toolbar设置菜单
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);

        if (savedInstanceState == null) {
            int nevigationId = SharePrefenceUtil.getNevigationItem(this);
            if (nevigationId != -1) {
                // 存在
                currentMenuItem = navView.getMenu().findItem(nevigationId);
            }
            // 默认加载知乎
            if (currentMenuItem == null) {
                currentMenuItem = navView.getMenu().findItem(R.id.zhihuitem);
            }
            if (currentMenuItem != null) {
                currentMenuItem.setChecked(true);
                Fragment fragment = getFragmentById(currentMenuItem.getItemId());
                String title = mTitleMap.get((Integer) currentMenuItem.getItemId());
                if (fragment != null) {
                    switchFragment(fragment, title);
                }
            }
        } else {
            if (currentMenuItem != null) {
                Fragment fragment = getFragmentById(currentMenuItem.getItemId());
                String title = mTitleMap.get(currentMenuItem.getItemId());
                if (fragment != null) {
                    switchFragment(fragment, title);
                }
            } else {
                switchFragment(new ZhihuFragment(), " ");
                currentMenuItem = navView.getMenu().findItem(R.id.zhihuitem);
            }
        }

        // 为右边菜单设置监听
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (currentMenuItem != item && currentMenuItem != null) {
                    currentMenuItem.setChecked(false);// 把点击之前的那个item设置为false
                    int itemId = item.getItemId();
                    // 将当前的itemId存放到SharePrefence
                    SharePrefenceUtil.setNevigationItem(MainActivity.this, itemId);
                    // 将当前item设置为点击的item
                    currentMenuItem = item;
                    currentMenuItem.setChecked(true);
                    switchFragment(getFragmentById(currentMenuItem.getItemId()), mTitleMap.get(currentMenuItem.getItemId()));
                }
                drawer.closeDrawer(GravityCompat.END, true);
                return true;
            }
        });
    }

    // 创建菜单监听器
    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_open:
                    // 打开右边菜单
                    drawer.openDrawer(GravityCompat.END);
                    break;
                case R.id.menu_about:
                    // 进入关于页面
                    break;
            }
            return true;
        }
    };

    // 创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // 添加菜单的item
    private void addFragment() {
        mTitleMap.put(R.id.meiziitem, getResources().getString(R.string.zhihu));
        mTitleMap.put(R.id.topnewsitem, getResources().getString(R.string.topnews));
        mTitleMap.put(R.id.zhihuitem, getResources().getString(R.string.zhihu));
    }

    // 切换Fragment
    public void switchFragment(Fragment fragment, String title) {
        if (currentFragment == null || !currentFragment.getClass().getName().equals(fragment.getClass().getName())) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
        // 设置当前的Fragment为fragment
        currentFragment = fragment;
    }

    /**
     * 根据id找到对应的fragment
     *
     * @param id : 对应的ID
     * @return
     */
    private Fragment getFragmentById(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.zhihuitem:
                fragment = new ZhihuFragment();
                break;
            case R.id.topnewsitem:
                fragment = new TopNewsFragment();
                break;
            case R.id.meiziitem:
                fragment = new MeiziFragment();
                break;

        }
        return fragment;
    }


    long exitTime = 0;

    /**
     * 双击退出
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MainActivity.this, "再点一次，退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }

    /**
     * 加载更多
     */
    public interface LoadingMore {
        /**
         * 开始加载
         */
        void loadingStart();

        /**
         * 加载完成
         */
        void loadingfinish();
    }
}
