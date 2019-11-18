package com.bhj.mvvmutil;


import com.bhj.lib_mvvmutil.model.PagerConfig;
import com.bhj.lib_mvvmutil.ui.LoadingActivity;
import com.bhj.lib_mvvmutil.ui.PagerState;
import com.bhj.lib_mvvmutil.viewmodel.NoViewModel;
import com.bhj.mvvmutil.databinding.ActivityMainBinding;

public class MainActivity extends LoadingActivity<NoViewModel, ActivityMainBinding> {

    @Override
    protected void setPagerConfig(PagerConfig pagerConfig) {
        pagerConfig.setLeftIconRes(this,R.drawable.icon_back)
                .setTitleText("标题");
    }

    @Override
    protected int setContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void loadData() {
        setState(PagerState.SUCCEED);
    }
}
