package com.supergao.softwere.adapter;

import android.view.ViewGroup;

import com.avoscloud.leanchatlib.adapter.CommonListAdapter;
import com.avoscloud.leanchatlib.viewholder.CommonViewHolder;
import com.supergao.softwere.entity.UserInfo;
import com.supergao.softwere.viewholder.MemeberCheckableItemHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 *@author superGao
 *creat at 2016/4/8
 */
public class MemeberAddAdapter extends CommonListAdapter<UserInfo> {
  private Map<Integer, Boolean> checkStatusMap = new HashMap<>();

  public MemeberAddAdapter() {
    super(MemeberCheckableItemHolder.class);
  }

  @Override
  public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final MemeberCheckableItemHolder itemHolder = (MemeberCheckableItemHolder) super.onCreateViewHolder(parent, viewType);
    itemHolder.setOnCheckedChangeListener(new MemeberCheckableItemHolder.OnItemHolderCheckedChangeListener() {
      @Override
      public void onCheckedChanged(boolean isChecked) {
        checkStatusMap.put(itemHolder.getAdapterPosition(), isChecked);
      }
    });
    return itemHolder;
  }

  @Override
  public void onBindViewHolder(CommonViewHolder holder, int position) {
    super.onBindViewHolder(holder, position);
    ((MemeberCheckableItemHolder)holder).setChecked(checkStatusMap.containsKey(position) ? checkStatusMap.get(position) : false);
  }

  public List<String> getCheckedIds() {
    List<String> idList = new ArrayList<>();
    Set<Integer> keySet = checkStatusMap.keySet();
    for (Integer integer : keySet) {
      if (checkStatusMap.get(integer)) {
        idList.add(getDataList().get(integer).getObjectId());
      }
    }
    return idList;
  }
}
