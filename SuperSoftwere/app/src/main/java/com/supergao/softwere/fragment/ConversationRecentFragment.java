package com.supergao.softwere.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMSingleMessageQueryCallback;
import com.avoscloud.leanchatlib.event.ConnectionChangeEvent;
import com.avoscloud.leanchatlib.utils.ConversationManager;
import com.avoscloud.leanchatlib.event.ConversationItemClickEvent;
import com.avoscloud.leanchatlib.controller.ConversationHelper;
import com.avoscloud.leanchatlib.event.ImTypeMessageEvent;
import com.avoscloud.leanchatlib.model.ConversationType;
import com.avoscloud.leanchatlib.model.Room;
import com.avoscloud.leanchatlib.utils.Constants;
import com.supergao.softwere.R;
import com.supergao.softwere.activity.ChatRoomActivity;
import com.supergao.softwere.activity.MainActivity;
import com.supergao.softwere.adapter.ConversationListAdapter;
import com.supergao.softwere.entity.AppConfig;
import com.supergao.softwere.entity.UserInfo;
import com.supergao.softwere.popup.CustomProgressDialog;
import com.supergao.softwere.utils.UserCacheUtils;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *会话fragment
 *@author superGao
 *creat at 2016/4/14
 */
public class ConversationRecentFragment extends BaseFragment {

  @Bind(R.id.im_client_state_view)
  View imClientStateView;

  @Bind(R.id.fragment_conversation_srl_pullrefresh)
  protected SwipeRefreshLayout refreshLayout;

  @Bind(R.id.fragment_conversation_srl_view)
  protected RecyclerView recyclerView;

  protected ConversationListAdapter<Room> itemAdapter;
  protected LinearLayoutManager layoutManager;

  private boolean hidden;
  private ConversationManager conversationManager;
  private CustomProgressDialog customProgressDialog;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.message_fragment, container, false);
    ButterKnife.bind(this, view);
    conversationManager = ConversationManager.getInstance();
    layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);
    itemAdapter = new ConversationListAdapter<Room>();
    recyclerView.setAdapter(itemAdapter);
    EventBus.getDefault().register(this);
    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        updateConversationList();
      }
    });
    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    headerLayout.showTitle(R.string.conversation_messages);
    customProgressDialog=new CustomProgressDialog(getActivity(),"看我一步两步，似魔鬼的步伐。。。",R.drawable.frame_haha);
    updateConversationList();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    EventBus.getDefault().unregister(this);
  }

  @Override
  public void onHiddenChanged(boolean hidden) {
    super.onHiddenChanged(hidden);
    this.hidden = hidden;
    if (!hidden) {
      updateConversationList();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if (!hidden) {
      updateConversationList();
    }
    showAvatar();
  }
  private void showAvatar(){
    headerLayout.showLeftImageByUrl(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MainActivity.mDragLayout.open();//侧滑
      }
    });
  }
  public void onEvent(ConversationItemClickEvent event) {
    Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
    intent.putExtra(Constants.CONVERSATION_ID, event.conversationId);
    startActivity(intent);
  }

  public void onEvent(ImTypeMessageEvent event) {
    updateConversationList();
  }

  /**
   * 加载聊天列表
   */
  private void updateConversationList() {
    customProgressDialog.show();
    conversationManager.findAndCacheRooms(new Room.MultiRoomsCallback() {
      @Override
      public void done(List<Room> roomList, AVException exception) {
        customProgressDialog.dismiss();
        if (filterException(exception)) {
          refreshLayout.setRefreshing(false);
          updateLastMessage(roomList);
          cacheRelatedUsers(roomList);

          List<Room> sortedRooms = sortRooms(roomList);
          itemAdapter.setDataList(sortedRooms);
          itemAdapter.notifyDataSetChanged();
        }
      }
    });
  }

  private void updateLastMessage(final List<Room> roomList) {
    customProgressDialog.show();
    for (final Room room : roomList) {
      AVIMConversation conversation = room.getConversation();
      if (null != conversation) {
        conversation.getLastMessage(new AVIMSingleMessageQueryCallback() {
          @Override
          public void done(AVIMMessage avimMessage, AVIMException e) {
            customProgressDialog.dismiss();
            if (filterException(e) && null != avimMessage) {
              room.setLastMessage(avimMessage);
              int index = roomList.indexOf(room);
              itemAdapter.notifyItemChanged(index);
            }
          }
        });
      }
    }
  }

  private void cacheRelatedUsers(List<Room> rooms) {
    List<String> needCacheUsers = new ArrayList<String>();
    for(Room room : rooms) {
      AVIMConversation conversation = room.getConversation();
      if (ConversationHelper.typeOfConversation(conversation) == ConversationType.Single) {
        needCacheUsers.add(ConversationHelper.otherIdOfConversation(conversation));
      }
    }
    UserCacheUtils.fetchUsers(needCacheUsers, new UserCacheUtils.CacheUserCallback() {
      @Override
      public void done(List<UserInfo> userList, Exception e) {
        itemAdapter.notifyDataSetChanged();
      }
    });
  }

  private List<Room> sortRooms(final List<Room> roomList) {
    List<Room> sortedList = new ArrayList<Room>();
    if (null != roomList) {
      sortedList.addAll(roomList);
      Collections.sort(sortedList, new Comparator<Room>() {
        @Override
        public int compare(Room lhs, Room rhs) {
          long value = lhs.getLastModifyTime() - rhs.getLastModifyTime();
          if (value > 0) {
            return -1;
          } else if (value < 0) {
            return 1;
          } else {
            return 0;
          }
        }
      });
    }
    return sortedList;
  }

  public void onEvent(ConnectionChangeEvent event) {
    imClientStateView.setVisibility(event.isConnect ? View.GONE : View.VISIBLE);
  }
}
