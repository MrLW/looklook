package com.lw.looklook.presenter.implview;

/**
 * Created by lw on 2017/1/31.
 */

public interface IBaseFragment {
    /**
     * 弹出进度条
     */
    void showProgressDialog();

    /**
     * 隐藏进度条
     */
    void hidProgressDialog();

    /**
     * 显示加载错误
     *
     * @param error
     */
    void showError(String error);
}
