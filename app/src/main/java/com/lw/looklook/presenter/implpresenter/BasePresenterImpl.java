package com.lw.looklook.presenter.implpresenter;

import com.lw.looklook.presenter.BasePresenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lw on 2017/1/31.
 */

public class BasePresenterImpl implements BasePresenter {

    // 使用CompositeSubscription来管理所有的Subscriptions
    private CompositeSubscription mCompositeSubscription;

    protected void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    @Override
    public void unsubcrible() {
        // // TODO: 2017/2/1 find when unsubcrible
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }
}
