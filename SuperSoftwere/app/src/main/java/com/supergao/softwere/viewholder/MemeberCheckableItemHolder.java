package com.supergao.softwere.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.avoscloud.leanchatlib.utils.PhotoUtils;
import com.avoscloud.leanchatlib.viewholder.CommonViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.supergao.softwere.R;
import com.supergao.softwere.entity.UserInfo;

/**
 *
 *@author superGao
 *creat at 2016/4/8
 */
public class MemeberCheckableItemHolder extends CommonViewHolder<UserInfo> {

  ImageView avatarView;
  TextView nameView;
  CheckBox checkBox;
  OnItemHolderCheckedChangeListener checkedChangeListener;

  public MemeberCheckableItemHolder(Context context, ViewGroup root) {
    super(context, root, R.layout.conversation_add_members_item);
    avatarView = (ImageView)itemView.findViewById(R.id.avatar);
    nameView = (TextView)itemView.findViewById(R.id.username);
    checkBox = (CheckBox)itemView.findViewById(R.id.checkbox);

    itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        checkBox.toggle();
      }
    });

    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        checkedChangeListener.onCheckedChanged(isChecked);
      }
    });
  }

  public void setOnCheckedChangeListener(OnItemHolderCheckedChangeListener listener) {
    checkedChangeListener = listener;
  }

  @Override
  public void bindData(UserInfo user) {
    if (null != user) {
      ImageLoader.getInstance().displayImage(user.getAvatarUrl(), avatarView, PhotoUtils.avatarImageOptions);
      nameView.setText(user.getUsername());
    } else {
      nameView.setText("");
    }
  }

  public void setChecked(boolean checked) {
    checkBox.setChecked(checked);
  }

  public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<MemeberCheckableItemHolder>() {
    @Override
    public MemeberCheckableItemHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
      return new MemeberCheckableItemHolder(parent.getContext(), parent);
    }
  };

  public interface OnItemHolderCheckedChangeListener {
    public void onCheckedChanged(boolean isChecked);
  }
}
