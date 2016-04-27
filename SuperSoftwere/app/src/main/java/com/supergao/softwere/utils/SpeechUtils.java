package com.supergao.softwere.utils;

import android.content.Context;
import android.os.Environment;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 语音操作工具类
 * author：superGao on 2016/4/6.
 * note：借用请注明来源，侵权必究！
 */
public class SpeechUtils {
    private static String Tag="iflytek";
    // 语音听写对象
    private static SpeechRecognizer mIat;
    // 语音听写UI
    private static RecognizerDialog mIatDialog;
    // 引擎类型
    private static String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 用HashMap存储听写结果
    private static HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();


    public static void initSpeech(final Context context){
        mIat = SpeechRecognizer.createRecognizer(context, new InitListener() {

            @Override
            public void onInit(int code) {
                Log.d(Tag, "SpeechRecognizer init() code = " + code);
                if (code != ErrorCode.SUCCESS) {
                    showTip(context,"初始化失败，错误码：" + code);
                }
            }
        });
        mIatDialog = new RecognizerDialog(context,  new InitListener() {

            @Override
            public void onInit(int code) {
                Log.d(Tag, "SpeechRecognizer init() code = " + code);
                if (code != ErrorCode.SUCCESS) {
                    showTip(context,"初始化失败，错误码：" + code);
                }
            }
        });
    }


    private static void showTip(Context context,final String str) {
        ToastUtil.showShort(context,str);
    }

    /**
     * 语音输入 UI
     */
    int ret = 0; // 函数调用返回值
    public static void voiceOperation(final Context context){
        mIatResults.clear();
        // 设置参数
        setParam();
        mIatDialog.setListener(new RecognizerDialogListener() {
            public void onResult(RecognizerResult results, boolean isLast) {
                printResult(context,results);
            }

            /**
             * 识别回调错误.
             */
            public void onError(SpeechError error) {
                showTip(context,error.getPlainDescription(true));
            }

        });
        mIatDialog.show();
        showTip(context,"请发出指令。。。");
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
     * 语音听写参数设置
     */
    public static void setParam() {
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
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }


    private static void printResult(Context context, RecognizerResult results) {
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
        SpeechOperateUtils.operate(context, resultBuffer.toString());
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
}
