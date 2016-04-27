package com.supergao.softwere.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.CloudQueryCallback;
import com.avoscloud.leanchatlib.utils.Constants;
import com.supergao.softwere.R;
import com.supergao.softwere.activity.MainActivity;
import com.supergao.softwere.activity.MipcaActivityCapture;
import com.supergao.softwere.activity.WebActivity;
import com.supergao.softwere.bean.ImageInfo;
import com.supergao.softwere.entity.AppConfig;
import com.supergao.softwere.friends.ContactPersonInfoActivity;
import com.supergao.softwere.handler.MessageDefine;
import com.supergao.softwere.handler.MessageManager;
import com.supergao.softwere.utils.AVService;
import com.supergao.softwere.utils.ImageUtil;
import com.supergao.softwere.utils.Log;
import com.supergao.softwere.utils.NetWorkUtil;
import com.supergao.softwere.utils.QRCodeUtils;
import com.supergao.softwere.utils.SharedPreferencesUtil;
import com.supergao.softwere.utils.SpeechUtils;
import com.supergao.softwere.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import lib.support.utils.LogUtils;

/**
 *彩蛋fragment
 *@author superGao
 */
public class HomeSoftwFragment extends BaseFragment implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private RelativeLayout orderQueryRlayout ;

    private RelativeLayout orderForwardRlayout ;

    private RelativeLayout receiptRlayout ;

    private RelativeLayout confirmReceiptRlayout ;
    private ViewPager mViewPager;
    private LinearLayout llPointGroup;
    private MyPagerAdapter mAdapter;
    private List<ImageInfo> list;
    private List<ImageView> imageViewList;
    private int previousPosition = 0;
    private boolean isStop = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_function, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initHeader();
        showRightButton();
        registerClickListener() ;
        // 展示广告
        switchAd();
    }

    @Override
    public void onResume() {
        super.onResume();
        showAvatar();
    }

    private void initHeader() {
        headerLayout.showTitle("彩蛋");
    }

    /**
     * 显示头像
     */
    private void showAvatar(){
        headerLayout.showLeftImageByUrl(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mDragLayout.open();//侧滑
            }
        });
    }

    /**
     * 显示标题栏右侧按钮
     */
    private void showRightButton(){
        headerLayout.showRightImageButton(R.drawable.scan_qrcode_selector, new View.OnClickListener() {
            @Override
            public void onClick(View v) {//进入二维码扫描页面
                Intent intent = new Intent();
                intent.setClass(getActivity(), MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, MainActivity.REQUEST_CODE_SCANNIN_QRCODE);
            }
        });
    }

    /**
     * 处理扫描二维码得到的结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (MainActivity.REQUEST_CODE_SCANNIN_QRCODE == requestCode) {
            if (resultCode == getActivity().RESULT_OK) {
                Bundle bundle = data.getExtras();
                String result = bundle.getString("result"); // 扫描二维码获取的信息
                Log.d("result","   result: " + result);
                String userId=QRCodeUtils.parseContent(result).get("userId");
                if(!TextUtils.isEmpty(userId)){
                    Intent intent = new Intent(getActivity(), ContactPersonInfoActivity.class);
                    intent.putExtra(Constants.LEANCHAT_USER_ID, userId);
                    startActivity(intent);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
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
        mViewPager = (ViewPager)getView().findViewById(R.id.viewpager_homepage);
        llPointGroup = (LinearLayout)getView().findViewById(R.id.ll_point_group);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null ;
        switch (v.getId()) {
            case R.id.rlayout_order_query: { // 语音操作
                SpeechUtils.voiceOperation(getActivity());
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
                startActivity(new Intent(getActivity(),WebActivity.class));
                break ;
            }
        }
        if (null != intent) {
            startActivity(intent);
        }
    }

    /**
     * 切换广告图片
     * 加载用户头像
     */
    private void switchAd() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStop) {
                    SystemClock.sleep(3000);
                    MessageManager.sendMessage(handler, MessageDefine.PIC, null);
                }
            }
        }).start();
        loadAdvertisement();
    }
    /**
     * 消息接收器
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessageDefine.PIC:
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    break;
            }
        }
    };

    /**
     * 加载广告图
     */
    private void loadAdvertisement(){
        if (!NetWorkUtil.isNetworkConnected(getActivity())) {
            ToastUtil.showLong(getActivity().getApplicationContext(), "网络请求失败，请检查您的网络设置");

            list = SharedPreferencesUtil.getRollImage(getActivity());
            if (list.size() > 0) {
                initViewHomePage();
            } else {
                mViewPager.setBackgroundResource(R.drawable.pic_link);
            }
        } else {
            mViewPager.setBackgroundResource(R.drawable.pic_loading);
            //加载广告图片
            CloudQueryCallback<AVCloudQueryResult> cloudQueryCallback=new CloudQueryCallback<AVCloudQueryResult>() {
                @Override
                public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                    if(e==null){
                        list=new ArrayList<ImageInfo>();
                        for(int i=0;i<avCloudQueryResult.getResults().size();i++){
                            ImageInfo imageInfo=new ImageInfo();
                            imageInfo.setUrl(avCloudQueryResult.getResults().get(i).getString("url"));
                            list.add(imageInfo);
                        }
                        Log.d("resStr", list.get(2).getUrl());
                        if(null!=list){
                            SharedPreferencesUtil.saveRollImage(getActivity(), list);
                            initViewHomePage();
                        }
                    }else{
                        Log.d("res",e.getMessage()+"  code="+e.getCode());
                    }
                }
            };
            AVService.loadPicture(cloudQueryCallback);
        }
    }

    /**
     * 广告切换适配器
     */
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        /**
         * 移动的对象和进来的对象如果是同一个就返回true, 代表复用view对象 false 使用object对象
         */
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        /**
         * 需要销毁的对象的position传进来
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 移除掉指定position的对象
            // container.removeView(imageViewList.get(position % imageViewList.size()));
        }

        /**
         * 加载position位置的view对象
         */
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            // 添加指定position的对象
            try {
                View view = imageViewList.get(position % imageViewList.size()) ;
                container.removeView(view);
                container.addView(view);
            } catch (Exception e) {
                e.printStackTrace();
            }

            imageViewList.get(position % imageViewList.size())
                    .setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {//点击图片跳转到网页
//                            Intent intent = new Intent(MainActivity.this, AdvertActivity.class);
//                            intent.putExtra("url", list.get(position % imageViewList.size()).getUrl());
//                            startActivity(intent);
                        }
                    });
            return imageViewList.get(position % imageViewList.size());
        }

    }

    /**
     * 广告条
     * 显示广告图片
     */
    private void initViewHomePage() {
        imageViewList = new ArrayList<ImageView>();
        ImageView iv;
        View pointView;
        DrawerLayout.LayoutParams params;

        llPointGroup.removeAllViews();

        ImageUtil tool = new ImageUtil();
        for (int i = 0; i < list.size(); i++) {
            iv = new ImageView(getActivity());
            imageViewList.add(iv);
            tool.getImageBitmap(iv,list.get(i).getUrl(),
                    new ImageUtil.ImageCallBack() {

                        @Override
                        public void loadImage(final ImageView imageView,
                                              final Bitmap bitmap) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        }

                        @Override
                        public void loadDefaultImage(ImageView imageView) {
                            mAdapter.notifyDataSetChanged();
                        }
                    });

            // 根据图片添加点
            pointView = new View(getActivity());
            //params = new LayoutParams(DensityUtil.dip2px(
            //getApplicationContext(), 5), DensityUtil.dip2px(
            //getApplicationContext(), 5));
            //params.leftMargin = DensityUtil.dip2px(getApplicationContext(), 10);
            LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(10, 10);
            para.setMargins(10, 0, 10, 0);
            pointView.setLayoutParams(para);
            pointView.setEnabled(false);
            pointView.setBackgroundResource(R.drawable.point_bg);
            llPointGroup.addView(pointView);
        }

        mAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(mAdapter);

        mViewPager.setOnPageChangeListener(this);
        llPointGroup.getChildAt(previousPosition).setEnabled(true); // 第一点被选中
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        llPointGroup.getChildAt(position % imageViewList.size()).setEnabled(true);
        llPointGroup.getChildAt(previousPosition).setEnabled(false);
        previousPosition = position % imageViewList.size();

    }
}
