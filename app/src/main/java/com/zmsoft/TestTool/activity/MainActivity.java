package com.zmsoft.TestTool.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.taobao.sophix.SophixManager;
import com.zmsoft.TestTool.R;
import com.zmsoft.TestTool.basees.BaseActivity;
import com.zmsoft.TestTool.fragment.DianDanFragment;
import com.zmsoft.TestTool.fragment.DingDanHeYanFragment;
import com.zmsoft.TestTool.fragment.DingDanLiuFragment;
import com.zmsoft.TestTool.fragment.ShouKuanFragment;
import com.zmsoft.TestTool.fragment.TiaoMaFragment;
import com.zmsoft.TestTool.fragment.WeiXinFragment;
import com.zmsoft.TestTool.fragment.XianjinFragment;
import com.zmsoft.TestTool.utils.AnimFragmentUtil;
import com.zmsoft.TestTool.utils.Logger;

import org.lzh.framework.updatepluginlib.UpdateBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by gly on 2017/9/13.
 */

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;
    @BindView(R.id.rb_diandanshouyin)
    RadioButton rb_diandanshouyin;

    //所要申请的权限
    private String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.STATUS_BAR,
            Manifest.permission.EXPAND_STATUS_BAR,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE};
    private TiaoMaFragment tiaoMaFragment;
    private AnimFragmentUtil fragmentUtil;
    private DianDanFragment diandanFragment;
    private XianjinFragment xianjinFragment;
    private ShouKuanFragment shouKuanFragment;
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

        if (EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限

        } else {
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "请开启权限,否则程序将无法正常运行!", 0, perms);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SophixManager.getInstance().queryAndLoadNewPatch();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //把申请权限的回调交由EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //下面两个方法是实现EasyPermissions的EasyPermissions.PermissionCallbacks接口
    //分别返回授权成功和失败的权限
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Logger.log("获取成功的权限:" + perms);
         UpdateBuilder.create().check();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Logger.log("获取失败的权限:" + perms);
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
//                        Integer.parseInt("as");
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
