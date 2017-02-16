package com.lw.looklook.presenter.implview;

import com.lw.looklook.bean.zhihu.ZhihuDaily;

/**
 * Created by lw on 2017/1/31.
 */

public interface IZhihuFragment extends IBaseFragment {
    /**
     * 更新每日知乎列表
     *
     * @param zhihuDaily
     */
    void updateList(ZhihuDaily zhihuDaily);
}
