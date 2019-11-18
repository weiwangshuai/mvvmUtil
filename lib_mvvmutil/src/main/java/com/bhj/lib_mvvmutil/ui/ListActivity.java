package com.bhj.lib_mvvmutil.ui;

import android.support.v7.widget.LinearLayoutManager;

import com.bhj.lib_mvvmutil.R;
import com.bhj.lib_mvvmutil.databinding.ActivityListBinding;
import com.bhj.lib_mvvmutil.model.RecyclerModel;
import com.bhj.lib_mvvmutil.viewmodel.BaseViewModel;

/**
 * 有列表的页面
 * @param <VM>
 */
public abstract class ListActivity<VM extends BaseViewModel> extends LoadingActivity<VM, ActivityListBinding> {

    @Override
    protected void loadData() {
        super.loadData();
        RecyclerModel recyclerModel = new RecyclerModel();
        recyclerModel.setLayoutManager(new LinearLayoutManager(this));
        setRecyclerModel(recyclerModel);
        mBindingView.setRecyclerModel(recyclerModel);
    }

    protected abstract void setRecyclerModel(RecyclerModel recyclerModel);

    @Override
    protected int setContentViewId() {
        return R.layout.activity_list;
    }

}
