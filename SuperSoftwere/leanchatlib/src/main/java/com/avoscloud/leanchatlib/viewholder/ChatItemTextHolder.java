package com.avoscloud.leanchatlib.viewholder;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avoscloud.leanchatlib.R;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.controller.EmotionHelper;
import com.avoscloud.leanchatlib.utils.TextManager;

import java.util.zip.Inflater;

/**
 *聊天text item viewHolder
 *@author superGao
 *creat at 2016/4/18
 */
public class ChatItemTextHolder extends ChatItemHolder {

  protected TextView contentView;
  private PopupWindow popupWindow;
  private TextView copyTv;
  private LayoutInflater inflater = null;
  private Context context;
  private String text;
  public ChatItemTextHolder(Context context, ViewGroup root, boolean isLeft) {
    super(context, root, isLeft);
    this.context=context;
    inflater = LayoutInflater.from(context);
    initPopupWindow(inflater);
  }

  @Override
  public void initView() {
    super.initView();
    if (isLeft) {
      conventLayout.addView(View.inflate(getContext(), R.layout.chat_item_left_text_layout, null));
      contentView = (TextView) itemView.findViewById(R.id.chat_left_text_tv_content);
    } else {
      conventLayout.addView(View.inflate(getContext(), R.layout.chat_item_right_text_layout, null));
      contentView = (TextView) itemView.findViewById(R.id.chat_right_text_tv_content);
    }
    contentView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        //弹框提示是否复制文字
        text=contentView.getText().toString().trim();
        showPop(v);
        copyTv.setOnTouchListener(new tvOnTouch(context));
        return false;
      }
    });
  }

  @Override
  public void bindData(Object o) {
    super.bindData(o);
    AVIMMessage message = (AVIMMessage)o;
    if (message instanceof AVIMTextMessage) {
      AVIMTextMessage textMessage = (AVIMTextMessage) message;
      contentView.setText(EmotionHelper.replace(getContext(), textMessage.getText()));
    }
  }

  /**
   * 初始化Popupwindow
   *
   * @param inflater
   */
  private void initPopupWindow(LayoutInflater inflater) {
    View view = inflater.inflate(R.layout.pop_item_layout, null);
    popupWindow = new PopupWindow(view,WindowManager.LayoutParams.WRAP_CONTENT , WindowManager.LayoutParams.WRAP_CONTENT);
    copyTv = (TextView) view.findViewById(R.id.pop_copy_tv);
  }

  /**
   * Popupwindow显示
   *
   * @param v
   */
  @SuppressWarnings("deprecation")
  private void showPop(View v) {
    popupWindow.setFocusable(false);
    popupWindow.setOutsideTouchable(true);// 设置此项可点击Popupwindow外区域消失，注释则不消失
    popupWindow.setBackgroundDrawable(new BitmapDrawable());

    // 设置出现位置
    int[] location = new int[2];
    v.getLocationOnScreen(location);
    popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
            location[0] + v.getWidth() / 2 - 20,
            location[1] - 80);
  }

  /**
   * 触摸事件
   *
   * @author zihao
   *
   */
  class tvOnTouch implements View.OnTouchListener {
    private Context mContext;

    public tvOnTouch(Context context) {
      // TODO Auto-generated method stub
      this.mContext = context;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
      // TODO Auto-generated method stub
      if (v.getId() == R.id.pop_copy_tv) {
        TextView tv = (TextView) v;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {// 按下
          tv.setTextColor(0xff00CD66);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {// 松开
          tv.setTextColor(0xffffffff);
          TextManager.copyText(mContext,text);
          Toast.makeText(mContext, "复制成功", Toast.LENGTH_SHORT).show();
          if (popupWindow != null) {
            popupWindow.dismiss();
          }
        }
      }
      return true;
    }

  }

}
