package com.gly.quickgoods.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.gly.quickgoods.application.MyApplication;
import com.gly.quickgoods.dao.ConnectDao;
import com.gly.quickgoods.utils.okhttp.listener.DisposeDataListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gly.quickgoods.R;

/**
 * @author gly
 * @Description:自定义对话框
 */
public class ReceiveDialog extends Dialog {
    public static ReceiveDialog receiveDialog;
    private String order_id;
    @BindView(R.id.tablerowkefu)
    TableRow tablerowkefu;
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
    private String payType = "1";

    public static void ShowDialog(Context mContext, double totalPrice, int totalCount, String order_id) {
        if (null != receiveDialog) {
            if (null != receiveDialog.getWindow() && receiveDialog.isShowing()) {
                receiveDialog.cancel();
                receiveDialog = null;
            }
        }
        receiveDialog = new ReceiveDialog(mContext, totalPrice, totalCount, order_id);
        receiveDialog.setCancelable(true);
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
        int heightPixels = mContext.getResources().getDisplayMetrics().heightPixels;

//        Window dialogWindow = getWindow();
//        dialogWindow.setGravity(Gravity.CENTER);
//        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        dialogWindow.setAttributes(p);
        int size = (int) (heightPixels * 0.8f);

        View inflate = LayoutInflater.from(mContext).inflate(R.layout.receice_dialog, null);
        setContentView(inflate/*, new RelativeLayout.LayoutParams(size, size)*/);
        ButterKnife.bind(this, inflate);
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
        tvReceive.setText(df.format(totalPrice) + "");
        tvCount.setText(totalCount + "");
        ed_pay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    Integer kefu = Integer.valueOf(charSequence.toString());
                    tvZhaoling.setText(kefu - totalPrice + "");
                } catch (Exception e) {

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                        payType = "1";
                        tablerowkefu.setVisibility(View.VISIBLE);
                        tablerowshouquanma.setVisibility(View.GONE);
                        tablerowzhaoling.setVisibility(View.VISIBLE);
                        break;
                    case R.id.button2:
                        payType = "2";
                        tablerowkefu.setVisibility(View.GONE);
                        tablerowshouquanma.setVisibility(View.VISIBLE);
                        tablerowzhaoling.setVisibility(View.GONE);
                        break;
                    case R.id.button3:
                        payType = "3";
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

    @OnClick({R.id.imb_close, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imb_close:
                dismiss();
                break;
            case R.id.btn_sure:
                ConnectDao.collection(ed_shouquanma.getText().toString(), payType, order_id, ed_pay.getText().toString(), tvReceive.getText().toString(), edPhonenum.getText().toString(), MyApplication.userId, new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String responseObj) {
                        if (responseObj.equals("1")) {
                            PayDialog.ShowDialog(getContext(), true);
                            dismiss();
                        } else {
                            PayDialog.ShowDialog(getContext(), false);
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        PayDialog.ShowDialog(getContext(), false);
                    }
                });
                break;
        }
    }

    @OnClick(R.id.btn_sure)
    public void onViewClicked() {
    }
}
