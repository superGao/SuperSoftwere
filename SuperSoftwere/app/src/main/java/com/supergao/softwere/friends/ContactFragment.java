package com.supergao.softwere.friends;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.avoscloud.leanchatlib.event.MemberLetterEvent;
import com.avoscloud.leanchatlib.utils.Constants;
import com.avoscloud.leanchatlib.view.ClearEditText;
import com.supergao.softwere.R;
import com.supergao.softwere.activity.ChatRoomActivity;
import com.supergao.softwere.activity.ConversationGroupListActivity;
import com.supergao.softwere.activity.MainActivity;
import com.supergao.softwere.adapter.ContactsAdapter;
import com.supergao.softwere.entity.App;
import com.supergao.softwere.entity.AppConfig;
import com.supergao.softwere.entity.UserInfo;
import com.supergao.softwere.event.ContactItemClickEvent;
import com.supergao.softwere.event.ContactItemLongClickEvent;
import com.supergao.softwere.event.ContactRefreshEvent;
import com.supergao.softwere.event.InvitationEvent;
import com.supergao.softwere.fragment.BaseFragment;
import com.supergao.softwere.popup.CustomProgressDialog;
import com.supergao.softwere.utils.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


/**
 * 联系人列表
 * author：superGao on 2016/3/29.
 */
public class ContactFragment extends BaseFragment {

    @Bind(R.id.activity_square_members_srl_list)
    protected SwipeRefreshLayout refreshLayout;
    @Bind(R.id.activity_square_members_rv_list)
    protected RecyclerView recyclerView;
    @Bind(R.id.filter_edit)
    protected ClearEditText mClearEditText;
    @Bind(R.id.title_layout_no_friends)
    protected TextView tvNofriends;

    private View headerView;
    private ImageView msgTipsView;
    private ContactsAdapter itemAdapter;
    private LinearLayoutManager layoutManager;
    private Handler handler = new Handler(Looper.getMainLooper());
    private CustomProgressDialog customProgressDialog;
    private List<UserInfo> contactInfos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_add_friends, container, false);
        headerView = inflater.inflate(R.layout.contact_fragment_header_layout, container, false);
        ButterKnife.bind(this, view);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        itemAdapter = new ContactsAdapter();
        itemAdapter.setHeaderView(headerView);
        recyclerView.setAdapter(itemAdapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMembers(false);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customProgressDialog=new CustomProgressDialog(getActivity(),"不要催我，我很快了。。。",R.drawable.frame_haha);
        initHeaderView();
        initHeader();
        refresh();
        EventBus.getDefault().register(this);
        getMembers(false);
        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                hideSoftKeyboard();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateNewRequestBadge();
        showAvatar();
        getMembers(false);
    }

    private void initHeaderView() {
        msgTipsView = (ImageView)headerView.findViewById(R.id.iv_msg_tips);
        View newView = headerView.findViewById(R.id.layout_new);
        newView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//添加好友请求
                Intent intent = new Intent(ctx, ContactNewFriendActivity.class);
                ctx.startActivity(intent);
            }
        });

        View groupView = headerView.findViewById(R.id.layout_group);
        groupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//群组
                Intent intent = new Intent(ctx, ConversationGroupListActivity.class);
                ctx.startActivity(intent);
            }
        });
    }

    private void showAvatar(){
        headerLayout.showLeftImageByUrl(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mDragLayout.open();//侧滑
            }
        });
    }

    /**
     * 获取联系人列表
     * @param isforce
     */
    private void getMembers(final boolean isforce) {
        customProgressDialog.show();
        FriendsManager.fetchFriends(isforce, new FindCallback<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, AVException e) {
                customProgressDialog.dismiss();
                refreshLayout.setRefreshing(false);
                itemAdapter.setUserList(list);
                itemAdapter.notifyDataSetChanged();
                contactInfos=list;
            }
        });
    }

    private void initHeader() {
        headerLayout.showTitle(App.ctx.getString(R.string.contact));
        headerLayout.showRightImageButton(R.drawable.base_action_bar_add_bg_selector, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, ContactAddFriendActivity.class);
                ctx.startActivity(intent);
            }
        });
    }

    private void updateNewRequestBadge() {
        msgTipsView.setVisibility(
                AddRequestManager.getInstance().hasUnreadRequests() ? View.VISIBLE : View.GONE);
    }

    private void refresh() {
        AddRequestManager.getInstance().countUnreadRequests(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                updateNewRequestBadge();
            }
        });
    }
    public void showDeleteDialog(final String memberId) {
        new AlertDialog.Builder(ctx).setMessage(R.string.contact_deleteContact)
                .setPositiveButton(R.string.common_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final ProgressDialog dialog1 = showSpinnerDialog();
                        UserInfo.getCurrentUser().removeFriend(memberId, new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                dialog1.dismiss();
                                if (filterException(e)) {
                                    getMembers(true);
                                }
                            }
                        });
                    }
                }).setNegativeButton(R.string.chat_common_cancel, null).show();
    }

    /**
     * 根据输入框中的值来过滤数据并更新List
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<UserInfo> filterDateList = new ArrayList<UserInfo>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = contactInfos;
            tvNofriends.setVisibility(View.GONE);
        } else {
            filterDateList.clear();
            for (UserInfo sortModel : contactInfos) {
                String name = sortModel.getUsername();
                Log.d("filter:  ","name==="+name+"    filterStr=="+filterStr);
                if (name.contains(filterStr)) {
                    filterDateList.add(sortModel);
                }
            }
        }
        for (UserInfo sortModel : filterDateList) {
            String name = sortModel.getUsername();
            Log.d("filtered---------:  ","name==="+name+"    filterStr=="+filterStr);
        }
        itemAdapter.setUserList(filterDateList);
        itemAdapter.notifyDataSetChanged();
        if (filterDateList.size() == 0) {
            tvNofriends.setVisibility(View.VISIBLE);
        }
    }

    public void onEvent(ContactRefreshEvent event) {
        getMembers(true);
    }

    public void onEvent(InvitationEvent event) {
        AddRequestManager.getInstance().unreadRequestsIncrement();
        updateNewRequestBadge();
    }

    public void onEvent(ContactItemClickEvent event) {
        Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
        intent.putExtra(Constants.MEMBER_ID, event.memberId);
        startActivity(intent);
    }

    public void onEvent(ContactItemLongClickEvent event) {
        showDeleteDialog(event.memberId);
    }

    /**
     * 处理 LetterView 发送过来的 MemberLetterEvent
     * 会通过 MembersAdapter 获取应该要跳转到的位置，然后跳转
     */
    public void onEvent(MemberLetterEvent event) {
        Character targetChar = Character.toLowerCase(event.letter);
        if (itemAdapter.getIndexMap().containsKey(targetChar)) {
            int index = itemAdapter.getIndexMap().get(targetChar);
            if (index > 0 && index < itemAdapter.getItemCount()) {
                layoutManager.scrollToPositionWithOffset(index, 0);
            }
        }
    }
}

