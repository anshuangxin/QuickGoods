package gly.quickgoods.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import gly.quickgoods.R;
import gly.quickgoods.basees.BaseFragment;

/**
 * Created by gly on 2017/9/13.
 */

public class XianjinFragment extends BaseFragment {

    @BindView(R.id.ed_kefu)
    EditText edKefu;
    @BindView(R.id.ed_yingshou)
    EditText edYingshou;
    @BindView(R.id.ed_phonenum)
    EditText edPhonenum;
    @BindView(R.id.ed_zhaoling)
    TextView edZhaoling;
    @BindView(R.id.btn_sure)
    Button btnSure;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return getView(inflater, R.layout.fragment_xianjin, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick(R.id.btn_sure)
    public void onViewClicked() {

    }
}
