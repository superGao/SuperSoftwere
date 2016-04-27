package com.supergao.softwere.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avoscloud.leanchatlib.utils.PhotoUtils;
import com.avoscloud.leanchatlib.viewholder.CommonViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.supergao.softwere.R;
import com.supergao.softwere.entity.UserInfo;
import com.supergao.softwere.event.NewFriendItemClickEvent;
import com.supergao.softwere.friends.AddRequest;

import de.greenrobot.event.EventBus;

/**
 *好友请求viewholder
 *@author superGao
 *creat at 2016/4/8
 */
public class NewFriendItemHolder extends CommonViewHolder<AddRequest> {

  TextView nameView;
  ImageView avatarView;
  Button addBtn;
  View agreedView;
  private AddRequest addRequest;

  public NewFriendItemHolder(Context context, ViewGroup root) {
    super(context, root, R.layout.contact_add_friend_item);

    nameView = (TextView)itemView.findViewById(R.id.name);
    avatarView = (ImageView)itemView.findViewById(R.id.avatar);
    addBtn = (Button)itemView.findViewById(R.id.add);
    agreedView = itemView.findViewById(R.id.agreedView);

    itemView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        EventBus.getDefault().post(new NewFriendItemClickEvent(addRequest, true));
        return true;
      }
    });

    addBtn.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View arg0) {
        EventBus.getDefault().post(new NewFriendItemClickEvent(addRequest, false));
      }
    });
  }

  @Override
  public void bindData(final AddRequest addRequest) {
    this.addRequest = addRequest;
    UserInfo from = addRequest.getFromUser();
    ImageLoader.getInstance().displayImage(from.getAvatarUrl(), avatarView, PhotoUtils.avatarImageOptions);
    if (from != null) {
      nameView.setText(from.getUsername());
    }
    int status = addRequest.getStatus();
    if (status == AddRequest.STATUS_WAIT) {
      addBtn.setVisibility(View.VISIBLE);
      agreedView.setVisibility(View.GONE);
    } else if (status == AddRequest.STATUS_DONE) {
      addBtn.setVisibility(View.GONE);
      agreedView.setVisibility(View.VISIBLE);
    }
  }

  public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<NewFriendItemHolder>() {
    @Override
    public NewFriendItemHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
      return new NewFriendItemHolder(parent.getContext(), parent);
    }
  };
}
