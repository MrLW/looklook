package com.lw.looklook.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lw.looklook.R;
import com.lw.looklook.adapter.ZhihuAdapter;
import com.lw.looklook.bean.zhihu.ZhihuDaily;
import com.lw.looklook.presenter.implpresenter.ZhihuPresenterImpl;
import com.lw.looklook.presenter.implview.IZhihuFragment;
import com.lw.looklook.utils.NetWorkUtil;
import com.lw.looklook.view.GridItemDividerDecoration;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lw on 2017/1/31.
 * 知乎Fragment
 */
public class ZhihuFragment extends BaseFragment implements IZhihuFragment {

    private View view;
    ZhihuPresenterImpl zhihuPresenter;

    // 是否有网络
    private boolean connected = true;
    @InjectView(R.id.recycle_zhihu)
    RecyclerView recycle_zhihu;
    @InjectView(R.id.prograss)
    ProgressBar progressBar;
    private ZhihuAdapter zhihuAdapter;
    private LinearLayoutManager linearLayoutManager;
    boolean loading = false;
    private RecyclerView.OnScrollListener onScrollListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 会跳过onCreate()逻辑
        setRetainInstance(true);
        view = inflater.inflate(R.layout.zhihu_fragment_layout, container, false);
        // 检查当前网络情况
        connected = NetWorkUtil.getCurrentNetWorkState(getActivity());
        // 注入
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
    }

    private void initView() {
        initListener();
        // 设置LayoutManager
        linearLayoutManager = new LinearLayoutManager(getContext());
        recycle_zhihu.setLayoutManager(linearLayoutManager);
        zhihuAdapter = new ZhihuAdapter(getContext());
        recycle_zhihu.setItemAnimator(new DefaultItemAnimator());
        recycle_zhihu.addItemDecoration(new GridItemDividerDecoration(getContext(), R.dimen.divider_height, R.color.divider));
        recycle_zhihu.setAdapter(zhihuAdapter);
        recycle_zhihu.addOnScrollListener(onScrollListener);
        loadData();
    }

    private void initListener() {
        // 向下滚动
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { // 向下滚动
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                    System.out.println("totalItemCount:" + totalItemCount + ";visibleItemCount" + visibleItemCount + ";pastVisiblesItems" + pastVisiblesItems);
                    if (!loading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = true;
                        loadMoreDate();
                    }
                }
            }
        };
    }

    /**
     * 加载更多数据
     */
    private void loadMoreDate() {
        zhihuAdapter.loadingStart();
        zhihuPresenter.getTheDaily(currentLoadDate);
    }

    /**
     * 请求数据
     */
    private void loadData() {
        zhihuPresenter.getLastZhihuNews();
    }

    private void initData() {
        zhihuPresenter = new ZhihuPresenterImpl(this);
    }

    @Override
    public void updateList(ZhihuDaily zhihuDaily) {
        if (loading) {
            loading = false;
            zhihuAdapter.loadingfinish();
        }
        //正在获取新闻的时间
        currentLoadDate = zhihuDaily.getDate();
        zhihuAdapter.addItems(zhihuDaily.getStories());
        if (!recycle_zhihu.canScrollVertically(View.SCROLL_INDICATOR_BOTTOM)) {
            loadMoreDate();
        }
    }

    @Override
    public void showProgressDialog() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hidProgressDialog() {
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    String currentLoadDate = "0";

    @Override
    public void showError(String error) {
        if (recycle_zhihu != null) {
            Snackbar.make(recycle_zhihu, "请检查网络", Snackbar.LENGTH_SHORT).setAction("重试", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentLoadDate.equals("0")) {
                        zhihuPresenter.getLastZhihuNews();
                    } else {
                        zhihuPresenter.getTheDaily(currentLoadDate);
                    }
                }
            }).show();

        }
    }
}
