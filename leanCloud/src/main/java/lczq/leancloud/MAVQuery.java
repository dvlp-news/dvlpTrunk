//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package lczq.leancloud;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVCacheManager;
import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVErrorUtils;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVExceptionHolder;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVPowerfulUtils;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRequestParams;
import com.avos.avoscloud.AVResponse;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.AVUtils;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GenericObjectCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.PaasClient;
import com.avos.avoscloud.QueryConditions;
import com.avos.avoscloud.QueryOperation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MAVQuery<T> extends AVQuery  {


    public MAVQuery(String theClassName) {
        super(theClassName);
    }
}
