package cn.sharelink.intelligentled.sql4_for_rgb_project;

/**
 * 保存彩色灯单个设备的具体计划
 * @author Administrator
 *
 */
public class ProjectRGB {
	private long id;
	private String name; // 计划名
	private int weekday; // 星期
	private int mode;//模式
	private String etOne;
	private String etTwo;
	private String etThree;
	private String etFour;
	private String etFive;
	private String etSix;
	private String etSeven;
	private String etEight;
	private String etOneR;
	private String etOneG;
	private String etOneB;
	private String etTwoR;
	private String etTwoG;
	private String etTwoB;
	private String etThreeR;
	private String etThreeG;
	private String etThreeB;
	private String etFourR;
	private String etFourG;
	private String etFourB;
	private String etFiveR;
	private String etFiveG;
	private String etFiveB;
	private String etRotationTime;
	private String groupName; //该计划为设备计划时，groupName为""
	private String physical;//设备物理ID，isGroup为0时，physical为""

	public ProjectRGB() {

	}

	public ProjectRGB(String name,int weekday,int mode,
			String etOne,String etTwo,String etThree,String etFour,String etFive,
			String etSix,String etSeven,String etEight,
			String etOneR,String etOneG,String etOneB,
			String etTwoR,String etTwoG,String etTwoB,
			String etThreeR,String etThreeG,String etThreeB,
			String etFourR,String etFourG,String etFourB,
			String etFiveR,String etFiveG,String etFiveB,
			String etRotationTime , String groupName,String physical
			) {
		super();
		this.name=name; 
		this.weekday=weekday; 
		this.mode = mode;
		this.etOne=etOne;
		this.etTwo=etTwo;
		this.etThree=etThree;
		this.etFour=etFour;
		this.etFive=etFive;
		this.etSix=etSix;
		this.etSeven=etSeven;
		this.etEight=etEight;
		this.etOneR=etOneR;
		this.etOneG=etOneG;
		this.etOneB=etOneB;
		this.etTwoR=etTwoR;
		this.etTwoG=etTwoG;
		this.etTwoB=etTwoB;
		this.etThreeR=etThreeR;
		this.etThreeG=etThreeG;
		this.etThreeB=etThreeB;
		this.etFourR=etFourR;
		this.etFourG=etFourG;
		this.etFourB=etFourB;
		this.etFiveR=etFiveR;
		this.etFiveG=etFiveG;
		this.etFiveB=etFiveB;
		this.etRotationTime = etRotationTime;
		this.groupName = groupName;
		this.physical = physical;
	}



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

	public int getWeekday() {
		return weekday;
	}

	public void setWeekday(int weekday) {
		this.weekday = weekday;
	}

	
	public void setMode(int mode){
		this.mode = mode;
	}
	
	public int getMode(){
		return mode;
	}


	public String getEtOne() {
		return etOne;
	}

	public void setEtOne(String etOne) {
		this.etOne = etOne;
	}

	public String getEtTwo() {
		return etTwo;
	}

	public void setEtTwo(String etTwo) {
		this.etTwo = etTwo;
	}

	public String getEtThree() {
		return etThree;
	}

	public void setEtThree(String etThree) {
		this.etThree = etThree;
	}

	public String getEtFour() {
		return etFour;
	}

	public void setEtFour(String etFour) {
		this.etFour = etFour;
	}

	public String getEtFive() {
		return etFive;
	}

	public void setEtFive(String etFive) {
		this.etFive = etFive;
	}

	public String getEtSix() {
		return etSix;
	}

	public void setEtSix(String etSix) {
		this.etSix = etSix;
	}

	public String getEtSeven() {
		return etSeven;
	}

	public void setEtSeven(String etSeven) {
		this.etSeven = etSeven;
	}

	public String getEtEight() {
		return etEight;
	}

	public void setEtEight(String etEight) {
		this.etEight = etEight;
	}

	public String getEtOneR() {
		return etOneR;
	}

	public void setEtOneR(String etOneR) {
		this.etOneR = etOneR;
	}

	public String getEtOneG() {
		return etOneG;
	}

	public void setEtOneG(String etOneG) {
		this.etOneG = etOneG;
	}

	public String getEtOneB() {
		return etOneB;
	}

	public void setEtOneB(String etOneB) {
		this.etOneB = etOneB;
	}

	public String getEtTwoR() {
		return etTwoR;
	}

	public void setEtTwoR(String etTwoR) {
		this.etTwoR = etTwoR;
	}

	public String getEtTwoG() {
		return etTwoG;
	}

	public void setEtTwoG(String etTwoG) {
		this.etTwoG = etTwoG;
	}

	public String getEtTwoB() {
		return etTwoB;
	}

	public void setEtTwoB(String etTwoB) {
		this.etTwoB = etTwoB;
	}

	public String getEtThreeR() {
		return etThreeR;
	}

	public void setEtThreeR(String etThreeR) {
		this.etThreeR = etThreeR;
	}

	public String getEtThreeG() {
		return etThreeG;
	}

	public void setEtThreeG(String etThreeG) {
		this.etThreeG = etThreeG;
	}

	public String getEtThreeB() {
		return etThreeB;
	}

	public void setEtThreeB(String etThreeB) {
		this.etThreeB = etThreeB;
	}

	public String getEtFourR() {
		return etFourR;
	}

	public void setEtFourR(String etFourR) {
		this.etFourR = etFourR;
	}

	public String getEtFourG() {
		return etFourG;
	}

	public void setEtFourG(String etFourG) {
		this.etFourG = etFourG;
	}

	public String getEtFourB() {
		return etFourB;
	}

	public void setEtFourB(String etFourB) {
		this.etFourB = etFourB;
	}

	public String getEtFiveR() {
		return etFiveR;
	}

	public void setEtFiveR(String etFiveR) {
		this.etFiveR = etFiveR;
	}

	public String getEtFiveG() {
		return etFiveG;
	}

	public void setEtFiveG(String etFiveG) {
		this.etFiveG = etFiveG;
	}

	public String getEtFiveB() {
		return etFiveB;
	}

	public void setEtFiveB(String etFiveB) {
		this.etFiveB = etFiveB;
	}
	public String getEtRotationTime() {
		return etRotationTime;
	}

	public void setEtRotationTime(String etRotationTime) {
		this.etRotationTime = etRotationTime;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name+ ", weekday=" + weekday +", mode=" + mode
				+ ", etOne=" + etOne + ", etTwo="+ etTwo +", etThree="+ etThree
				+ ", etFour="+ etFour + ", etFive="+etFive + ", etSix="+etSix
				+ ", etSeven="+etSeven + ", etEight="+etEight
				+ ", etOneR="+etOneR + ", etOneG="+etOneG + ", etOneB="+etOneB
				+ ", etTwoR="+etTwoR + ", etTwoG="+etTwoG + ", etTwoB="+etTwoB
				+ ", etThreeR="+etThreeR + ", etThreeG="+etThreeG + ", etThreeB="+etThreeB
				+ ", etFourR="+etFourR + ", etFourG="+etFourG + ", etFourB="+etFourB
				+ ", etFiveR="+etFiveR + ", etFiveG="+etFiveG + ", etFiveB="+etFiveB
				+ ", etRotationTime="+etRotationTime
				+ ", groupName=" + groupName
				+ ", physical="+physical
				+ "]";
	}
}
