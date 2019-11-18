package com.bhj.lib_mvvmutil.view.loadingHeader;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bhj.lib_mvvmutil.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

public class YunRefreshHeader implements RefreshHeader {
    private View view;
    private AnimationDrawable mAnimationDrawable;

    public YunRefreshHeader(Context context) {
        initView(context);
    }

    private void initView(Context context) {
        view = View.inflate(context, R.layout.layout_loading_view, null);
        mAnimationDrawable = (AnimationDrawable) ((ImageView)view.findViewById(R.id.img_progress)).getDrawable();
    }

    @NonNull
    @Override
    public View getView() {
        return view;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        mAnimationDrawable.start();//开始动画
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        mAnimationDrawable.stop();
        return 0;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None:
                //下拉开始刷新
            case PullDownToRefresh:
                break;
            //正在刷新
            case Refreshing:
                break;
            //释放立即刷新
            case ReleaseToRefresh:
                break;
        }

    }
}
