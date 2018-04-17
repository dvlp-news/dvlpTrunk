package com.qianyilc.library.Bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 缓存对象
 * Created by zhangweiwei on 15/12/14.
 */
@Table(name = "cache")
public class CacheBean {

    public CacheBean() {

    }

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "method")
    public String method;

    @Column(name = "version")
    public String version;//软件版本

    @Column(name = "param")
    public String param;

    @Column(name = "result")
    public String result;

    @Column(name = "date")
    public long date;
}
