package cn.sharelink.intelligentled.sql6_all_plan_name;

import java.util.List;


public interface PlanNameDao {
	long insert(PlanName planName);

	List<PlanName> query(String whereClause, String[] whereArgs);

	int delete(long id);

	long update(long id,int type, String planName,String physical,String groupName);
}
