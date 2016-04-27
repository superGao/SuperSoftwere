package com.supergao.softwere.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 公共实现适配器抽象类
 *@author superGao
 *creat at 2016/3/29
 */
public abstract class IBaseAdapter<T> extends BaseAdapter {

    protected Context context;
    protected LayoutInflater inflater;
    protected List<T> itemList = new ArrayList<T>();

    public IBaseAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 判断数据是否为空
     *
     * @return 为空返回true，不为空返回false
     */
    public boolean isEmpty() {
        return itemList.isEmpty();
    }

    /**
     * 在原有的数据上添加新数据
     *
     * @param itemList
     */
    public void addItems(List<T> itemList) {
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    /**
     * 设置为新的数据，旧数据会被清空
     *
     * @param itemList
     */
    public void setItems(List<T> itemList) {
        this.itemList.clear();
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    public void clearItems() {
        itemList.clear();
        notifyDataSetChanged();
    }

    /**
     * 获取所有的条目
     */
    public List<T> getItems() {
        return itemList ;
    }

    /**
     * 移除指定位置的 item
     * @param position
     */
    public void removeItem(int position) {
        if (position >= 0 && position < itemList.size()) {
            itemList.remove(position) ;
            notifyDataSetChanged();
        }
    }

    /**
     * 移除 item
     * @param t
     */
    public void removeItem(T t) {
        if (null != t && itemList.contains(t)) {
            itemList.remove(t) ;
            notifyDataSetChanged();
        }
    }

    /**
     * 移除 一组 items
     * @param items 要移除的items
     */
    public void removeItems(List<T> items) {
        if (null != items && items.size() > 0) {
            itemList.removeAll(items);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    abstract public View getView(int i, View view, ViewGroup viewGroup);
}