package com.supergao.software.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.supergao.software.R;

/**
 * 标题栏 Fragment
 * Created by YangJie on 2015/11/10.
 */
public class TitleBarFragment extends Fragment {
    /**
     * 左侧按钮
     */
    private LinearLayout mLeftButtonView ;

    /**
     * 标题内容
     */
    private TextView mTvTitleText ;

    /**
     * 标题显示内容
     */
    protected CharSequence mTitleText ;

    /**
     * 左侧按钮点击事件监听器
     */
    protected View.OnClickListener mLeftButtonOnClickListener ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.titlebar, container, false);

        return view ;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView()) ;
        // 注册左侧按钮默认点击处理
        registerDefaultClickListener();
    }

    private void initView(View view) {
        // 左侧按钮
        mLeftButtonView = (LinearLayout) view.findViewById(R.id.llayout_title_bar_left);

        mTvTitleText = (TextView) view.findViewById(R.id.txt_title_bar_center_title);

        if (!TextUtils.isEmpty(mTitleText)) {
            mTvTitleText.setText(mTitleText);
        }
    }

    /**
     * 注册默认 click listener
     */
    private void registerDefaultClickListener() {

        if (null != mLeftButtonOnClickListener) {
            mLeftButtonView.setOnClickListener(mLeftButtonOnClickListener) ;
        } else {
            mLeftButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }
    }

    /**
     * 设置显示标题
     * @param titleText 标题内容字符串
     */
    public void setTitleText(String titleText) {
        if (!TextUtils.isEmpty(titleText)) {
            this.mTitleText = titleText ;
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
            this.mTitleText = titleText ;
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
        if (null != listener) {
            mLeftButtonOnClickListener = listener ;
            if (null != mLeftButtonView) {
                mLeftButtonView.setOnClickListener(listener);
            }
        }
    }

    /**
     * 设置左侧返回按钮点击处理事件
     * @param listener 监听器对象
     */
    public void setBackButtonClickListener(View.OnClickListener listener) {
        setLeftButtonClickListener(listener);
    }
}
