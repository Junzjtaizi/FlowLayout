package com.example.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NieQing Liu on 2017/7/30.
 * <p>
 * 自定义流布局
 */

public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // wrap_content
        int width = 0;
        int height = 0;

        // 记录每一行的宽高
        int lineWidth = 0;
        int lineHeight = 0;

        // 得到内部元素的个数
        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);

            // 测量子 View 的宽高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            // 得到子 View 的 LayoutParams
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            // 子 View 占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            // 子 View 占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            // 换行
            if (lineWidth + childWidth > sizeWidth) {
                // 对比得到最大宽度
                width = Math.max(width, lineWidth);
                // 重置 lineWidth
                lineWidth = childWidth;
                // 记录行高
                height += lineHeight;
                lineHeight = childHeight;
            } else { // 未换行
                // 叠加行款
                lineWidth += childWidth;
                // 得到当前行最大高度
                lineHeight = Math.max(lineHeight, childHeight);
            }
            // 最后一个子 View
            if (i == cCount - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }


        setMeasuredDimension(
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width,
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height
        );
    }

    /**
     * 存储所有的 View
     */
    private List<List<View>> mAllViews = new ArrayList<>();

    /**
     * 每行行高
     */
    private List<Integer> mLineHeight = new ArrayList<>();

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        mAllViews.clear();
        mLineHeight.clear();
        // 当前 ViewGroup 的宽度
        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        List<View> lineViews = new ArrayList<>();

        int cCount = getChildCount();

        for (int c = 0; c < cCount; c++) {
            View child = getChildAt(c);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            // 如果需要换行
            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width) {
                // 记录 LineHeight
                mLineHeight.add(lineHeight);
                // 记录当前行的 View
                mAllViews.add(lineViews);
                // 重置行宽高
                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
                // 重置 View 集合
                lineViews = new ArrayList<>();
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);

            lineViews.add(child);
        }

        // 处理最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        // 设置子 View 的位置
        int left = 0;
        int top = 0;

        // 行数
        int lineNum = mAllViews.size();

        for (int l = 0; l < lineNum; l++) {
            // 当前行所有的 View
            lineViews = mAllViews.get(l);
            lineHeight = mLineHeight.get(l);

            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                // 判断 child 状态
                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int rb = tc + child.getMeasuredHeight();

                // 为子 View 布局
                child.layout(lc, tc, rc, rb);

                left += child.getMeasuredWidth() + lp.leftMargin + lp.leftMargin;
            }

            left = 0;
            top += lineHeight;
        }
    }

    /**
     * 与当前 ViewGroup 对应的 LayoutParams
     *
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
