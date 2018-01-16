package cn.sharelink.intelligentled.activity_for_led;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.cloudservice.VoidCallback;
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
import cn.sharelink.intelligentled.activity_for_led.SingleColorAttributeActivity.MyThread;
import cn.sharelink.intelligentled.config.Config;
import cn.sharelink.intelligentled.sql2.Device;
import cn.sharelink.intelligentled.sql2.DeviceDaoImpl;
import cn.sharelink.intelligentled.sql3_for_time_project.Project;
import cn.sharelink.intelligentled.sql3_for_time_project.ProjectDaoImpl;
import cn.sharelink.intelligentled.sql4_for_rgb_project.ProjectRGB;
import cn.sharelink.intelligentled.sql4_for_rgb_project.ProjectRGBDaoImpl;
import cn.sharelink.intelligentled.sql5_for_group_plan_name.GroupN;
import cn.sharelink.intelligentled.sql5_for_group_plan_name.GroupNDaoImpl;
import cn.sharelink.intelligentled.sql6_all_plan_name.PlanName;
import cn.sharelink.intelligentled.sql6_all_plan_name.PlanNameDaoImpl;
import cn.sharelink.intelligentled.utils.ActionSheetDialog;
import cn.sharelink.intelligentled.utils.GsonUtil;
import cn.sharelink.intelligentled.utils.ItonAdecimalConver;
import cn.sharelink.intelligentled.utils.MyDevice;
import cn.sharelink.intelligentled.utils.OnReceive;
import cn.sharelink.intelligentled.utils.SendData;
import cn.sharelink.intelligentled.utils.ActionSheetDialog.OnSheetItemClickListener;
import cn.sharelink.intelligentled.utils.ActionSheetDialog.SheetItemColor;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 群组计划列表界面
 * 
 * @author Administrator
 * 
 */
public class GroupPlanActivity extends Activity {
	private static final String TAG = GroupPlanActivity.class.getSimpleName();

	private Button back, addGroupPlan;
	private TextView groupPlanName, groupPlanNum;
	private ListView groupPlanListView;
	GroupNDaoImpl groupDao;

	MyAdapter adapter;
	/**
	 * 组计划名称集合
	 */
	List<String> groupPlanNames;
	/**
	 * type表示灯的种类，0-单色灯，1-色温灯，2-RGB灯
	 */
	int type = 0;
	String groupName;

	List<ACUserDevice> acDevices;
	Map<String, Receiver<TopicData>> maps;
	private String subDomain;
	SendData senddata;
	List<String> lists;
	/**
	 * RGB灯下的计划参数
	 */
	String one14_2, two14_2, three14_2, four14_2, five14_2, six14_2, seven14_2,
			eight14_2; // 星期一，模式4的值
	String one24_2, two24_2, three24_2, four24_2, five24_2, six24_2, seven24_2,
			eight24_2;// 星期二，模式4的值
	String one34_2, two34_2, three34_2, four34_2, five34_2, six34_2, seven34_2,
			eight34_2;// 星期三，模式4的值
	String one44_2, two44_2, three44_2, four44_2, five44_2, six44_2, seven44_2,
			eight44_2;// 星期四，模式4的值
	String one54_2, two54_2, three54_2, four54_2, five54_2, six54_2, seven54_2,
			eight54_2;// 星期五，模式4的值
	String one64_2, two64_2, three64_2, four64_2, five64_2, six64_2, seven64_2,
			eight64_2;// 星期六，模式4的值
	String one74_2, two74_2, three74_2, four74_2, five74_2, six74_2, seven74_2,
			eight74_2;// 星期日，模式4的值
	String oneR14_2, oneG14_2, oneB14_2, twoR14_2, twoG14_2, twoB14_2,
			threeR14_2, threeG14_2, threeB14_2, fourR14_2, fourG14_2,
			fourB14_2, fiveR14_2, fiveG14_2, fiveB14_2;
	String oneR24_2, oneG24_2, oneB24_2, twoR24_2, twoG24_2, twoB24_2,
			threeR24_2, threeG24_2, threeB24_2, fourR24_2, fourG24_2,
			fourB24_2, fiveR24_2, fiveG24_2, fiveB24_2;
	String oneR34_2, oneG34_2, oneB34_2, twoR34_2, twoG34_2, twoB34_2,
			threeR34_2, threeG34_2, threeB34_2, fourR34_2, fourG34_2,
			fourB34_2, fiveR34_2, fiveG34_2, fiveB34_2;
	String oneR44_2, oneG44_2, oneB44_2, twoR44_2, twoG44_2, twoB44_2,
			threeR44_2, threeG44_2, threeB44_2, fourR44_2, fourG44_2,
			fourB44_2, fiveR44_2, fiveG44_2, fiveB44_2;
	String oneR54_2, oneG54_2, oneB54_2, twoR54_2, twoG54_2, twoB54_2,
			threeR54_2, threeG54_2, threeB54_2, fourR54_2, fourG54_2,
			fourB54_2, fiveR54_2, fiveG54_2, fiveB54_2;
	String oneR64_2, oneG64_2, oneB64_2, twoR64_2, twoG64_2, twoB64_2,
			threeR64_2, threeG64_2, threeB64_2, fourR64_2, fourG64_2,
			fourB64_2, fiveR64_2, fiveG64_2, fiveB64_2;
	String oneR74_2, oneG74_2, oneB74_2, twoR74_2, twoG74_2, twoB74_2,
			threeR74_2, threeG74_2, threeB74_2, fourR74_2, fourG74_2,
			fourB74_2, fiveR74_2, fiveG74_2, fiveB74_2;
	int mode1_2, mode2_2, mode3_2, mode4_2, mode5_2, mode6_2, mode7_2;
	String rotationTime1, rotationTime2, rotationTime3, rotationTime4,
			rotationTime5, rotationTime6, rotationTime7;

	/**
	 * 单色灯和色温灯的计划参数
	 */
	int minNum = 0;
	String progress111_0 = "0";
	String progress112_0 = "0";
	String progress121_0 = "0";
	String progress122_0 = "0";
	String progress131_0 = "0";
	String progress132_0 = "0";
	String progress141_0 = "0";
	String progress142_0 = "0";

	String progress211_0 = "0";
	String progress212_0 = "0";
	String progress221_0 = "0";
	String progress222_0 = "0";
	String progress231_0 = "0";
	String progress232_0 = "0";
	String progress241_0 = "0";
	String progress242_0 = "0";

	String progress311_0 = "0";
	String progress312_0 = "0";
	String progress321_0 = "0";
	String progress322_0 = "0";
	String progress331_0 = "0";
	String progress332_0 = "0";
	String progress341_0 = "0";
	String progress342_0 = "0";

	String progress411_0 = "0";
	String progress412_0 = "0";
	String progress421_0 = "0";
	String progress422_0 = "0";
	String progress431_0 = "0";
	String progress432_0 = "0";
	String progress441_0 = "0";
	String progress442_0 = "0";

	String progress511_0 = "0";
	String progress512_0 = "0";
	String progress521_0 = "0";
	String progress522_0 = "0";
	String progress531_0 = "0";
	String progress532_0 = "0";
	String progress541_0 = "0";
	String progress542_0 = "0";

	String progress611_0 = "0";
	String progress612_0 = "0";
	String progress621_0 = "0";
	String progress622_0 = "0";
	String progress631_0 = "0";
	String progress632_0 = "0";
	String progress641_0 = "0";
	String progress642_0 = "0";

	String progress711_0 = "0";
	String progress712_0 = "0";
	String progress721_0 = "0";
	String progress722_0 = "0";
	String progress731_0 = "0";
	String progress732_0 = "0";
	String progress741_0 = "0";
	String progress742_0 = "0";

	String begintime11_0, begintime12_0, begintime13_0, begintime14_0,
			begintime21_0, begintime22_0, begintime23_0, begintime24_0,
			begintime31_0, begintime32_0, begintime33_0, begintime34_0,
			begintime41_0, begintime42_0, begintime43_0, begintime44_0,
			begintime51_0, begintime52_0, begintime53_0, begintime54_0,
			begintime61_0, begintime62_0, begintime63_0, begintime64_0,
			begintime71_0, begintime72_0, begintime73_0, begintime74_0;
	String endtime11_0, endtime12_0, endtime13_0, endtime14_0, endtime21_0,
			endtime22_0, endtime23_0, endtime24_0, endtime31_0, endtime32_0,
			endtime33_0, endtime34_0, endtime41_0, endtime42_0, endtime43_0,
			endtime44_0, endtime51_0, endtime52_0, endtime53_0, endtime54_0,
			endtime61_0, endtime62_0, endtime63_0, endtime64_0, endtime71_0,
			endtime72_0, endtime73_0, endtime74_0;

	Map<String, Integer> mapNum;
	List<Map<String, Integer>> listmap;
	int x = 0;

	String message1_1, message1_2, message1_3, message1_4, message2_1,
			message2_2, message2_3, message2_4, message3_1, message3_2,
			message3_3, message3_4, message4_1, message4_2, message4_3,
			message4_4, message5_1, message5_2, message5_3, message5_4,
			message6_1, message6_2, message6_3, message6_4, message7_1,
			message7_2, message7_3, message7_4;
	String me_1, me_2, me_3, me_4, me_5, me_6, me_7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_group_plan);
		acDevices = new ArrayList<ACUserDevice>();
		Log.e(TAG, "onCreate");
		type = getIntent().getIntExtra("model", 0);
		groupName = getIntent().getStringExtra("group_name");
		acDevices = (List<ACUserDevice>) getIntent().getSerializableExtra(
				"device");
		subDomain = PreferencesUtils.getString(this, "subDomain",
				Config.SUBDOMAIN);
		maps = new HashMap<String, Receiver<TopicData>>();
		mapNum = new HashMap<String, Integer>();
		listmap = new ArrayList<Map<String, Integer>>();
		for (ACUserDevice device : acDevices) {
			mapNum.put(device.getPhysicalDeviceId(), x);
			listmap.add(mapNum);
		}
		lists = new ArrayList<String>();
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
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
				final String respondId = arg0.getKey();
				String jsonData = arg0.getValue();
				OnReceive onRece = GsonUtil.parseJsonWithGson(jsonData,
						OnReceive.class);
				String[] pay = onRece.getPayload();
				byte[] arraysPay = Base64.decode(pay[0], 0);
				String payload = ItonAdecimalConver.byte2hex(arraysPay)
						.replace(" ", "");
				Log.e(TAG, respondId + "接收到的返回值:" + payload);
				parse(respondId, payload);
				// String[] payloads = payload.split("9966");
				// for(int i=1;i<payloads.length-1;i++){
				// payloads[0]=payloads[0]+"99";
				// payloads[i]="66"+payloads[i]+"99";
				// payloads[payloads.length-1] =
				// "66"+payloads[payloads.length-1];
				// }
				// for(String str:payloads){
				// parse(respondId,payload);
				// }

			}

		};
		maps.put(deviceId, receiver);
		AC.customDataMgr().registerDataReceiver(receiver);
	}

	protected void parse(final String respondId, String payload) {
		// 解析04 //66 04 00 0002 030199

		if (payload.length() > 10 && payload.substring(2, 6).equals("0400")) { // 命令类型为04，状态为00

			timeReclen = 0;
			new Thread(new Runnable() {

				@Override
				public void run() {
					for (ACUserDevice device : acDevices) {
						if ((device.getDeviceId() + "").equals(respondId)) {
							for (int i = 0; i < listmap.size(); i++) {
								if (listmap.get(i).containsKey(
										device.getPhysicalDeviceId())) {
									int xx = listmap.get(i).get(
											device.getPhysicalDeviceId());
									xx++;
									listmap.get(i).put(
											device.getPhysicalDeviceId(), xx);

									if (type == 2) {
										sendRbg(device, xx);
									} else if (!(type == 2)) {
										sendCommon(device, xx);
									}
								}
							}
						}
					}

				}
			}).start();

		}

	}

	protected void sendCommon(ACUserDevice device, int xx) {
		senddata = new SendData(subDomain, device.getPhysicalDeviceId());
		switch (xx) {
		case 1:
			senddata.sendData(message1_2);
			break;
		case 2:
			senddata.sendData(message1_3);
			break;
		case 3:
			senddata.sendData(message1_4);
			break;
		case 4:
			senddata.sendData(message2_1);
			break;
		case 5:
			senddata.sendData(message2_2);
			break;
		case 6:
			senddata.sendData(message2_3);
			break;
		case 7:
			senddata.sendData(message2_4);
			break;
		case 8:
			senddata.sendData(message3_1);
			break;
		case 9:
			senddata.sendData(message3_2);
			break;
		case 10:
			senddata.sendData(message3_3);
			break;
		case 11:
			senddata.sendData(message3_4);
			break;
		case 12:
			senddata.sendData(message4_1);
			break;
		case 13:
			senddata.sendData(message4_2);
			break;
		case 14:
			senddata.sendData(message4_3);
			break;
		case 15:
			senddata.sendData(message4_4);
			break;
		case 16:
			senddata.sendData(message5_1);
			break;
		case 17:
			senddata.sendData(message5_2);
			break;
		case 18:
			senddata.sendData(message5_3);
			break;
		case 19:
			senddata.sendData(message5_4);
			break;
		case 20:
			senddata.sendData(message6_1);
			break;
		case 21:
			senddata.sendData(message6_2);
			break;
		case 22:
			senddata.sendData(message6_3);
			break;
		case 23:
			senddata.sendData(message6_4);
			break;
		case 24:
			senddata.sendData(message7_1);
			break;
		case 25:
			senddata.sendData(message7_2);
			break;
		case 26:
			senddata.sendData(message7_3);
			break;
		case 27:
			senddata.sendData(message7_4);
			break;
		case 28:
			Message message = handler.obtainMessage();
			message.what = 1;
			Bundle bundle = new Bundle();
			bundle.putString("physical", device.getPhysicalDeviceId());
			message.setData(bundle);
			handler.sendMessage(message);
			break;
		}

	}

	protected void sendRbg(ACUserDevice device, int xx) {
		senddata = new SendData(subDomain, device.getPhysicalDeviceId());
		switch (xx) {
		case 1:
			senddata.sendData(me_2);
			break;
		case 2:
			senddata.sendData(me_3);
			break;
		case 3:
			senddata.sendData(me_4);
			break;
		case 4:
			senddata.sendData(me_5);
			break;
		case 5:
			senddata.sendData(me_6);
			break;
		case 6:
			senddata.sendData(me_7);
			break;
		case 7:
			Message message = handler.obtainMessage();
			message.what = 1;
			Bundle bundle = new Bundle();
			bundle.putString("physical", device.getPhysicalDeviceId());
			message.setData(bundle);
			handler.sendMessage(message);
			break;
		}

	}

	private void initView() {
		groupDao = new GroupNDaoImpl(GroupPlanActivity.this);
		groupPlanNames = new ArrayList<String>();
		Log.e(TAG, "groupPlanNames的长度1：" + groupPlanNames.size());
		Log.e(TAG, "GROUPdAO:" + groupDao.query(null, null).size());
		for (GroupN groupN : groupDao.query(null, null)) {
			if (groupN.getType() == type
					&& groupN.getGroupName().equals(groupName)) {
				if (!groupN.getGroupPlanName().equals("")) {
					groupPlanNames.add(groupN.getGroupPlanName());
					Log.e(TAG, "groupPlanNames的长度2：" + groupPlanNames.size());
				}
			}
		}

		Log.e(TAG, "groupPlanNames的长度:" + groupPlanNames.size());
		for (String str : groupPlanNames) {
			Log.e(TAG, "groupPlanNames的内容:" + str);
		}
		adapter = new MyAdapter();
		back = (Button) findViewById(R.id.btn_group_plan_back);
		addGroupPlan = (Button) findViewById(R.id.add_group_plan);
		groupPlanName = (TextView) findViewById(R.id.tv_group_name);
		groupPlanName.setText(groupName);
		groupPlanNum = (TextView) findViewById(R.id.tv_group_plan_num);
		groupPlanNum.setText(groupPlanNames.size() + "");
		back.setOnClickListener(listener);
		addGroupPlan.setOnClickListener(listener);
		groupPlanListView = (ListView) findViewById(R.id.listview_group_plan);

		groupPlanListView.setAdapter(adapter);
		groupPlanListView.setOnItemClickListener(itemListener);
		groupPlanListView.setOnItemLongClickListener(itemLongListener);
	}

	OnItemClickListener itemListener = new OnItemClickListener() { // 短按

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent();
			if (type == 2) {
				intent.setClass(GroupPlanActivity.this,
						PlanListRGBActivity.class);
				intent.putExtra("TYPE", type);
				intent.putExtra("planName", groupPlanNames.get(position));
				intent.putExtra("style", 1);
				intent.putExtra("physical", "");
				intent.putExtra("groupName", groupName);
				startActivityForResult(intent, 5552);
			} else if (type == 1) {
				intent.setClass(GroupPlanActivity.this, PlanListActivity.class);
				intent.putExtra("TYPE", type);
				intent.putExtra("planName", groupPlanNames.get(position));
				intent.putExtra("style", 1);
				intent.putExtra("physical", "");
				intent.putExtra("groupName", groupName);
				startActivityForResult(intent, 5552);
			} else if (type == 0) {
				intent.setClass(GroupPlanActivity.this, PlanListActivity.class);
				intent.putExtra("TYPE", type);
				intent.putExtra("planName", groupPlanNames.get(position));
				intent.putExtra("style", 1);
				intent.putExtra("physical", "");
				intent.putExtra("groupName", groupName);
				startActivityForResult(intent, 5552);
			}

		}
	};

	OnItemLongClickListener itemLongListener = new OnItemLongClickListener() { // 长按

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			showItemLongClick(position);
			return true;
		}
	};

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_group_plan_back:
				GroupPlanActivity.this.finish();
				break;
			case R.id.add_group_plan: // 添加组计划按钮
				Intent intent = new Intent();
				if (type == 2) {
					intent.setClass(GroupPlanActivity.this,
							SingleColorAttributeEdit2Activity.class);
					intent.putExtra("TYPE", type);
					intent.putExtra("style", 0);
					intent.putStringArrayListExtra("plans",
							(ArrayList<String>) groupPlanNames);
					intent.putExtra("physical", "");
					intent.putExtra("groupName", groupName);
					startActivityForResult(intent, 5551);
				} else if (type == 1) {
					intent.setClass(GroupPlanActivity.this,
							SingleColorAttributeEditActivity.class);
					intent.putExtra("TYPE", type);
					intent.putExtra("style", 0);
					intent.putStringArrayListExtra("plans",
							(ArrayList<String>) groupPlanNames);
					intent.putExtra("physical", "");
					intent.putExtra("groupName", groupName);
					startActivityForResult(intent, 5551);
				} else if (type == 0) {
					intent.setClass(GroupPlanActivity.this,
							SingleColorAttributeEditActivity.class);
					intent.putExtra("TYPE", type);
					intent.putExtra("style", 0);
					intent.putStringArrayListExtra("plans",
							(ArrayList<String>) groupPlanNames);
					intent.putExtra("physical", "");
					intent.putExtra("groupName", groupName);
					startActivityForResult(intent, 5551);
				}

				break;
			}

		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 5551:
			if (resultCode == 2111) {// 表示从SingleColorAttributeEditActivity中返回
				String newGroupPlanName = data.getStringExtra("planName");
				switch (type) {
				case 0:
					groupPlanNames.add(newGroupPlanName);
					groupPlanNum.setText(groupPlanNames.size() + "");
					adapter.notifyDataSetChanged();
					groupDao.insert(new GroupN(type, groupName,
							newGroupPlanName));
					break;

				case 1:
					groupPlanNames.add(newGroupPlanName);
					groupPlanNum.setText(groupPlanNames.size() + "");
					adapter.notifyDataSetChanged();
					groupDao.insert(new GroupN(type, groupName,
							newGroupPlanName));
					break;

				}

			} else if (resultCode == 21113) { // 表示从SingleColorAttributeEdit2Activity中返回

				String newGroupPlanName2 = data.getStringExtra("planName");

				groupPlanNames.add(newGroupPlanName2);
				groupPlanNum.setText(groupPlanNames.size() + "");
				adapter.notifyDataSetChanged();
				groupDao.insert(new GroupN(type, groupName, newGroupPlanName2));
			}
			break;

		case 5552:
			break;
		}
	}

	ProgressDialog dialog;
	boolean isRun = false;
	int timeReclen = 0;

	AlertDialog alertdialog;
	AlertDialog.Builder customDia;

	/* 对话框，用于对计划发送结果展现 */
	private void showCustomDia(String message) {
		if (customDia == null) {
			customDia = new AlertDialog.Builder(GroupPlanActivity.this);
		}
		final View viewDia = LayoutInflater.from(GroupPlanActivity.this)
				.inflate(R.layout.issue_plan_alertdialog, null);
		TextView plan_result = (TextView) viewDia
				.findViewById(R.id.tv_plan_suss_fail);
		plan_result.setText(message);
		customDia.setView(viewDia);
		customDia.setPositiveButton(getResources().getString(R.string.confirm),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		alertdialog = customDia.create();
		alertdialog.show();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 1:

				Log.e(TAG, "handler:" + msg.getData().getString("physical"));
				lists.add(msg.getData().getString("physical"));
				Log.e(TAG, "lists的长度" + lists.size());
				if (lists.size() == listmap.size()) {
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
					isRun = false;
					timeReclen = 0;
					showCustomDia(getResources().getString(R.string.success));
					lists.clear();
				}
				// for (ACUserDevice de : acDevices) {
				// if (listmap.size() == 1
				// && listmap.get(0).containsKey(
				// de.getPhysicalDeviceId())) {
				// if (type == 2) {
				// if (listmap.get(0).get(de.getPhysicalDeviceId()) == 7) {
				// if (dialog.isShowing()) {
				// dialog.dismiss();
				// }
				// isRun = false;
				// timeReclen = 0;
				// // if(alertdialog!=null && alertdialog.isShowing()){
				// // alertdialog.dismiss();
				// //
				// // }
				// showCustomDia("\""+de.getName()+"\""+getResources().getString(
				// R.string.success));
				// // senddata.sendData("6605000099");
				// }
				// } else {
				// if (listmap.get(0).get(de.getPhysicalDeviceId()) == 28) {
				// if (dialog.isShowing()) {
				// dialog.dismiss();
				// }
				// isRun = false;
				// timeReclen = 0;
				// // if(alertdialog!=null && alertdialog.isShowing()){
				// // alertdialog.dismiss();
				// // }
				// showCustomDia("\""+de.getName()+"\""+getResources().getString(
				// R.string.success));
				// // senddata.sendData("6605000099");
				// }
				// }
				// } else if (listmap.size() > 1) {
				// for (int i = 1; i < listmap.size(); i++) {
				// if(type==2){
				// if(listmap.get(0).get(de.getPhysicalDeviceId())==7
				// && listmap.get(i).get(de.getPhysicalDeviceId())==
				// listmap.get(i).get(de.getPhysicalDeviceId())){
				// Log.e(TAG, "listmap长度大于1，且值都为7："+
				// listmap.get(i).get(de.getPhysicalDeviceId())+"/"
				// +listmap.get(i).get(de.getPhysicalDeviceId())+"/"
				//
				// );
				// if (dialog.isShowing()) {
				// dialog.dismiss();
				// }
				// isRun = false;
				// timeReclen = 0;
				// // if(alertdialog!=null && alertdialog.isShowing()){
				// // alertdialog.dismiss();
				// // }
				// showCustomDia("\""+de.getName()+"\""+getResources().getString(
				// R.string.success));
				//
				// }
				// }else {
				// if(listmap.get(0).get(de.getPhysicalDeviceId())==28
				// && listmap.get(i).get(de.getPhysicalDeviceId())==
				// listmap.get(i-1).get(de.getPhysicalDeviceId())){
				// Log.e(TAG, "listmap长度大于1，且值都为28："+
				// listmap.get(i).get(de.getPhysicalDeviceId())+"/"
				// +listmap.get(i).get(de.getPhysicalDeviceId())+"/"
				//
				// );
				// if (dialog.isShowing()) {
				// dialog.dismiss();
				// }
				// isRun = false;
				// timeReclen = 0;
				// // if(alertdialog!=null && alertdialog.isShowing()){
				// // alertdialog.dismiss();
				// // }
				// showCustomDia("\""+de.getName()+"\""+getResources().getString(
				// R.string.success));
				// }
				// }
				// }
				// }
				//
				// }
				// // if (dialog.isShowing()) {
				// // dialog.dismiss();
				// // }
				// // isRun =false;
				// // timeReclen=0;
				// // showCustomDia(getResources().getString(R.string.success));
				break;
			case 2:
				timeReclen++;
				Log.e(TAG, "timeReclen：" + timeReclen);
				if (timeReclen == 10) {
					isRun = false;
					timeReclen = 0;
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
					// if(alertdialog!=null && alertdialog.isShowing()){
					// alertdialog.dismiss();
					showCustomDia(getResources().getString(R.string.failure));
					lists.clear();
					// }
				}
				break;

			}
		}

	};

	protected void showItemLongClick(final int position) {
		new ActionSheetDialog(GroupPlanActivity.this)
				.builder()
				.setCancelable(false)
				.setCanceledOnTouchOutside(false)
				.addSheetItem(
						getResources().getString(R.string.delete_project), // 删除计划
						SheetItemColor.Blue, new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {

								// 0.删除GroupN类总的数据库计划名称相同的条目sql5
								GroupNDaoImpl groupDao = new GroupNDaoImpl(
										GroupPlanActivity.this);
								for (GroupN groupN : groupDao.query(null, null)) {
									if (groupN.getGroupPlanName().equals(
											groupPlanNames.get(position))) {
										groupDao.delete(groupN.getId());
									}
								}

								// 1.删除sql6
								PlanNameDaoImpl planDao = new PlanNameDaoImpl(
										GroupPlanActivity.this);
								for (PlanName planName : planDao.query(null,
										null)) {
									if (planName.getType() == type
											&& planName.equals(groupPlanNames
													.get(position))
											&& planName.getPhysical()
													.equals("")) {
										planDao.delete(planName.getId());
									}
								}

								// 2.删除Project类的sql数据库中计划名称相同的计划sql3
								ProjectDaoImpl dao = new ProjectDaoImpl(
										GroupPlanActivity.this);
								List<Project> pros = dao.query(null, null);
								if (pros.size() > 0) {
									for (Project pro : pros) {
										if (type == 0) {
											if (pro.getName().equals(
													groupPlanNames
															.get(position))
													&& pro.getGroupName()
															.equals(groupName)) {
												dao.delete(pro.getId());
											}
										} else if (type == 1) {
											if (pro.getName().equals(
													groupPlanNames
															.get(position))
													&& pro.getGroupName()
															.equals(groupName)) {
												dao.delete(pro.getId());
											}
										}
									}
								}
								// 3.删除ProjectRGB类的sql数据库计划名称相同的计划sql4
								if (type == 2) {
									ProjectRGBDaoImpl rgbDao = new ProjectRGBDaoImpl(
											GroupPlanActivity.this);
									List<ProjectRGB> proRgbs = rgbDao.query(
											null, null);
									if (proRgbs.size() > 0) {
										for (ProjectRGB proRgb : proRgbs) {
											if (proRgb.getName().equals(
													groupPlanNames
															.get(position))
													&& proRgb.getGroupName()
															.equals(groupName)) {
												rgbDao.delete(proRgb.getId());
											}
										}
									}
								}

								// 4.移除groupPlanNames集合中的该项
								switch (type) {
								case 0:
									groupPlanNames.remove(position);
									groupPlanNum.setText(groupPlanNames.size()
											+ "");
									break;

								case 1:
									groupPlanNames.remove(position);
									groupPlanNum.setText(groupPlanNames.size()
											+ "");
									break;
								case 2:
									groupPlanNames.remove(position);
									groupPlanNum.setText(groupPlanNames.size()
											+ "");
									break;
								}

								adapter.notifyDataSetChanged();
							}
						})
				.addSheetItem(
						getResources().getString(R.string.issued_project), // 下发计划
						SheetItemColor.Blue, new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								dialog = new ProgressDialog(
										GroupPlanActivity.this);
								dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
								dialog.setCancelable(false);// 设置是否可以通过点击Back键取消
								dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
								dialog.setMessage("\""
										+ groupPlanNames.get(position)
										+ "\""
										+ "\t"
										+ getResources().getString(
												R.string.plan_sending));
								dialog.show();
								isRun = true;
								new Thread(new MyThread()).start();
								new Thread(new Runnable() {

									@Override
									public void run() {
										for (ACUserDevice device : acDevices) {
											senddata = new SendData(
													subDomain,
													device.getPhysicalDeviceId());
											for (int i = 0; i < listmap.size(); i++) {
												listmap.get(i)
														.put(device
																.getPhysicalDeviceId(),
																x);
											}

											if (type == 2) {// 彩色灯
												sendRGBMes(device,
														groupPlanNames
																.get(position));

												senddata.sendData(me_1);
											} else {// 色温灯，色温灯
												sendCommonMes(device,
														groupPlanNames
																.get(position));
												senddata.sendData(message1_1);
											}

										}

									}
								}).start();

							}
						}).show();

	}

	public class MyThread implements Runnable { // thread计时线程
		@Override
		public void run() {
			while (isRun) {
				try {
					Thread.sleep(1000); // sleep 1000ms
					Message message = new Message();
					message.what = 2;
					handler.sendMessage(message);
				} catch (Exception e) {

				}
			}
		}
	}

	protected void sendCommonMes(ACUserDevice device, String name) {
		ProjectDaoImpl commonDao = new ProjectDaoImpl(GroupPlanActivity.this);
		List<Project> pros = commonDao.query(null, null);
		List<Project> projects = new ArrayList<Project>();
		for (Project pro : pros) {
			if (pro.getName().equals(name)
					&& pro.getGroupName().equals(groupName)
					&& pro.getPhysical().equals("")) {
				projects.add(pro);
			}
		}

		if (type == 0) {
			minNum = 0;
		} else if (type == 1) {
			minNum = 2700;
		}

		for (int i = 0; i < projects.size(); i++) {
			begintime11_0 = projects.get(6).getBegintime1();
			endtime11_0 = projects.get(6).getEndtime1();
			progress111_0 = Integer.parseInt(projects.get(6).getSeek11())
					+ minNum + "";
			progress112_0 = projects.get(6).getSeek12();
			begintime12_0 = projects.get(6).getBegintime2();
			endtime12_0 = projects.get(6).getEndtime2();
			progress121_0 = Integer.parseInt(projects.get(6).getSeek21())
					+ minNum + "";
			progress122_0 = projects.get(6).getSeek22();
			begintime13_0 = projects.get(6).getBegintime3();
			endtime13_0 = projects.get(6).getEndtime3();
			progress131_0 = Integer.parseInt(projects.get(6).getSeek31())
					+ minNum + "";
			progress132_0 = projects.get(6).getSeek32();
			begintime14_0 = projects.get(6).getBegintime4();
			endtime14_0 = projects.get(6).getEndtime4();
			progress141_0 = Integer.parseInt(projects.get(6).getSeek41())
					+ minNum + "";
			progress142_0 = projects.get(6).getSeek42();

			begintime21_0 = projects.get(5).getBegintime1();
			endtime21_0 = projects.get(5).getEndtime1();
			progress211_0 = Integer.parseInt(projects.get(5).getSeek11())
					+ minNum + "";
			progress212_0 = projects.get(5).getSeek12();
			begintime22_0 = projects.get(5).getBegintime2();
			endtime22_0 = projects.get(5).getEndtime2();
			progress221_0 = Integer.parseInt(projects.get(5).getSeek21())
					+ minNum + "";
			progress222_0 = projects.get(5).getSeek22();
			begintime23_0 = projects.get(5).getBegintime3();
			endtime23_0 = projects.get(5).getEndtime3();
			progress231_0 = Integer.parseInt(projects.get(5).getSeek31())
					+ minNum + "";
			progress232_0 = projects.get(5).getSeek32();
			begintime24_0 = projects.get(5).getBegintime4();
			endtime24_0 = projects.get(5).getEndtime4();
			progress241_0 = Integer.parseInt(projects.get(5).getSeek41())
					+ minNum + "";
			progress242_0 = projects.get(5).getSeek42();

			begintime31_0 = projects.get(4).getBegintime1();
			endtime31_0 = projects.get(4).getEndtime1();
			progress311_0 = Integer.parseInt(projects.get(4).getSeek11())
					+ minNum + "";
			progress312_0 = projects.get(4).getSeek12();
			begintime32_0 = projects.get(4).getBegintime2();
			endtime32_0 = projects.get(4).getEndtime2();
			progress321_0 = Integer.parseInt(projects.get(4).getSeek21())
					+ minNum + "";
			progress322_0 = projects.get(4).getSeek22();
			begintime33_0 = projects.get(4).getBegintime3();
			endtime33_0 = projects.get(4).getEndtime3();
			progress331_0 = Integer.parseInt(projects.get(4).getSeek31())
					+ minNum + "";
			progress332_0 = projects.get(4).getSeek32();
			begintime34_0 = projects.get(4).getBegintime4();
			endtime34_0 = projects.get(4).getEndtime4();
			progress341_0 = Integer.parseInt(projects.get(4).getSeek41())
					+ minNum + "";
			progress342_0 = projects.get(4).getSeek42();

			begintime41_0 = projects.get(3).getBegintime1();
			endtime41_0 = projects.get(3).getEndtime1();
			progress411_0 = Integer.parseInt(projects.get(3).getSeek11())
					+ minNum + "";
			progress412_0 = projects.get(3).getSeek12();
			begintime42_0 = projects.get(3).getBegintime2();
			endtime42_0 = projects.get(3).getEndtime2();
			progress421_0 = Integer.parseInt(projects.get(3).getSeek21())
					+ minNum + "";
			progress422_0 = projects.get(3).getSeek22();
			begintime43_0 = projects.get(3).getBegintime3();
			endtime43_0 = projects.get(3).getEndtime3();
			progress431_0 = Integer.parseInt(projects.get(3).getSeek31())
					+ minNum + "";
			progress432_0 = projects.get(3).getSeek32();
			begintime44_0 = projects.get(3).getBegintime4();
			endtime44_0 = projects.get(3).getEndtime4();
			progress441_0 = Integer.parseInt(projects.get(3).getSeek41())
					+ minNum + "";
			progress442_0 = projects.get(3).getSeek42();

			begintime51_0 = projects.get(2).getBegintime1();
			endtime51_0 = projects.get(2).getEndtime1();
			progress511_0 = Integer.parseInt(projects.get(2).getSeek11())
					+ minNum + "";
			progress512_0 = projects.get(2).getSeek12();
			begintime52_0 = projects.get(2).getBegintime2();
			endtime52_0 = projects.get(2).getEndtime2();
			progress521_0 = Integer.parseInt(projects.get(2).getSeek21())
					+ minNum + "";
			progress522_0 = projects.get(2).getSeek22();
			begintime53_0 = projects.get(2).getBegintime3();
			endtime53_0 = projects.get(2).getEndtime3();
			progress531_0 = Integer.parseInt(projects.get(2).getSeek31())
					+ minNum + "";
			progress532_0 = projects.get(2).getSeek32();
			begintime54_0 = projects.get(2).getBegintime4();
			endtime54_0 = projects.get(2).getEndtime4();
			progress541_0 = Integer.parseInt(projects.get(2).getSeek41())
					+ minNum + "";
			progress542_0 = projects.get(2).getSeek42();

			begintime61_0 = projects.get(1).getBegintime1();
			endtime61_0 = projects.get(1).getEndtime1();
			progress611_0 = Integer.parseInt(projects.get(1).getSeek11())
					+ minNum + "";
			progress612_0 = projects.get(1).getSeek12();
			begintime62_0 = projects.get(1).getBegintime2();
			endtime62_0 = projects.get(1).getEndtime2();
			progress621_0 = Integer.parseInt(projects.get(1).getSeek21())
					+ minNum + "";
			progress622_0 = projects.get(1).getSeek22();
			begintime63_0 = projects.get(1).getBegintime3();
			endtime63_0 = projects.get(1).getEndtime3();
			progress631_0 = Integer.parseInt(projects.get(1).getSeek31())
					+ minNum + "";
			progress632_0 = projects.get(1).getSeek32();
			begintime64_0 = projects.get(1).getBegintime4();
			endtime64_0 = projects.get(1).getEndtime4();
			progress641_0 = Integer.parseInt(projects.get(1).getSeek41())
					+ minNum + "";
			progress642_0 = projects.get(1).getSeek42();

			begintime71_0 = projects.get(0).getBegintime1();
			endtime71_0 = projects.get(0).getEndtime1();
			progress711_0 = Integer.parseInt(projects.get(0).getSeek11())
					+ minNum + "";
			progress712_0 = projects.get(0).getSeek12();
			begintime72_0 = projects.get(0).getBegintime2();
			endtime72_0 = projects.get(0).getEndtime2();
			progress721_0 = Integer.parseInt(projects.get(0).getSeek21())
					+ minNum + "";
			progress722_0 = projects.get(0).getSeek22();
			begintime73_0 = projects.get(0).getBegintime3();
			endtime73_0 = projects.get(0).getEndtime3();
			progress731_0 = Integer.parseInt(projects.get(0).getSeek31())
					+ minNum + "";
			progress732_0 = projects.get(0).getSeek32();
			begintime74_0 = projects.get(0).getBegintime4();
			endtime74_0 = projects.get(0).getEndtime4();
			progress741_0 = Integer.parseInt(projects.get(0).getSeek41())
					+ minNum + "";
			progress742_0 = projects.get(0).getSeek42();
		}

		String strType = ItonAdecimalConver.algorismToHEXString(type + 1, 2);

		message1_1 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0211"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime11_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime11_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime11_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime11_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress111_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress112_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0211"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime11_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime11_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime11_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime11_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress111_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress112_0), 2) + "99";
		message1_2 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0212"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime12_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime12_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime12_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime12_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress121_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress122_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0212"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime12_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime12_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime12_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime12_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress121_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress122_0), 2) + "99";
		message1_3 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0213"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime13_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime13_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime13_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime13_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress131_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress132_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0213"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime13_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime13_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime13_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime13_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress131_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress132_0), 2) + "99";
		message1_4 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0214"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime14_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime14_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime14_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime14_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress141_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress142_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0214"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime14_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime14_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime14_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime14_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress141_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress142_0), 2) + "99";
		message2_1 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0221"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime21_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime21_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime21_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime21_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress211_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress212_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0221"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime21_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime21_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime21_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime21_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress211_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress212_0), 2) + "99";
		message2_2 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0222"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime22_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime22_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime22_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime22_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress221_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress222_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0222"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime22_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime22_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime22_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime22_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress221_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress222_0), 2) + "99";
		message2_3 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0223"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime23_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime23_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime23_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime23_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress231_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress232_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0223"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime23_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime23_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime23_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime23_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress231_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress232_0), 2) + "99";
		message2_4 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0224"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime24_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime24_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime24_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime24_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress241_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress242_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0224"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime24_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime24_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime24_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime24_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress241_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress242_0), 2) + "99";
		message3_1 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0231"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime31_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime31_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime31_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime31_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress311_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress312_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0231"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime31_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime31_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime31_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime31_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress311_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress312_0), 2) + "99";
		message3_2 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0232"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime32_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime32_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime32_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime32_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress321_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress322_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0232"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime32_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime32_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime32_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime32_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress321_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress322_0), 2) + "99";
		message3_3 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0233"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime33_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime33_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime33_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime33_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress331_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress332_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0233"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime33_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime33_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime33_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime33_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress331_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress332_0), 2) + "99";
		message3_4 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0234"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime34_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime34_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime34_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime34_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress341_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress342_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0234"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime34_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime34_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime34_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime34_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress341_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress342_0), 2) + "99";
		message4_1 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0241"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime41_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime41_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime41_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime41_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress411_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress412_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0241"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime41_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime41_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime41_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime41_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress411_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress412_0), 2) + "99";
		message4_2 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0242"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime42_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime42_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime42_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime42_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress421_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress422_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0242"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime42_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime42_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime42_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime42_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress421_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress422_0), 2) + "99";
		message4_3 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0243"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime43_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime43_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime43_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime43_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress431_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress432_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0243"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime43_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime43_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime43_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime43_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress431_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress432_0), 2) + "99";
		message4_4 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0244"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime44_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime44_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime44_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime44_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress441_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress442_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0244"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime44_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime44_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime44_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime44_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress441_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress442_0), 2) + "99";
		message5_1 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0251"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime51_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime51_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime51_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime51_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress511_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress512_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0251"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime51_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime51_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime51_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime51_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress511_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress512_0), 2) + "99";
		message5_2 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0252"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime52_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime52_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime52_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime52_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress521_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress522_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0252"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime52_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime52_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime52_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime52_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress521_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress522_0), 2) + "99";
		message5_3 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0253"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime53_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime53_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime53_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime53_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress531_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress532_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0253"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime53_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime53_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime53_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime53_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress531_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress532_0), 2) + "99";
		message5_4 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0254"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime54_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime54_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime54_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime54_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress541_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress542_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0254"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime54_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime54_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime54_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime54_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress541_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress542_0), 2) + "99";
		message6_1 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0261"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime61_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime61_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime61_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime61_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress611_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress612_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0261"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime61_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime61_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime61_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime61_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress611_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress612_0), 2) + "99";
		message6_2 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0262"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime62_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime62_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime62_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime62_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress621_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress622_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0262"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime62_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime62_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime62_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime62_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress621_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress622_0), 2) + "99";
		message6_3 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0263"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime63_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime63_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime63_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime63_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress631_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress632_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0263"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime63_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime63_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime63_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime63_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress631_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress632_0), 2) + "99";
		message6_4 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0264"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime64_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime64_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime64_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime64_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress641_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress642_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0264"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime64_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime64_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime64_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime64_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress641_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress642_0), 2) + "99";
		message7_1 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0271"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime71_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime71_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime71_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime71_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress711_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress712_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0271"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime71_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime71_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime71_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime71_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress711_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress712_0), 2) + "99";
		message7_2 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0272"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime72_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime72_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime72_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime72_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress721_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress722_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0272"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime72_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime72_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime72_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime72_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress721_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress722_0), 2) + "99";
		message7_3 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0273"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime73_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime73_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime73_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime73_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress731_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress732_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0273"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime73_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime73_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime73_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime73_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress731_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress732_0), 2) + "99";
		message7_4 = "6604"
				+ ItonAdecimalConver
						.algorismToHEXString(
								(strType
										+ "0274"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(begintime74_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(begintime74_0
																.substring(3)),
														2)
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(endtime74_0
														.substring(0, 2)), 2)
										+ ItonAdecimalConver
												.algorismToHEXString(Integer
														.parseInt(endtime74_0
																.substring(3)),
														2)
										+ "00"
										+ ItonAdecimalConver.algorismToHEXString(
												Integer.parseInt(progress741_0),
												2) + ItonAdecimalConver.algorismToHEXString(
										Integer.parseInt(progress742_0), 2))
										.length() / 2, 4)
				+ strType
				+ "0274"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime74_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(begintime74_0.substring(3)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime74_0.substring(0, 2)), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(endtime74_0.substring(3)), 2)
				+ "00"
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress741_0), 2)
				+ ItonAdecimalConver.algorismToHEXString(
						Integer.parseInt(progress742_0), 2) + "99";

	}

	protected void sendRGBMes(ACUserDevice device, String name) {
		ProjectRGBDaoImpl rgbDao = new ProjectRGBDaoImpl(GroupPlanActivity.this);
		List<ProjectRGB> proRgbs = rgbDao.query(null, null);
		List<ProjectRGB> projectRgbs = new ArrayList<ProjectRGB>();
		for (ProjectRGB proRgb : proRgbs) {
			if (proRgb.getName().equals(name)
					&& proRgb.getGroupName().equals(groupName)
					&& proRgb.getPhysical().equals("")) {
				projectRgbs.add(proRgb);
			}
		}

		for (int i = 0; i < projectRgbs.size(); i++) {
			mode1_2 = projectRgbs.get(6).getMode();
			one14_2 = projectRgbs.get(6).getEtOne();
			two14_2 = projectRgbs.get(6).getEtTwo();
			three14_2 = projectRgbs.get(6).getEtThree();
			four14_2 = projectRgbs.get(6).getEtFour();
			five14_2 = projectRgbs.get(6).getEtFive();
			six14_2 = projectRgbs.get(6).getEtSix();
			seven14_2 = projectRgbs.get(6).getEtSeven();
			eight14_2 = projectRgbs.get(6).getEtEight();
			oneR14_2 = projectRgbs.get(6).getEtOneR();
			oneG14_2 = projectRgbs.get(6).getEtOneG();
			oneB14_2 = projectRgbs.get(6).getEtOneB();
			twoR14_2 = projectRgbs.get(6).getEtTwoR();
			twoG14_2 = projectRgbs.get(6).getEtTwoG();
			twoB14_2 = projectRgbs.get(6).getEtTwoB();
			threeR14_2 = projectRgbs.get(6).getEtThreeR();
			threeG14_2 = projectRgbs.get(6).getEtThreeG();
			threeB14_2 = projectRgbs.get(6).getEtThreeB();
			fourR14_2 = projectRgbs.get(6).getEtFourR();
			fourG14_2 = projectRgbs.get(6).getEtFourG();
			fourB14_2 = projectRgbs.get(6).getEtFourB();
			fiveR14_2 = projectRgbs.get(6).getEtFiveR();
			fiveG14_2 = projectRgbs.get(6).getEtFiveG();
			fiveB14_2 = projectRgbs.get(6).getEtFiveB();
			rotationTime1 = projectRgbs.get(6).getEtRotationTime();

			mode2_2 = projectRgbs.get(5).getMode();
			one24_2 = projectRgbs.get(5).getEtOne();
			two24_2 = projectRgbs.get(5).getEtTwo();
			three24_2 = projectRgbs.get(5).getEtThree();
			four24_2 = projectRgbs.get(5).getEtFour();
			five24_2 = projectRgbs.get(5).getEtFive();
			six24_2 = projectRgbs.get(5).getEtSix();
			seven24_2 = projectRgbs.get(5).getEtSeven();
			eight24_2 = projectRgbs.get(5).getEtEight();
			oneR24_2 = projectRgbs.get(5).getEtOneR();
			oneG24_2 = projectRgbs.get(5).getEtOneG();
			oneB24_2 = projectRgbs.get(5).getEtOneB();
			twoR24_2 = projectRgbs.get(5).getEtTwoR();
			twoG24_2 = projectRgbs.get(5).getEtTwoG();
			twoB24_2 = projectRgbs.get(5).getEtTwoB();
			threeR24_2 = projectRgbs.get(5).getEtThreeR();
			threeG24_2 = projectRgbs.get(5).getEtThreeG();
			threeB24_2 = projectRgbs.get(5).getEtThreeB();
			fourR24_2 = projectRgbs.get(5).getEtFourR();
			fourG24_2 = projectRgbs.get(5).getEtFourG();
			fourB24_2 = projectRgbs.get(5).getEtFourB();
			fiveR24_2 = projectRgbs.get(5).getEtFiveR();
			fiveG24_2 = projectRgbs.get(5).getEtFiveG();
			fiveB24_2 = projectRgbs.get(5).getEtFiveB();
			rotationTime2 = projectRgbs.get(5).getEtRotationTime();

			mode3_2 = projectRgbs.get(4).getMode();
			one34_2 = projectRgbs.get(4).getEtOne();
			two34_2 = projectRgbs.get(4).getEtTwo();
			three34_2 = projectRgbs.get(4).getEtThree();
			four34_2 = projectRgbs.get(4).getEtFour();
			five34_2 = projectRgbs.get(4).getEtFive();
			six34_2 = projectRgbs.get(4).getEtSix();
			seven34_2 = projectRgbs.get(4).getEtSeven();
			eight34_2 = projectRgbs.get(4).getEtEight();
			oneR34_2 = projectRgbs.get(4).getEtOneR();
			oneG34_2 = projectRgbs.get(4).getEtOneG();
			oneB34_2 = projectRgbs.get(4).getEtOneB();
			twoR34_2 = projectRgbs.get(4).getEtTwoR();
			twoG34_2 = projectRgbs.get(4).getEtTwoG();
			twoB34_2 = projectRgbs.get(4).getEtTwoB();
			threeR34_2 = projectRgbs.get(4).getEtThreeR();
			threeG34_2 = projectRgbs.get(4).getEtThreeG();
			threeB34_2 = projectRgbs.get(4).getEtThreeB();
			fourR34_2 = projectRgbs.get(4).getEtFourR();
			fourG34_2 = projectRgbs.get(4).getEtFourG();
			fourB34_2 = projectRgbs.get(4).getEtFourB();
			fiveR34_2 = projectRgbs.get(4).getEtFiveR();
			fiveG34_2 = projectRgbs.get(4).getEtFiveG();
			fiveB34_2 = projectRgbs.get(4).getEtFiveB();
			rotationTime3 = projectRgbs.get(4).getEtRotationTime();

			mode4_2 = projectRgbs.get(3).getMode();
			one44_2 = projectRgbs.get(3).getEtOne();
			two44_2 = projectRgbs.get(3).getEtTwo();
			three44_2 = projectRgbs.get(3).getEtThree();
			four44_2 = projectRgbs.get(3).getEtFour();
			five44_2 = projectRgbs.get(3).getEtFive();
			six44_2 = projectRgbs.get(3).getEtSix();
			seven44_2 = projectRgbs.get(3).getEtSeven();
			eight44_2 = projectRgbs.get(3).getEtEight();
			oneR44_2 = projectRgbs.get(3).getEtOneR();
			oneG44_2 = projectRgbs.get(3).getEtOneG();
			oneB44_2 = projectRgbs.get(3).getEtOneB();
			twoR44_2 = projectRgbs.get(3).getEtTwoR();
			twoG44_2 = projectRgbs.get(3).getEtTwoG();
			twoB44_2 = projectRgbs.get(3).getEtTwoB();
			threeR44_2 = projectRgbs.get(3).getEtThreeR();
			threeG44_2 = projectRgbs.get(3).getEtThreeG();
			threeB44_2 = projectRgbs.get(3).getEtThreeB();
			fourR44_2 = projectRgbs.get(3).getEtFourR();
			fourG44_2 = projectRgbs.get(3).getEtFourG();
			fourB44_2 = projectRgbs.get(3).getEtFourB();
			fiveR44_2 = projectRgbs.get(3).getEtFiveR();
			fiveG44_2 = projectRgbs.get(3).getEtFiveG();
			fiveB44_2 = projectRgbs.get(3).getEtFiveB();
			rotationTime4 = projectRgbs.get(3).getEtRotationTime();

			mode5_2 = projectRgbs.get(2).getMode();
			one54_2 = projectRgbs.get(2).getEtOne();
			two54_2 = projectRgbs.get(2).getEtTwo();
			three54_2 = projectRgbs.get(2).getEtThree();
			four54_2 = projectRgbs.get(2).getEtFour();
			five54_2 = projectRgbs.get(2).getEtFive();
			six54_2 = projectRgbs.get(2).getEtSix();
			seven54_2 = projectRgbs.get(2).getEtSeven();
			eight54_2 = projectRgbs.get(2).getEtEight();
			oneR54_2 = projectRgbs.get(2).getEtOneR();
			oneG54_2 = projectRgbs.get(2).getEtOneG();
			oneB54_2 = projectRgbs.get(2).getEtOneB();
			twoR54_2 = projectRgbs.get(2).getEtTwoR();
			twoG54_2 = projectRgbs.get(2).getEtTwoG();
			twoB54_2 = projectRgbs.get(2).getEtTwoB();
			threeR54_2 = projectRgbs.get(2).getEtThreeR();
			threeG54_2 = projectRgbs.get(2).getEtThreeG();
			threeB54_2 = projectRgbs.get(2).getEtThreeB();
			fourR54_2 = projectRgbs.get(2).getEtFourR();
			fourG54_2 = projectRgbs.get(2).getEtFourG();
			fourB54_2 = projectRgbs.get(2).getEtFourB();
			fiveR54_2 = projectRgbs.get(2).getEtFiveR();
			fiveG54_2 = projectRgbs.get(2).getEtFiveG();
			fiveB54_2 = projectRgbs.get(2).getEtFiveB();
			rotationTime5 = projectRgbs.get(2).getEtRotationTime();

			mode6_2 = projectRgbs.get(1).getMode();
			one64_2 = projectRgbs.get(1).getEtOne();
			two64_2 = projectRgbs.get(1).getEtTwo();
			three64_2 = projectRgbs.get(1).getEtThree();
			four64_2 = projectRgbs.get(1).getEtFour();
			five64_2 = projectRgbs.get(1).getEtFive();
			six64_2 = projectRgbs.get(1).getEtSix();
			seven64_2 = projectRgbs.get(1).getEtSeven();
			eight64_2 = projectRgbs.get(1).getEtEight();
			oneR64_2 = projectRgbs.get(1).getEtOneR();
			oneG64_2 = projectRgbs.get(1).getEtOneG();
			oneB64_2 = projectRgbs.get(1).getEtOneB();
			twoR64_2 = projectRgbs.get(1).getEtTwoR();
			twoG64_2 = projectRgbs.get(1).getEtTwoG();
			twoB64_2 = projectRgbs.get(1).getEtTwoB();
			threeR64_2 = projectRgbs.get(1).getEtThreeR();
			threeG64_2 = projectRgbs.get(1).getEtThreeG();
			threeB64_2 = projectRgbs.get(1).getEtThreeB();
			fourR64_2 = projectRgbs.get(1).getEtFourR();
			fourG64_2 = projectRgbs.get(1).getEtFourG();
			fourB64_2 = projectRgbs.get(1).getEtFourB();
			fiveR64_2 = projectRgbs.get(1).getEtFiveR();
			fiveG64_2 = projectRgbs.get(1).getEtFiveG();
			fiveB64_2 = projectRgbs.get(1).getEtFiveB();
			rotationTime6 = projectRgbs.get(1).getEtRotationTime();

			mode7_2 = projectRgbs.get(0).getMode();
			one74_2 = projectRgbs.get(0).getEtOne();
			two74_2 = projectRgbs.get(0).getEtTwo();
			three74_2 = projectRgbs.get(0).getEtThree();
			four74_2 = projectRgbs.get(0).getEtFour();
			five74_2 = projectRgbs.get(0).getEtFive();
			six74_2 = projectRgbs.get(0).getEtSix();
			seven74_2 = projectRgbs.get(0).getEtSeven();
			eight74_2 = projectRgbs.get(0).getEtEight();
			oneR74_2 = projectRgbs.get(0).getEtOneR();
			oneG74_2 = projectRgbs.get(0).getEtOneG();
			oneB74_2 = projectRgbs.get(0).getEtOneB();
			twoR74_2 = projectRgbs.get(0).getEtTwoR();
			twoG74_2 = projectRgbs.get(0).getEtTwoG();
			twoB74_2 = projectRgbs.get(0).getEtTwoB();
			threeR74_2 = projectRgbs.get(0).getEtThreeR();
			threeG74_2 = projectRgbs.get(0).getEtThreeG();
			threeB74_2 = projectRgbs.get(0).getEtThreeB();
			fourR74_2 = projectRgbs.get(0).getEtFourR();
			fourG74_2 = projectRgbs.get(0).getEtFourG();
			fourB74_2 = projectRgbs.get(0).getEtFourB();
			fiveR74_2 = projectRgbs.get(0).getEtFiveR();
			fiveG74_2 = projectRgbs.get(0).getEtFiveG();
			fiveB74_2 = projectRgbs.get(0).getEtFiveB();
			rotationTime7 = projectRgbs.get(0).getEtRotationTime();

			if (one14_2.equals("")) {
				one14_2 = "0";
			}
			if (two14_2.equals("")) {
				two14_2 = "0";
			}
			if (three14_2.equals("")) {
				three14_2 = "0";
			}
			if (four14_2.equals("")) {
				four14_2 = "0";
			}
			if (five14_2.equals("")) {
				five14_2 = "0";
			}
			if (six14_2.equals("")) {
				six14_2 = "0";
			}
			if (seven14_2.equals("")) {
				seven14_2 = "0";
			}
			if (eight14_2.equals("")) {
				eight14_2 = "0";
			}
			if (oneR14_2.equals("")) {
				oneR14_2 = "0";
			}
			if (oneG14_2.equals("")) {
				oneG14_2 = "0";
			}
			if (oneB14_2.equals("")) {
				oneB14_2 = "0";
			}
			if (twoR14_2.equals("")) {
				twoR14_2 = "0";
			}
			if (twoG14_2.equals("")) {
				twoG14_2 = "0";
			}
			if (twoB14_2.equals("")) {
				twoB14_2 = "0";
			}
			if (threeR14_2.equals("")) {
				threeR14_2 = "0";
			}
			if (threeG14_2.equals("")) {
				threeG14_2 = "0";
			}
			if (threeB14_2.equals("")) {
				threeB14_2 = "0";
			}
			if (fourR14_2.equals("")) {
				fourR14_2 = "0";
			}
			if (fourG14_2.equals("")) {
				fourG14_2 = "0";
			}
			if (fourB14_2.equals("")) {
				fourB14_2 = "0";
			}
			if (fiveR14_2.equals("")) {
				fiveR14_2 = "0";
			}
			if (fiveG14_2.equals("")) {
				fiveG14_2 = "0";
			}
			if (fiveB14_2.equals("")) {
				fiveB14_2 = "0";
			}
			if (rotationTime1.equals("")) {
				rotationTime1 = "0";
			}

			if (one24_2.equals("")) {
				one24_2 = "0";
			}
			if (two24_2.equals("")) {
				two24_2 = "0";
			}
			if (three24_2.equals("")) {
				three24_2 = "0";
			}
			if (four24_2.equals("")) {
				four24_2 = "0";
			}
			if (five24_2.equals("")) {
				five24_2 = "0";
			}
			if (six24_2.equals("")) {
				six24_2 = "0";
			}
			if (seven24_2.equals("")) {
				seven24_2 = "0";
			}
			if (eight24_2.equals("")) {
				eight24_2 = "0";
			}
			if (oneR24_2.equals("")) {
				oneR24_2 = "0";
			}
			if (oneG24_2.equals("")) {
				oneG24_2 = "0";
			}
			if (oneB24_2.equals("")) {
				oneB24_2 = "0";
			}
			if (twoR24_2.equals("")) {
				twoR24_2 = "0";
			}
			if (twoG24_2.equals("")) {
				twoG24_2 = "0";
			}
			if (twoB24_2.equals("")) {
				twoB24_2 = "0";
			}
			if (threeR24_2.equals("")) {
				threeR24_2 = "0";
			}
			if (threeG24_2.equals("")) {
				threeG24_2 = "0";
			}
			if (threeB24_2.equals("")) {
				threeB24_2 = "0";
			}
			if (fourR24_2.equals("")) {
				fourR24_2 = "0";
			}
			if (fourG24_2.equals("")) {
				fourG24_2 = "0";
			}
			if (fourB24_2.equals("")) {
				fourB24_2 = "0";
			}
			if (fiveR24_2.equals("")) {
				fiveR24_2 = "0";
			}
			if (fiveG24_2.equals("")) {
				fiveG24_2 = "0";
			}
			if (fiveB24_2.equals("")) {
				fiveB24_2 = "0";
			}
			if (rotationTime2.equals("")) {
				rotationTime2 = "0";
			}

			if (one34_2.equals("")) {
				one34_2 = "0";
			}
			if (two34_2.equals("")) {
				two34_2 = "0";
			}
			if (three34_2.equals("")) {
				three34_2 = "0";
			}
			if (four34_2.equals("")) {
				four34_2 = "0";
			}
			if (five34_2.equals("")) {
				five34_2 = "0";
			}
			if (six34_2.equals("")) {
				six34_2 = "0";
			}
			if (seven34_2.equals("")) {
				seven34_2 = "0";
			}
			if (eight34_2.equals("")) {
				eight34_2 = "0";
			}
			if (oneR34_2.equals("")) {
				oneR34_2 = "0";
			}
			if (oneG34_2.equals("")) {
				oneG34_2 = "0";
			}
			if (oneB34_2.equals("")) {
				oneB34_2 = "0";
			}
			if (twoR34_2.equals("")) {
				twoR34_2 = "0";
			}
			if (twoG34_2.equals("")) {
				twoG34_2 = "0";
			}
			if (twoB34_2.equals("")) {
				twoB34_2 = "0";
			}
			if (threeR34_2.equals("")) {
				threeR34_2 = "0";
			}
			if (threeG34_2.equals("")) {
				threeG34_2 = "0";
			}
			if (threeB34_2.equals("")) {
				threeB34_2 = "0";
			}
			if (fourR34_2.equals("")) {
				fourR34_2 = "0";
			}
			if (fourG34_2.equals("")) {
				fourG34_2 = "0";
			}
			if (fourB34_2.equals("")) {
				fourB34_2 = "0";
			}
			if (fiveR34_2.equals("")) {
				fiveR34_2 = "0";
			}
			if (fiveG34_2.equals("")) {
				fiveG34_2 = "0";
			}
			if (fiveB34_2.equals("")) {
				fiveB34_2 = "0";
			}
			if (rotationTime3.equals("")) {
				rotationTime3 = "0";
			}

			if (one44_2.equals("")) {
				one44_2 = "0";
			}
			if (two44_2.equals("")) {
				two44_2 = "0";
			}
			if (three44_2.equals("")) {
				three44_2 = "0";
			}
			if (four44_2.equals("")) {
				four44_2 = "0";
			}
			if (five44_2.equals("")) {
				five44_2 = "0";
			}
			if (six44_2.equals("")) {
				six44_2 = "0";
			}
			if (seven44_2.equals("")) {
				seven44_2 = "0";
			}
			if (eight44_2.equals("")) {
				eight44_2 = "0";
			}
			if (oneR44_2.equals("")) {
				oneR44_2 = "0";
			}
			if (oneG44_2.equals("")) {
				oneG44_2 = "0";
			}
			if (oneB44_2.equals("")) {
				oneB44_2 = "0";
			}
			if (twoR44_2.equals("")) {
				twoR44_2 = "0";
			}
			if (twoG44_2.equals("")) {
				twoG44_2 = "0";
			}
			if (twoB44_2.equals("")) {
				twoB44_2 = "0";
			}
			if (threeR44_2.equals("")) {
				threeR44_2 = "0";
			}
			if (threeG44_2.equals("")) {
				threeG44_2 = "0";
			}
			if (threeB44_2.equals("")) {
				threeB44_2 = "0";
			}
			if (fourR44_2.equals("")) {
				fourR44_2 = "0";
			}
			if (fourG44_2.equals("")) {
				fourG44_2 = "0";
			}
			if (fourB44_2.equals("")) {
				fourB44_2 = "0";
			}
			if (fiveR44_2.equals("")) {
				fiveR44_2 = "0";
			}
			if (fiveG44_2.equals("")) {
				fiveG44_2 = "0";
			}
			if (fiveB44_2.equals("")) {
				fiveB44_2 = "0";
			}
			if (rotationTime4.equals("")) {
				rotationTime4 = "0";
			}

			if (one54_2.equals("")) {
				one54_2 = "0";
			}
			if (two54_2.equals("")) {
				two54_2 = "0";
			}
			if (three54_2.equals("")) {
				three54_2 = "0";
			}
			if (four54_2.equals("")) {
				four54_2 = "0";
			}
			if (five54_2.equals("")) {
				five54_2 = "0";
			}
			if (six54_2.equals("")) {
				six54_2 = "0";
			}
			if (seven54_2.equals("")) {
				seven54_2 = "0";
			}
			if (eight54_2.equals("")) {
				eight54_2 = "0";
			}
			if (oneR54_2.equals("")) {
				oneR54_2 = "0";
			}
			if (oneG54_2.equals("")) {
				oneG54_2 = "0";
			}
			if (oneB54_2.equals("")) {
				oneB54_2 = "0";
			}
			if (twoR54_2.equals("")) {
				twoR54_2 = "0";
			}
			if (twoG54_2.equals("")) {
				twoG54_2 = "0";
			}
			if (twoB54_2.equals("")) {
				twoB54_2 = "0";
			}
			if (threeR54_2.equals("")) {
				threeR54_2 = "0";
			}
			if (threeG54_2.equals("")) {
				threeG54_2 = "0";
			}
			if (threeB54_2.equals("")) {
				threeB54_2 = "0";
			}
			if (fourR54_2.equals("")) {
				fourR54_2 = "0";
			}
			if (fourG54_2.equals("")) {
				fourG54_2 = "0";
			}
			if (fourB54_2.equals("")) {
				fourB54_2 = "0";
			}
			if (fiveR54_2.equals("")) {
				fiveR54_2 = "0";
			}
			if (fiveG54_2.equals("")) {
				fiveG54_2 = "0";
			}
			if (fiveB54_2.equals("")) {
				fiveB54_2 = "0";
			}
			if (rotationTime5.equals("")) {
				rotationTime5 = "0";
			}

			if (one64_2.equals("")) {
				one64_2 = "0";
			}
			if (two64_2.equals("")) {
				two64_2 = "0";
			}
			if (three64_2.equals("")) {
				three64_2 = "0";
			}
			if (four64_2.equals("")) {
				four64_2 = "0";
			}
			if (five64_2.equals("")) {
				five64_2 = "0";
			}
			if (six64_2.equals("")) {
				six64_2 = "0";
			}
			if (seven64_2.equals("")) {
				seven64_2 = "0";
			}
			if (eight64_2.equals("")) {
				eight64_2 = "0";
			}
			if (oneR64_2.equals("")) {
				oneR64_2 = "0";
			}
			if (oneG64_2.equals("")) {
				oneG64_2 = "0";
			}
			if (oneB64_2.equals("")) {
				oneB64_2 = "0";
			}
			if (twoR64_2.equals("")) {
				twoR64_2 = "0";
			}
			if (twoG64_2.equals("")) {
				twoG64_2 = "0";
			}
			if (twoB64_2.equals("")) {
				twoB64_2 = "0";
			}
			if (threeR64_2.equals("")) {
				threeR64_2 = "0";
			}
			if (threeG64_2.equals("")) {
				threeG64_2 = "0";
			}
			if (threeB64_2.equals("")) {
				threeB64_2 = "0";
			}
			if (fourR64_2.equals("")) {
				fourR64_2 = "0";
			}
			if (fourG64_2.equals("")) {
				fourG64_2 = "0";
			}
			if (fourB64_2.equals("")) {
				fourB64_2 = "0";
			}
			if (fiveR64_2.equals("")) {
				fiveR64_2 = "0";
			}
			if (fiveG64_2.equals("")) {
				fiveG64_2 = "0";
			}
			if (fiveB64_2.equals("")) {
				fiveB64_2 = "0";
			}
			if (rotationTime6.equals("")) {
				rotationTime6 = "0";
			}

			if (one74_2.equals("")) {
				one74_2 = "0";
			}
			if (two74_2.equals("")) {
				two74_2 = "0";
			}
			if (three74_2.equals("")) {
				three74_2 = "0";
			}
			if (four74_2.equals("")) {
				four74_2 = "0";
			}
			if (five74_2.equals("")) {
				five74_2 = "0";
			}
			if (six74_2.equals("")) {
				six74_2 = "0";
			}
			if (seven74_2.equals("")) {
				seven74_2 = "0";
			}
			if (eight74_2.equals("")) {
				eight74_2 = "0";
			}
			if (oneR74_2.equals("")) {
				oneR74_2 = "0";
			}
			if (oneG74_2.equals("")) {
				oneG74_2 = "0";
			}
			if (oneB74_2.equals("")) {
				oneB74_2 = "0";
			}
			if (twoR74_2.equals("")) {
				twoR74_2 = "0";
			}
			if (twoG74_2.equals("")) {
				twoG74_2 = "0";
			}
			if (twoB74_2.equals("")) {
				twoB74_2 = "0";
			}
			if (threeR74_2.equals("")) {
				threeR74_2 = "0";
			}
			if (threeG74_2.equals("")) {
				threeG74_2 = "0";
			}
			if (threeB74_2.equals("")) {
				threeB74_2 = "0";
			}
			if (fourR74_2.equals("")) {
				fourR74_2 = "0";
			}
			if (fourG74_2.equals("")) {
				fourG74_2 = "0";
			}
			if (fourB74_2.equals("")) {
				fourB74_2 = "0";
			}
			if (fiveR74_2.equals("")) {
				fiveR74_2 = "0";
			}
			if (fiveG74_2.equals("")) {
				fiveG74_2 = "0";
			}
			if (fiveB74_2.equals("")) {
				fiveB74_2 = "0";
			}
			if (rotationTime7.equals("")) {
				rotationTime7 = "0";
			}
		}
		String message1 = null;
		String message2 = null;
		String message3 = null;
		String message4 = null;
		String message5 = null;
		String message6 = null;
		String message7 = null;
		if (mode1_2 == 0) {
			message1 = "0302100000000001";
		} else if (mode1_2 == 1) {
			message1 = "0302100000000002";
		} else if (mode1_2 == 2) {
			message1 = "0302100000000003";
		} else if (mode1_2 == 3) {
			String mes_1 = ItonAdecimalConver.algorismToHEXString(Integer
					.parseInt(one14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(two14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(three14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(four14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(five14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(six14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(seven14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(eight14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneR14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneG14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneB14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoR14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoG14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoB14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeR14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeG14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeB14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourR14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourG14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourB14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveR14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveG14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveB14_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(rotationTime1));
			message1 = "0302100000000004" + mes_1;
		}
		me_1 = "6604"
				+ ItonAdecimalConver.algorismToHEXString(message1.length() / 2,
						4) + message1 + "99";

		if (mode2_2 == 0) {
			message2 = "0302200000000001";
		} else if (mode2_2 == 1) {
			message2 = "0302200000000002";
		} else if (mode2_2 == 2) {
			message2 = "0302200000000003";
		} else if (mode2_2 == 3) {
			String mes_2 = ItonAdecimalConver.algorismToHEXString(Integer
					.parseInt(one24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(two24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(three24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(four24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(five24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(six24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(seven24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(eight24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneR24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneG24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneB24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoR24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoG24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoB24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeR24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeG24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeB24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourR24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourG24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourB24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveR24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveG24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveB24_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(rotationTime2));
			message2 = "0302200000000004" + mes_2;
		}
		me_2 = "6604"
				+ ItonAdecimalConver.algorismToHEXString(message2.length() / 2,
						4) + message2 + "99";

		if (mode3_2 == 0) {
			message3 = "0302300000000001";
		} else if (mode3_2 == 1) {
			message3 = "0302300000000002";
		} else if (mode3_2 == 2) {
			message3 = "0302300000000003";
		} else if (mode3_2 == 3) {
			String mes_3 = ItonAdecimalConver.algorismToHEXString(Integer
					.parseInt(one34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(two34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(three34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(four34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(five34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(six34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(seven34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(eight34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneR34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneG34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneB34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoR34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoG34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoB34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeR34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeG34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeB34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourR34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourG34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourB34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveR34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveG34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveB34_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(rotationTime3));
			message3 = "0302300000000004" + mes_3;
		}
		me_3 = "6604"
				+ ItonAdecimalConver.algorismToHEXString(message3.length() / 2,
						4) + message3 + "99";

		if (mode4_2 == 0) {
			message4 = "0302400000000001";
		} else if (mode4_2 == 1) {
			message4 = "0302400000000002";
		} else if (mode4_2 == 2) {
			message4 = "0302400000000003";
		} else if (mode4_2 == 3) {
			String mes_4 = ItonAdecimalConver.algorismToHEXString(Integer
					.parseInt(one44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(two44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(three44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(four44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(five44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(six44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(seven44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(eight44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneR44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneG44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneB44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoR44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoG44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoB44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeR44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeG44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeB44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourR44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourG44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourB44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveR44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveG44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveB44_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(rotationTime4));
			message4 = "0302400000000004" + mes_4;
		}
		me_4 = "6604"
				+ ItonAdecimalConver.algorismToHEXString(message4.length() / 2,
						4) + message4 + "99";

		if (mode5_2 == 0) {
			message5 = "0302500000000001";
		} else if (mode5_2 == 1) {
			message5 = "0302500000000002";
		} else if (mode5_2 == 2) {
			message5 = "0302500000000003";
		} else if (mode5_2 == 3) {
			String mes_5 = ItonAdecimalConver.algorismToHEXString(Integer
					.parseInt(one54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(two54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(three54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(four54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(five54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(six54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(seven54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(eight54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneR54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneG54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneB54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoR54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoG54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoB54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeR54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeG54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeB54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourR54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourG54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourB54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveR54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveG54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveB54_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(rotationTime5));
			message5 = "0302500000000004" + mes_5;
		}
		me_5 = "6604"
				+ ItonAdecimalConver.algorismToHEXString(message5.length() / 2,
						4) + message5 + "99";

		if (mode6_2 == 0) {
			message6 = "0302600000000001";
		} else if (mode6_2 == 1) {
			message6 = "0302600000000002";
		} else if (mode6_2 == 2) {
			message6 = "0302600000000003";
		} else if (mode6_2 == 3) {
			String mes_6 = ItonAdecimalConver.algorismToHEXString(Integer
					.parseInt(one64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(two64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(three64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(four64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(five64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(six64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(seven64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(eight64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneR64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneG64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneB64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoR64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoG64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoB64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeR64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeG64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeB64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourR64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourG64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourB64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveR64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveG64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveB64_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(rotationTime6));
			message6 = "0302600000000004" + mes_6;
		}
		me_6 = "6604"
				+ ItonAdecimalConver.algorismToHEXString(message6.length() / 2,
						4) + message6 + "99";

		if (mode7_2 == 0) {
			message7 = "0302700000000001";
		} else if (mode7_2 == 1) {
			message6 = "0302700000000002";
		} else if (mode7_2 == 2) {
			message6 = "0302700000000003";
		} else if (mode7_2 == 3) {
			String mes_7 = ItonAdecimalConver.algorismToHEXString(Integer
					.parseInt(one74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(two74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(three74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(four74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(five74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(six74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(seven74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(eight74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneR74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneG74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(oneB74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoR74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoG74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(twoB74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeR74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeG74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(threeB74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourR74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourG74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fourB74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveR74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveG74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(fiveB74_2))
					+ ItonAdecimalConver.algorismToHEXString(Integer
							.parseInt(rotationTime7));
			message7 = "0302700000000004" + mes_7;
		}
		me_7 = "6604"
				+ ItonAdecimalConver.algorismToHEXString(message7.length() / 2,
						4) + message7 + "99";

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

	private class MyAdapter extends BaseAdapter {
		LayoutInflater inflater = LayoutInflater.from(GroupPlanActivity.this);

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return groupPlanNames.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return groupPlanNames.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;

			if (viewHolder == null) {
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(R.layout.group_list_item1, null);
				viewHolder.tvPlanName = (TextView) convertView
						.findViewById(R.id.group_name);
				viewHolder.ivPic = (ImageView) convertView
						.findViewById(R.id.iv);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.tvPlanName.setText(groupPlanNames.get(position));
			viewHolder.ivPic.setBackgroundResource(R.drawable.document_group);

			return convertView;
		}

	}

	private class ViewHolder {
		public TextView tvPlanName;
		public ImageView ivPic;
	}
}
