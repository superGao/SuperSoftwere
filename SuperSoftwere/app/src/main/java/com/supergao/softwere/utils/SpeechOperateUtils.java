package com.supergao.softwere.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.supergao.softwere.activity.SettingActivity;
import com.supergao.softwere.activity.user.UserInfoActivity;

import com.supergao.softwere.utils.ToastUtil;

/**
 * author：superGao on 2016/3/16.
 * note：借用请注明来源，侵权必究！
 */
public class SpeechOperateUtils {
    // 语音合成对象
    private static SpeechSynthesizer mTts;

    // 默认发音人
    private static String voicer = "xiaoyan";

    /*<item>xiaoyan</item>
    <item>xiaoyu</item>
    <item>catherine</item>
    <item>henry</item>
    <item>vimary</item>
    <item>vixy</item>
    <item>xiaoqi</item>
    <item>vixf</item>
    <item>xiaomei</item>
    <item>xiaolin</item>
    <item>xiaorong</item>
    <item>xiaoqian</item>
    <item>xiaokun</item>
    <item>xiaoqiang</item>
    <item>vixying</item>
    <item>xiaoxin</item>
    <item>nannan</item>
    <item>vils</item>*/

    private static Toast mToast;


    public static void operate(Context context,String order){
        if(order.contains("你好")){
            answer(context,order);
        }else if(order.contains("信息")){
            order="正在跳转到个人信息页面";
            if(answer(context,order)) {
                context.startActivity(new Intent(context, UserInfoActivity.class));
            }
        }else if(order.contains("设置")){
            order="正在跳转到设置页面";
            if(answer(context,order)) {
                context.startActivity(new Intent(context, SettingActivity.class));
            }
        }else if(order.contains("傻")||order.contains("笨")){
            order="你才是个大傻逼";
            answer(context,order);
        }else if(order.contains("吃")||order.contains("饭")){
            order="你是猪吗，天天想着吃";
            answer(context,order);
        }else if(order.contains("玩")||order.contains("耍")){
            order="整天不学好，就知道玩";
            answer(context,order);
        }else if(order.contains("挖掘机")||order.contains("强")){
            order="中国山东找蓝翔";
            answer(context,order);
        }else{
            ToastUtil.showShort(context, "没有此功能");
            order="你彪啊，没有这个功能";
            answer(context,order);
        }
    }

    private static boolean answer(Context context, String answer){
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        // 设置参数
        setParam();
        int code = mTts.startSpeaking(answer, mTtsListener);
//			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
//			String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
//			int code = mTts.synthesizeToUri(text, path, mTtsListener);

        if (code != ErrorCode.SUCCESS) {
            if(code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED){
                //未安装则跳转到提示安装页面
                //mInstaller.install();
                showTip("未安装语记,错误码: " + code);
            }else {
                showTip("语音合成失败,错误码: " + code);
            }
            return false;
        }else{
            return true;
        }
    }

    /**
     * 初始化监听。
     */
    private static InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码："+code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    private static void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    /**
     * 合成回调监听。
     */
    private static SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {

        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {

        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            //合成进度
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                //showTip("播放完成");
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
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
    };

    /**
     * 参数设置
     */
    private static void setParam(){
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置在线合成发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED,"50");
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH,"50");
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME,"50");
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE,"3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
    }
}
