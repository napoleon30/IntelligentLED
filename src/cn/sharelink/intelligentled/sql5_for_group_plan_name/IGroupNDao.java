package cn.sharelink.intelligentled.sql5_for_group_plan_name;

import java.util.List;

public interface IGroupNDao {
	/**
	 * 增加学生记录
	 * 
	 * @param student
	 *            学生记录的数据
	 * @return 返回最新增加的学生记录的ID，如果增加过程中出现错误，则返回-1
	 */
	long insert(GroupN groupN);

	/**
	 * 查询学生记录
	 * 
	 * @param whereClause
	 *            WHERE子句，其中，各值使用?表示，例如 _id=?，如果查询所有数据，则该参数值为null即可
	 * @param whereArgs
	 *            WHERE子句中各?对应的值
	 * @return 学生记录的List集合，如果没有查询到匹配的数据，则List集合的长度为0
	 */
	List<GroupN> query(String whereClause, String[] whereArgs);

	/**
	 * 删除学生记录
	 * @param id 学生记录的ID
	 * @return 受影响的行数，如果删除失败，则返回0
	 */
	int delete(long id);
	
	long update(long id,int type,String groupName,String groupPlanName);
	
	
}
