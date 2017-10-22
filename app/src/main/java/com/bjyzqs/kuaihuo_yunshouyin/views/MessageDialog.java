package com.bjyzqs.kuaihuo_yunshouyin.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjyzqs.kuaihuo_yunshouyin.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author gly
 * @Description:自定义对话框
 */
public class MessageDialog extends Dialog {
    private static final int DELAY_CLOSE = 100;
    private long delayTime = 2000;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.img_img)
    ImageView imgImg;
    @BindView(R.id.tv_dialog_message)
    TextView tvDialogMessage;

    public MessageDialog isSuccess(boolean isSuccess) {
        int img_res = isSuccess ? R.drawable.icon_suc : R.drawable.icon_gb;
        imgImg.setImageResource(img_res);
        return this;
    }

    public MessageDialog delayTime(long delayTime) {
        this.delayTime = delayTime;
        return this;
    }

    public MessageDialog title(String title) {
        tv_title.setText(title);
        return this;
    }

    public MessageDialog message(String message) {
        tvDialogMessage.setText(message);
        return this;
    }

    public MessageDialog listener(OnDismissListener onDismissListener) {
        setOnDismissListener(onDismissListener);
        return this;
    }

    public MessageDialog(Context mContext) {
        super(mContext, R.style.sign_dialog);
//        float density = mContext.getResources().getDisplayMetrics().density;
//        int size = (int) (density * 90);
//        int heightPixels = mContext.getResources().getDisplayMetrics().heightPixels;
//
//        Window dialogWindow = getWindow();
//        dialogWindow.setGravity(Gravity.CENTER);
//        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.height = (int) (heightPixels * 0.8f); // 高度设置为屏幕的0.6
//        p.width = p.height; // 宽度设置为屏幕的0.65
//        dialogWindow.setAttributes(p);
//        int size = (int) (heightPixels * 0.8f);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.pay_success_dialog, null);
        setContentView(inflate/*, new RelativeLayout.LayoutParams(size, size)*/);
        ButterKnife.bind(this, inflate);
    }

    @Override
    public void show() {
        super.show();
        handler.sendEmptyMessageDelayed(DELAY_CLOSE, delayTime);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DELAY_CLOSE:
                    cancel();
                    dismiss();
                    break;
            }
        }
    };

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
