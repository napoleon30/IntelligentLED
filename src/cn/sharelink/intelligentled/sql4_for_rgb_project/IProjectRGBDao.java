package cn.sharelink.intelligentled.sql4_for_rgb_project;

import java.util.List;


public interface IProjectRGBDao {
	/**
	 * 增加计划
	 * 
	 * @param project
	 *            计划记录的数据
	 * @return 返回最新增加的计划记录的ID，如果增加过程中出现错误，则返回-1
	 */
	long insert(ProjectRGB project);

	/**
	 * 查询计划记录
	 * 
	 * @param whereClause
	 *            WHERE子句，其中，各值使用?表示，例如 _id=?，如果查询所有数据，则该参数值为null即可
	 * @param whereArgs
	 *            WHERE子句中各?对应的值
	 * @return 计划记录的List集合，如果没有查询到匹配的数据，则List集合的长度为0
	 */
	List<ProjectRGB> query(String whereClause, String[] whereArgs);

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
	 * @param weekday
	 * @param mode
	 * @param etOne
	 * @param etTwo
	 * @param etThree
	 * @param etFour
	 * @param etFive
	 * @param etSix
	 * @param etSeven
	 * @param etEight
	 * @param etOneR
	 * @param etOneG
	 * @param etOneB
	 * @param etTwoR
	 * @param etTwoG
	 * @param etTwoB
	 * @param etThreeR
	 * @param etThreeG
	 * @param etThreeB
	 * @param etFourR
	 * @param etFourG
	 * @param etFourB
	 * @param etFiveR
	 * @param etFiveG
	 * @param etFiveB
	 * @param etRotationTime
	 * @param groupName
	 * @param physical
	 * @return
	 */
	long update(long id,String name,int weekday,int mode,
			String etOne,String etTwo,String etThree,String etFour,String etFive,
			String etSix,String etSeven,String etEight,
			String etOneR,String etOneG,String etOneB,
			String etTwoR,String etTwoG,String etTwoB,
			String etThreeR,String etThreeG,String etThreeB,
			String etFourR,String etFourG,String etFourB,
			String etFiveR,String etFiveG,String etFiveB,String etRotationTime,
			String groupName,String physical);
}
