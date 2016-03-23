package com.supergao.software.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.supergao.software.R;

import lib.support.utils.ToastUtil;


public class HomeSoftwFragment extends ContentFragment implements View.OnClickListener {

    private RelativeLayout orderQueryRlayout ;

    private RelativeLayout orderForwardRlayout ;

    private RelativeLayout receiptRlayout ;

    private RelativeLayout confirmReceiptRlayout ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_function, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        registerClickListener() ;
    }

    private void registerClickListener() {
        orderQueryRlayout.setOnClickListener(this);
        orderForwardRlayout.setOnClickListener(this);
        receiptRlayout.setOnClickListener(this);
        confirmReceiptRlayout.setOnClickListener(this);

    }

    private void initView() {
        orderQueryRlayout = (RelativeLayout) getView().findViewById(R.id.rlayout_order_query) ;
        orderForwardRlayout = (RelativeLayout) getView().findViewById(R.id.rlayout_order_forward) ;
        receiptRlayout = (RelativeLayout) getView().findViewById(R.id.rlayout_receipt) ;
        confirmReceiptRlayout = (RelativeLayout) getView().findViewById(R.id.rlayout_confirm_receipt);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null ;
        switch (v.getId()) {
            case R.id.rlayout_order_query: { // function1
                ToastUtil.showShort(getActivity(),"function1");
                break ;
            }
            case R.id.rlayout_order_forward: { // function2
                ToastUtil.showShort(getActivity(), "function2");
                break ;
            }
            case R.id.rlayout_receipt: { // function3
                ToastUtil.showShort(getActivity(),"function3");
                break ;
            }
            case R.id.rlayout_confirm_receipt: { // function4
                ToastUtil.showShort(getActivity(),"function4");
                break ;
            }
        }
        if (null != intent) {
            startActivity(intent);
        }
    }
}
