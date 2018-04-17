package com.dvlp.news.ui.platform.activity.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


import com.dvlp.news.R;
import com.dvlp.news.ui.platform.activity.impl.TitleBarStyle;
import com.dvlp.news.ui.platform.entity.TabEntity;
import com.dvlp.news.ui.platform.fragment.BaseFragment;
import com.dvlp.news.ui.utils.ViewFindUtils;
import com.dvlp.news.ui.views.CustomeViewPager;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 类说明 包含底部菜单的activity
 *
 * @author 刘伟 E-mail:liuwei1@leju.com
 * @version 创建时间：2014年9月22日 下午12:14:27
 *
 * @history
 *
 */
@TitleBarStyle(show = false)
public abstract class TabMenuActivity extends BaseFragmentActivity  {


    @SuppressLint("UseSparseArrays")
    protected LinkedHashMap<Integer, Element> menuMap = new LinkedHashMap<Integer, Element>();




    protected TabPagerAdapter mAdapter;
    protected int currentSelectedIndex=0;


    //***************************************************
    protected CustomeViewPager fragmentContener;
    private View mDecorView;
    private CommonTabLayout mTabLayout_2;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        addView(R.layout.tab_activity);


        mDecorView = getWindow().getDecorView();
        fragmentContener = ViewFindUtils.find(mDecorView, R.id.fragment_contener);

        mTabLayout_2 = ViewFindUtils.find(mDecorView, R.id.tl_2);
        fragmentContener.setOffscreenPageLimit(4);

        mAdapter = new TabPagerAdapter(getSupportFragmentManager());

        initMenu();
    }
    public void initMenu(){
        try {
            initMenu(initMenuData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected abstract LinkedHashMap<Integer, Element> initMenuData();
    /**
     * 初始化页面的底部菜单选项（基于radiogroup）
     *
     * @param
     * @throws Exception
     */
    private void initMenu(LinkedHashMap<Integer, Element> menu) throws Exception {

        menuMap = menu;
        Iterator it = menuMap.keySet().iterator();

        List<BaseFragment> fragments = new ArrayList<>();
        List<String> tags = new ArrayList<>();

        while (it.hasNext()) {
            Integer id = (Integer) it.next();
            Element element = menuMap.get(id);

            fragments.add(element.fragment);
            tags.add(element.tag);

            createTab(element);
        }
        mAdapter.setFragments(fragments);
        fragmentContener.setAdapter(mAdapter);
        mAdapter.setTags(tags);
        mTabLayout_2.setTabData(mTabEntities);
        fixTabButton(mTabLayout_2);
        mTabLayout_2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                Log.e("page",position+"=======position2");
                fragmentContener.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                //重选
            }
        });
    }
    //创建Tab
    private void createTab(Element element){
        if(element.selectedIcon!=0&&element.unSelectedIcon!=0&&!element.title.isEmpty()){
            mTabEntities.add(new TabEntity(element.title, element.selectedIcon, element.unSelectedIcon));
        }else if(element.selectedIcon==0||element.unSelectedIcon==0){
            mTabEntities.add(new TabEntity(element.title, 0, 0));
        }else {
            mTabEntities.add(new TabEntity("设置title", element.selectedIcon, element.unSelectedIcon));
        }
    }
    /**
     * 必要时要在子类中修改此方法，调整一下细节，如padding，margin之类的属性
     *详细属性地址：https://github.com/H07000223/FlycoTabLayout/blob/master/README_CN.md
     * @param tabLayout
     */
    protected void fixTabButton(CommonTabLayout tabLayout) {


//       //两位数
//        tabLayout.showMsg(0, 55);
//        tabLayout.setMsgMargin(0, -5, 5);
//
//        //三位数
//        tabLayout.showMsg(1, 33);
//        tabLayout.setMsgMargin(1, -5, 5);
//
//        //设置未读消息红点
//        tabLayout.showDot(2);
//        MsgView rtv_2_2 = mTabLayout_2.getMsgView(2);
//        if (rtv_2_2 != null) {
//            UnreadMsgUtils.setSize(rtv_2_2, dp2px(7.5f));
//        }
//
//
//        //设置未读消息背景
//        tabLayout.showMsg(3, 7);
//        tabLayout.setMsgMargin(3, 0, 5);
//        MsgView rtv_2_3 = tabLayout.getMsgView(3);
//        if (rtv_2_3 != null) {
//            rtv_2_3.setBackgroundColor(Color.parseColor("#6D8FB0"));
//        }

    }

    protected int dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    //默认选中
    public void selectedById(int radioId) {
        if(radioId< mTabLayout_2.getTabCount()){
            mTabLayout_2.setCurrentTab(radioId);
            fragmentContener.setCurrentItem(radioId);
        }else {
            fragmentContener.setCurrentItem(0);
            mTabLayout_2.setCurrentTab(0);
        }
    }
    //默认选中
    public void selectedByTag(String tag) {
        int size = menuMap.size();
        for (int i = 0; i < size; i++) {
            Element element = menuMap.get(i);
            if (element.tag.equals(tag)) {
                mTabLayout_2.setCurrentTab(i);
                fragmentContener.setCurrentItem(i);
                return;
            }
        }
    }





    /**
     * 菜单的Item 数据
     */
    public class Element {
        public int drawableRes;// radiobutton需要显示的drawable资源id
        public int backgroundRes;// radiobutton 的背景图片id
        public int backgroundColor;// radiobutton 的背景颜色
        public String title; // radiobutton 的名称
        public int textcolor = -1; // radiobutton 的名称
        public BaseFragment fragment;// 点击该radiobutton需要打开的fragement的class
        public String tag;
        public int selectedIcon;//tab选中图片
        public int unSelectedIcon;//tab未选中的图片
    }



    public class TabPagerAdapter extends FragmentPagerAdapter {

        private FragmentManager mFragmentManager;
        private FragmentTransaction mCurTransaction;
        private Fragment mCurrentPrimaryItem = null;

        private List<BaseFragment> mFragments;
        private List<String> mTags;

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
            mFragments = new ArrayList<>();
            mTags = new ArrayList<>();
        }

        public void setFragments(List<BaseFragment> fragments) {
            mFragments.addAll(fragments);
            notifyDataSetChanged();
        }

        public void setTags(List<String> tags) {
            mTags.addAll(tags);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            BaseFragment baseFragment = mFragments.get(position);
            return baseFragment == null ? "" : baseFragment.getPageTitle();
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (mCurTransaction == null) {
                mCurTransaction = mFragmentManager.beginTransaction();
            }

            final long itemId = getItemId(position);

            // Do we already have this fragment?
            String name = makeFragmentName(position);
            Fragment fragment = mFragmentManager.findFragmentByTag(name);
            if (fragment != null) {
                mCurTransaction.attach(fragment);
            } else {
                fragment = getItem(position);
                mCurTransaction.add(container.getId(), fragment,
                        makeFragmentName(position));
            }
            if (fragment != mCurrentPrimaryItem) {
                fragment.setMenuVisibility(false);
                fragment.setUserVisibleHint(false);
            }

            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (mCurTransaction == null) {
                mCurTransaction = mFragmentManager.beginTransaction();
            }
            mCurTransaction.detach((Fragment)object);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            Fragment fragment = (Fragment)object;
            if (fragment != mCurrentPrimaryItem) {
                if (mCurrentPrimaryItem != null) {
                    mCurrentPrimaryItem.setMenuVisibility(false);
                    mCurrentPrimaryItem.setUserVisibleHint(false);
                }
                if (fragment != null) {
                    fragment.setMenuVisibility(true);
                    fragment.setUserVisibleHint(true);
                }
                mCurrentPrimaryItem = fragment;
            }
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            if (mCurTransaction != null) {
                mCurTransaction.commitAllowingStateLoss();
                mCurTransaction = null;
                mFragmentManager.executePendingTransactions();
            }
        }

        private String makeFragmentName(int position) {
            return mTags.get(position);
        }

    }


}
