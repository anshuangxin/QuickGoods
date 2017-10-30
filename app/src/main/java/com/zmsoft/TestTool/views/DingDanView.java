package com.zmsoft.TestTool.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zmsoft.TestTool.R;
import com.zmsoft.TestTool.application.MyApplication;
import com.zmsoft.TestTool.basees.BaseLayoutView;
import com.zmsoft.TestTool.constants.HttpConstants;
import com.zmsoft.TestTool.dao.ConnectDao;
import com.zmsoft.TestTool.dao.SpeechDao;
import com.zmsoft.TestTool.modle.CommitOrderInfo;
import com.zmsoft.TestTool.modle.GoodSInfo;
import com.zmsoft.TestTool.utils.Logger;
import com.zmsoft.TestTool.utils.ToastUtil;
import com.zmsoft.TestTool.utils.baseListadapter.CommonAdapter;
import com.zmsoft.TestTool.utils.baseListadapter.ViewHolder;
import com.zmsoft.TestTool.utils.okhttp.listener.DisposeDataListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gly on 2017/9/15.
 */

public class DingDanView extends BaseLayoutView {
    @BindView(R.id.list)
    ListView list;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.btn_clear)
    Button btnClear;
    @BindView(R.id.tv_total_price)
    TextView tv_total_price;
    @BindView(R.id.tv_total_count)
    TextView tv_total_count;
    private onChangeLinstener onChangeLinstener;
    private double totalPrice = 0;
    private int totalCount = 0;
    private List<GoodSInfo.GoodsInfoBean> datas;
    private CommonAdapter<GoodSInfo.GoodsInfoBean> adapter;

    public DingDanView(Context context) {
        this(context, null);
    }

    public DingDanView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DingDanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @NotNull
    @Override
    public int initLayout() {
        Logger.log("211221");
        return R.layout.view_dingdan;
    }


    @Override
    public void initView() {
        list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        datas = new ArrayList<>();
        adapter = new CommonAdapter<GoodSInfo.GoodsInfoBean>(mContext, datas, R.layout.dingdanview_list_item) {
            @Override
            public void convert(ViewHolder helper, final GoodSInfo.GoodsInfoBean item) {
//                helper.getConvertView().getLayoutParams().height = (int) (list.getHeight() / 3f);
                helper.getConvertView().getLayoutParams().width = list.getWidth();
                helper.setText(R.id.tv_name, item.goods_name);
                helper.setText(R.id.tv_price, String.valueOf(item.price));
                helper.setOnclickLinstener(R.id.imb_delete, new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeGood(item);
                        notifyDataSetChanged();
                    }
                });
                final TextView tv_count = helper.getView(R.id.tv_count);
                OnClickListener onClickListener = new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int count = item.jianShu;
                        switch (view.getId()) {
                            case R.id.imb_plus:
                                item.jianShu = ++count;
                                addGoods(item.price);
                                break;
                            case R.id.imb_reduce:
                                if (item.jianShu > 1) {
                                    subtractGoods(item.price, 1);
                                    item.jianShu = --count;
                                }
                                break;
                        }
                        tv_count.setText(String.valueOf(item.jianShu));
                    }
                };
                tv_count.setText(String.valueOf(item.jianShu));
                helper.getView(R.id.imb_plus).setOnClickListener(onClickListener);
                helper.getView(R.id.imb_reduce).setOnClickListener(onClickListener);
            }
        };
        list.setAdapter(adapter);
    }

//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
//                getDefaultSize(0, heightMeasureSpec));
//        int childHeightSize = getMeasuredHeight();
//        int childWidthSize = (int) (childHeightSize / 1.41f);
//        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,
//                MeasureSpec.EXACTLY);
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize,
//                MeasureSpec.EXACTLY);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }

    @OnClick({R.id.btn_commit, R.id.btn_clear, R.id.btn_guadan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                final double mtotalPrice = totalPrice;
                final int mtotalCount = totalCount;
                if (datas.size() == 0) {
                    return;
                }
                StringBuilder urlBuilder = new StringBuilder(HttpConstants.COMMITORDER).append("?");
                for (GoodSInfo.GoodsInfoBean bean : datas) {
                    urlBuilder.append("name[]").append("=").append(bean.id).append("&");
                    urlBuilder.append("num[]").append("=").append(bean.jianShu).append("&");
                }
                urlBuilder.append("user_id=" + MyApplication.userId);
                ConnectDao.commitOrder(urlBuilder.toString(), new DisposeDataListener<CommitOrderInfo>() {
                    @Override
                    public void onSuccess(CommitOrderInfo responseObj) {
                        if (responseObj.state == 1) {
                            new ReceiveDialog(mContext, mtotalPrice, mtotalCount, responseObj.order_id, new ReceiveDialog.OnCanclelinstener() {
                                @Override
                                public void onSuccess() {
                                    clearGoods();
                                }

                                @Override
                                public void onFail() {

                                }
                            }).show();
                        } else {
                            ToastUtil.showToast(mContext, "提交失败!", 2000);
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        ToastUtil.showToast(mContext, "提交失败!", 2000);
                    }
                });

                break;
            case R.id.btn_clear:
                clearGoods();
                break;
            case R.id.btn_guadan:
                if (datas.size() == 0) {
                    return;
                }
                StringBuilder url2Builder = new StringBuilder(HttpConstants.GUA_DAN).append("?");
                for (GoodSInfo.GoodsInfoBean bean : datas) {
                    url2Builder.append("name[]").append("=").append(bean.id).append("&");
                    url2Builder.append("num[]").append("=").append(bean.jianShu).append("&");
                }
                url2Builder.append("user_id=" + MyApplication.userId);
                ConnectDao.guaDan(url2Builder.toString(), new DisposeDataListener<CommitOrderInfo>() {
                    @Override
                    public void onSuccess(CommitOrderInfo responseObj) {
                        if (responseObj.state == 1) {
                            new MessageDialog(mContext).title("").message("挂单成功，请到订单流完成支付").isSuccess(true).show();
                            SpeechDao.guadan();
                            clearGoods();
                        } else {
                            ToastUtil.showToast(mContext, "挂单失败!", 2000);
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        ToastUtil.showToast(mContext, "挂单失败!", 2000);
                    }
                });

                break;
        }
    }

    public interface onChangeLinstener {
        void onClear();
    }

    public void setOnChangeLinstener(DingDanView.onChangeLinstener onChangeLinstener) {
        this.onChangeLinstener = onChangeLinstener;
    }

    private void clearGoods() {
        if (null != onChangeLinstener) {
            onChangeLinstener.onClear();
        }
        for (GoodSInfo.GoodsInfoBean bean : datas) {
            bean.jianShu = 0;
        }
        datas.clear();
        adapter.notifyDataSetChanged();
        totalPrice = 0;
        totalCount = 0;
        initText();
    }

    private void removeGood(GoodSInfo.GoodsInfoBean bean) {
        subtractGoods(bean.price, bean.jianShu);
        bean.jianShu = 0;
        datas.remove(bean);
    }


    public void addGoods(GoodSInfo.GoodsInfoBean goodsInfoBean) {
        if (datas.contains(goodsInfoBean)) {
            datas.get(datas.indexOf(goodsInfoBean)).jianShu++;
        } else {
            goodsInfoBean.jianShu++;
            datas.add(goodsInfoBean);
        }
        addGoods(goodsInfoBean.price);
        adapter.notifyDataSetChanged();
    }

    private void addGoods(double price) {
        totalPrice += price;
        totalCount++;
        initText();
        list.setSelection(adapter.getCount());
    }

    private void subtractGoods(double price, int jianShu) {
        totalPrice -= price * jianShu;
        totalCount -= jianShu;
        initText();
    }

    private void initText() {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
        tv_total_count.setText(String.valueOf(totalCount));
        tv_total_price.setText(String.valueOf(df.format(totalPrice)));
    }
}
