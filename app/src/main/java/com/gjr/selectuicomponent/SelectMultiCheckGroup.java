package com.gjr.selectuicomponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geng
 * on 2017/4/13.
 * <p>
 * 用于筛选器，点击选择
 */
public class SelectMultiCheckGroup extends LinearLayout {

    private boolean isSingleSelected;// 默认多选
    private int column;// 列
    private int row;// 行
    private int itemHorizontalSpace;// item之间的水平间距
    private int itemVerticalSpace;// item之间的垂直间距
    private int itemHorizontalPadding;// item的水平内边距
    private int itemVerticalPadding;// item的垂直内边距
    private int itemTextSize;// 字体大小，单位 sp

    private List<RadioButton> radioButtonList;
    private List<CheckBox> checkBoxList;

    private int mSelected;
    private List<Integer> mSelectedList;
    private OnItemSelectedListener listener;
    private List<String> mData;

    public SelectMultiCheckGroup(Context context) {
        this(context, null);
    }

    public SelectMultiCheckGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectMultiCheckGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
        initView();
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SelectMultiCheckGroup);
            isSingleSelected = ta.getBoolean(R.styleable.SelectMultiCheckGroup_isSingleSelected, false);// 默认多选
            column = ta.getInteger(R.styleable.SelectMultiCheckGroup_column, 1);// 默认 1列
            row = ta.getInteger(R.styleable.SelectMultiCheckGroup_row, 1);// 默认 1行
            itemHorizontalSpace = ta.getDimensionPixelSize(R.styleable.SelectMultiCheckGroup_item_horizontal_space,
                    getResources().getDimensionPixelSize(R.dimen.item_horizontal_space));// 默认 14dp
            itemVerticalSpace = ta.getDimensionPixelSize(R.styleable.SelectMultiCheckGroup_item_vertical_space,
                    getResources().getDimensionPixelSize(R.dimen.item_horizontal_space));// 默认 14dp
            itemHorizontalPadding = ta.getDimensionPixelSize(R.styleable.SelectMultiCheckGroup_item_horizontal_padding,
                    getResources().getDimensionPixelSize(R.dimen.item_horizontal_padding));// 默认 8dp
            itemVerticalPadding = ta.getDimensionPixelSize(R.styleable.SelectMultiCheckGroup_item_vertical_padding,
                    getResources().getDimensionPixelSize(R.dimen.item_vertical_padding));// 默认 6dp
            itemTextSize = ta.getDimensionPixelSize(R.styleable.SelectMultiCheckGroup_item_text_size, 12);// 默认 12sp
            ta.recycle();
        } else {
            column = 1;
            row = 1;
            itemHorizontalSpace = ScreenUtil.dip2px(getContext(), 14f);
            itemVerticalSpace = ScreenUtil.dip2px(getContext(), 6f);
            itemHorizontalPadding = ScreenUtil.dip2px(getContext(), 8f);
            itemVerticalPadding = ScreenUtil.dip2px(getContext(), 6f);
            itemTextSize = 12;
        }

        if (isSingleSelected) {
            radioButtonList = new ArrayList<>();
        } else {
            mSelectedList = new ArrayList<>();
            checkBoxList = new ArrayList<>();
        }
    }

    private void initView() {
        setOrientation(VERTICAL);
        for (int i = 0; i < row; i++) {
            RadioGroup rg = null;
            LinearLayout rowLL = null;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, itemVerticalSpace, itemHorizontalSpace, 0);
            if (isSingleSelected) {// 单选
                rg = new RadioGroup(getContext());
                rg.setLayoutParams(lp);
                rg.setOrientation(HORIZONTAL);
            } else {// 多选
                rowLL = new LinearLayout(getContext());
                rowLL.setLayoutParams(lp);
                rowLL.setOrientation(HORIZONTAL);
            }

            for (int j = 0; j < column; j++) {
                final int finalI = i;
                final int finalJ = j;

                if (isSingleSelected) {// 单选
                    RadioGroup.LayoutParams rglp = new RadioGroup.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                    rglp.leftMargin = itemHorizontalSpace;
                    rglp.bottomMargin = 5;// 加点底部边距，否则，底部的 bound 被遮住一点
                    RadioButton rb = new RadioButton(getContext());
                    rb.setLayoutParams(rglp);
                    rb.setBackgroundResource(R.drawable.select_cb_selector);
                    rb.setTextColor(getResources().getColorStateList(R.color.cb_textcolor_selector));
                    rb.setButtonDrawable(android.R.color.transparent);
                    rb.setGravity(Gravity.CENTER);
                    rb.setPadding(itemHorizontalPadding, itemVerticalPadding, itemHorizontalPadding, itemVerticalPadding);
                    rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, itemTextSize);
                    rb.setSingleLine(true);
                    rb.setEllipsize(TextUtils.TruncateAt.END);

                    rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {// 再次点击已经处于选中状态的radiobutton，本身就不会进行监听回调的
                                mSelected = finalI * column + finalJ;
                                for (int k=0; k< row; k++) {
                                    if (radioButtonList.get(mSelected).getParent() != getChildAt(k)) {
                                        ((RadioGroup)getChildAt(k)).clearCheck();
                                    }
                                }
                                if (listener != null) {
                                    listener.checked(buttonView, mSelected, true);
                                }
                            }
                        }
                    });

                    if (rg != null) {
                        rg.addView(rb);
                        radioButtonList.add(rb);
                    }
                } else {// 多选
                    LinearLayout.LayoutParams lllp = new LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                    lllp.leftMargin = itemHorizontalSpace;
                    lllp.bottomMargin = 5;
                    CheckBox cb = new CheckBox(getContext());
                    cb.setLayoutParams(lllp);
                    cb.setBackgroundResource(R.drawable.select_cb_selector);
                    cb.setTextColor(getResources().getColorStateList(R.color.cb_textcolor_selector));
                    cb.setButtonDrawable(android.R.color.transparent);
                    cb.setGravity(Gravity.CENTER);
                    cb.setPadding(itemHorizontalPadding, itemVerticalPadding, itemHorizontalPadding, itemVerticalPadding);
                    cb.setTextSize(TypedValue.COMPLEX_UNIT_SP, itemTextSize);
                    cb.setSingleLine(true);
                    cb.setEllipsize(TextUtils.TruncateAt.END);

                    cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            mSelected = finalI * column + finalJ;
                            if (mSelectedList.contains(mSelected)) {
                                mSelectedList.remove(mSelectedList.indexOf(mSelected));
                            } else {
                                mSelectedList.add(mSelected);
                            }
                            if (listener != null) {
                                listener.checked(buttonView, mSelected, isChecked);
                            }
                        }
                    });

                    if (rowLL != null) {
                        rowLL.addView(cb);
                        checkBoxList.add(cb);
                    }
                }
            }
            if (isSingleSelected) {
                addView(rg);
            } else {
                addView(rowLL);
            }
        }
    }

    /**
     * 初始化数据
     */
    public void initData(List<String> data) {
        if (data == null || data.isEmpty()
                || data.size() > ((isSingleSelected) ? radioButtonList.size() : checkBoxList.size())) {
            throw new RuntimeException("setData() 参数不合法");
        }
        mData = new ArrayList<>();
        mData.addAll(data);

        setData();
    }

    /**
     * 设置显示的数据
     */
    private void setData() {
        if (isSingleSelected) {
            if (mData.size() < radioButtonList.size()) {
                for (int i = mData.size(); i < radioButtonList.size(); i++) {
                    radioButtonList.get(i).setVisibility(INVISIBLE);
                    radioButtonList.remove(i);
                }
            }
            for (int i = 0; i < mData.size(); i++) {
                radioButtonList.get(i).setText(mData.get(i));
            }
        } else {
            if (mData.size() < checkBoxList.size()) {
                for (int i = mData.size(); i < checkBoxList.size(); i++) {
                    checkBoxList.get(i).setVisibility(INVISIBLE);
                    checkBoxList.remove(i);
                }
            }
            for (int i = 0; i < mData.size(); i++) {
                checkBoxList.get(i).setText(mData.get(i));
            }
        }
        setSeleted(0);
        mSelected = 0;
    }

    /**
     * 针对单选 使用
     */
    public int getSelectedOne() {
        if (isSingleSelected) {
            return mSelected;
        } else {
            throw new RuntimeException("针对单选使用，app:isSingleSelected=\"true\"");
        }
    }

    /**
     * 针对 多选 使用
     */
    public List<Integer> getSelectedAll() {
        if (isSingleSelected) {
            throw new RuntimeException("针对多选使用，app:isSingleSelected=\"false\"");
        } else {
            return mSelectedList;
        }
    }

    /**
     * 选择某个
     * @param position position
     */
    public void setSeleted(int position) {
        if (position<0 || position>=mData.size()) {
            return;
        }
        if (isSingleSelected) {
            radioButtonList.get(position).setChecked(true);
        } else {
            checkBoxList.get(position).setChecked(true);
        }
    }


    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }


    interface OnItemSelectedListener {
        void checked(View view, int position, boolean isChecked);
    }

}