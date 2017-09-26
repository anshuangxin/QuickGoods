package com.gly.quickgoods.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gly.quickgoods.R;

/**
 * @author gly
 * @Description:自定义对话框
 */
public class PayDialog extends Dialog {
    public static PayDialog payDialog;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.img_img)
    ImageView imgImg;
    @BindView(R.id.tv_dialog_message)
    TextView tvDialogMessage;

    public static void ShowDialog(Context mContext, boolean isSuccess) {
        if (null != payDialog) {
            if (null != payDialog.getWindow() && payDialog.isShowing()) {
                payDialog.cancel();
                payDialog = null;
            }
        }
        payDialog = new PayDialog(mContext, isSuccess);
        payDialog.setCancelable(true);
        payDialog.show();

    }

    public static void cancle() {
        if (null != payDialog && payDialog.isShowing()) {
            payDialog.dismiss();
            payDialog = null;
        }
    }

    public PayDialog(Context mContext, boolean isSuccess) {
        super(mContext, R.style.sign_dialog);
//        float density = mContext.getResources().getDisplayMetrics().density;
//        int size = (int) (density * 90);
        int heightPixels = mContext.getResources().getDisplayMetrics().heightPixels;
//
//        Window dialogWindow = getWindow();
//        dialogWindow.setGravity(Gravity.CENTER);
//        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.height = (int) (heightPixels * 0.8f); // 高度设置为屏幕的0.6
//        p.width = p.height; // 宽度设置为屏幕的0.65
//        dialogWindow.setAttributes(p);
        int size = (int) (heightPixels * 0.8f);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.pay_success_dialog, null);
        setContentView(inflate/*, new RelativeLayout.LayoutParams(size, size)*/);
        ButterKnife.bind(this,inflate);
        String message = isSuccess ? "付款成功" : "付款失败";
        int img_res = isSuccess ? R.drawable.icon_suc : R.drawable.icon_gb;
        tvDialogMessage.setText(message);
        imgImg.setImageResource(img_res);

    }

    protected PayDialog(Context context, boolean cancelable,
                        OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
    }

    @OnClick({R.id.imb_close, R.id.btn_sure})
    public void onViewClicked(View view) {
        dismiss();
        switch (view.getId()) {
            case R.id.imb_close:
                break;
            case R.id.btn_sure:
                break;
        }
    }
}
