package com.gly.quickgoods.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gly.quickgoods.basees.BaseLayoutView;
import com.gly.quickgoods.dao.ConnectDao;
import com.gly.quickgoods.modle.CommitOrderInfo;
import com.gly.quickgoods.modle.GoodSInfo;
import com.gly.quickgoods.utils.ReceiveDialog;
import com.gly.quickgoods.utils.ToastUtil;
import com.gly.quickgoods.utils.baseListadapter.CommonAdapter;
import com.gly.quickgoods.utils.baseListadapter.ViewHolder;
import com.gly.quickgoods.utils.okhttp.listener.DisposeDataListener;
import com.gly.quickgoods.utils.okhttp.request.RequestParams;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import gly.quickgoods.R;

import static gly.quickgoods.R.id.imb_plus;

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
        return R.layout.view_dingdan;
    }


    @Override
    public void initView() {
        datas = new ArrayList<>();
        adapter = new CommonAdapter<GoodSInfo.GoodsInfoBean>(mContext, datas, R.layout.dingdanview_list_item) {
            @Override
            public void convert(ViewHolder helper, final GoodSInfo.GoodsInfoBean item) {
                helper.getConvertView().getLayoutParams().height = (int) (list.getMeasuredHeight() / 2f);
                helper.setText(R.id.tv_name, item.goods_name);
                helper.setText(R.id.tv_price, item.price + "");
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
                        tv_count.setText(item.jianShu + "");
                    }
                };
                tv_count.setText(item.jianShu + "");
                helper.getView(imb_plus).setOnClickListener(onClickListener);
                helper.getView(R.id.imb_reduce).setOnClickListener(onClickListener);
            }
        };
        list.setAdapter(adapter);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));
        int childHeightSize = getMeasuredHeight();
        int childWidthSize = (int) (childHeightSize / 1.41f);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,
                MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @OnClick({R.id.btn_commit, R.id.btn_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                RequestParams params = new RequestParams();
                for (GoodSInfo.GoodsInfoBean bean : datas) {
                    params.put("goods_id", bean.id + "");
                }
                ConnectDao.commitOrder(params, new DisposeDataListener<CommitOrderInfo>() {
                    @Override
                    public void onSuccess(CommitOrderInfo responseObj) {
                        if (responseObj.state == 1) {
                            ReceiveDialog.ShowDialog(mContext, totalPrice, totalCount, responseObj.order_id);
                        } else {
                            ToastUtil.showToast(mContext, "提交失败!", 2000).show();
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {

                    }
                });

                break;
            case R.id.btn_clear:
                clearGoods();
                break;
        }
    }

    private void clearGoods() {
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
    }

    private void subtractGoods(double price, int jianShu) {
        totalPrice -= price * jianShu;
        totalCount -= jianShu;
        initText();
    }

    private void initText() {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
        tv_total_count.setText(totalCount + "");
        tv_total_price.setText(df.format(totalPrice) + "");
    }
}
