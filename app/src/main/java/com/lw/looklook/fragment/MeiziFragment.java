package com.lw.looklook.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lw.looklook.R;

import butterknife.ButterKnife;

/**
 * Created by lw on 2017/1/31.
 */

public class MeiziFragment extends BaseFragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.meizi_fragment_layout, container, false);
        // 注入
        ButterKnife.inject(this, view);
        return view;
    }
}
