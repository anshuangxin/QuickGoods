
package com.bjyzqs.kuaihuo_yunshouyin.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @desc 最大化 无法滚动ListView
 * @auth  
 */
public class MaxHeightListView extends ListView {
    public MaxHeightListView(Context context) {
        super(context);
    }

    public MaxHeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxHeightListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
