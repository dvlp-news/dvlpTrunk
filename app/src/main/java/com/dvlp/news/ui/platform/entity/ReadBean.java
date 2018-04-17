package com.dvlp.news.ui.platform.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liubaba on 2018/4/9.
 */

public class ReadBean implements Parcelable {
    private String ctitle;
    private String cTitle2;
    private String contentUrl;
    private String cUrlH5;


    public String getCtitle() {
        return ctitle;
    }

    public void setCtitle(String ctitle) {
        this.ctitle = ctitle;
    }

    public String getcTitle2() {
        return cTitle2;
    }

    public void setcTitle2(String cTitle2) {
        this.cTitle2 = cTitle2;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getcUrlH5() {
        return cUrlH5;
    }

    public void setcUrlH5(String cUrlH5) {
        this.cUrlH5 = cUrlH5;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ctitle);
        dest.writeString(this.cTitle2);
        dest.writeString(this.contentUrl);
        dest.writeString(this.cUrlH5);
    }

    public ReadBean() {
    }

    protected ReadBean(Parcel in) {
        this.ctitle = in.readString();
        this.cTitle2 = in.readString();
        this.contentUrl = in.readString();
        this.cUrlH5 = in.readString();
    }

    public static final Parcelable.Creator<ReadBean> CREATOR = new Parcelable.Creator<ReadBean>() {
        @Override
        public ReadBean createFromParcel(Parcel source) {
            return new ReadBean(source);
        }

        @Override
        public ReadBean[] newArray(int size) {
            return new ReadBean[size];
        }
    };
}
