package cn.sharelink.intelligentled.sql7_group_device_list;

/**
 * 用于保存在GroupDeviceListActivity界面中分组选中的设备物理id
 * @author Administrator
 */
public class GroupDeviceList {
	private long id;
	private String type;
	private String physical;
	private String groupName;
	

	public GroupDeviceList(){
		
	}
	
	public GroupDeviceList(String type,String physical,String groupName){
		this.type = type;
		this.physical = physical;
		this.groupName = groupName;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
				+ ",type=" + type
				+ ",physical=" + physical 
				+ ",groupName=" + groupName
				+ "]";
	}
}
