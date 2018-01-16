package cn.sharelink.intelligentled.sql7_group_device_list;

import java.util.List;


public interface GroupDeviceListDao {
	long insert(GroupDeviceList physical);

	List<GroupDeviceList> query(String whereClause, String[] whereArgs);

	int delete(long id);

	long update(long id, String type,String physical,String groupName);
}
