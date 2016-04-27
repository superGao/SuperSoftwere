package com.supergao.softwere.friends;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import butterknife.Bind;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.activity.AVBaseActivity;
import com.avoscloud.leanchatlib.adapter.HeaderListAdapter;
import com.avoscloud.leanchatlib.model.ConversationType;
import com.avoscloud.leanchatlib.view.RefreshableRecyclerView;
import com.supergao.softwere.R;
import com.supergao.softwere.entity.UserInfo;
import com.supergao.softwere.event.ContactRefreshEvent;
import com.supergao.softwere.event.NewFriendItemClickEvent;
import com.supergao.softwere.service.PreferenceMap;
import com.supergao.softwere.viewholder.NewFriendItemHolder;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *好友请求
 *@author superGao
 *creat at 2016/4/8
 */
public class ContactNewFriendActivity extends AVBaseActivity {

  @Bind(R.id.newfriendList)
  RefreshableRecyclerView recyclerView;

  LinearLayoutManager layoutManager;

  private HeaderListAdapter<AddRequest> adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.contact_new_friend_activity);
    initView();
    loadMoreAddRequest(true);
  }

  private void initView() {
    setTitle(R.string.contact_new_friends);
    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);
    adapter = new HeaderListAdapter<>(NewFriendItemHolder.class);
    recyclerView.setOnLoadDataListener(new RefreshableRecyclerView.OnLoadDataListener() {
      @Override
      public void onLoad(int skip, int limit, boolean isRefresh) {
        loadMoreAddRequest(false);
      }
    });
    recyclerView.setAdapter(adapter);
  }

    private void loadMoreAddRequest(final boolean isRefresh) {
      AddRequestManager.getInstance().findAddRequests(isRefresh ? 0 : adapter.getDataList().size(), 20, new FindCallback<AddRequest>() {
        @Override
        public void done(List<AddRequest> list, AVException e) {
          AddRequestManager.getInstance().markAddRequestsRead(list);
          final List<AddRequest> filters = new ArrayList<AddRequest>();
          for (AddRequest addRequest : list) {
            if (addRequest.getFromUser() != null) {
              filters.add(addRequest);
            }
          }
          PreferenceMap preferenceMap = new PreferenceMap(ContactNewFriendActivity.this, UserInfo.getCurrentUserId());
          preferenceMap.setAddRequestN(filters.size());
          recyclerView.setLoadComplete(list.toArray(), isRefresh);
        }
      });
    }

  public void onEvent(NewFriendItemClickEvent event) {
    if (event.isLongClick) {
      deleteAddRequest(event.addRequest);
    } else {
      agreeAddRequest(event.addRequest);
    }
  }

  private void agreeAddRequest(final AddRequest addRequest) {
    final ProgressDialog dialog = showSpinnerDialog();
    AddRequestManager.getInstance().agreeAddRequest(addRequest, new SaveCallback() {
      @Override
      public void done(AVException e) {
        dialog.dismiss();
        if (filterException(e)) {
          if (addRequest.getFromUser() != null) {
            sendWelcomeMessage(addRequest.getFromUser().getObjectId());
          }
          loadMoreAddRequest(false);
          ContactRefreshEvent event = new ContactRefreshEvent();
          EventBus.getDefault().post(event);
        }
      }
    });
  }

  public void sendWelcomeMessage(String toUserId) {
    Map<String, Object> attrs = new HashMap<>();
    attrs.put(ConversationType.TYPE_KEY, ConversationType.Single.getValue());
    ChatManager.getInstance().getImClient().createConversation(Arrays.asList(toUserId), "", attrs, false, true, new AVIMConversationCreatedCallback() {
      @Override
      public void done(AVIMConversation avimConversation, AVIMException e) {
        if (e == null) {
          AVIMTextMessage message = new AVIMTextMessage();
          message.setText(getString(R.string.message_when_agree_request));
          avimConversation.sendMessage(message, null);
        }
      }
    });
  }

  private void deleteAddRequest(final AddRequest addRequest) {
    new AlertDialog.Builder(this).setMessage(R.string.contact_deleteFriendRequest)
      .setPositiveButton(R.string.common_sure, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          addRequest.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(AVException e) {
              loadMoreAddRequest(true);
            }
          });
        }
      }).setNegativeButton(R.string.chat_common_cancel, null).show();
  }
}
