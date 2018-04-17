package com.dvlp.news;

import android.os.Bundle;


import com.dvlp.news.ui.platform.activity.base.TabMenuActivity;
import com.dvlp.news.ui.platform.activity.impl.TitleBarStyle;
import com.dvlp.news.ui.platform.fragment.AccountFragment;
import com.dvlp.news.ui.platform.fragment.HomeFragment;
import com.dvlp.news.ui.platform.fragment.HomeFragment2;

import java.util.LinkedHashMap;

@TitleBarStyle(show = false)
public class MainActivity extends TabMenuActivity {
    /**
     * 首页
     */
    public static final int INDEX_HOME = 0;
    /**
     * 自选
     */
    public static final int INDEX_CHOICE = 1;
    /**
     * 市场（行情）
     */
    public static final int INDEX_MARKET = 2;
    /**
     * 发现（资讯）
     */
    public static final int INDEX_DISCOVER = 3;
    /**
     * 账户
     */
    public static final int INDEX_ACCOUNT = 4;

    /**
     * 首页
     */
    public static final String TAB_HOME = "sy";
    /**
     * 自选
     */
    public static final String TAB_CHOICE = "zx";
    /**
     * 市场（行情）
     */
    public static final String TAB_MARKET = "hq";
    /**
     * 直播（大咖直播和资讯）
     */
    public static final String TAB_LIVE = "zb";
    /**
     * 发现（资讯）
     */
    public static final String TAB_DISCOVER = "fx";
    /**
     * 账户
     */
    public static final String TAB_ACCOUNT = "zh";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
        }

        selectedByTag(TAB_HOME);
    }



    @Override
    protected LinkedHashMap<Integer, Element> initMenuData() {
        LinkedHashMap<Integer, Element> map = new LinkedHashMap<>();
        Element element = new Element();
        element.drawableRes = R.drawable.selector_tab_account;
        element.title = "首页";
        element.fragment = HomeFragment.newInstance();
        element.tag = TAB_HOME;
        element.selectedIcon=R.mipmap.icon_tab_account_on;
        element.unSelectedIcon=R.mipmap.icon_tab_account_off;
        map.put(0, element);


        element = new Element();
        element.drawableRes = R.drawable.selector_tab_account;
        element.title = "市场";
        element.fragment = HomeFragment2.newInstance();
        element.tag = TAB_MARKET;
        element.selectedIcon=R.mipmap.icon_tab_account_on;
        element.unSelectedIcon=R.mipmap.icon_tab_account_off;
        map.put(1, element);

        element = new Element();
        element.drawableRes = R.drawable.selector_tab_account;
        element.title = "发现";
        element.fragment = HomeFragment2.newInstance();
        element.tag = TAB_CHOICE;
        element.selectedIcon=R.mipmap.icon_tab_account_on;
        element.unSelectedIcon=R.mipmap.icon_tab_account_off;
        map.put(2, element);


        element = new Element();
        element.backgroundColor = 0xffffffff;
        element.drawableRes = R.drawable.selector_tab_account;
        element.title = "我的";
        element.fragment = AccountFragment.newInstance();
        element.tag = TAB_ACCOUNT;
        element.selectedIcon=R.mipmap.icon_tab_account_on;
        element.unSelectedIcon=R.mipmap.icon_tab_account_off;
        map.put(3, element);
        return map;
    }
}
