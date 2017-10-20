package com.bjyzqs.kuaihuo_yunshouyin.views;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.bjyzqs.kuaihuo_yunshouyin.R;
import com.bjyzqs.kuaihuo_yunshouyin.application.MyApplication;
import com.bjyzqs.kuaihuo_yunshouyin.dao.ConnectDao;
import com.bjyzqs.kuaihuo_yunshouyin.dao.SpeechDao;
import com.bjyzqs.kuaihuo_yunshouyin.utils.ToastUtil;
import com.bjyzqs.kuaihuo_yunshouyin.utils.okhttp.listener.DisposeDataListener;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bjyzqs.kuaihuo_yunshouyin.R.id.btn_sure;

/**
 * @author gly
 * @Description:自定义对话框
 */
public class ReceiveDialog extends Dialog {
    public static ReceiveDialog receiveDialog;
    private String order_id;
    @BindView(R.id.tablerowkefu)
    TableRow tablerowkefu;
    @BindView(R.id.passkeyboard)
    PassKeyBoard passkeyboard;
    @BindView(R.id.tablerowshouquanma)
    TableRow tablerowshouquanma;
    @BindView(R.id.tablerowzhaoling)
    TableRow tablerowzhaoling;

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

    public ReceiveDialog(Context mContext, double mtotalPrice, int mtotalCount, String order_id, OnDismissListener onDismissListener) {
        this(mContext, mtotalPrice, mtotalCount, order_id);
        setOnDismissListener(onDismissListener);
    }

    public static void ShowDialog(Context mContext, double totalPrice, int totalCount, String order_id) {
        if (null != receiveDialog) {
            if (null != receiveDialog.getWindow() && receiveDialog.isShowing()) {
                receiveDialog.cancel();
                receiveDialog = null;
            }
        }
        receiveDialog = new ReceiveDialog(mContext, totalPrice, totalCount, order_id);
        receiveDialog.show();

    }

    public static void cancle() {
        if (null != receiveDialog && receiveDialog.isShowing()) {
            receiveDialog.dismiss();
            receiveDialog = null;
        }
    }

    public ReceiveDialog(Context mContext, final double totalPrice, int totalCount, String order_id) {
        super(mContext, R.style.sign_dialog);
        this.order_id = order_id;
        setCancelable(true);
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
                        tablerowkefu.setVisibility(View.VISIBLE);
                        tablerowshouquanma.setVisibility(View.GONE);
                        tablerowzhaoling.setVisibility(View.VISIBLE);
                        ed_pay.requestFocus();
                        break;
                    case R.id.button2:
                        payType = 2;
                        tablerowkefu.setVisibility(View.GONE);
                        tablerowshouquanma.setVisibility(View.VISIBLE);
                        tablerowzhaoling.setVisibility(View.GONE);
                        ed_shouquanma.requestFocus();
                        break;
                    case R.id.button3:
                        payType = 3;
                        tablerowkefu.setVisibility(View.GONE);
                        tablerowshouquanma.setVisibility(View.VISIBLE);
                        tablerowzhaoling.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    protected ReceiveDialog(Context context, boolean cancelable,
                            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
    }

    @OnClick({R.id.imb_close, btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imb_close:
                dismiss();
                break;
            case btn_sure:
                upload();
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
                    new MessageDialog(getContext()).isSuccess(true).message("付款成功").show();
                } else {
                    new MessageDialog(getContext()).isSuccess(false).message("付款失败").show();
                }
                ReceiveDialog.cancle();
            }

            @Override
            public void onFailure(Object reasonObj) {
                new MessageDialog(getContext()).isSuccess(false).message("付款失败").show();
            }
        });
    }

    @OnClick(btn_sure)
    public void onViewClicked() {
    }
}
