package com.bhj.lib_mvvmutil.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bhj.lib_mvvmutil.R;


public class LoadingDialog extends Dialog {

    private TextView tv_reminder;

    public LoadingDialog(@NonNull Context context) {
        this(context, R.style.loading_dialog);
    }

    public LoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.layout_loading_dialog, null);
        tv_reminder = dialogView.findViewById(R.id.tv_reminder);
        setContentView(dialogView);
        setCancelable(false);
        final Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = dp2px(getContext(),100);
            lp.height = dp2px(getContext(),100);
            lp.gravity = Gravity.CENTER;
        }
        setText("正在加载...");
    }

    public void setText(String text) {
        if (!TextUtils.isEmpty(text)) {
            tv_reminder.setText(text);
        }
    }

    public int dp2px(@NonNull final Context context, final float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}