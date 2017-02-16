package com.lw.looklook.presenter.implview;

import com.lw.looklook.bean.zhihu.ZhihuStory;

/**
 * Created by lw on 2017/2/8.
 */

public interface IZhihuStory {
    void showError(String error);

    void showZhihuStory(ZhihuStory zhihuStory);

}
