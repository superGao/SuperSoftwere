package com.supergao.softwere.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.supergao.softwere.R;
import com.supergao.softwere.fragment.ContentFragment;


/**
 *公共Fragment基础类
 *@author superGao
 *creat at 2016/3/14
 */
public abstract class BaseSingleFragmentActivity extends BaseTitleBarActivity {

    /**
     * 展示内容 content fragment
     */
    protected ContentFragment mContentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_single_base);

        // 初始化View
        initViewOnTitleBar(); ;

        // 注册默认 click listener
        registerDefaultClickListener() ;

        initContentFragment() ;
    }

    /**
     * 初始化 内容 fragment
     */
    protected void initContentFragment() {
        // 创建fragment
        FragmentManager fm = getSupportFragmentManager();
        mContentFragment = (ContentFragment) fm.findFragmentById(getFragmentLayoutResourceId());

        if(mContentFragment == null ) {
            mContentFragment = createContentFragment() ;
            if (null != mContentFragment) {
                fm.beginTransaction().add(getFragmentLayoutResourceId(), mContentFragment).commit();
            }
        }
    }

    /**
     * 创建展示内容的 fragment
     * @return 展示内容的fragment
     */
    protected abstract ContentFragment createContentFragment();

    /**
     * 获取容纳Fragment布局的资源id
     * @return 容纳fragment的布局容器的资源id
     */
    public int getFragmentLayoutResourceId() {
        return R.id.flayout_single_base_child_content ;
    }

    /**
     * start activity with interceptor
     */
//    public void startActivityWithInterceptor(Intent intent, Interceptor interceptor) {
//        if (null != interceptor) {
//            if (interceptor.beforeStartActivity()) {
//                startActivity(intent);
//            }
//        } else {
//            startActivity(intent);
//        }
//    }
}
