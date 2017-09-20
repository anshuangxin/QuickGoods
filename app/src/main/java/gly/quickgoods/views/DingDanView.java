package gly.quickgoods.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import gly.quickgoods.R;
import gly.quickgoods.basees.BaseLayoutView;
import gly.quickgoods.utils.baseListadapter.CommonAdapter;
import gly.quickgoods.utils.baseListadapter.ViewHolder;

import static gly.quickgoods.R.id.imb_plus;

/**
 * Created by gly on 2017/9/15.
 */

public class DingDanView extends BaseLayoutView {
    @BindView(R.id.list)
    ListView list;
    private CommonAdapter<String> adapter;

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
        final List<String> datas = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            datas.add("可口可乐/含糖可乐" + i + "听");
        }
        adapter = new CommonAdapter<String>(mContext, datas, R.layout.dingdanview_list_item) {
            @Override
            public void convert(ViewHolder helper, final String item) {
                helper.getConvertView().getLayoutParams().height = (int) (list.getMeasuredHeight() / 2f);
                helper.setText(R.id.tv_name, item);
                helper.setOnclickLinstener(R.id.imb_delete, new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        datas.remove(item);
                        notifyDataSetChanged();
                    }
                });
                final TextView tv_count = helper.getView(R.id.tv_count);
                OnClickListener onClickListener = new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int count = Integer.parseInt(tv_count.getText().toString());
                        switch (view.getId()) {
                            case R.id.imb_plus:
                                tv_count.setText(++count + "");
                                break;
                            case R.id.imb_reduce:
                                if (count > 0) {
                                    tv_count.setText(--count + "");
                                }
                                break;
                            default:
                                break;
                        }
                    }
                };
                helper.getView(imb_plus).setOnClickListener(onClickListener);
                helper.getView(R.id.imb_reduce).setOnClickListener(onClickListener);
            }
        };
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
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
}
