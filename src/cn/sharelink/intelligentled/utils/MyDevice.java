package cn.sharelink.intelligentled.utils;

import java.io.Serializable;

public class MyDevice implements Serializable{
	String deviceName;
	String physicalDeviceID;
	String deviceID;
	
	public MyDevice(String deviceName,String deviceID,String physicalDeviceID){
		this.deviceName = deviceName;
		this.deviceID = deviceID;
		this.physicalDeviceID = physicalDeviceID;
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getPhysicalDeviceID() {
		return physicalDeviceID;
	}
	public void setPhysicalDeviceID(String physicalDeviceID) {
		this.physicalDeviceID = physicalDeviceID;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

}
