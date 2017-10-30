package com.zmsoft.TestTool.bluetooth;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zmsoft.TestTool.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author gly
 * @Description:自定义对话框
 */
public class BlueToothDialog extends Dialog {
    private static final int DELAY_CLOSE = 100;
    private long delayTime = 600000;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.listview)
    ListView listView;
    private BaseAdapter adapter;


    public BlueToothDialog title(String title) {
        tv_title.setText(title);
        return this;
    }

    public BlueToothDialog adapter(BaseAdapter adapter, AdapterView.OnItemClickListener onItemClickListener) {
        if (null == this.adapter) {
            this.adapter = adapter;
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(onItemClickListener);
        }
        return this;
    }


    public BlueToothDialog listener(OnDismissListener onDismissListener) {
        setOnDismissListener(onDismissListener);
        return this;
    }

    public BlueToothDialog(Context mContext) {
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
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.bluetooth_dialog, null);
        setContentView(inflate/*, new RelativeLayout.LayoutParams(size, size)*/);
        ButterKnife.bind(this, inflate);
    }

    public BlueToothDialog Show() {
        show();
        handler.sendEmptyMessageDelayed(DELAY_CLOSE, delayTime);
        return this;
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
