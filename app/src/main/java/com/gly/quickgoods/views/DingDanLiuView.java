package com.gly.quickgoods.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gly.quickgoods.basees.BaseLayoutView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import gly.quickgoods.R;

import com.gly.quickgoods.utils.baseListadapter.CommonAdapter;
import com.gly.quickgoods.utils.baseListadapter.ViewHolder;

/**
 * Created by gly on 2017/9/19.
 */

public class DingDanLiuView extends BaseLayoutView {
    @BindView(R.id.circle_view)
    ImageView circleView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.textview2)
    TextView textview2;
    @BindView(R.id.tv_tablecode)
    TextView tvTablecode;
    @BindView(R.id.tv_pay_man)
    TextView tvPayMan;
    @BindView(R.id.listview)
    ListView listview;

    public DingDanLiuView(Context context) {
        this(context, null);
    }

    public DingDanLiuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DingDanLiuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @NotNull
    @Override
    public int initLayout() {
        return R.layout.dingdanliu_view;
    }

    @Override
    public void initView() {
        List<String> datas = new ArrayList<>();
       for (int i = 0; i< Math.random()*4;i++){
           datas.add("");
       }
        listview.setAdapter(new CommonAdapter<String>(mContext, datas, R.layout.dingdanliuview_listitem) {
            @Override
            public void convert(ViewHolder helper, String item) {

            }
        });
    }
}
