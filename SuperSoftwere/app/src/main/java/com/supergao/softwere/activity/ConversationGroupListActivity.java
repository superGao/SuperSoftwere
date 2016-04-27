package com.supergao.softwere.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.Bind;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avoscloud.leanchatlib.utils.ConversationManager;
import com.avoscloud.leanchatlib.activity.AVBaseActivity;
import com.avoscloud.leanchatlib.adapter.CommonListAdapter;
import com.avoscloud.leanchatlib.utils.Constants;
import com.supergao.softwere.R;
import com.supergao.softwere.entity.App;
import com.supergao.softwere.event.GroupItemClickEvent;
import com.supergao.softwere.viewholder.GroupItemHolder;

import java.util.List;

/**
 *聊天群组列表
 *@author superGao
 *creat at 2016/4/8
 */
public class ConversationGroupListActivity extends AVBaseActivity {

  @Bind(R.id.activity_group_list_srl_view)
  protected RecyclerView recyclerView;
  @Bind(R.id.activity_group_list_srl_pullrefresh)
  protected SwipeRefreshLayout refreshLayout;
  LinearLayoutManager layoutManager;
  private CommonListAdapter<AVIMConversation> itemAdapter;

  private ConversationManager conversationManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.group_list_activity);
    initView();
    conversationManager = ConversationManager.getInstance();
    setTitle(App.ctx.getString(R.string.conversation_group));
    refreshGroupList();

    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        refreshGroupList();
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    refreshGroupList();
  }

  private void initView() {
    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);
    itemAdapter = new CommonListAdapter<>(GroupItemHolder.class);
    recyclerView.setAdapter(itemAdapter);
  }

  private void refreshGroupList() {
    conversationManager.findGroupConversationsIncludeMe(new AVIMConversationQueryCallback() {
      @Override
      public void done(List<AVIMConversation> conversations, AVIMException e) {
        if (filterException(e)) {
          refreshLayout.setRefreshing(false);
          itemAdapter.setDataList(conversations);
          itemAdapter.notifyDataSetChanged();
        }
      }
    });
  }

  public void onEvent(GroupItemClickEvent event) {
    Intent intent = new Intent(ConversationGroupListActivity.this, ChatRoomActivity.class);
    intent.putExtra(Constants.CONVERSATION_ID, event.conversationId);
    startActivity(intent);
  }
}
