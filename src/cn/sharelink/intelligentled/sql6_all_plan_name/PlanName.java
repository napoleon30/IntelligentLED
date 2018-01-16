package cn.sharelink.intelligentled.sql6_all_plan_name;

/**
 * 用于保存在SingleColorAttributeEditActivity和SingleColorAttributeEdit2Activity界面中所有创建的计划名称
 * 防止创建重复的计划名称
 * @author Administrator
 *
 */
public class PlanName {
	private long id;
	private int type;//0表示单色灯，1表示色温灯，2表示彩色灯
	private String planName;
	private String physical;
	private String groupName;//""表示不是群组计划，不为""表示为群组计划，且为群组计划名
	



	public PlanName(){
		
	}
	
	public PlanName(int type,String planName,String physical,String groupName){
		this.type = type;
		this.planName = planName;
		this.physical = physical;
		this.groupName = groupName;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getPhysical() {
		return physical;
	}

	public void setPhysical(String physical) {
		this.physical = physical;
	}
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public String toString() {
		return "Student [id=" + id 
				+ ",type="+ type
				+ ",planName=" + planName 
				+ ",physical=" + physical
				+ ",groupName=" + groupName
				+ "]";
	}
}
