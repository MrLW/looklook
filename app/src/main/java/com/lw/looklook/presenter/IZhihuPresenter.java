package com.lw.looklook.presenter;

/**
 * Created by lw on 2017/2/1.
 */

public interface IZhihuPresenter extends BasePresenter {
    void getLastZhihuNews();

    void getTheDaily(String date);

    void getLastFromCache();
}
