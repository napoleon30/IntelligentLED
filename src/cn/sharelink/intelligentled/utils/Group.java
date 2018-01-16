package cn.sharelink.intelligentled.utils;

public class Group {
	String strGroupName;
	int groupMemberAmount;
	
	public Group(String strGroupName,int groupMemberAmout){
		this.strGroupName = strGroupName;
		this.groupMemberAmount = groupMemberAmout;
	}

	public String getStrGroupName() {
		return strGroupName;
	}

	public void setStrGroupName(String strGroupName) {
		this.strGroupName = strGroupName;
	}

	public int getGroupMemberAmount() {
		return groupMemberAmount;
	}

	public void setGroupMemberAmount(int groupMemberAmount) {
		this.groupMemberAmount = groupMemberAmount;
	}

}
