package com.bhj.lib_mvvmutil.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.view.View;

public class PagerConfig {
    // 标题
    private String titleText;
    // 左边图标
    private Drawable leftIcon;
    // 标题是否可见
    private int visible;
    // 左边图标点击事件
    private View.OnClickListener leftIconListener;
    // 下拉刷新是否可用
    private boolean enabledRefresh;
    public String getTitleText() {
        return titleText;
    }

    public PagerConfig setTitleText(String titleText) {
        this.titleText = titleText;
        return this;
    }

    public Drawable getLeftIcon() {
        return leftIcon;
    }

    public PagerConfig setLeftIcon(Drawable leftIcon) {
        this.leftIcon = leftIcon;
        return this;
    }

    public View.OnClickListener getLeftIconListener() {
        return leftIconListener;
    }

    public PagerConfig setLeftIconListener(View.OnClickListener leftIconListener) {
        this.leftIconListener = leftIconListener;
        return this;
    }

    public boolean isEnabledRefresh() {
        return enabledRefresh;
    }

    public PagerConfig setEnabledRefresh(boolean enabledRefresh) {
        this.enabledRefresh = enabledRefresh;
        return this;
    }

    public int getVisible() {
        return visible;
    }

    public PagerConfig setVisible(int visible) {
        this.visible = visible;
        return this;
    }

    public PagerConfig setLeftIconRes(Context context, @DrawableRes int res) {
        setLeftIcon(ContextCompat.getDrawable(context, res));
        return this;
    }
}
