package com.dvlp.news.ui.platform.activity.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;


import com.dvlp.news.R;
import com.dvlp.news.ui.platform.activity.impl.TitleBarStyle;
import com.dvlp.news.ui.platform.fragment.BaseFragment;
import com.dvlp.news.ui.views.CustomeViewPager;

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
public abstract class QianYiTabMenuActivity extends BaseFragmentActivity implements OnCheckedChangeListener {

    private OnMenuItemSelectedChangeListener onMenuItemSelectedChangeListener;
    @SuppressLint("UseSparseArrays")
    protected LinkedHashMap<Integer, Element> menuMap = new LinkedHashMap<Integer, Element>();

    public RadioGroup menuGroup;

    protected CustomeViewPager fragmentContener;
    protected TabPagerAdapter mAdapter;
    protected int currentSelectedIndex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        addView(R.layout.layout_tabmenu);
        menuGroup = (RadioGroup) findViewById(R.id.menuGroup);

        fragmentContener = (CustomeViewPager) findViewById(R.id.fragment_contener);

        fragmentContener.setOffscreenPageLimit(3);

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

    public void selectedById(int radioId) {
        menuGroup.check(radioId);
    }

    public void selectedByTag(String tag) {
        int size = menuMap.size();
        for (int i = 0; i < size; i++) {
            Element element = menuMap.get(i);
            if (element.tag.equals(tag)) {
                menuGroup.check(i);
                return;
            }
        }
    }

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

            RadioButton radioButton = createRadioButton(id, element);
            fixRadioButton(radioButton);

            menuGroup.addView(radioButton);
        }

        setOnCheckedChangeListener();

        mAdapter.setFragments(fragments);
        mAdapter.setTags(tags);
        fragmentContener.setAdapter(mAdapter);

    }

    protected void setOnCheckedChangeListener() {
        menuGroup.setOnCheckedChangeListener(this);
    }

    protected void removeCheckedChangeListener() {
        menuGroup.setOnCheckedChangeListener(null);
    }

    /**
     * 必要时要在子类中修改此方法，调整一下细节，如padding，margin之类的属性
     *
     * @param radioButton
     */
    protected void fixRadioButton(RadioButton radioButton) {

    }

    /**
     * 创建底部菜单的radiobutton，必要时需要重写此方法
     *
     * @param id
     * @param element
     * @return
     */
    public RadioButton createRadioButton(int id, Element element) {
        RadioButton radioButton = (RadioButton) View.inflate(getApplicationContext(), R.layout.item_radiobutton, null);
        radioButton.setId(id);
        radioButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(element.drawableRes), null, null);

        if (!TextUtils.isEmpty(element.title)) {
            radioButton.setText(element.title);
        }

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);

        radioButton.setLayoutParams(layoutParams);
        radioButton.setPadding(0, 0, 0, 0);

        return radioButton;

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        fragmentContener.setCurrentItem(checkedId, false);
        currentSelectedIndex=checkedId;

        if (onMenuItemSelectedChangeListener != null) {
            onMenuItemSelectedChangeListener.onSelectChanged(checkedId);
        }
    }

    /**
     * 底部菜单选择改变事件的监听接口
     *
     * @author leju
     */
    public interface OnMenuItemSelectedChangeListener {
        void onSelectChanged(int itemId);
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

    /**
     * 设置底部菜单选中的监听接口
     */
    public void setOnMenuItemSelectedChangeListener(OnMenuItemSelectedChangeListener onMenuItemSelectedChangeListener) {
        this.onMenuItemSelectedChangeListener = onMenuItemSelectedChangeListener;
    }

    /**
     * Fragment滑动到最顶部监听
     */
    public interface OnGoTopListener {
        void onGoTop();
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
