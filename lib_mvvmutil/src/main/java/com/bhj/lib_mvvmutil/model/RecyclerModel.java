package com.bhj.lib_mvvmutil.model;

import android.support.v7.widget.RecyclerView;

public class RecyclerModel {
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }
}
