package com.supergao.softwere.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.supergao.softwere.R;
import com.supergao.softwere.fragment.ContentFragment;
import com.supergao.softwere.fragment.TabContentFragment;
import com.supergao.softwere.fragment.TabTitleFragment;

import java.util.ArrayList;
import java.util.List;

/**
 *标题 + tab + content fragment activity
 *@author superGao
 *creat at 2016/3/14
 */
public abstract class BaseTabFragmentActivity extends BaseTitleBarActivity implements TabTitleFragment.TabTitleSelectedChangeListener, ContentFragment.OnContentFragmentLifecycleListener {

    public final static String FOCUS_TAB_CONTENT_INDEX = "focus_tab_content_index" ;

    /**
     * tab标题管理控件
     */
    protected TabTitleFragment mTabTitleFragment;

    /**
     * 标签页内容 fragment集合
     */
    protected List<TabContentFragment> mContentFragments = new ArrayList<>();

    protected int mFocusTabIndex ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_titlebar_tab_base);

        mFocusTabIndex = getIntent().getIntExtra(FOCUS_TAB_CONTENT_INDEX, 0) ;

        // 初始化View
        initViewOnTitleBar(); ;

        // 注册默认 click listener
        registerDefaultClickListener() ;

        // 初始化 Tab 标题签 的 fragment
        initTabTitleFragment() ;

        // 初始化 Tab 内容 的 fragments（一组）
        initTabContentFragments() ;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化 Tab 标题签 的 fragment
     */
    private void initTabTitleFragment() {
        FragmentManager fm = getSupportFragmentManager();

        // tab 标题
        mTabTitleFragment = (TabTitleFragment) fm.findFragmentById(R.id.flayout_tab_base_child_title);
        if (null == mTabTitleFragment) {
            mTabTitleFragment = createTabTitleFragment(this) ;
            if (null != mTabTitleFragment) {
                fm.beginTransaction().add(R.id.flayout_tab_base_child_title, mTabTitleFragment, mTabTitleFragment.getClass().getSimpleName()).commit();
            }
        }
    }

    /**
     * 初始化 Tab 内容 的 fragments（一组）
     */
    private void initTabContentFragments() {
        // Tab 内容
        if(mContentFragments.size() == 0 ) {
            List<TabContentFragment> contentFMs= createContentFragments() ;
            if (null != contentFMs && contentFMs.size()>0) {
                mContentFragments.addAll(contentFMs) ;
            }

            if (null != mContentFragments && mContentFragments.size()>0) {
                FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction() ;
                for (ContentFragment cf : mContentFragments) {

                    cf.setLifecycleListener(this); // 设置生命周期监听器对象

                    fTransaction.add(R.id.flayout_tab_base_child_content, cf) ;
                }

                mContentFragments.get(mFocusTabIndex).setFocusFlag(true); // 获得焦点状态

                // 隐藏
                for (int i=1; i< mContentFragments.size(); ++i) {
                    if (i != mFocusTabIndex) {
                        mContentFragments.get(i).setFocusFlag(false); // 失去焦点状态
                        fTransaction.hide(mContentFragments.get(i));
                    }
                }

                fTransaction.commit() ;
            }
        }
    }

    /**
     * 创建标签页标题的 Fragment
     * @return 展示标签页标题的 fragment
     */
    protected abstract TabTitleFragment createTabTitleFragment(TabTitleFragment.TabTitleSelectedChangeListener tabTitleSelectedChangeListener) ;

    /**
     * 创建展示内容的 fragments
     * @return 展示内容的 fragments
     */
    protected abstract List<TabContentFragment> createContentFragments();

    /**
     * 选中标签页位置改变
     * @param selectedPosition 选中的位置
     */
    @Override
    public void onSelectedChanged(int selectedPosition) {
        showContentFragment(selectedPosition) ;
    }

    @Override
    public void onActivityCreated(ContentFragment contentFragment) {
        TabContentFragment focusContentFragment = mContentFragments.get(mFocusTabIndex) ;
        if (focusContentFragment == contentFragment) {
            focusContentFragment.onFocus();
        }
    }

    /**
     * 显示指定索引的标签内容
     * @param position
     */
    protected final void showContentFragment(int position) {
        if (mContentFragments.size() > position) { // 判断索引的有效性
            FragmentManager fm = getSupportFragmentManager();

            TabContentFragment beforeShowedFragment = mContentFragments.get(mFocusTabIndex) ;
            mFocusTabIndex = position ; // focus 的 tabContentFragment 索引

            TabContentFragment willShowFragment = mContentFragments.get(position);
            FragmentTransaction fTransaction = fm.beginTransaction();
            for (ContentFragment cf : mContentFragments) {
                if (cf == willShowFragment) {
                    fTransaction.show(willShowFragment);
                } else {
                    fTransaction.hide(cf);
                }
            }
            fTransaction.commit();

            // 设置之前显示的fragment的 失去焦点
            beforeShowedFragment.setFocusFlag(false);

            // 调用 刷新内容
            willShowFragment.setFocusFlag(true);
            willShowFragment.onFocus();
        }
    }

    @Override
    protected void onDestroy() {
        for (TabContentFragment tabFragment : mContentFragments) {
            tabFragment.setFocusFlag(false);
        }
        mContentFragments.clear();
        super.onDestroy();
    }
}
