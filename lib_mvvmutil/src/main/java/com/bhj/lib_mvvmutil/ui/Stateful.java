package com.bhj.lib_mvvmutil.ui;

public interface Stateful {

    void setState(PagerState state);

    void onRefresh();

    void showDialog();

    void hideDialog();
}
