package com.supergao.softwere.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.supergao.softwere.R;

import java.util.ArrayList;
import java.util.List;

/**
 *标签页标题管理 fragment
 *@author superGao
 *creat at 2016/3/14
 */
public abstract class TabTitleFragment extends Fragment {
    public static final String KEY_TAB_TITLE_SELECTED_CHANGE_LISTENER = "tab_title_selected_change_listener" ;

    /**
     * 标签页标题控件集合
     */
    protected List<RadioButton> tabTitleRbtns;

    protected RadioGroup tabTitleRgroup ;

    /**
     * 标签页标题选中改变监听器
     */
    protected TabTitleSelectedChangeListener tabTitleSelectedChangeListener ;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        defaultInitView() ;
        defaultRegisterClickListener() ;
    }

    protected void defaultInitView() {
        tabTitleRgroup = (RadioGroup) getView().findViewById(R.id.rgroup_tab_title);
        if (null == tabTitleRbtns) {
            tabTitleRbtns = new ArrayList<RadioButton>(tabTitleRgroup.getChildCount()) ;
        }
        for (int i = 0; i < tabTitleRgroup.getChildCount(); i++) {
            tabTitleRbtns.add((RadioButton) tabTitleRgroup.getChildAt(i));
        }
    }

    private void defaultRegisterClickListener() {
        tabTitleRgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                for (int i = 0; i <tabTitleRbtns.size() ; i++) {
                    if (checkedId == tabTitleRbtns.get(i).getId()) {
                        notifySelectedChangeListener(i) ;
                        break ;
                    }
                }
            }
        });
    }

    public void setTabTitleSelectedChangeListener(TabTitleSelectedChangeListener listener) {
        this.tabTitleSelectedChangeListener = listener;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        if (null != args) {
            tabTitleSelectedChangeListener = (TabTitleSelectedChangeListener) args.getSerializable(KEY_TAB_TITLE_SELECTED_CHANGE_LISTENER);
        }
    }

    /**
     * 通知 选中改变的监听者
     * @param position
     */
    protected void notifySelectedChangeListener(int position) {
        if (null != this.tabTitleSelectedChangeListener) {
            this.tabTitleSelectedChangeListener.onSelectedChanged(position);
        }
    }

    /**
     * 标签页标题选中改变监听器
     */
    public interface TabTitleSelectedChangeListener {
        /**
         * 选中标签页位置改变
         * @param selectedPosition 选中的位置
         */
        void onSelectedChanged(int selectedPosition) ;
    }
}
