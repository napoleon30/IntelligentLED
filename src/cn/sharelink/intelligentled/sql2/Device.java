package cn.sharelink.intelligentled.sql2;

import java.io.Serializable;

public class Device implements Serializable{

	private long id;
	private String name;  //6060名称
	private String deviceID;
	private String physicalDeviceID;
	private int type; //6060类型
	private String group; //6060所在群组
	private int grouptype;//0表示未分组，1表示已分组

	public int getGrouptype() {
		return grouptype;
	}

	public void setGrouptype(int grouptype) {
		this.grouptype = grouptype;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getPhysicalDeviceID() {
		return physicalDeviceID;
	}

	public void setPhysicalDeviceID(String physicalDeviceID) {
		this.physicalDeviceID = physicalDeviceID;
	}

	

	public Device() {

	}

	public Device(String name,String deviceID,String physicalDeviceID, int type, String group,int grouptype) {
		this.name = name;
		this.deviceID = deviceID;
		this.physicalDeviceID = physicalDeviceID;
		this.type = type;
		this.group = group;
		this.grouptype=grouptype;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "Student [id=" + id 
				+ ", name=" + name 
				+ ",deviceID=" + deviceID
				+ ",physicaDeviceID=" + physicalDeviceID 
				+ ", type=" + type
				+ ", group=" + group 
				+ ", grouptype="+ grouptype
				+ "]";
	}

}
