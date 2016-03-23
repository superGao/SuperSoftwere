package com.supergao.software.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.supergao.software.entity.App;
import com.supergao.software.activity.BaseActivity;
import com.supergao.software.fragment.dialog.LoadingDialog;

/**
 * content fragment
 * Created by YangJie on 2015/11/7.
 */
public class ContentFragment extends Fragment {
    private App mApp ;

    /**
     * 加载对话框
     */
    private LoadingDialog mLoadingDialog ;

    /**
     * 生命周期监听
     */
    private OnContentFragmentLifecycleListener mLifecycleListener ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BaseActivity baseActivity = (BaseActivity) getActivity() ;
        mApp = baseActivity.getApp() ;

        // 通知Activity创建事件
        notifyActivityCreatedEvent() ;
    }

    protected App getApp() {
        return mApp ;
    }

    /**
     * 通知 ContentFragment ActivityCreated创建事件
     */
    protected void notifyActivityCreatedEvent() {
        // 通知监听
        if (null != mLifecycleListener) {
            mLifecycleListener.onActivityCreated(this);
        }
    }

    /**
     * 显示加载对话框
     * @param dialogMsg 加载对话框提示文字
     */
    protected void showLoadingDialog(String dialogMsg) {
        mLoadingDialog = new LoadingDialog() ;
        Bundle bundle = new Bundle() ;
        bundle.putString("msg", dialogMsg);
        mLoadingDialog.setArguments(bundle);
        mLoadingDialog.show(getActivity().getFragmentManager(), LoadingDialog.class.getSimpleName());
    }

    /**
     * 显示加载对话框
     * @param stringResId 加载对话框提示文字资源id
     */
    protected void showLoadingDialog(int stringResId) {
        mLoadingDialog = new LoadingDialog() ;
        Bundle bundle = new Bundle() ;
        bundle.putString("msg", getString(stringResId));
        mLoadingDialog.setArguments(bundle);
        mLoadingDialog.show(getActivity().getFragmentManager(), LoadingDialog.class.getSimpleName());
    }

    /**
     * 显示加载对话框
     * @param stringResId 加载对话框提示文字资源id
     * @param canCancel 点击对话框之外的区域是否可以取消loading 对话框，true：表示可以，false表示不可以
     */
    protected void showLoadingDialog(int stringResId, boolean canCancel) {
        mLoadingDialog = new LoadingDialog() ;
        mLoadingDialog.setCancelable(canCancel);
        Bundle bundle = new Bundle() ;
        bundle.putString("msg", getString(stringResId));
        mLoadingDialog.setArguments(bundle);
        mLoadingDialog.show(getActivity().getFragmentManager(), LoadingDialog.class.getSimpleName());
    }

    /**
     * 隐藏对话框
     */
    protected void dismissLoadingDialog() {
        if (null != mLoadingDialog) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null ;
        }
    }

    /**
     * 调用裁剪图片的activity
     * @param uri 要裁剪图片的uri描述对象
     */
    protected void startPhotoZoomActivityForResult(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    public void setLifecycleListener(OnContentFragmentLifecycleListener lifecycleListener) {
        this.mLifecycleListener = lifecycleListener;
    }

    /**
     * 内容Fragment
     */
    public interface OnContentFragmentLifecycleListener {
        /**
         * 当fragment创建后
         * @param contentFragment
         */
        void onActivityCreated(ContentFragment contentFragment) ;
    }
}
