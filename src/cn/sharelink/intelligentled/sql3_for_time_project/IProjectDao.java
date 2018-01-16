package cn.sharelink.intelligentled.sql3_for_time_project;

import java.util.List;


public interface IProjectDao {
	/**
	 * 增加计划
	 * 
	 * @param project
	 *            计划记录的数据
	 * @return 返回最新增加的计划记录的ID，如果增加过程中出现错误，则返回-1
	 */
	long insert(Project project);

	/**
	 * 查询计划记录
	 * 
	 * @param whereClause
	 *            WHERE子句，其中，各值使用?表示，例如 _id=?，如果查询所有数据，则该参数值为null即可
	 * @param whereArgs
	 *            WHERE子句中各?对应的值
	 * @return 计划记录的List集合，如果没有查询到匹配的数据，则List集合的长度为0
	 */
	List<Project> query(String whereClause, String[] whereArgs);

	/**
	 * 删除计划记录
	 * @param id 计划记录的ID
	 * @return 受影响的行数，如果删除失败，则返回0
	 */
	int delete(long id);
	
	/**
	 * 更新计划
	 * @param id
	 * @param name
	 * @param type
	 * @param weekday
	 * @param begintime1
	 * @param endtime1
	 * @param seek11
	 * @param seek12
	 * @param begintime2
	 * @param endtime2
	 * @param seek21
	 * @param seek22
	 * @param begintime3
	 * @param endtime3
	 * @param seek31
	 * @param seek32
	 * @param begintime4
	 * @param endtime4
	 * @param seek41
	 * @param seek42
	 * @param groupName
	 * @param physical
	 * @return 
	 */
	long update(long id,String name,int type,int weekday,
			String begintime1,String endtime1,String seek11,String seek12,
			String begintime2,String endtime2,String seek21,String seek22,
			String begintime3,String endtime3,String seek31,String seek32,
			String begintime4,String endtime4,String seek41,String seek42,
			String groupName,String physical);
}
