package com.bjyzqs.kuaihuo_yunshouyin.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bjyzqs.kuaihuo_yunshouyin.R;
import com.bjyzqs.kuaihuo_yunshouyin.basees.BaseActivity;
import com.bjyzqs.kuaihuo_yunshouyin.fragment.DianDanFragment;
import com.bjyzqs.kuaihuo_yunshouyin.fragment.DingDanHeYanFragment;
import com.bjyzqs.kuaihuo_yunshouyin.fragment.DingDanLiuFragment;
import com.bjyzqs.kuaihuo_yunshouyin.fragment.ShouKuanFragment;
import com.bjyzqs.kuaihuo_yunshouyin.fragment.TiaoMaFragment;
import com.bjyzqs.kuaihuo_yunshouyin.fragment.WeiXinFragment;
import com.bjyzqs.kuaihuo_yunshouyin.fragment.XianjinFragment;
import com.bjyzqs.kuaihuo_yunshouyin.fragment.ZhiFuBaoFragment;
import com.bjyzqs.kuaihuo_yunshouyin.utils.AnimFragmentUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gly on 2017/9/13.
 */

public class MainActivity extends BaseActivity {

    public static boolean isForeground;
    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;
    @BindView(R.id.rb_diandanshouyin)
    RadioButton rb_diandanshouyin;

    private TiaoMaFragment tiaoMaFragment;
    private AnimFragmentUtil fragmentUtil;
    private DianDanFragment diandanFragment;
    private XianjinFragment xianjinFragment;
    private ShouKuanFragment shouKuanFragment;
    private ZhiFuBaoFragment zhiFuBaoFragment;
    private WeiXinFragment weiXinFragment;
    private DingDanHeYanFragment dingDanHeYanFragment;
    private DingDanLiuFragment dingDanLiuFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        ButterKnife.bind(this);
        fragmentUtil = new AnimFragmentUtil(getSupportFragmentManager(), R.id.container);
        initView();
    }

    private void initView() {
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rb_tiaomashouyin:
                        if (null == tiaoMaFragment) {
                            tiaoMaFragment = new TiaoMaFragment();
                        }
                        fragmentUtil.selectFragment(tiaoMaFragment);
                        break;
                    case R.id.rb_diandanshouyin:
                        if (null == diandanFragment) {
                            diandanFragment = new DianDanFragment();
                        }
                        fragmentUtil.selectFragment(diandanFragment);
                        break;
                    case R.id.rb_xianjinshouyin:
                        if (null == xianjinFragment) {
                            xianjinFragment = new XianjinFragment();
                        }
                        fragmentUtil.selectFragment(xianjinFragment);
                        break;
                    case R.id.rb_weixinshouyin:
                        if (null == weiXinFragment) {
                            weiXinFragment = new WeiXinFragment();
                        }
                        fragmentUtil.selectFragment(weiXinFragment);
                        break;
//                    case R.id.rb_zhifubaoshouyin:
//                        if (null == zhiFuBaoFragment) {
//                            zhiFuBaoFragment = new ZhiFuBaoFragment();
//                        }
//                        fragmentUtil.selectFragment(zhiFuBaoFragment);
//                        break;
                    case R.id.rb_dengdanheyan:
                        if (null == dingDanHeYanFragment) {
                            dingDanHeYanFragment = new DingDanHeYanFragment();
                        }
                        fragmentUtil.selectFragment(dingDanHeYanFragment);
                        break;
                    case R.id.rb_shouguanma:
                        if (null == shouKuanFragment) {
                            shouKuanFragment = new ShouKuanFragment();
                        }
                        fragmentUtil.selectFragment(shouKuanFragment);
                        break;
                    case R.id.rb_dingdanliu:
                        if (null == dingDanLiuFragment) {
                            dingDanLiuFragment = new DingDanLiuFragment();
                        }
                        fragmentUtil.selectFragment(dingDanLiuFragment);
                        break;

                }
            }
        });
        rb_diandanshouyin.performClick();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
