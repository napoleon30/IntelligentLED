package cn.sharelink.intelligentled.activity_for_led;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharelink.intelligentled.R;
import cn.sharelink.intelligentled.R.drawable;
import cn.sharelink.intelligentled.R.id;
import cn.sharelink.intelligentled.R.layout;
import cn.sharelink.intelligentled.R.string;
import cn.sharelink.intelligentled.activity.LoginActivity;
import cn.sharelink.intelligentled.activity.MainActivity;
import cn.sharelink.intelligentled.config.Config;
import cn.sharelink.intelligentled.controller.Light;
import cn.sharelink.intelligentled.sql2.Device;
import cn.sharelink.intelligentled.sql2.DeviceDaoImpl;
import cn.sharelink.intelligentled.sql3_for_time_project.Project;
import cn.sharelink.intelligentled.sql3_for_time_project.ProjectDaoImpl;
import cn.sharelink.intelligentled.sql4_for_rgb_project.ProjectRGB;
import cn.sharelink.intelligentled.sql4_for_rgb_project.ProjectRGBDaoImpl;
import cn.sharelink.intelligentled.sql6_all_plan_name.PlanName;
import cn.sharelink.intelligentled.sql6_all_plan_name.PlanNameDaoImpl;
import cn.sharelink.intelligentled.sql7_group_device_list.GroupDeviceList;
import cn.sharelink.intelligentled.sql7_group_device_list.GroupDeviceListDaoImpl;
import cn.sharelink.intelligentled.utils.ActionSheetDialog;
import cn.sharelink.intelligentled.utils.DBOXException;
import cn.sharelink.intelligentled.utils.MyDevice;
import cn.sharelink.intelligentled.utils.Pop;
import cn.sharelink.intelligentled.utils.ViewHolder;
import cn.sharelink.intelligentled.utils.XListView;
import cn.sharelink.intelligentled.utils.ActionSheetDialog.OnSheetItemClickListener;
import cn.sharelink.intelligentled.utils.ActionSheetDialog.SheetItemColor;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.service.ACBindMgr;
import com.accloud.service.ACDeviceFind;
import com.accloud.service.ACException;
import com.accloud.service.ACOTACheckInfo;
import com.accloud.service.ACOTAUpgradeInfo;
import com.accloud.service.ACUserDevice;
import com.accloud.utils.PreferencesUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 设备管理界面，用于展示和解绑账号下绑定的所有6060设备，
 * 防止意外（如设备配网但没有给设备类型的返回值，无法显示出来，重新配网需在官网解绑的尴尬情况）出现时
 * 的解决办法
 * @author Administrator
 *
 */
public class ManagerDeviceActivity extends Activity {
	
	private String deviceName,deviceId,physicalDeviceId;
	
	private XListView listView;
	private MyAdapter adapter;
	private Button back;
	// 设备管理�?
	ACBindMgr bindMgr;

	private String subDomain;

	Timer timer;
	boolean isRunning = false;
	List<MyDevice> myDeviceList;
	
	DeviceDaoImpl dao;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_manager_device);
		dao = new DeviceDaoImpl(ManagerDeviceActivity.this);
		listView = (XListView) findViewById(R.id.lv_manager_device_list);
		back = (Button)findViewById(R.id.btn_manager_device_back);
		myDeviceList = new ArrayList<MyDevice>();
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ManagerDeviceActivity.this.finish();
				
			}
		});
		
		// 获取设备管理�?
				bindMgr = AC.bindMgr();

				adapter = new MyAdapter(this);
				listView.setAdapter(adapter);
				listView.setXListViewListener(new XListView.IXListViewListener() {
					@Override
					public void onRefresh() {
						getDeviceList();
					}
				});
				
		// �?查设备是否有OTA升级
		checkOTAUpdates();
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		subDomain = PreferencesUtils.getString(this, "subDomain",
				Config.SUBDOMAIN);
		getDeviceList();
	}
	
	class MyAdapter extends BaseAdapter {
		public List<ACUserDevice> deviceList;
		private Context context;

		public MyAdapter(Context context) {
			this.context = context;
			deviceList = new ArrayList<ACUserDevice>();
		}

		@Override
		public int getCount() {
			return deviceList.size();
		}

		@Override
		public Object getItem(int i) {
			return deviceList.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(final int i, View view, ViewGroup viewGroup) {
			view = LayoutInflater.from(context).inflate(
					R.layout.adapter_list_device2, null);

			LinearLayout rl = ViewHolder.get(view, R.id.list_item);
			ImageView pic = ViewHolder.get(view, R.id.iv_pic);
			final TextView deviceName = ViewHolder
					.get(view, R.id.tv_deviceName);
			TextView physical = ViewHolder.get(view, R.id.tv_physicalDeviceId);
			
			TextView lightType = ViewHolder.get(view, R.id.tv_light_type);

			final ACUserDevice device = deviceList.get(i);

			List<String> physicals = new ArrayList<String>();
			physicalDeviceId = device.getPhysicalDeviceId();
			for(Device dev : dao.query(null, null)){
				physicals.add(dev.getPhysicalDeviceID());
				if(dev.getPhysicalDeviceID().equals(physicalDeviceId)){
					if(dev.getType()==0){
						lightType.setText(getResources().getString(R.string.single_color));
					}else if(dev.getType()==1){
						lightType.setText(getResources().getString(R.string.cct_change));
					}else if(dev.getType()==2){
						lightType.setText(getResources().getString(R.string.rgb));
					}
				}
			}
			
			physicals = removeDuplicate(physicals);
			
			if(!physicals.contains(physicalDeviceId)){
				lightType.setText(getResources().getString(R.string.unknown));
			}

			deviceId = device.getDeviceId() + "";

			physical.setText(physicalDeviceId);
			deviceName.setText(PreferencesUtils.getString(ManagerDeviceActivity.this,
					physicalDeviceId + "deviceName", device.getName()));
			switch (device.getStatus()) {

			case ACUserDevice.OFFLINE:
				deviceName.setTextColor(Color.GRAY);
				physical.setTextColor(Color.GRAY);
				lightType.setTextColor(Color.GRAY);
				pic.setBackgroundResource(R.drawable.cloud_off);
				Log.e("LINE", "OFFLINE");

				break;
			case ACUserDevice.NETWORK_ONLINE:
				deviceName.setTextColor(Color.GREEN);
				physical.setTextColor(Color.GREEN);
				lightType.setTextColor(Color.GREEN);
				pic.setBackgroundResource(R.drawable.cloud_on);
				Log.e("LINE", "NETWORK_ONLINE");
				break;
			case ACUserDevice.LOCAL_ONLINE:
				deviceName.setTextColor(Color.GREEN);
				physical.setTextColor(Color.GREEN);
				lightType.setTextColor(Color.GREEN);
				pic.setBackgroundResource(R.drawable.cloud_on);
				Log.e("LINE", "LOCAL_ONLINE");
				break;
			case ACUserDevice.BOTH_ONLINE:
				deviceName.setTextColor(Color.GREEN);
				physical.setTextColor(Color.GREEN);
				lightType.setTextColor(Color.GREEN);
				pic.setBackgroundResource(R.drawable.cloud_on);
				Log.e("LINE", "BOTH_ONLINE");
				break;
			}

			rl.setOnClickListener(new View.OnClickListener() {// 短按跳转

				@Override
				public void onClick(View v) {
					showConfigurationDialog(device);

				}
			});
			return view;
		}
	}
	
	// 获取设备列表
		public void getDeviceList() {
			myDeviceList.clear();
			bindMgr.listDevicesWithStatus(new PayloadCallback<List<ACUserDevice>>() {
				@Override
				public void success(List<ACUserDevice> deviceList) {
					for (ACUserDevice device : deviceList) {
						device.getStatus();
						MyDevice myDevice = new MyDevice(device.getName(),device.getDeviceId()+"",device.getPhysicalDeviceId());
						myDeviceList.add(myDevice);
					}
					if (deviceList.size() == 0) {
						listView.setPullRefreshEnable(false);
					} else {
						listView.setPullRefreshEnable(true);
					}
					adapter.deviceList = deviceList;
					adapter.notifyDataSetChanged();
					listView.stopRefresh();
					// 启动定时�?,定时更新�?域网状�??
					startTimer();
				}

				@Override
				public void error(ACException e) {
					DBOXException.errorCode(ManagerDeviceActivity.this, e.getErrorCode());
				}
			});
		}
	
	public void showConfigurationDialog(final ACUserDevice device) {
		new ActionSheetDialog(ManagerDeviceActivity.this)
				.builder()
				.setCancelable(false)
				.setCanceledOnTouchOutside(false)
				.addSheetItem(getResources().getString(R.string.delete),
						SheetItemColor.Blue, new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								unbindDevice(device);
							}
						})
				
				.show();
	}
	
	// 删除设备�?
		public void unbindDevice(final ACUserDevice device) {
			bindMgr.unbindDevice(subDomain, device.getDeviceId(),
					new VoidCallback() {
						@Override
						public void success() {
							Pop.popToast(
									ManagerDeviceActivity.this,
									getString(R.string.main_aty_delete_device_success));
							//sql6
							PlanNameDaoImpl planNameDao = new PlanNameDaoImpl(ManagerDeviceActivity.this);
							for(PlanName planName : planNameDao.query(null, null)){
								if(planName.getPhysical().equals(device.getPhysicalDeviceId())){
									planNameDao.delete(planName.getId());
								}
									
							}
							
							//sql4
							ProjectRGBDaoImpl projectRgbDao = new ProjectRGBDaoImpl(ManagerDeviceActivity.this);
							for(ProjectRGB prorgb:projectRgbDao.query(null, null)){
								if(prorgb.getPhysical().equals(device.getPhysicalDeviceId())){
									projectRgbDao.delete(prorgb.getId());
								}
							}
							
							//sql3
							ProjectDaoImpl projectDao = new ProjectDaoImpl(ManagerDeviceActivity.this);
							for(Project pro:projectDao.query(null, null)){
								if(pro.getPhysical().equals(device.getPhysicalDeviceId())){
									projectDao.delete(pro.getId());
								}
							}
							
							//sql7删除GroupDeviceListActivity界面中的physicals集合包含的相应设备物理ID
							GroupDeviceListDaoImpl groupDao = new GroupDeviceListDaoImpl(ManagerDeviceActivity.this);
							if(groupDao.query(null, null).size()>0){
								for(GroupDeviceList groupdevicelist:groupDao.query(null, null)){
									if(groupdevicelist.getPhysical().equals(device.getPhysicalDeviceId())){
										groupDao.delete(groupdevicelist.getId());
									}
								}
							}

							//sql2
							for (Device dev : dao.query(null, null)) {
								if (dev.getPhysicalDeviceID().equals(
										device.getPhysicalDeviceId())) {
									dao.delete(dev.getId());
								}
							}
							getDeviceList();
						
						}

						@Override
						public void error(ACException e) {
							// Pop.popToast(MainActivity.this, e.getErrorCode()
							// + "-->" + e.getMessage());
							DBOXException.errorCode(ManagerDeviceActivity.this,
									e.getErrorCode());
						}
					});
		}
		
		// 定时更新设备当前的局域网状�??
		public void refreshDeviceStatus() {
			// 当设备掉线或网络环境不稳定导致获取局域网显示状�?�不准确时，�?要手动刷新设备列表与�?域网状�??
			AC.findLocalDevice(AC.FIND_DEVICE_DEFAULT_TIMEOUT,
					new PayloadCallback<List<ACDeviceFind>>() {
						@Override
						public void success(List<ACDeviceFind> deviceFinds) {
							// �?域网状�?�是否发生改�?,是否�?要更新界�?
							boolean isRefresh = false;
							// 遍历当前用户绑定的所有设备列�?
							for (ACUserDevice device : adapter.deviceList) {
								// 判断当前设备是否�?域网本地在线
								boolean isLocalOnline = false;
								// 遍历当前发现的局域网在线列表
								for (ACDeviceFind deviceFind : deviceFinds) {
									// 通过设备的物理Id进行匹配,若当前设备在发现的局域网列表�?,则置为局域网在线
									if (device.getPhysicalDeviceId().equals(
											deviceFind.getPhysicalDeviceId())) {
										isLocalOnline = true;
									}
								}
								if (isLocalOnline) {
									// 当前设备由不在线更新为局域网在线
									if (device.getStatus() == ACUserDevice.OFFLINE) {
										device.setStatus(ACUserDevice.LOCAL_ONLINE);
										isRefresh = true;
										// 当前设备由云端在线更新为云端�?域网同时在线
									} else if (device.getStatus() == ACUserDevice.NETWORK_ONLINE) {
										device.setStatus(ACUserDevice.BOTH_ONLINE);
										isRefresh = true;
									}
								} else {
									// 当前设备由局域网在线更新为不在线
									if (device.getStatus() == ACUserDevice.LOCAL_ONLINE) {
										device.setStatus(ACUserDevice.OFFLINE);
										AC.SEND_TO_LOCAL_DEVICE_DEFAULT_TIMEOUT = 6000;
										isRefresh = true;
										// 当前设备由云端局域网同时在线更新为云端在�?
									} else if (device.getStatus() == ACUserDevice.BOTH_ONLINE) {
										device.setStatus(ACUserDevice.NETWORK_ONLINE);
										isRefresh = true;
									}
								}
							}
							// �?域网状�?�需要发生改�?,更新列表界面
							if (isRefresh)
								adapter.notifyDataSetChanged();
						}

						@Override
						public void error(ACException e) {
							// �?域网状�?�是否发生改�?,是否�?要更新列表界�?
							boolean isRefresh = false;
							for (ACUserDevice device : adapter.deviceList) {
								// 没有设备当前�?域网在线,�?以把�?有当前显示局域网在线的设备状态重�?
								if (device.getStatus() == ACUserDevice.LOCAL_ONLINE) {
									device.setStatus(ACUserDevice.OFFLINE);
									isRefresh = true;
								} else if (device.getStatus() == ACUserDevice.BOTH_ONLINE) {
									device.setStatus(ACUserDevice.NETWORK_ONLINE);
									isRefresh = true;
								}
							}
							// �?域网状�?�需要发生改�?,更新列表界面
							if (isRefresh)
								adapter.notifyDataSetChanged();
						}
					});
		}

		// 启动定时�?,定时更新�?域网状�??
		private void startTimer() {
			if (!isRunning) {
				timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						refreshDeviceStatus();
					}
				}, 0, 3000);
				isRunning = true;
			}
		}

		@Override
		protected void onStop() {
			super.onStop();
			stopTimer();
		}

		// 关闭�?域网在线状�?�定时器
		private void stopTimer() {
			if (timer != null) {
				timer.cancel();
				isRunning = false;
			}
		}

		// �?查所有设备是否有OTA升级
		private void checkOTAUpdates() {
			// 获取�?有设备列�?
			AC.bindMgr().listDevices(new PayloadCallback<List<ACUserDevice>>() {
				@Override
				public void success(List<ACUserDevice> devices) {
					// 遍历�?有绑定的设备
					for (final ACUserDevice device : devices) {
						ACOTACheckInfo checkInfo = new ACOTACheckInfo(device
								.getDeviceId(), 1);
						// �?查该设备是否有OTA升级
						AC.otaMgr().checkUpdate(subDomain, checkInfo,
								new PayloadCallback<ACOTAUpgradeInfo>() {
									@Override
									public void success(ACOTAUpgradeInfo upgradeInfo) {
										// 如果有OTA新版�?,则弹框显示是否确认升�?
										if (upgradeInfo.isUpdate())
											showOTADialog(device, upgradeInfo);
									}

									@Override
									public void error(ACException e) {
									}
								});
					}
				}

				@Override
				public void error(ACException e) {
				}
			});
		}

		// 如果有OTA新版本升�?,则弹框显�?
		private void showOTADialog(final ACUserDevice device,
				final ACOTAUpgradeInfo info) {
			new AlertDialog.Builder(this)
					.setTitle(R.string.main_aty_ota_upgrade_title)
					.setMessage(
							getString(R.string.main_aty_ota_upgrade_desc,
									device.getPhysicalDeviceId(), device.getName(),
									info.getTargetVersion(), info.getUpgradeLog()))
					.setPositiveButton(
							getString(R.string.main_aty_ota_upgrade_confirm),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// 确认升级
									AC.otaMgr().confirmUpdate(subDomain,
											device.getDeviceId(),
											info.getTargetVersion(), 1,
											new VoidCallback() {
												@Override
												public void success() {
													Pop.popToast(
															ManagerDeviceActivity.this,
															getString(R.string.main_aty_ota_upgrade_toast_hint));
												}

												@Override
												public void error(ACException e) {
													Pop.popToast(ManagerDeviceActivity.this,
															e.toString());
												}
											});
								}
							})
					.setNegativeButton(
							getString(R.string.main_aty_ota_upgrade_cancle), null)
					.create().show();
		}
		
		/**
		 * list去重
		 * @param list
		 * @return
		 */
		public static List removeDuplicate(List list) {   
			HashSet h = new HashSet(list);   
			list.clear();   
			list.addAll(h);   
			return list;   
			} 
}
