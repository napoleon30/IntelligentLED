package cn.sharelink.intelligentled.sql3_for_time_project;

/**
 * 保存单色灯和色温灯的具体计划
 * @author Administrator
 *
 */
public class Project {
	private long id;
	private String name; //计划名
	private int type; //灯类型
	private int weekday;
	private String begintime1;
	private String endtime1;
	private String seek11;
	private String seek12;
	private String begintime2;
	private String endtime2;
	private String seek21;
	private String seek22;
	private String begintime3;
	private String endtime3;
	private String seek31;
	private String seek32;
	private String begintime4;
	private String endtime4;
	private String seek41;
	private String seek42;
	private String groupName; //计划所在组名，该计划为设备单个计划时，groupName为""
	private String physical;//设备物理ID，isGroup为0时，physical为""
	



	public String getGroupName() {
		return groupName;
	}



	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}



	public String getPhysical() {
		return physical;
	}



	public void setPhysical(String physical) {
		this.physical = physical;
	}

	
	
	public Project(){
		
	}
	

	
	public Project(String name,int type,int weekday, String begintime1, String endtime1,
			String seek11, String seek12, String begintime2, String endtime2,
			String seek21, String seek22, String begintime3, String endtime3,
			String seek31, String seek32, String begintime4, String endtime4,
			String seek41, String seek42, String groupName,String physical) {
		super();
		this.name = name;
		this.type = type;
		this.weekday = weekday;
		this.begintime1 = begintime1;
		this.endtime1 = endtime1;
		this.seek11 = seek11;
		this.seek12 = seek12;
		this.begintime2 = begintime2;
		this.endtime2 = endtime2;
		this.seek21 = seek21;
		this.seek22 = seek22;
		this.begintime3 = begintime3;
		this.endtime3 = endtime3;
		this.seek31 = seek31;
		this.seek32 = seek32;
		this.begintime4 = begintime4;
		this.endtime4 = endtime4;
		this.seek41 = seek41;
		this.seek42 = seek42;
		this.groupName = groupName;
		this.physical = physical;
	}



	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getType(){
		return type;
	}
	public void setType(int type){
		this.type = type;
	}


	public int getWeekday() {
		return weekday;
	}


	public void setWeekday(int weekday) {
		this.weekday = weekday;
	}


	public String getBegintime1() {
		return begintime1;
	}


	public void setBegintime1(String begintime1) {
		this.begintime1 = begintime1;
	}


	public String getEndtime1() {
		return endtime1;
	}


	public void setEndtime1(String endtime1) {
		this.endtime1 = endtime1;
	}


	public String getSeek11() {
		return seek11;
	}


	public void setSeek11(String seek11) {
		this.seek11 = seek11;
	}


	public String getSeek12() {
		return seek12;
	}


	public void setSeek12(String seek12) {
		this.seek12 = seek12;
	}


	public String getBegintime2() {
		return begintime2;
	}


	public void setBegintime2(String begintime2) {
		this.begintime2 = begintime2;
	}


	public String getEndtime2() {
		return endtime2;
	}


	public void setEndtime2(String endtime2) {
		this.endtime2 = endtime2;
	}


	public String getSeek21() {
		return seek21;
	}


	public void setSeek21(String seek21) {
		this.seek21 = seek21;
	}


	public String getSeek22() {
		return seek22;
	}


	public void setSeek22(String seek22) {
		this.seek22 = seek22;
	}


	public String getBegintime3() {
		return begintime3;
	}


	public void setBegintime3(String begintime3) {
		this.begintime3 = begintime3;
	}


	public String getEndtime3() {
		return endtime3;
	}


	public void setEndtime3(String endtime3) {
		this.endtime3 = endtime3;
	}


	public String getSeek31() {
		return seek31;
	}


	public void setSeek31(String seek31) {
		this.seek31 = seek31;
	}


	public String getSeek32() {
		return seek32;
	}


	public void setSeek32(String seek32) {
		this.seek32 = seek32;
	}


	public String getBegintime4() {
		return begintime4;
	}


	public void setBegintime4(String begintime4) {
		this.begintime4 = begintime4;
	}


	public String getEndtime4() {
		return endtime4;
	}


	public void setEndtime4(String endtime4) {
		this.endtime4 = endtime4;
	}


	public String getSeek41() {
		return seek41;
	}


	public void setSeek41(String seek41) {
		this.seek41 = seek41;
	}


	public String getSeek42() {
		return seek42;
	}


	public void setSeek42(String seek42) {
		this.seek42 = seek42;
	}

	@Override
	public String toString() {
		return "Project [id=" + id 
				+ ", name="+ name
				+ ", type="+ type
				+ ", weekday=" + weekday 
				+ ", begintime1="+ begintime1 
				+ ", endtime1="+ endtime1 
				+ ", seek11=" + seek11
				+ ", seek12=" + seek12 
				+ ", begintime2=" + begintime2
				+ ", endtime2=" + endtime2 
				+ ", seek21=" + seek21 
				+ ", seek22="+ seek22 
				+ ", begintime3=" + begintime3 
				+ ", endtime3="+ endtime3 
				+ ", seek31=" + seek31 
				+ ", seek32=" + seek32
				+ ", begintime4=" + begintime4 
				+ ", endtime4=" + endtime4
				+ ", seek41=" + seek41 
				+ ", seek42=" + seek42 
				+ ", groupName=" + groupName
				+ ", physical="+physical
				+ "]";
	}
}
