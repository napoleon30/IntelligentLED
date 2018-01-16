package cn.sharelink.intelligentled.activity_for_led;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.service.ACBindMgr;
import com.accloud.service.ACDeviceFind;
import com.accloud.service.ACDeviceMsg;
import com.accloud.service.ACException;
import com.accloud.service.ACUserDevice;
import com.accloud.service.Receiver;
import com.accloud.service.Topic;
import com.accloud.service.TopicData;
import com.accloud.utils.PreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.sharelink.intelligentled.R;
import cn.sharelink.intelligentled.R.layout;
import cn.sharelink.intelligentled.config.Config;
import cn.sharelink.intelligentled.for_led_other.CreateQRImage;
import cn.sharelink.intelligentled.for_led_other.MyAdapter;
import cn.sharelink.intelligentled.sql2.Device;
import cn.sharelink.intelligentled.sql2.DeviceDaoImpl;
import cn.sharelink.intelligentled.sql7_group_device_list.GroupDeviceList;
import cn.sharelink.intelligentled.sql7_group_device_list.GroupDeviceListDaoImpl;
import cn.sharelink.intelligentled.utils.ActionSheetDialog;
import cn.sharelink.intelligentled.utils.DBOXException;
import cn.sharelink.intelligentled.utils.GsonUtil;
import cn.sharelink.intelligentled.utils.ItonAdecimalConver;
import cn.sharelink.intelligentled.utils.OnReceive;
import cn.sharelink.intelligentled.utils.XListView;
import cn.sharelink.intelligentled.utils.ActionSheetDialog.OnSheetItemClickListener;
import cn.sharelink.intelligentled.utils.ActionSheetDialog.SheetItemColor;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * 色温灯组界面
 * @author Administrator
 *
 */
public class GroupDeviceList2Activity extends Activity {
	private static final String TAG = GroupDeviceList2Activity.class
			.getSimpleName();
	private Button back, allOpen, allClose, allControl, addDevice;
	private TextView tvGroupName;
	private XListView listDevice;
	private MyAdapter adapter;
	ACBindMgr bindMgr;

	List<Device> sqlDevices;
	List<ACUserDevice> acDevices; // 符合要求的设备集合
	/**
	 * onActivityForResult返回的选中的物理ID集合
	 */
	ArrayList<String> physicals;

	String deviceId, physicalDeviceId;
	private String subDomain;
	String strGroupName;
	int pos;// 设备所属群组在GroupActivity中的位置

	Timer timer;
	boolean isRunning = false;

	DeviceDaoImpl dao;
	GroupDeviceListDaoImpl groupDao;
	
	Map<String, Receiver<TopicData>> maps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		setContentView(R.layout.activity_group_device_list2);
		strGroupName = getIntent().getStringExtra("name");
		bindMgr = AC.bindMgr();
		dao = new DeviceDaoImpl(GroupDeviceList2Activity.this);
		groupDao = new GroupDeviceListDaoImpl(GroupDeviceList2Activity.this);
		maps = new HashMap<String, Receiver<TopicData>>();
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
		subDomain = PreferencesUtils.getString(this, "subDomain",
				Config.SUBDOMAIN);
		sqlDevices = dao.query(null, null);
		Log.e(TAG, "onResume中sqlDevices的长度：" + sqlDevices.size());
		for (Device dev : sqlDevices) {
			Log.e(TAG, "onResume中sqlDevcies的名字/长度/deviceID:" + dev.getName()
					+ "/" + dev.getDeviceID());
		}
		if (AC.accountMgr().isLogin()) {
			getDeviceList();
		}

		for (int i = 0; i < acDevices.size(); i++) {
			subscribe("xinlian01", "topic_type", acDevices.get(i).getDeviceId()
					+ "");// 订阅，可获取到返回值
		}

	}

	private void subscribe(String submain, String topic_type,
			final String deviceId) {
		AC.customDataMgr().subscribe(
				Topic.customTopic(submain, topic_type, deviceId),
				new VoidCallback() {
					@Override
					public void error(ACException arg0) {
						Log.e(TAG, "订阅失败");
					}

					@Override
					public void success() {
						Log.e(TAG, deviceId + "订阅成功");
					}
				});
		Receiver<TopicData> receiver = new Receiver<TopicData>() {

			@Override
			public void onReceive(TopicData arg0) {
				Log.e("订阅onReceive", arg0.getValue() + "/" + arg0.getKey());

				String jsonData = arg0.getValue();
				OnReceive onRece = GsonUtil.parseJsonWithGson(jsonData,
						OnReceive.class);
				String[] pay = onRece.getPayload();
				byte[] arraysPay = Base64.decode(pay[0], 0);
				String payload = ItonAdecimalConver.byte2hex(arraysPay)
						.replace(" ", "");
				Log.e(TAG, "接收到的返回值" + payload);
				// 解析06，全开、全关的返回值
			}

		};
		maps.put(deviceId, receiver);
		AC.customDataMgr().registerDataReceiver(receiver);

	}

	private void initView() {
			physicals = new ArrayList<String>();
			if (groupDao.query(null, null).size()>0) {
				for(GroupDeviceList groupdevicelist:groupDao.query(null, null)){
					if (groupdevicelist.getType().equals("1") && groupdevicelist.getGroupName().equals(strGroupName)) {
						physicals.add(groupdevicelist.getPhysical());
					}
				}
			};
		sqlDevices = new ArrayList<Device>();
		acDevices = new ArrayList<ACUserDevice>();
		listDevice = (XListView) findViewById(R.id.lv_device);
		back = (Button) findViewById(R.id.btn_group_list_back);
		tvGroupName = (TextView) findViewById(R.id.group_name);
		tvGroupName.setText(strGroupName);
		addDevice = (Button) findViewById(R.id.btn_add_device_to_group);
		allOpen = (Button) findViewById(R.id.all_open);
		allClose = (Button) findViewById(R.id.all_close);
		allControl = (Button) findViewById(R.id.all_control);
		back.setOnClickListener(listener);
		allOpen.setOnClickListener(listener);
		allClose.setOnClickListener(listener);
		allControl.setOnClickListener(listener);
		addDevice.setOnClickListener(listener);

		adapter = new MyAdapter(this, acDevices);
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
			for (Device dev : sqlDevices) {
				Log.e(TAG, "跳转前sqlDevices的DeviceID:" + dev.getDeviceID());
			}
			Intent intent = new Intent(GroupDeviceList2Activity.this,
					SingleColorAttributeActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("myDevice", acDevices.get(arg2 - 1));
			intent.putExtras(bundle);
			intent.putExtra("TYPE", 1);
			startActivity(intent);

		}
	};

	OnItemLongClickListener itemLongListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			showConfigurationDialog(acDevices.get(arg2 - 1), arg2);
			return true;
		}
	};

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_group_list_back:
				GroupDeviceList2Activity.this.finish();
				break;
			case R.id.all_open:// 全开
				if (acDevices.size() == 0) {
					Toast.makeText(
							GroupDeviceList2Activity.this,
							getResources()
									.getString(R.string.join_device_first), 0)
							.show();
				} else {
					for (ACUserDevice device : acDevices) {
						sendData(device.getPhysicalDeviceId(), "660600010199");
					}
				}
				break;
			case R.id.all_close:// 全关
				if (acDevices.size() == 0) {
					Toast.makeText(
							GroupDeviceList2Activity.this,
							getResources()
									.getString(R.string.join_device_first), 0)
							.show();
				} else {
					for (ACUserDevice device : acDevices) {
						sendData(device.getPhysicalDeviceId(), "660600010299");
					}
				}
				break;
			case R.id.all_control:
				if (acDevices.size() == 0) {
					Toast.makeText(
							GroupDeviceList2Activity.this,
							getResources()
									.getString(R.string.join_device_first), 0)
							.show();
				} else {
					Intent inten = new Intent(GroupDeviceList2Activity.this,
							GroupPlanActivity.class);
					inten.putExtra("model", 1);// 1表示灯为色温灯
					inten.putExtra("group_name", strGroupName);
					inten.putExtra("device", (Serializable) acDevices);
					startActivity(inten);
				}
				break;
			case R.id.btn_add_device_to_group:
				Intent intent = new Intent(GroupDeviceList2Activity.this,
						AddDeviceToGroupActivity.class);
				intent.putExtra("model", 1);
				startActivityForResult(intent, 1001);
				break;
			}

		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 101:
			Log.e(TAG, "onActivityResult");
			physicals.clear();
			if (data.getStringArrayListExtra("physicals").size() > 0) {
				physicals = data.getStringArrayListExtra("physicals");
				for(String str:physicals){
					groupDao.insert(new GroupDeviceList("1", str, strGroupName));
						}
				for (int i = 0; i < physicals.size(); i++) {
					Log.e(TAG, "获取到的返回集合physicals内容 ：" + physicals.get(i));

					for (Device dev : sqlDevices) {
						if (physicals.get(i).equals(dev.getPhysicalDeviceID())) {
							Log.e(TAG, "onActivityResult中：" + dev.getDeviceID()
									+ "/" + dev.getPhysicalDeviceID());
							dao.update(dev.getId(), dev.getName(),
									dev.getDeviceID(),
									dev.getPhysicalDeviceID(), dev.getType(),
									strGroupName,1);
							Log.e(TAG, "strGroupName的名称：" + strGroupName);
							sqlDevices = dao.query(null, null);
							for (Device devs : sqlDevices) {

								Log.e(TAG,
										"返回后更新sqlDevices的值中的group:"
												+ devs.getGroup());
							}
						}
					}
				}
			}
			break;
		case 102:
			break;

		}
	};

	protected void sendData(final String physicalDeviceId, String message) {
		Log.e(TAG, "物理ID的6060 ：" + physicalDeviceId + ","
		 + "发送数据message :"+ message
		);
		byte[] midbytes = message.getBytes();

		byte[] b = new byte[midbytes.length / 2];
		for (int i = 0; i < midbytes.length / 2; i++) {
			b[i] = uniteBytes(midbytes[i * 2], midbytes[i * 2 + 1]);
		}
		AC.bindMgr().sendToDeviceWithOption(subDomain, physicalDeviceId,
				getDeviceMsg(b), AC.ONLY_CLOUD,// ///////////////////////////////////////////////////
				new PayloadCallback<ACDeviceMsg>() {
					@Override
					public void success(ACDeviceMsg msg) {
						Log.e(TAG, physicalDeviceId + "的数据发送成功");
						if (parseDeviceMsg(msg)) {
							String returnedValue = ItonAdecimalConver
									.byte2hex(msg.getContent());
							Log.e(TAG, "callBack :" + returnedValue);
						}
					}

					@Override
					public void error(ACException arg0) {
						// TODO Auto-generated method stub
						Log.e(TAG, physicalDeviceId+"失败的回调:" + arg0.getMessage());
					}

				});

	}

	private ACDeviceMsg getDeviceMsg(byte[] b) {
		return new ACDeviceMsg(Config.LIGHT_MSGCODE, b);

	}

	protected boolean parseDeviceMsg(ACDeviceMsg msg) {
		byte[] bytes = msg.getContent();
		if (bytes != null)
			// return bytes[0] == 0x69 ? true : false;//
			return true;
		return false;
	}

	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	// 获取设备列表
	public void getDeviceList() {
		bindMgr.listDevicesWithStatus(new PayloadCallback<List<ACUserDevice>>() {
			@Override
			public void success(List<ACUserDevice> deviceList) {
				for (ACUserDevice device : deviceList) {
					device.getStatus();
					for (String physical : physicals) {
						Log.e(TAG, "获取设备列表时physicals的值：" + physical);
						if (device.getPhysicalDeviceId().equals(physical)) {
							Log.e(TAG, "符合要求，则加入acDevcies");
							acDevices.add(device);
						}
					}
				}
				if (deviceList.size() == 0) {
					listDevice.setPullRefreshEnable(false);
				} else {
					listDevice.setPullRefreshEnable(true);
				}
				Log.e(TAG, "/*/*1:"+acDevices.size());
				acDevices = removeDuplicate(acDevices);
				Log.e(TAG, "/*/*2:"+acDevices.size());
				adapter.deviceList = acDevices;
				adapter.notifyDataSetChanged();
				listDevice.stopRefresh();
				// 启动定时器,定时更新6060状态
				startTimer();
			}

			@Override
			public void error(ACException e) {
				DBOXException.errorCode(GroupDeviceList2Activity.this,
						e.getErrorCode());
			}
		});
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

	public void showConfigurationDialog(final ACUserDevice device,
			final int position) {
		new ActionSheetDialog(GroupDeviceList2Activity.this)
				.builder()
				.setCancelable(false)
				.setCanceledOnTouchOutside(false)
				.addSheetItem(getResources().getString(R.string.remove),
						SheetItemColor.Blue, new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								remove(device, position);
							}
						})
				.addSheetItem(getResources().getString(R.string.rename),
						SheetItemColor.Blue, new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								showSetDialog(device.getDeviceId(),
										device.getPhysicalDeviceId(), position);
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
																GroupDeviceList2Activity.this,
																e.getErrorCode());

											}
										});
							}
						}).show();
	}

	// 移除设备
	public void remove(ACUserDevice device, int position) {
		for (Device dev : sqlDevices) {
			if (device.getPhysicalDeviceId().equals(dev.getPhysicalDeviceID())) {
				dao.update(dev.getId(), dev.getName(), dev.getDeviceID(),
						dev.getPhysicalDeviceID(), dev.getType(),"",0);
				sqlDevices = dao.query(null, null);
				Log.e(TAG, "移除之后sqlDevices的长度：" + sqlDevices.size());
			}
		}
		for (int i = 0; i < physicals.size(); i++) {
			if (physicals.get(i).equals(device.getPhysicalDeviceId())) {
				
				if(groupDao.query(null, null).size()>0){
					for (GroupDeviceList groupdevicelist:groupDao.query(null, null)) {
						if(groupdevicelist.getPhysical().equals(device.getPhysicalDeviceId())){
							groupDao.delete(groupdevicelist.getId());
						}
					}
				}
				physicals.remove(i);
			}
		}
		acDevices.remove(position - 1);
		adapter.notifyDataSetChanged();

	}

	// 重命名
	protected void showSetDialog(final long deviceId,
			final String physicalDeviceId, final int position) {
		AlertDialog.Builder builder = new Builder(GroupDeviceList2Activity.this);

		View view; // 使用view来接入方法写出的dialog，方便相关初始化
		LayoutInflater inflater; // 引用自定义dialog布局
		inflater = LayoutInflater.from(GroupDeviceList2Activity.this);
		view = inflater.inflate(R.layout.dialog_set, null);
		builder.setView(view);

		final AlertDialog alert = builder.create();

		Button cancel = (Button) view.findViewById(R.id.cancel_btn);
		Button confirm = (Button) view.findViewById(R.id.confirm_btn);
		final EditText edit_name = (EditText) view.findViewById(R.id.edit_name);

		confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!edit_name.getText().toString().trim().equals("")) {
					bindMgr.changeName(subDomain, deviceId, edit_name.getText()
							.toString().trim(), new VoidCallback() {

						@Override
						public void error(ACException arg0) {
							Log.e(TAG, "名称修改失败---" + arg0.toString());
							Toast.makeText(
									GroupDeviceList2Activity.this,
									getResources()
											.getString(
													R.string.the_device_name_change_fails_please_try_again),
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void success() {
							PreferencesUtils.putString(
									GroupDeviceList2Activity.this,
									physicalDeviceId + "deviceName", edit_name
											.getText().toString().trim());
							for (Device dev : sqlDevices) {
								if (physicalDeviceId.equals(dev
										.getPhysicalDeviceID())) {
									dao.update(dev.getId(), edit_name.getText()
											.toString().trim(),
											dev.getDeviceID(),
											dev.getPhysicalDeviceID(),
											dev.getType(), dev.getGroup(),dev.getGrouptype());
									sqlDevices = dao.query(null, null);
								}

							}

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

	// dialog展示分享的二维码的imageview
	private void showShareCodeDialog(Bitmap bitmap) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				GroupDeviceList2Activity.this);
		View view = LayoutInflater.from(GroupDeviceList2Activity.this).inflate(
				R.layout.share_code_dialog, null);
		builder.setView(view);
		ImageView iv = (ImageView) view.findViewById(R.id.iv_share_code);
		iv.setImageBitmap(bitmap);
		builder.create().show();
	}

	/**
	 * list集合去除重复元素1
	 * 
	 * @param list
	 * @return
	 */
	public static List removeDuplicate(List<ACUserDevice> list) {
		List<ACUserDevice> lis  = new ArrayList<ACUserDevice>();
		List<String> strs = new ArrayList<String>();
		for(ACUserDevice ll : list){
			if(!strs.contains(ll.getPhysicalDeviceId())){
				strs.add(ll.getPhysicalDeviceId());
				lis.add(ll);
			}
		}
		
		return lis;
	}
	
	/**
	 * list集合去除重复元素2
	 */
	public static List removeDuplicate2(List list) {
		HashSet h = new HashSet(list);
		list.clear();
		list.addAll(h);
		return list;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		for (String str : maps.keySet()) {
			Log.e(TAG, "OnDestroy");
			AC.customDataMgr().unregisterDataReceiver(maps.get(str));
		}
	}
}
