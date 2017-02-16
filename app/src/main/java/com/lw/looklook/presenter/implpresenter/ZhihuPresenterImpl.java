package com.lw.looklook.presenter.implpresenter;

import com.lw.looklook.api.ApiManager;
import com.lw.looklook.bean.zhihu.ZhihuDaily;
import com.lw.looklook.bean.zhihu.ZhihuDailyItem;
import com.lw.looklook.presenter.IZhihuPresenter;
import com.lw.looklook.presenter.implview.IZhihuFragment;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lw on 2017/2/1.
 * 实现具体的逻辑
 */
public class ZhihuPresenterImpl extends BasePresenterImpl implements IZhihuPresenter {

    private IZhihuFragment mIZhihuFragment;

    public ZhihuPresenterImpl(IZhihuFragment mIZhihuFragment) {
        this.mIZhihuFragment = mIZhihuFragment;
    }

    @Override
    public void getLastZhihuNews() {
        Observable<ZhihuDaily> observable = ApiManager.getInstance().getZhihuApiService().getLastDaily();
        Subscription subscription = observable
                .map(new Func1<ZhihuDaily, ZhihuDaily>() {
                    @Override
                    public ZhihuDaily call(ZhihuDaily zhihuDaily) {
                        String date = zhihuDaily.getDate();
                        for (ZhihuDailyItem zhihuDailyItem : zhihuDaily.getStories()) {
                            zhihuDailyItem.setDate(date);
                        }
                        return zhihuDaily;
                    }
                })
                .subscribeOn(Schedulers.io())// 在io线程加载数据
                .observeOn(AndroidSchedulers.mainThread()) // 主线程显示
                .subscribe(new Observer<ZhihuDaily>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) { // 当请求出错的时候调用这个方法
                        mIZhihuFragment.hidProgressDialog();
                        mIZhihuFragment.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ZhihuDaily zhihuDaily) {
                        mIZhihuFragment.hidProgressDialog();
                        mIZhihuFragment.updateList(zhihuDaily);
                    }
                });
        // 添加到CompositeSubscription
        addSubscription(subscription);
    }

    @Override
    public void getTheDaily(String date) {
        Subscription s = ApiManager.getInstance().getZhihuApiService().getTheDaily(date)
                .map(new Func1<ZhihuDaily, ZhihuDaily>() {
                    @Override
                    public ZhihuDaily call(ZhihuDaily zhihuDaily) {
                        String date = zhihuDaily.getDate();
                        for (ZhihuDailyItem zhihuDailyItem : zhihuDaily.getStories()) {
                            zhihuDailyItem.setDate(date);
                        }
                        return zhihuDaily;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhihuDaily>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mIZhihuFragment.hidProgressDialog();
                        mIZhihuFragment.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ZhihuDaily zhihuDaily) {
                        mIZhihuFragment.hidProgressDialog();
                        mIZhihuFragment.updateList(zhihuDaily);
                    }
                });
        addSubscription(s);
    }

    @Override
    public void getLastFromCache() {

    }
}
