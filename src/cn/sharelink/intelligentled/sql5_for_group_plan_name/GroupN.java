package cn.sharelink.intelligentled.sql5_for_group_plan_name;

/**
 * 保存创建的群组和群组中的计划
 * @author Administrator
 *
 */
public class GroupN {
	private long id;
	private int type;  //组中所有灯类型
	private String groupName;//组名
	private String groupPlanName;//组计划名


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupPlanName() {
		return groupPlanName;
	}

	public void setGroupPlanName(String groupPlanName) {
		this.groupPlanName = groupPlanName;
	}

	public GroupN() {

	}

	public GroupN(int type, String groupName,String groupPlanName) {
		this.type = type;
		this.groupName = groupName;
		this.groupPlanName = groupPlanName;
	}

	

	@Override
	public String toString() {
		return "Student [id=" + id 
				+ ", type=" + type 
				+ ",groupName=" + groupName
				+ ",groupPlanName=" + groupPlanName 
				+ "]";
	}

}
