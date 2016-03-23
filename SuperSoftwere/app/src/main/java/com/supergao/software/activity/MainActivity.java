package com.supergao.software.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.koushikdutta.ion.Ion;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.supergao.circledimageview.CircledImageView;
import com.supergao.software.R;
import com.supergao.software.activity.user.LoginActivity;
import com.supergao.software.activity.user.UserInfoActivity;
import com.supergao.software.bean.ImageInfo;
import com.supergao.software.entity.AppConfig;
import com.supergao.software.fragment.HomeLeftmenuFragment;
import com.supergao.software.fragment.HomeSoftwFragment;
import com.supergao.software.handler.MessageDefine;
import com.supergao.software.handler.MessageManager;
import com.supergao.software.utils.AVService;
import com.supergao.software.utils.ImageUtil;
import com.supergao.software.utils.JsonParser;
import com.supergao.software.utils.NetWorkUtil;
import com.supergao.software.utils.SharedPreferencesUtil;
import com.supergao.software.utils.SpeechOperateUtils;
import com.supergao.software.view.DragLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import lib.support.utils.ToastUtil;

/**
 *主界面Activity
 *@author superGao
 *creat at 2016/3/14
 */
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    ImageLoader mImageLoader;

    DisplayImageOptions mDisplayImageOptions;


    /**
     * 侧滑栏组件
     */
    private DragLayout mDragLayout;

    /**
     * 主界面头像组件
     */
    private CircledImageView mTitleHeaderImg;

    /**
     * 左侧菜单头像
     */
    public CircledImageView mLeftmenuHeaderImg;

    /**
     * 左侧菜单 用户名 控件
     */
    public TextView usernameLeftMenuTxt ;

    /**
     * 布局标题 右侧 更多按钮
     */
    private TextView moreTxt ;


    /**
     * 物流公司功能Fragment对象
     */
    public HomeSoftwFragment mHomeSoftwFragment;

    /**
     * 老板功能左侧菜单Fragment对象
     */
    public Fragment mHomeBossLeftmenuFragment;

    /**
     * 判断是否首次登录
     */
    private long firstTime;

    private boolean isLogin = true;

    private AVUser avUser;

    private String Tag="iflytek";

    private Toast mToast;

    // 语音听写对象
    private SpeechRecognizer mIat;

    // 语音听写UI
    private RecognizerDialog mIatDialog;

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    boolean isShowDialog = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        avUser=AppConfig.avUser;
        // 初始化View
        initSelfView();
        // 初始化侧滑栏
        initDragLayout();
        // 注册点击按钮点击监听器
        registerClickListener();
        // 展示广告
        switchAd();
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(MainActivity.this, mInitListener);
        mIatDialog = new RecognizerDialog(MainActivity.this, mInitListener);

        //AVAnalytics.trackAppOpened(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 初始化显示数据
        initShowData() ;
    }

    /**
     * 初始化显示数据
     */
    private void initShowData() {
        if (null != avUser) {
            usernameLeftMenuTxt.setText(avUser.getUsername());
            // 左侧菜单显示头像
            Ion.with(mLeftmenuHeaderImg)
                        .placeholder(R.drawable.icon_leftmenu_header).error(R.drawable.icon_leftmenu_header)
                        .load(AppConfig.userInfo.getHeader()) ;
            // 标题栏 头像
            Ion.with(mTitleHeaderImg)
                    .placeholder(R.drawable.icon_header).error(R.drawable.icon_header)
                        .load(AppConfig.userInfo.getHeader()) ;
        } else {
            usernameLeftMenuTxt.setText("登录用户");
        }
    }

    /**
     * 初始化view
     */
    private void initSelfView() {
        mTitleHeaderImg = (CircledImageView) findViewById(R.id.iv_home_header);
        mTitleHeaderImg.setOval(true);

        // 更多
        moreTxt = (TextView) findViewById(R.id.tv_home_more) ;
        moreTxt.setText("");
        moreTxt.setBackgroundResource(R.drawable.icon_voice);

        // 初始化左侧menu view
        initLeftMenuView();

        mHomeSoftwFragment = new HomeSoftwFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_function_container, mHomeSoftwFragment)
                .commit();

        mHomeBossLeftmenuFragment = new HomeLeftmenuFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ll_fragment_leftmenu_container, mHomeBossLeftmenuFragment)
                .commit();
    }

    /**
     * 初始化侧滑
     */
    private void initDragLayout() {
        mDragLayout = (DragLayout) findViewById(R.id.dl);
        mDragLayout.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {
//                if (AppConfig.userInfo != null) {
//                    tv_leftmenu_username.setText(AppConfig.userInfo.getUsername());
//                }
            }

            @Override
            public void onClose() {
                shake();
            }

            @Override
            public void onDrag(float percent) {
                ViewHelper.setAlpha(mTitleHeaderImg, 1 - percent);
            }
        });
    }

    /**
     * 初始化左侧menu view
     */
    private void initLeftMenuView() {
        mLeftmenuHeaderImg = (CircledImageView) findViewById(R.id.iv_leftmenu_header) ;
        mLeftmenuHeaderImg.setOval(true);
        mLeftmenuHeaderImg.setOnClickListener(this);

        usernameLeftMenuTxt = (TextView) findViewById(R.id.tv_leftmenu_username) ;
    }

    private void registerClickListener() {
        mTitleHeaderImg.setOnClickListener(this);

        // 标题栏 右侧 更多
        moreTxt.setOnClickListener(this);
    }


    /**
     * 头像晃动效果
     */
    private void shake() {
        mTitleHeaderImg.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.shake));
    }

    /**
     * 任务栈模式 设置为singleTask 在栈底 再次启动mainActivity会调用该方法
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if ("reLogin".equals(intent.getStringExtra("reLogin"))) {
            if (isLogin) {
                isLogin = false;
                startActivity(new Intent(MainActivity.this, LoginActivity.class).putExtra("isReLogin", "isReLogin"));
                finish();
            }
        }
    }

    /**
     * 退出程序
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {// 如果两次按键时间间隔大于800毫秒，则不退出
                Toast.makeText(MainActivity.this, "再按一次可退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;// 更新firstTime
                return true;
            } else { // System.exit(0);// 否则退出程序
                System.exit(0);
                //finish();
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // 标题头部分
            case R.id.iv_home_header: {
                mDragLayout.open();
                hideSoftKeyboard();
                break;
            }
            case R.id.tv_home_more: {       //语音操作
                voiceOperation();
                break;
            }
            case R.id.iv_leftmenu_header: {// 左侧菜单头像部分
                Intent intent = new Intent(this, UserInfoActivity.class) ;
                startActivityForResult(intent, 1);
                break;
            }
        }
    }

    /**
     * 语音输入 UI
     */
    int ret = 0; // 函数调用返回值
    private void voiceOperation(){
        mIatResults.clear();
        // 设置参数
        setParam();
        mIatDialog.setListener(mRecognizerDialogListener);
        mIatDialog.show();
        showTip("请发出指令。。。");
        /*if (isShowDialog) {
            // 显示听写对话框
            mIatDialog.setListener(mRecognizerDialogListener);
            mIatDialog.show();
            showTip("请发出指令。。。");
            isShowDialog=false;
        } else {
            isShowDialog=true;
            // 不显示听写对话框，开始听写
            ret = mIat.startListening(mRecognizerListener);
            if (ret != ErrorCode.SUCCESS) {
                showTip("听写失败,错误码：" + ret);
            } else {
                showTip("请发出指令。。。");
            }
        }*/
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(Tag, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    /**
     * 听写监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));
        }

    };

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        Log.d(Tag, resultBuffer.toString());
        //showTip(resultBuffer.toString());
        SpeechOperateUtils.operate(this,resultBuffer.toString());
    }

    /**
     * 参数设置
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS,"30000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS,"1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    /**
     * 听写监听器。
     */
    /*private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(Tag, results.getResultString());
            printResult(results);

            if (isLast) {
                // TODO 最后的结果
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
            Log.d(Tag, "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) { // 用户infoactivity返回
            if (resultCode == RESULT_OK) { // 用户注销
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                initShowData();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isStop = false;
    private ViewPager mViewPager;
    private List<ImageInfo> list;
    private List<ImageView> imageViewList;
    private MyPagerAdapter mAdapter;
    private LinearLayout llPointGroup;
    private int previousPosition = 0;

    private EditText numSearch;// 搜索功能
    private String keyword;// 输入内容
    private View titleView;
    private static final int ID_MSG = 1;
    private static final int ID_SAO = 2;
    //    QuickAction quickAction;
    private RelativeLayout deiverSend;

    /**
     * 切换广告图片
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

        mViewPager = (ViewPager) findViewById(R.id.viewpager_homepage);
        llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);
        if (!NetWorkUtil.isNetworkConnected(MainActivity.this)) {
            ToastUtil.showLong(getApplicationContext(), "网络请求失败，请检查您的网络设置");

            list = SharedPreferencesUtil.getRollImage(MainActivity.this);
            if (list.size() > 0) {
                initViewHomePage();
            } else {
                mViewPager.setBackgroundResource(R.drawable.pic_link);
            }
        } else {
            mViewPager.setBackgroundResource(R.drawable.pic_loading);
//            加载广告图片
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
                        Log.d("resStr",list.get(0).getUrl());
                        SharedPreferencesUtil.saveRollImage(
                                getApplicationContext(), list);
                        initViewHomePage();
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
     */
    private void initViewHomePage() {
        imageViewList = new ArrayList<ImageView>();
        ImageView iv;
        View pointView;
        DrawerLayout.LayoutParams params;

        llPointGroup.removeAllViews();

        ImageUtil tool = new ImageUtil();
        for (int i = 0; i < list.size(); i++) {
            iv = new ImageView(MainActivity.this);
            imageViewList.add(iv);
            tool.getImageBitmap(iv, list.get(i).getUrl(),
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
            pointView = new View(MainActivity.this);
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
