package cn.sharelink.intelligentled.activity_for_led;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.service.ACBindMgr;
import com.accloud.service.ACDeviceFind;
import com.accloud.service.ACException;
import com.accloud.service.ACUserDevice;
import com.accloud.service.Receiver;
import com.accloud.service.Topic;
import com.accloud.service.TopicData;
import com.accloud.utils.PreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zbar.lib.CaptureActivity;

import cn.sharelink.intelligentled.R;
import cn.sharelink.intelligentled.activity.AddDeviceActivity;
import cn.sharelink.intelligentled.application.MainApplication;
import cn.sharelink.intelligentled.config.Config;
import cn.sharelink.intelligentled.for_led_other.CreateQRImage;
import cn.sharelink.intelligentled.for_led_other.MyAdapter;
import cn.sharelink.intelligentled.sql2.Device;
import cn.sharelink.intelligentled.sql2.DeviceDaoImpl;
import cn.sharelink.intelligentled.sql3_for_time_project.Project;
import cn.sharelink.intelligentled.sql3_for_time_project.ProjectDaoImpl;
import cn.sharelink.intelligentled.sql6_all_plan_name.PlanName;
import cn.sharelink.intelligentled.sql6_all_plan_name.PlanNameDaoImpl;
import cn.sharelink.intelligentled.sql7_group_device_list.GroupDeviceList;
import cn.sharelink.intelligentled.sql7_group_device_list.GroupDeviceListDaoImpl;
import cn.sharelink.intelligentled.utils.ActionSheetDialog;
import cn.sharelink.intelligentled.utils.DBOXException;
import cn.sharelink.intelligentled.utils.DensityUtils;
import cn.sharelink.intelligentled.utils.GsonUtil;
import cn.sharelink.intelligentled.utils.ItonAdecimalConver;
import cn.sharelink.intelligentled.utils.OnReceive;
import cn.sharelink.intelligentled.utils.Pop;
import cn.sharelink.intelligentled.utils.SendData;
import cn.sharelink.intelligentled.utils.XListView;
import cn.sharelink.intelligentled.utils.ActionSheetDialog.OnSheetItemClickListener;
import cn.sharelink.intelligentled.utils.ActionSheetDialog.SheetItemColor;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.PopupWindow.OnDismissListener;

/**
 * 单色灯界面
 * @author Administrator
 *
 */
public class SingleColorListActivity extends Activity implements
		OnClickListener {
	private static final String TAG = SingleColorListActivity.class
			.getSimpleName();
	private Button back, add, listControl;
	private XListView listDevice;
	private MyAdapter adapter;

	String deviceId, physicalDeviceId;
	private String subDomain;
	ACBindMgr bindMgr;
	Timer timer;
	boolean isRunning = false;
	
	private PopupWindow mPopupWindow;

	/**
	 * 展示的6060设备集合
	 */
	List<ACUserDevice> acDevics;
	/**
	 * 数据库存储的数据
	 */
	List<Device> sqlDevices;
	DeviceDaoImpl dao;
	

	Receiver<TopicData> receiver;// 订阅
	SendData senddata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "onCreate");
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		setContentView(R.layout.activity_single_color_list);
		bindMgr = AC.bindMgr();
		acDevics = new ArrayList<ACUserDevice>();
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
		subDomain = PreferencesUtils.getString(this, "subDomain",
				Config.SUBDOMAIN);
		querySQL();
		if (AC.accountMgr().isLogin()) {
			getDeviceList();
		}

	}
	
	protected void subscribe(ACUserDevice userDevice) {
		AC.customDataMgr().subscribe(
				Topic.customTopic("xinlian01", "topic_type", userDevice.getDeviceId()+""),
				new VoidCallback() {

					@Override
					public void error(ACException arg0) {
						Log.e(TAG, "/*/*/*"+"订阅失败");
					}

					@Override
					public void success() {
						Log.e(TAG, "/*/*/*"+"订阅成功");
					}
				});
		receiver = new Receiver<TopicData>() {

			@Override
			public void onReceive(TopicData arg0) {
				Log.e("订阅onReceive", arg0.getValue());
				String jsonData = arg0.getValue();
				OnReceive onRece = GsonUtil.parseJsonWithGson(jsonData,
						OnReceive.class);
				String[] pay = onRece.getPayload();
				byte[] arraysPay = Base64.decode(pay[0], 0);
				String payload = ItonAdecimalConver.byte2hex(arraysPay)
						.replace(" ", "");
				Log.e(TAG, "接收到的返回值:" + payload);
				//解析06
				if(!payload.contains("660600000099")){
					Toast.makeText(SingleColorListActivity.this, getResources().getString(R.string.configuration_lighttype_failed), 0).show();
				}else {
					
				}
			}

		};
		AC.customDataMgr().registerDataReceiver(receiver);
		
	}

	private void querySQL() {
		dao = new DeviceDaoImpl(SingleColorListActivity.this);
		sqlDevices = new ArrayList<Device>();
		// 查询数据的结果
		sqlDevices = dao.query(null, null);
		for (Device dev : sqlDevices) {
			Log.e(TAG, "开始时sqlDevices的设备ID：" + dev.getGroup());
		}
	}

	private void initView() {
		listDevice = (XListView) findViewById(R.id.lv_single_color_device_list);
		back = (Button) findViewById(R.id.btn_single_color_back);
		add = (Button) findViewById(R.id.btn_single_color_add);
		listControl = (Button) findViewById(R.id.btn_single_color_group_control);
		back.setOnClickListener(this);
		add.setOnClickListener(this);
		listControl.setOnClickListener(this);
		adapter = new MyAdapter(this, acDevics);
		listDevice.setAdapter(adapter);
		listDevice.setXListViewListener(new XListView.IXListViewListener() {
			@Override
			public void onRefresh() {
				getDeviceList();
			}
		});
		listDevice.setOnItemClickListener(itemListener);
		listDevice.setOnItemLongClickListener(itemLongListener);
	}

	OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Log.e(TAG, "POSITION = " + arg2);
			Log.e(TAG, "跳转的设备物理ID"
					+ acDevics.get(arg2 - 1).getPhysicalDeviceId());
			Intent intent = new Intent(SingleColorListActivity.this,
					SingleColorAttributeActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("myDevice", acDevics.get(arg2 - 1));
			
			intent.putExtras(bundle);
			intent.putExtra("TYPE", 0);
			startActivity(intent);

		}
	};

	OnItemLongClickListener itemLongListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			showConfigurationDialog(acDevics.get(arg2 - 1));
			return true;
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_single_color_back:
			SingleColorListActivity.this.finish();
			break;
		case R.id.btn_single_color_add:
			
			View popupView = getLayoutInflater().inflate(
					R.layout.mian_popupwindow, null);
			mPopupWindow = new PopupWindow(popupView, DensityUtils.dipTopx(SingleColorListActivity.this,
					113), DensityUtils.dipTopx(SingleColorListActivity.this, 100), true);
			TextView wifi_add = (TextView) popupView
					.findViewById(R.id.add_for_wifi);
			TextView scan_add = (TextView) popupView
					.findViewById(R.id.add_for_scan);
			wifi_add.setOnClickListener(this);
			scan_add.setOnClickListener(this);
			setBackgroundAlpha(0.5f);// 设置屏幕透明度
			mPopupWindow.setTouchable(true);
			mPopupWindow.setOutsideTouchable(true);
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable(
					getResources(), (Bitmap) null));

			mPopupWindow.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					// popupWindow隐藏时恢复屏幕正常透明度
					setBackgroundAlpha(1.0f);
				}
			});
			mPopupWindow.showAsDropDown(v, DensityUtils.dipTopx(SingleColorListActivity.this, -75),
					DensityUtils.dipTopx(SingleColorListActivity.this, 5));
			
			break;
		case R.id.btn_single_color_group_control:
			Intent intent2 = new Intent(SingleColorListActivity.this,
					GroupActivity.class);
			startActivity(intent2);
			break;
			
		case R.id.add_for_wifi:
			Intent intent3 = new Intent(SingleColorListActivity.this, AddDeviceActivity.class);
			intent3.putExtra("deviceType", 0);
			startActivity(intent3);
			mPopupWindow.dismiss();
			break;
		case R.id.add_for_scan:
			Intent intent4 = new Intent(SingleColorListActivity.this, CaptureActivity.class);
			startActivityForResult(intent4, 1234);
			mPopupWindow.dismiss();
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1234)
			switch (resultCode) {
			case 555:
				String shareCode = data.getStringExtra("scan_result");
				Log.e(TAG, "result :"+ shareCode);
				
				//普通用户通过扫分享码绑定设备
				bindMgr.bindDeviceWithShareCode(shareCode, new PayloadCallback<ACUserDevice>() {
				    @Override
				    public void success(ACUserDevice userDevice) {
				         //成功绑定管理员分享的设备
				    	Log.e(TAG,"SUCCESS ");
				    	adapter.deviceList.add(userDevice);
				    	adapter.notifyDataSetChanged();
				    	getDeviceList();
				    	
				    	//发送命令，配置设备灯类型（单色灯、色温灯、彩色灯）
						//1.订阅
						subscribe(userDevice);
						
						//2.发送命令
						String mesType="07";
						
						
						dao.insert(new Device(userDevice.getName(), userDevice
								.getDeviceId() + "", userDevice
								.getPhysicalDeviceId(), 0, "",0));
						
						senddata = new SendData(subDomain, userDevice.getPhysicalDeviceId());
						try {
							Thread.sleep(200);
							senddata.sendData("66060001"+mesType+"99");
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    }

				    @Override
				    public void error(ACException e) {
				         //网络错误或其他，根据e.getErrorCode()做不同的提示或处理
				    }
				});
				break;

			case 554:
				break;
			}
	}
	
	
	/**
	 * 设置添加屏幕的背景透明度
	 * @param bgAlpha
	 *            屏幕透明度0.0-1.0 1表示完全不透明
	 */
	public void setBackgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.alpha = bgAlpha;
		this.getWindow().setAttributes(lp);
	}

	// 获取设备列表
	public void getDeviceList() {
		Log.e(TAG, "更新6060设备列表");
		bindMgr.listDevicesWithStatus(new PayloadCallback<List<ACUserDevice>>() {
			@Override
			public void success(List<ACUserDevice> deviceList) {
				if (SingleColorListActivity.this.acDevics != null) {
					SingleColorListActivity.this.acDevics.clear();
				}
				for (ACUserDevice device : deviceList) {
					device.getStatus();
					
//					if (sqlDevices.size() == 0) {
//						Log.e(TAG, "sqlDevices的长度为0");
//						dao.insert(new Device(device.getName(), device
//								.getDeviceId() + "", device
//								.getPhysicalDeviceId(), 0, "",0));
//						acDevics.add(device);
//					} else {
						Log.e(TAG, "sqlDevices的长度不为0时的长度："+sqlDevices.size());
						for (Device dev : sqlDevices) {
							Log.e(TAG, "1111111:dev的物理ID/设备ID"+dev.getPhysicalDeviceID()+"/"+dev.getDeviceID());
							if (dev.getType()==0 && device.getPhysicalDeviceId().equals(dev.getPhysicalDeviceID())) {
								Log.e(TAG, "2222");
								acDevics.add(device);
//							}
							
						}
					}

//					sqlDevices = dao.query(null, null);

				}
				
				
				if (deviceList.size() == 0) {
					listDevice.setPullRefreshEnable(false);
				} else {
					listDevice.setPullRefreshEnable(true);
				}
				Log.e(TAG, "获取设备，去重前acDevices的长度："+acDevics.size());
				acDevics = removeDuplicate(acDevics);
				Log.e(TAG, "获取设备，去重后acDevices的长度："+acDevics.size());
				adapter.deviceList = acDevics;
				adapter.notifyDataSetChanged();
				listDevice.stopRefresh();
				// 启动定时器,定时更新6060状态
				startTimer();
			}

			@Override
			public void error(ACException e) {
				DBOXException.errorCode(SingleColorListActivity.this,
						e.getErrorCode());
			}
		});
	}
	
	/**
	 * List去重
	 * @param list
	 * @return
	 */
	public static List removeDuplicate(List list) {   
		HashSet h = new HashSet(list);   
		list.clear();   
		list.addAll(h);   
		return list;   
		}   

	// 启动定时器,定时更新6060状态
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

	// 定时更新设备当前的局域网状态
	public void refreshDeviceStatus() {
		// 当设备掉线或网络环境不稳定导致获取局域网显示状�?�不准确时，�?要手动刷新设备列表与�?域网状态
		AC.findLocalDevice(AC.FIND_DEVICE_DEFAULT_TIMEOUT,
				new PayloadCallback<List<ACDeviceFind>>() {
					@Override
					public void success(List<ACDeviceFind> deviceFinds) {
						// 局域网状态是否发生改变,是否�?要更新界面
						boolean isRefresh = false;
						// 遍历当前用户绑定的所有设备列表
						for (int i = 0; i < adapter.deviceList.size(); i++) {
							ACUserDevice device = adapter.deviceList.get(i);

							// 判断当前设备是否局域网本地在线
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
									// 当前设备由云端在线更新为云端局域网同时在线
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
									// 当前设备由云端局域网同时在线更新为云端在线
								} else if (device.getStatus() == ACUserDevice.BOTH_ONLINE) {
									device.setStatus(ACUserDevice.NETWORK_ONLINE);
									isRefresh = true;
								}
							}
						}
						// 局域网状�?�需要发生改�?,更新列表界面
						if (isRefresh)
							adapter.notifyDataSetChanged();
					}

					@Override
					public void error(ACException e) {
						// �?域网状�?�是否发生改�?,是否�?要更新列表界�?
						boolean isRefresh = false;
						for (int i = 0; i < adapter.deviceList.size(); i++) {
							ACUserDevice device = adapter.deviceList.get(i);
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

	@Override
	protected void onStop() {
		super.onStop();
		stopTimer();
	}

	// 关闭局域网在线状�?�定时器
	private void stopTimer() {
		if (timer != null) {
			timer.cancel();
			isRunning = false;
		}
	}

	public void showConfigurationDialog(final ACUserDevice device) {
		new ActionSheetDialog(SingleColorListActivity.this)
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
				.addSheetItem(getResources().getString(R.string.rename),
						SheetItemColor.Blue, new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								showSetDialog(device);
							}
						})
				.addSheetItem(getResources().getString(R.string.share),
						SheetItemColor.Blue, new OnSheetItemClickListener() {

							@Override
							public void onClick(int which) {

								// 管理员获取分享码
								AC.bindMgr().fetchShareCode(
										device.getDeviceId(), 3600,
										new PayloadCallback<String>() {

											@Override
											public void success(String shareCode) {
												Log.e(TAG, "分享成功" + shareCode);
												showShareCodeDialog(CreateQRImage
														.createQRImage(shareCode));

											}

											@Override
											public void error(ACException e) {
												DBOXException
														.errorCode(
																SingleColorListActivity.this,
																e.getErrorCode());

											}
										});
							}
						}).show();
	}

	// 删除设备
	public void unbindDevice(final ACUserDevice device) {
		bindMgr.unbindDevice(subDomain, device.getDeviceId(),
				new VoidCallback() {
					@Override
					public void success() {
						Pop.popToast(
								SingleColorListActivity.this,
								getString(R.string.main_aty_delete_device_success));
						//sql6
						PlanNameDaoImpl planNameDao = new PlanNameDaoImpl(SingleColorListActivity.this);
						for(PlanName planName : planNameDao.query(null, null)){
							if(planName.getType()==0 
									&& planName.getPhysical().equals(device.getPhysicalDeviceId())){
								planNameDao.delete(planName.getId());
							}
								
						}
						
						//sql3
						ProjectDaoImpl projectDao = new ProjectDaoImpl(SingleColorListActivity.this);
						for(Project pro:projectDao.query(null, null)){
							if(pro.getPhysical().equals(device.getPhysicalDeviceId())
									&& pro.getType()==0){
								projectDao.delete(pro.getId());
							}
						}
						
						//sql7删除GroupDeviceListActivity界面中的physicals集合包含的相应设备物理ID
						GroupDeviceListDaoImpl groupDao = new GroupDeviceListDaoImpl(SingleColorListActivity.this);
						if(groupDao.query(null, null).size()>0){
							for(GroupDeviceList groupdevicelist:groupDao.query(null, null)){
								if(groupdevicelist.getType().equals("0") && groupdevicelist.getPhysical().equals(device.getPhysicalDeviceId())){
									groupDao.delete(groupdevicelist.getId());
								}
							}
						}
						
						//sql2
						for (Device dev : sqlDevices) {
							if (dev.getPhysicalDeviceID().equals(
									device.getPhysicalDeviceId())) {
								dao.delete(dev.getId());
							}
						}
						sqlDevices = dao.query(null, null);
						getDeviceList();
					}

					@Override
					public void error(ACException e) {
						DBOXException.errorCode(SingleColorListActivity.this,
								e.getErrorCode());
					}
				});
	}

	// 重命名
	protected void showSetDialog(final ACUserDevice device) {
		AlertDialog.Builder builder = new Builder(SingleColorListActivity.this);

		View view; // 使用view来接入方法写出的dialog，方便相关初始化
		LayoutInflater inflater; // 引用自定义dialog布局
		inflater = LayoutInflater.from(SingleColorListActivity.this);
		view = inflater.inflate(R.layout.dialog_set, null);
		builder.setView(view);

		final AlertDialog alert = builder.create();

		Button cancel = (Button) view.findViewById(R.id.cancel_btn);
		Button confirm = (Button) view.findViewById(R.id.confirm_btn);
		final EditText edit_name = (EditText) view.findViewById(R.id.edit_name);

		confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e(TAG, edit_name.getText().toString().trim());
				Log.e(TAG, "点击确定");

				if (!edit_name.getText().toString().trim().equals("")) {
					bindMgr.changeName(subDomain, device.getDeviceId(),
							edit_name.getText().toString().trim(),
							new VoidCallback() {

								@Override
								public void error(ACException arg0) {
									Log.e(TAG, "名称修改失败---" + arg0.toString());
									Toast.makeText(
											SingleColorListActivity.this,
											getResources()
													.getString(
															R.string.the_device_name_change_fails_please_try_again),
											Toast.LENGTH_SHORT).show();
								}

								@Override
								public void success() {
									Log.e(TAG, "名称修改成功");
									for (Device dev : sqlDevices) {
										if (dev.getPhysicalDeviceID().equals(
												device.getPhysicalDeviceId())) {
											// dao.delete(dev.getId());
											dao.update(dev.getId(), edit_name
													.getText().toString()
													.trim(), dev.getDeviceID(),
													dev.getPhysicalDeviceID(),
													dev.getType(),
													dev.getGroup(),dev.getGrouptype());
										}
									}
									sqlDevices = dao.query(null, null);
									Log.e(TAG, "重命名成功后，sqlDevices的长度："
											+ sqlDevices.size());
									for (Device de : sqlDevices) {
										Log.e(TAG,
												"重命名成功后，sqlDevices的名称/物理ID："
														+ de.getName()
														+ "/"
														+ de.getPhysicalDeviceID());
									}

									PreferencesUtils.putString(
											SingleColorListActivity.this,
											device.getPhysicalDeviceId()
													+ "deviceName", edit_name
													.getText().toString()
													.trim());

									adapter.notifyDataSetChanged();
								}
							});
				}

				adapter.notifyDataSetChanged();

				alert.dismiss();

			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				alert.dismiss();
			}
		});
		alert.show();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		AC.customDataMgr().unregisterDataReceiver(receiver);
	}
	private void showShareCodeDialog(final Bitmap bitmap) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SingleColorListActivity.this);
		View view = LayoutInflater.from(SingleColorListActivity.this).inflate(
				R.layout.share_code_dialog, null);
		builder.setView(view);
		ImageView iv = (ImageView) view.findViewById(R.id.iv_share_code);
		iv.setImageBitmap(bitmap);
		builder.create().show();
	}
}
