package com.zmsoft.TestTool.views;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.IdRes;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zbar.lib.CaptureActivity;
import com.zmsoft.TestTool.R;
import com.zmsoft.TestTool.application.MyApplication;
import com.zmsoft.TestTool.dao.ActivityStarter;
import com.zmsoft.TestTool.dao.ConnectDao;
import com.zmsoft.TestTool.dao.SpeechDao;
import com.zmsoft.TestTool.utils.Logger;
import com.zmsoft.TestTool.utils.ToastUtil;
import com.zmsoft.TestTool.utils.okhttp.listener.DisposeDataListener;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zbar.lib.CaptureActivity.ACTION_DECODE_INTENT;
import static com.zbar.lib.CaptureActivity.KEY_DECODE_RESULT;

/**
 * @author gly
 * @Description:自定义对话框
 */
public class ReceiveDialog extends Dialog {
    public static boolean isShow = false;
    private String order_id;
    @BindView(R.id.linearlayoutkefu)
    LinearLayout linearlayoutkefu;
    @BindView(R.id.passkeyboard)
    PassKeyBoard passkeyboard;
    @BindView(R.id.linearlayoutshouquanma)
    LinearLayout tablerowshouquanma;
    @BindView(R.id.linearlayoutzhaoling)
    LinearLayout linearLayoutzhaoling;

    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;
    @BindView(R.id.tv_receive)
    TextView tvReceive;
    @BindView(R.id.ed_pay)
    EditText ed_pay;
    @BindView(R.id.ed_shouquanma)
    EditText ed_shouquanma;
    @BindView(R.id.tv_zhaoling)
    TextView tvZhaoling;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.ed_phonenum)
    EditText edPhonenum;
    private int payType = 1;
    public OnCanclelinstener onCanclelinstener;
    private ShouQuanReciver shouQuanReciver;
    private LocalBroadcastManager localBroadcastManager;

    public ReceiveDialog(Context mContext, double mtotalPrice, int mtotalCount, String order_id, OnCanclelinstener onCanclelinstener) {
        this(mContext, mtotalPrice, mtotalCount, order_id);
        this.onCanclelinstener = onCanclelinstener;
    }

    public ReceiveDialog(Context mContext, final double totalPrice, int totalCount, String order_id) {
        super(mContext, R.style.sign_dialog);
        this.order_id = order_id;
        setCancelable(false);
        int heightPixels = mContext.getResources().getDisplayMetrics().heightPixels;
//        Window dialogWindow = getWindow();
//        dialogWindow.setGravity(Gravity.CENTER);
//        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        dialogWindow.setAttributes(p);
        int size = (int) (heightPixels * 0.8f);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.receice_dialog, null);
        setContentView(inflate/*, new RelativeLayout.LayoutParams(size, size)*/);
        ButterKnife.bind(this, inflate);
        final DecimalFormat df = new java.text.DecimalFormat("#.##");
        ed_pay.setText(String.valueOf(df.format(totalPrice)));
        ed_pay.requestFocus();
        tvReceive.setText(String.valueOf(df.format(totalPrice)));
        tvCount.setText(String.valueOf(totalCount));
        ed_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_pay.setText("");
            }
        });
        ed_pay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    double kefu = Double.parseDouble(charSequence.toString());
                    tvZhaoling.setText(String.valueOf(df.format(kefu - totalPrice)));
                } catch (Exception e) {

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_shouquanma.setInputType(0);
        ed_shouquanma.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = charSequence.toString();
                if (!TextUtils.isEmpty(s) && s.length() == 18) {
                    upload();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_pay.setInputType(0);
        edPhonenum.setInputType(0);
        passkeyboard.setOnKeyClickLinstener(new PassKeyBoard.onKeyClickLinstener() {
            @Override
            public View getFocuseView() {
                return getWindow().getCurrentFocus();
            }
        });
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
//                for (int j = 0; j < radioGroup.getChildCount(); j++) {
//                    RadioButton childAt = (RadioButton) radioGroup.getChildAt(j);
//                    childAt.setButtonDrawable(R.drawable.radio_button);
//                    Logger.log("onCheckedChanged :" + childAt.isChecked() + "--" + childAt.isFocused());
//                }
                switch (i) {
                    case R.id.button1:
                        payType = 1;
                        linearlayoutkefu.setVisibility(View.VISIBLE);
                        tablerowshouquanma.setVisibility(View.GONE);
                        linearLayoutzhaoling.setVisibility(View.VISIBLE);
                        ed_pay.requestFocus();
                        break;
                    case R.id.button2:
                        payType = 2;
                        linearlayoutkefu.setVisibility(View.GONE);
                        tablerowshouquanma.setVisibility(View.VISIBLE);
                        linearLayoutzhaoling.setVisibility(View.GONE);
                        ed_shouquanma.requestFocus();
                        break;
                    case R.id.button3:
                        payType = 3;
                        linearlayoutkefu.setVisibility(View.GONE);
                        tablerowshouquanma.setVisibility(View.VISIBLE);
                        linearLayoutzhaoling.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void show() {
        if (!isShow) {
            isShow = true;
            super.show();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        isShow = false;
    }

    private class ShouQuanReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_DECODE_INTENT)) {
                String result = intent.getStringExtra(KEY_DECODE_RESULT);
                Logger.log("dialogrecive" + result);
                ed_shouquanma.setText(result);
                if (null != CaptureActivity.activity) {
                    CaptureActivity.activity.finish();
                }
            }
        }
    }


    @OnClick({R.id.imb_close, R.id.btn_sure, R.id.btn_saoma})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imb_close:
                dismiss();
                break;
            case R.id.btn_sure:
                upload();
                break;
            case R.id.btn_saoma:
                shouQuanReciver = new ShouQuanReciver();
                localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
                localBroadcastManager.registerReceiver(shouQuanReciver, new IntentFilter(ACTION_DECODE_INTENT));
                setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        localBroadcastManager.unregisterReceiver(shouQuanReciver);
                    }
                });
                ActivityStarter.startCapActivity(getContext());
                break;
        }
    }

    private void upload() {
        double pay = 0;
        double receive = 0;
        try {
            pay = Double.parseDouble(ed_pay.getText().toString());
            receive = Double.parseDouble(tvReceive.getText().toString());
        } catch (Exception e) {

        }
        if (pay < receive) {
            ToastUtil.showToast(getContext(), "客户付款不能小于应付款", 2000);
            return;
        }
        String s = ed_shouquanma.getText().toString();
        if (payType != 1) {
            if (s.startsWith("28")) {
                payType = 3;
            } else if (s.startsWith("13")) {
                payType = 2;
            }
        }

        ConnectDao.collection(s, payType + "", order_id, ed_pay.getText().toString(), tvReceive.getText().toString(), edPhonenum.getText().toString(), MyApplication.userId, new DisposeDataListener<String>() {
            @Override
            public void onSuccess(String responseObj) {
                if (responseObj.equals("1")) {
                    SpeechDao.receive(tvReceive.getText().toString());
                    MessageDialog paysuccess = new MessageDialog(getContext()).isSuccess(true).message("付款成功");
                    paysuccess.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            ReceiveDialog.this.dismiss();
                        }
                    });
                    paysuccess.show();
                    if (null != onCanclelinstener) {
                        onCanclelinstener.onSuccess();
                    }
                } else {
                    checkFail();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                checkFail();
            }
        });
    }

    private void checkFail() {
        new MessageDialog(getContext()).isSuccess(false).message("付款失败").show();
        if (null != onCanclelinstener) {
            onCanclelinstener.onFail();
        }
        ed_shouquanma.setText("");
        ed_shouquanma.setFocusable(true);
        ed_shouquanma.setFocusableInTouchMode(true);
        ed_shouquanma.requestFocus();

    }

    public interface OnCanclelinstener {
        void onSuccess();

        void onFail();
    }
}

