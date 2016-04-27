package com.supergao.softwere.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avoscloud.leanchatlib.utils.Constants;
import com.avoscloud.leanchatlib.utils.PhotoUtils;
import com.avoscloud.leanchatlib.viewholder.CommonViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.supergao.softwere.R;
import com.supergao.softwere.entity.UserInfo;
import com.supergao.softwere.friends.ContactPersonInfoActivity;


/**
 *
 *@author superGao
 *creat at 2016/4/8
 */
public class SearchUserItemHolder extends CommonViewHolder<UserInfo> {

  private TextView nameView;
  private ImageView avatarView;
  private UserInfo leanchatUser;

  public SearchUserItemHolder(Context context, ViewGroup root) {
    super(context, root, R.layout.search_user_item_layout);

    nameView = (TextView)itemView.findViewById(R.id.search_user_item_tv_name);
    avatarView = (ImageView)itemView.findViewById(R.id.search_user_item_im_avatar);

    itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getContext(), ContactPersonInfoActivity.class);//进入好友个人信息页面
        intent.putExtra(Constants.LEANCHAT_USER_ID, leanchatUser.getObjectId());
        getContext().startActivity(intent);
      }
    });
  }

  @Override
  public void bindData(final UserInfo leanchatUser) {
    this.leanchatUser = leanchatUser;
    ImageLoader.getInstance().displayImage(leanchatUser.getAvatarUrl(), avatarView, PhotoUtils.avatarImageOptions);
    nameView.setText(leanchatUser.getUsername());
  }

  public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<SearchUserItemHolder>() {
    @Override
    public SearchUserItemHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
      return new SearchUserItemHolder(parent.getContext(), parent);
    }
  };
}

