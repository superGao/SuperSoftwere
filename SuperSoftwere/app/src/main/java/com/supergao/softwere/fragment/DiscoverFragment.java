package com.supergao.softwere.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avoscloud.leanchatlib.adapter.HeaderListAdapter;
import com.avoscloud.leanchatlib.utils.Constants;
import com.avoscloud.leanchatlib.utils.LogUtils;
import com.avoscloud.leanchatlib.view.RefreshableRecyclerView;
import com.supergao.softwere.R;
import com.supergao.softwere.entity.App;
import com.supergao.softwere.entity.UserInfo;
import com.supergao.softwere.service.PreferenceMap;
import com.supergao.softwere.utils.UserCacheUtils;
import com.supergao.softwere.viewholder.DiscoverItemHolder;

import java.util.List;

/**
 *附近好友的fragment
 *@author superGao
 *creat at 2016/4/14
 */
public class DiscoverFragment extends BaseFragment {

  private final SortDialogListener distanceListener = new SortDialogListener(Constants.ORDER_DISTANCE);
  private final SortDialogListener updatedAtListener = new SortDialogListener(Constants.ORDER_UPDATED_AT);

  @Bind(R.id.fragment_near_srl_pullrefresh)
  protected SwipeRefreshLayout refreshLayout;

  @Bind(R.id.fragment_near_srl_view)
  protected RefreshableRecyclerView recyclerView;

  protected LinearLayoutManager layoutManager;

  HeaderListAdapter<UserInfo> discoverAdapter;
  int orderType;
  PreferenceMap preferenceMap;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.discover_fragment, container, false);
    ButterKnife.bind(this, view);

    layoutManager = new LinearLayoutManager(getActivity());
    discoverAdapter = new HeaderListAdapter<>(DiscoverItemHolder.class);
    recyclerView.setOnLoadDataListener(new RefreshableRecyclerView.OnLoadDataListener() {
      @Override
      public void onLoad(int skip, int limit, boolean isRefresh) {
        loadMoreDiscoverData(skip, limit, isRefresh);
      }
    });
    recyclerView.setRelationSwipeLayout(refreshLayout);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(discoverAdapter);
    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    preferenceMap = PreferenceMap.getCurUserPrefDao(getActivity());
    orderType = preferenceMap.getNearbyOrder();
    headerLayout.showTitle(R.string.discover_title);
    headerLayout.showRightImageButton(R.drawable.nearby_order, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.discover_fragment_sort).setPositiveButton(R.string.discover_fragment_loginTime,
          updatedAtListener).setNegativeButton(R.string.discover_fragment_distance, distanceListener).show();
      }
    });
    recyclerView.refreshData();
  }

  /**
   * 加载数据
   * @param skip
   * @param limit
   * @param isRefresh
   */
  private void loadMoreDiscoverData(final int skip, final int limit, final boolean isRefresh) {
    PreferenceMap preferenceMap = PreferenceMap.getCurUserPrefDao(App.ctx);
    AVGeoPoint geoPoint = preferenceMap.getLocation();
    if (geoPoint == null) {
      LogUtils.i("geo point is null");
      return;
    }
    AVQuery<UserInfo> q = UserInfo.getQuery(UserInfo.class);
    UserInfo user = UserInfo.getCurrentUser();
    q.whereNotEqualTo(Constants.OBJECT_ID, user.getObjectId());
    if (orderType == Constants.ORDER_DISTANCE) {
      q.whereNear(UserInfo.LOCATION, geoPoint);
    } else {
      q.orderByDescending(Constants.UPDATED_AT);
    }
    q.skip(skip);
    q.limit(limit);
    q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
    q.findInBackground(new FindCallback<UserInfo>() {
      @Override
      public void done(List<UserInfo> list, AVException e) {
        UserCacheUtils.cacheUsers(list);
        recyclerView.setLoadComplete(list.toArray(), isRefresh);
      }
    });
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    preferenceMap.setNearbyOrder(orderType);
  }

  public class SortDialogListener implements DialogInterface.OnClickListener {
    int orderType;

    public SortDialogListener(int orderType) {
      this.orderType = orderType;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
      DiscoverFragment.this.orderType = orderType;
      recyclerView.refreshData();
    }
  }
}
