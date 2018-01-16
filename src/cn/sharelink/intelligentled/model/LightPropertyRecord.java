package cn.sharelink.intelligentled.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wangkun on 04/01/2017.
 */
public class LightPropertyRecord extends PropertyRecord {
    //操作来源
    public int source;
    //开关
    @SerializedName("switch")
    public int light_on_off;
}
