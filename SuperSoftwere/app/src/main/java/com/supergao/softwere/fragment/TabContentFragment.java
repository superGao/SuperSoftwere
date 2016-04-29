package com.supergao.softwere.fragment;

import com.supergao.softwere.utils.ToastUtil;

/**
 * 标签内容 fragment
 * Created by YangJie on 2015/11/16.
 */
public abstract class TabContentFragment extends ContentFragment {
    /**
     * 焦点状态，true：正在焦点状态，false：隐藏
     */
    protected boolean mFocusFlag = false ;

    /**
     * 获取焦点的时候调用
     */
    public void onFocus() {}

    /**
     * 显示长时间的toast
     * @param msgResId toast显示内容字符串资源id
     */
    protected void showLongToast(int msgResId) {
        if (mFocusFlag) {
            ToastUtil.showLong(getActivity(), msgResId);
        }
    }

    /**
     * 显示长时间的toast
     * @param message toast显示内容字符串
     */
    protected void showLongToast(CharSequence message) {
        if (mFocusFlag) {
            ToastUtil.showLong(getActivity(), message);
        }
    }

    /**
     * 显示短时间的toast
     * @param msgResId toast显示内容字符串资源id
     */
    protected void showShortToast(int msgResId) {
        if (mFocusFlag) {
            ToastUtil.showShort(getActivity(), msgResId);
        }
    }

    /**
     * 显示短时间的toast
     * @param message toast显示内容字符串
     */
    protected void showShortToast(CharSequence message) {
        if (mFocusFlag) {
            ToastUtil.showShort(getActivity(), message);
        }
    }

    public boolean isFocusFlag() {
        return mFocusFlag;
    }

    public void setFocusFlag(boolean focusFlag) {
        this.mFocusFlag = focusFlag;
    }
}
