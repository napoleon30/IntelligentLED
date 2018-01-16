package cn.sharelink.intelligentled.for_led_other;


public class TypeModel {
    private String physicalID;
    private String isSelect;
    
    public TypeModel(String physicalID,String isSelect){
    	this.physicalID = physicalID;
    	this.isSelect = isSelect;
    }

    public String getPhysicalID() {
        return physicalID;
    }

    public void setPhysicalID(String physicalID) {
        this.physicalID = physicalID;
    }

    public String getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(String isSelect) {
        this.isSelect = isSelect;
    }
}
