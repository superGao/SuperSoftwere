package com.supergao.software.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.supergao.software.R;

/**
 *基础标题栏 activity
 *@author superGao
 *creat at 2016/3/14
 */
public abstract class BaseTitleBarActivity extends BaseActivity {
    /**
     * 左侧按钮
     */
    protected LinearLayout mLeftButtonView ;

    /**
     * 标题内容
     */
    protected TextView mTvTitleText ;

    /**
     * 右侧按钮
     */
    protected TextView mRightButtonView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置无标题
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * 初始化标题栏上的控件
     */
    protected void initViewOnTitleBar() {
        // 左侧按钮
        mLeftButtonView = (LinearLayout) findViewById(R.id.llayout_title_bar_left);
        // 标题文字
        mTvTitleText = (TextView) findViewById(R.id.txt_title_bar_center_title);
        // 右侧按钮
        mRightButtonView = (TextView) findViewById(R.id.txt_title_bar_right) ;
        // 设置右侧按钮，默认隐藏
        mRightButtonView.setVisibility(View.INVISIBLE);
    }

    /**
     * 注册默认 click listener
     */
    protected void registerDefaultClickListener() {
        mLeftButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public TextView getTitleTextView() {
        return mTvTitleText ;
    }

    /**
     * 设置显示标题
     * @param titleText 标题内容字符串
     */
    public void setTitleText(String titleText) {
        if (!TextUtils.isEmpty(titleText)) {
            if (null != mTvTitleText) {
                mTvTitleText.setText(titleText);
            }
        }
    }

    /**
     * 设置显示标题
     * @param titleTextResId 标题文字资源id
     */
    public void setTitleText(int titleTextResId) {

        CharSequence titleText = getText(titleTextResId) ;
        if (!TextUtils.isEmpty(titleText)) {
            if (null != mTvTitleText) {
                mTvTitleText.setText(titleText);
            }
        }
    }

    /**
     * 设置左侧按钮点击处理事件
     * @param listener 监听器对象
     */
    public void setLeftButtonClickListener(View.OnClickListener listener) {
        if (null != mLeftButtonView && null != listener) {
            mLeftButtonView.setOnClickListener(listener);
        }
    }

    /**
     * 设置左侧返回按钮点击处理事件
     * @param listener 监听器对象
     */
    public void setBackButtonClickListener(View.OnClickListener listener) {
        setLeftButtonClickListener(listener);
    }

    /**
     * 设置右侧按钮点击处理事件
     * @param listener 监听器对象
     */
    public void setRightButtonClickListener(View.OnClickListener listener) {
        if (null != mRightButtonView && null != listener) {
            mRightButtonView.setOnClickListener(listener);
        }
    }

    /**
     * 设置右侧按钮显示问题
     * @param buttonText 显示文字
     */
    public void setRightButtonText(String buttonText) {
        if (null != mRightButtonView && !TextUtils.isEmpty(buttonText)) {
            mRightButtonView.setText(buttonText);
        }
    }

    /**
     * 设置标题栏右侧按钮是否显示
     * @param visible 值：View.VISIBLE、View.INVISIBLE、View.GONE
     */
    public void setRightButtonVisibility(int visible) {
        if (null != mRightButtonView) {
            mRightButtonView.setVisibility(visible);
        }
    }

    /**
     * 设置标题栏右侧按钮背景图片
     * @param imgResId 背景图片资源id
     */
    public void setRightButtonBackground(int imgResId) {
        if (null != mRightButtonView) {
            mRightButtonView.setText("");
            mRightButtonView.setBackgroundResource(imgResId);
        }
    }

    /**
     * 设置右侧按钮显示内容
     * @param buttonTextResId 显示文字资源id
     */
    public void setRightButtonText(int buttonTextResId) {
        CharSequence buttonText = getText(buttonTextResId) ;
        if (null != mRightButtonView && !TextUtils.isEmpty(buttonText)) {
            mRightButtonView.setText(buttonText);
        }
    }

    /**
     * 设置右侧按钮
     * @param buttonText 按钮显示文字
     * @param listener 按钮点击监听器
     */
    public void setRightButton(String buttonText, View.OnClickListener listener) {
        setRightButtonText(buttonText);
        setRightButtonClickListener(listener);
    }

    /**
     * 设置右侧按钮
     * @param buttonTextResId 按钮显示文字资源id
     * @param listener 按钮点击监听器
     */
    public void setRightButton(int buttonTextResId, View.OnClickListener listener) {
        setRightButtonText(buttonTextResId);
        setRightButtonClickListener(listener);
    }

}
