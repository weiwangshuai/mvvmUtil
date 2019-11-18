package com.bhj.lib_mvvmutil.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;

import com.bhj.lib_mvvmutil.R;
import com.bhj.lib_mvvmutil.databinding.ActivityLoadingBinding;
import com.bhj.lib_mvvmutil.model.PagerConfig;
import com.bhj.lib_mvvmutil.util.ClassUtil;
import com.bhj.lib_mvvmutil.view.dialog.LoadingDialog;
import com.bhj.lib_mvvmutil.view.loadingHeader.YunRefreshHeader;
import com.bhj.lib_mvvmutil.viewmodel.BaseViewModel;

public abstract class LoadingActivity<VM extends BaseViewModel, SV extends ViewDataBinding> extends BaseActivity implements Stateful {
    // ViewModel
    protected VM mViewModel;
    // 布局view
    protected SV mBindingView;

    private View errorView;
    private View loadingView;
    private PagerState state;
    private ActivityLoadingBinding mBaseBinding;
    private AnimationDrawable mAnimationDrawable;
    protected PagerConfig mPagerConfig;
    protected LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_loading, null, false);
        mBindingView = DataBindingUtil.inflate(getLayoutInflater(), setContentViewId(), null, false);
        // content
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mBindingView.getRoot().setLayoutParams(params);
        mBaseBinding.container.addView(mBindingView.getRoot());
        mBaseBinding.container.setRefreshHeader(new YunRefreshHeader(this));
        mBaseBinding.container.setOnRefreshListener(refreshLayout -> onRefresh());
        mBaseBinding.container.setEnabled(false);
        getWindow().setContentView(mBaseBinding.getRoot());

        setSupportActionBar(mBaseBinding.toolBar);
        initViewModel();
        mPagerConfig = new PagerConfig();
        setPagerConfig(mPagerConfig);
        mBaseBinding.setTitleModel(mPagerConfig);
        setState(PagerState.UNKNOWN);
        loadData();
    }

    /**
     * 初始化ViewModel
     */
    private void initViewModel() {
        Class<VM> viewModelClass = ClassUtil.getViewModel(this);
        if (viewModelClass != null) {
            this.mViewModel = ViewModelProviders.of(this).get(viewModelClass);
            getLifecycle().addObserver(mViewModel);
        }
    }

    @Override
    public void setState(PagerState state) {
        this.state = state;
        switch (state) {
            case UNKNOWN:
                showLoading();
                break;
            case ERROR:
                showError();
                break;
            case SUCCEED:
                showContentView();
                break;
            case LOADING:
                showLoading();
                break;
        }
    }

    /**
     * 刷新回调
     */
    @Override
    public void onRefresh() {
    }

    private void showLoading() {
        if (state != PagerState.SUCCEED) {
            if (loadingView == null) {
                // 加载动画
                loadingView = ((ViewStub) findViewById(R.id.vs_loading)).inflate();
                ImageView img = loadingView.findViewById(R.id.img_progress);
                mAnimationDrawable = (AnimationDrawable) img.getDrawable();
            }
            if (loadingView != null && loadingView.getVisibility() != View.VISIBLE) {
                loadingView.setVisibility(View.VISIBLE);
            }
            // 开始动画
            if (!mAnimationDrawable.isRunning()) {
                mAnimationDrawable.start();
            }
            if (mBindingView.getRoot().getVisibility() != View.GONE) {
                mBindingView.getRoot().setVisibility(View.GONE);
            }
            if (errorView != null) {
                errorView.setVisibility(View.GONE);
            }
        }
    }


    private void showContentView() {
        mBaseBinding.container.setEnabled(mPagerConfig.isEnabledRefresh());
        mBaseBinding.container.finishRefresh();
        if (loadingView != null && loadingView.getVisibility() != View.GONE) {
            loadingView.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
        if (mBindingView.getRoot().getVisibility() != View.VISIBLE) {
            mBindingView.getRoot().setVisibility(View.VISIBLE);
        }
    }

    private void showError() {
        mBaseBinding.container.setEnabled(false);
        if (loadingView != null && loadingView.getVisibility() != View.GONE) {
            loadingView.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (errorView == null) {
            errorView = ((ViewStub) findViewById(R.id.vs_error_refresh)).inflate();
            // 点击加载失败布局
            errorView.setOnClickListener(v -> {
                showLoading();
                onRefresh();
            });
        } else {
            errorView.setVisibility(View.VISIBLE);
        }
        if (mBindingView.getRoot().getVisibility() != View.GONE) {
            mBindingView.getRoot().setVisibility(View.GONE);
        }
    }

    @Override
    public void showDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.show();
    }

    @Override
    public void hideDialog() {
        if (mLoadingDialog == null) return;
        mLoadingDialog.dismiss();
    }
    protected void loadData() {

    }
    protected abstract void setPagerConfig(PagerConfig pagerConfig);

    protected abstract int setContentViewId();
}
