package com.lw.looklook.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lw.looklook.R;
import com.lw.looklook.bean.zhihu.ZhihuStory;
import com.lw.looklook.config.Config;
import com.lw.looklook.presenter.IZhihuStoryPresenter;
import com.lw.looklook.presenter.implpresenter.ZhihuStoryPresenterImpl;
import com.lw.looklook.presenter.implview.IZhihuStory;
import com.lw.looklook.utils.WebUtil;
import com.lw.looklook.weidget.ParallaxScrimageView;
import com.lw.looklook.weidget.TranslateYTextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ZhihuDescribeActivity extends BaseActivity implements IZhihuStory {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.title)
    TranslateYTextView mTranslateYTextView;
    @InjectView(R.id.wv_zhihu)
    WebView wvZhihu;
    @InjectView(R.id.shot)
    ParallaxScrimageView mShot;


    private String id;
    private String title;
    private IZhihuStoryPresenter mIZhihuStoryPresenter;
    private String url;
    private boolean isEmpty;
    private String mBody;
    private String[] scss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu_describe);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);

        initData();
        initView();
    }


    private void initData() {
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        mIZhihuStoryPresenter = new ZhihuStoryPresenterImpl(this);
        getData();
    }

    private void getData() {
        //获取数据
        mIZhihuStoryPresenter.getZhihuStory(id);
    }

    private void initView() {
        // 设置左上角图标
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ZhihuDescribeActivity.this, "回退", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        mTranslateYTextView.setText(title);

        WebSettings settings = wvZhihu.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        //设置缓存目录
        settings.setAppCachePath(getCacheDir().getAbsolutePath() + "/webViewCache");
        settings.setAppCacheEnabled(true);
        wvZhihu.setWebChromeClient(new WebChromeClient());
    }

    @Override
    public void showError(String error) {
        Snackbar.make(wvZhihu, getString(R.string.snack_infor), Snackbar.LENGTH_INDEFINITE).setAction("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        }).show();
    }

    @Override
    public void showZhihuStory(ZhihuStory zhihuStory) {
        Glide.with(this)
                .load(zhihuStory.getImage())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mShot);
        url = zhihuStory.getShareUrl();
        isEmpty = TextUtils.isEmpty(zhihuStory.getBody());
        mBody = zhihuStory.getBody();
        scss = zhihuStory.getCss();
        if (isEmpty) {
            wvZhihu.loadUrl(url);
        } else {
            String data = WebUtil.buildHtmlWithCss(mBody, scss, Config.isNight);
            wvZhihu.loadDataWithBaseURL(null, data, WebUtil.MIME_TYPE, WebUtil.ENCODING, "");
        }
    }
}
