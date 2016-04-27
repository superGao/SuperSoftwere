package com.supergao.softwere.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.supergao.softwere.R;
import com.supergao.softwere.fragment.dialog.LoadingDialog;
import com.supergao.softwere.view.HeaderLayout;

public class BaseFragment extends Fragment {
  protected HeaderLayout headerLayout;
  protected Context ctx;
  /**
   * 加载对话框
   */
  private LoadingDialog mLoadingDialog ;
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    ctx = getActivity();
    headerLayout = (HeaderLayout) getView().findViewById(R.id.headerLayout);
  }

  protected void toast(String str) {
    Toast.makeText(this.getActivity(), str, Toast.LENGTH_SHORT).show();
  }

  protected void toast(int id) {
    Toast.makeText(this.getActivity(), id, Toast.LENGTH_SHORT).show();
  }

  protected boolean filterException(Exception e) {
    if (e != null) {
      toast(e.getMessage());
      return false;
    } else {
      return true;
    }
  }

  protected ProgressDialog showSpinnerDialog() {
    //activity = modifyDialogContext(activity);
    ProgressDialog dialog = new ProgressDialog(getActivity());
    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    dialog.setCancelable(true);
    dialog.setMessage(getString(R.string.chat_utils_hardLoading));
    if (!getActivity().isFinishing()) {
      dialog.show();
    }
    return dialog;
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
}
