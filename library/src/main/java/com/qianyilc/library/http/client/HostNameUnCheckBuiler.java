package com.qianyilc.library.http.client;

import org.xutils.http.app.DefaultParamsBuilder;

import javax.net.ssl.SSLSocketFactory;

/**
 * Created by liuwei on 15/11/10.
 */
public class HostNameUnCheckBuiler extends DefaultParamsBuilder {
    @Override
    public SSLSocketFactory getSSLSocketFactory() {
        SSLSocketFactory factory=super.getSSLSocketFactory();

//        factory.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        return super.getSSLSocketFactory();
    }
}
