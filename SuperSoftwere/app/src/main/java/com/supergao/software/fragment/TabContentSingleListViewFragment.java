package com.supergao.software.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Tab 展示内容只有一个ListView 的 fragment
 * Created by YangJie on 2015/11/24.
 */
public abstract class TabContentSingleListViewFragment extends TabContentFragment {

    /**
     * 内容展示ListView
     */
    protected ListView mContentListView ;

    /**
     * 内容布局 资源id
     */
    protected int mContentViewResId ;

    /**
     * 内容 listView id 资源id
     */
    protected int mContentListViewIdResId ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(mContentViewResId, container, false);
        initView(contentView) ;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initView(View view) {
        mContentListView = (ListView) view.findViewById(mContentListViewIdResId) ;
    }

    protected abstract View createContentView() ;


    protected void setRequireResId(int contentViewResId, int listViewIdResId) {
        this.mContentViewResId = contentViewResId ;
        this.mContentListViewIdResId = listViewIdResId ;
    }

}
