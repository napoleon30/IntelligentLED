package cn.sharelink.intelligentled.model;

/**
 * Created by wangkun on 04/01/2017.
 */
public class PropertyRecord {
    /**
     * �߼�id
     */
    public long id;

    /**
     * Timestamp when the record created
     */
    public long timestamp;

    @Override
    public String toString() {
        return "PropertyRecord{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                '}';
    }
}
