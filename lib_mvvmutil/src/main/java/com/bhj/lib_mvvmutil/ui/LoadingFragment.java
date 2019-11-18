package com.bhj.lib_mvvmutil.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bhj.lib_mvvmutil.R;
import com.bhj.lib_mvvmutil.util.ClassUtil;
import com.bhj.lib_mvvmutil.view.dialog.LoadingDialog;
import com.bhj.lib_mvvmutil.view.loadingHeader.YunRefreshHeader;
import com.bhj.lib_mvvmutil.viewmodel.BaseViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public abstract class LoadingFragment<VM extends BaseViewModel, SV extends ViewDataBinding> extends BaseFragment implements Stateful {
    // ViewModel
    protected VM mViewModel;
    // 布局view
    protected SV mBindingView;
    // fragment是否显示了
    protected boolean mIsVisible = false;
    // 加载中
    private View loadingView;
    // 加载失败
    private View errorView;
    // 空布局
    private View emptyView;
    // 动画
    private AnimationDrawable mAnimationDrawable;
    private View mRootView;
    private SmartRefreshLayout mRefreshLayout;
    protected LoadingDialog mLoadingDialog;
    private PagerState state;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_loading, container, false);
            mBindingView = DataBindingUtil.inflate(inflater, setContentViewId(), null, false);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mBindingView.getRoot().setLayoutParams(params);
            mRefreshLayout = mRootView.findViewById(R.id.container);
            loadingView = ((ViewStub) mRootView.findViewById(R.id.vs_loading)).inflate();
            ImageView img = loadingView.findViewById(R.id.img_progress);
            mAnimationDrawable = (AnimationDrawable) img.getDrawable();
            setState(PagerState.UNKNOWN);
            initViewModel();

            mRefreshLayout.addView(mBindingView.getRoot());
            mRefreshLayout.setRefreshHeader(new YunRefreshHeader(getContext()));
            mRefreshLayout.setOnRefreshListener(refreshLayout -> onRefresh());
            mRefreshLayout.setEnabled(false);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    /**
     * 在这里实现Fragment数据的缓加载.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    /**
     * 显示加载中状态
     */
    protected void showLoading() {
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
        if (emptyView != null && emptyView.getVisibility() != View.GONE) {
            emptyView.setVisibility(View.GONE);
        }
    }

    /**
     * 加载完成的状态
     */
    protected void showContentView() {
        if (mBindingView.getRoot().getVisibility() != View.VISIBLE) {
            mBindingView.getRoot().setVisibility(View.VISIBLE);
        }
        if (loadingView != null && loadingView.getVisibility() != View.GONE) {
            loadingView.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
        if (emptyView != null && emptyView.getVisibility() != View.GONE) {
            emptyView.setVisibility(View.GONE);
        }
    }

    /**
     * 加载失败点击重新加载的状态
     */
    protected void showError() {
        ViewStub viewStub = mRootView.findViewById(R.id.vs_error_refresh);
        if (viewStub != null) {
            errorView = viewStub.inflate();
            // 点击加载失败布局
            errorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoading();
                    onRefresh();
                }
            });
        }
        if (errorView != null) {
            errorView.setVisibility(View.VISIBLE);
        }
        if (loadingView != null && loadingView.getVisibility() != View.GONE) {
            loadingView.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (mBindingView.getRoot().getVisibility() != View.GONE) {
            mBindingView.getRoot().setVisibility(View.GONE);
        }
        if (emptyView != null && emptyView.getVisibility() != View.GONE) {
            emptyView.setVisibility(View.GONE);
        }
    }


    protected void showEmptyView(String text) {
        // 需要这样处理，否则二次显示会失败
        ViewStub viewStub = mRootView.findViewById(R.id.vs_empty);
        if (viewStub != null) {
            emptyView = viewStub.inflate();
            ((TextView) emptyView.findViewById(R.id.tv_tip_empty)).setText(text);
        }
        if (emptyView != null) {
            emptyView.setVisibility(View.VISIBLE);
        }

        if (loadingView != null && loadingView.getVisibility() != View.GONE) {
            loadingView.setVisibility(View.GONE);
        }
        // 停止动画
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        if (errorView != null) {
            errorView.setVisibility(View.GONE);
        }
        if (mBindingView != null && mBindingView.getRoot().getVisibility() != View.GONE) {
            mBindingView.getRoot().setVisibility(View.GONE);
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

    @Override
    public void showDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(getContext());
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

    protected void onInvisible() {

    }

    protected void onVisible() {
    }

    protected abstract int setContentViewId();

    public abstract void onRefresh();

}
