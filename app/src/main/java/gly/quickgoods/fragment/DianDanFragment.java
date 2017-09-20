package gly.quickgoods.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jingchen.pulltorefresh.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import gly.quickgoods.R;
import gly.quickgoods.basees.BaseFragment;
import gly.quickgoods.utils.ToastUtil;
import gly.quickgoods.utils.baseListadapter.CommonAdapter;
import gly.quickgoods.utils.baseListadapter.ViewHolder;

/**
 * Created by gly on 2017/9/13.
 */

public class DianDanFragment extends BaseFragment {
    @BindView(R.id.rb_top)
    RadioButton rbTop;
    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;
    private GridView gridView;
    private PullToRefreshLayout ptrl;
    private boolean isFirstIn = true;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return getView(inflater, R.layout.fragment_diandan, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ptrl = getView(R.id.refresh_view);
        // 此处设置下拉刷新或上拉加载更多监听器
        ptrl.setOnPullListener(new MyPullListener());
        // 设置带gif动画的上拉头与下拉头
//        try {
//            ptrl.setGifRefreshView(new GifDrawable(getResources(), R.drawable.anim));
//            ptrl.setGifLoadmoreView(new GifDrawable(getResources(), R.drawable.anim));
//
//        } catch (Resources.NotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        gridView = (GridView) ptrl.getPullableView();
        initListView();
    }

    private void initView() {
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rb_top:

                        break;
                    case R.id.rb_recai:
                        break;
                    case R.id.rb_liangcai:
                        break;
                    case R.id.rb_zhushi:
                        break;
                    case R.id.rb_jiushui:
                        break;
                }
            }
        });
        rbTop.performClick();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 第一次进入自动刷新
        if (isFirstIn) {
            ptrl.autoRefresh();
            isFirstIn = false;
        }
    }


    /**
     * ListView初始化方法
     */
    private void initListView() {
        List<String> items = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            items.add("这里是item " + i);
        }
        CommonAdapter<String> adapter = new CommonAdapter<String>(mContext, items, R.layout.tiaoma_list_item) {
            @Override
            public void convert(ViewHolder helper, String item) {
                helper.getConvertView().getLayoutParams().width = gridView.getMeasuredWidth() / gridView.getNumColumns();
                helper.getConvertView().getLayoutParams().height = (int) (helper.getConvertView().getLayoutParams().width * 1.533f);
                helper.setText(R.id.tv_name, item);
            }
        };
        gridView.setAdapter(adapter);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Toast.makeText(
                        mContext,
                        "LongClick on "
                                + parent.getAdapter().getItemId(position),
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ToastUtil.showToast(mContext,
                        " Click on " + parent.getAdapter().getItemId(position),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 下拉刷新与上拉加载更多监听器
     */
    public class MyPullListener implements PullToRefreshLayout.OnPullListener {

        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            // 下拉刷新操作
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // 千万别忘了告诉控件刷新完毕了哦！
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }.sendEmptyMessageDelayed(0, 5000);
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 加载更多操作
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // 千万别忘了告诉控件加载完毕了哦！
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }.sendEmptyMessageDelayed(0, 5000);
        }
    }
}
