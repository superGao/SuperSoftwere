package com.supergao.software.fragment.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.supergao.software.R;

/**
 * 加载数据对话框
 * Created by YangJie on 2015/9/10.
 */
public class LoadingDialog extends DialogFragment {
    private String mMsg = "Loading";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments() ;
        if (null != args) {
            mMsg = args.getString("msg", "加载数据中...");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_loading, null);
        TextView title = (TextView) view
                .findViewById(R.id.txt_msg);
        title.setText(mMsg);
        Dialog dialog = new Dialog(getActivity(), R.style.dialog_loading);
        dialog.setContentView(view);
        return dialog;
    }

//    @Override
//    public void setArguments(Bundle args) {
//        super.setArguments(args);
//
//        if (null != args) {
//            mMsg = args.getString("msg", "加载数据中...");
//        }
//    }
}
