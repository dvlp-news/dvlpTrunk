/**
 * 
 */
package com.qianyilc.library;

import java.util.ArrayList;

import android.content.Context;
import android.widget.BaseAdapter;

/*
 * @author 刘伟 E-mail: liuwei1@leju.com      
 *               
 * @version 创建时间：2013年9月4日 下午2:22:17 
 *
 * 泛型适配器
 */

public abstract class LibAdapter<M> extends BaseAdapter {
	public ArrayList<M> list = new ArrayList<M>();
	protected Context mContext=null;
    
	/**
	 * @param mContext
	 */
	public LibAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public M getItem(int position) {
        
		try {
			return list.get(position);
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void removeItem(M m){
		list.remove(m);
		notifyDataSetChanged();
		
	}
	public void removeItems(ArrayList<M> childList){
		
		list.removeAll(childList);
		
		notifyDataSetChanged();
		
	}
	public void addAndUpdate(ArrayList<M> list1) {
		if (list1!=null&&list1.size()>0) {
			list.addAll(list1);
			notifyDataSetChanged();

		}
		
	}
	public void addToTopAndUpdate(ArrayList<M> list1) {
		list.addAll(0,list1);
		
		notifyDataSetChanged();
		
	}

	public void clear() {
		list.clear();
		notifyDataSetChanged();

	}

	public void update(ArrayList<M> list) {
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();

	}

	

}
