package cn.sharelink.intelligentled.activity_for_led;

import java.text.DecimalFormat;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.service.ACException;
import com.accloud.service.ACUserDevice;
import com.accloud.service.Receiver;
import com.accloud.service.Topic;
import com.accloud.service.TopicData;
import com.accloud.utils.PreferencesUtils;

import cn.sharelink.intelligentled.R;
import cn.sharelink.intelligentled.R.layout;
import cn.sharelink.intelligentled.config.Config;
import cn.sharelink.intelligentled.utils.GsonUtil;
import cn.sharelink.intelligentled.utils.ItonAdecimalConver;
import cn.sharelink.intelligentled.utils.OnReceive;
import cn.sharelink.intelligentled.utils.SendData;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 设备当前计划界面
 * 
 * @author Administrator
 * 
 */
public class DeviceCurrentPlanActivity extends Activity {
	private static final String TAG = DeviceCurrentPlanActivity.class
			.getSimpleName();
	private Button back;
	private LinearLayout ll1;
	private LinearLayout ll2;
	private LinearLayout ll3;
	private LinearLayout ll4;
	private LinearLayout ll5;
	private LinearLayout ll6;
	private LinearLayout ll7;

	LayoutInflater inflater;

	/**
	 * 0表示为设备当前计划，1表示未设备当前组计划
	 */
	int currentPlanType = 0;
	ACUserDevice myDevice;
	int type = 0;

	private String subDomain;
	Receiver<TopicData> receiver1;// 订阅
	SendData senddata;

	String progress111, progress112, progress121, progress122, progress131,
			progress132, progress141, progress142, progress211, progress212,
			progress221, progress222, progress231, progress232, progress241,
			progress242, progress311, progress312, progress321, progress322,
			progress331, progress332, progress341, progress342, progress411,
			progress412, progress421, progress422, progress431, progress432,
			progress441, progress442, progress511, progress512, progress521,
			progress522, progress531, progress532, progress541, progress542,
			progress611, progress612, progress621, progress622, progress631,
			progress632, progress641, progress642, progress711, progress712,
			progress721, progress722, progress731, progress732, progress741,
			progress742;

	String begintime11, begintime12, begintime13, begintime14, begintime21,
			begintime22, begintime23, begintime24, begintime31, begintime32,
			begintime33, begintime34, begintime41, begintime42, begintime43,
			begintime44, begintime51, begintime52, begintime53, begintime54,
			begintime61, begintime62, begintime63, begintime64, begintime71,
			begintime72, begintime73, begintime74;
	String endtime11, endtime12, endtime13, endtime14, endtime21, endtime22,
			endtime23, endtime24, endtime31, endtime32, endtime33, endtime34,
			endtime41, endtime42, endtime43, endtime44, endtime51, endtime52,
			endtime53, endtime54, endtime61, endtime62, endtime63, endtime64,
			endtime71, endtime72, endtime73, endtime74;

	String one14, two14, three14, four14, five14, six14, seven14, eight14; // 星期一，模式4的值
	String one24, two24, three24, four24, five24, six24, seven24, eight24;// 星期二，模式4的值
	String one34, two34, three34, four34, five34, six34, seven34, eight34;// 星期三，模式4的值
	String one44, two44, three44, four44, five44, six44, seven44, eight44;// 星期四，模式4的值
	String one54, two54, three54, four54, five54, six54, seven54, eight54;// 星期五，模式4的值
	String one64, two64, three64, four64, five64, six64, seven64, eight64;// 星期六，模式4的值
	String one74, two74, three74, four74, five74, six74, seven74, eight74;// 星期日，模式4的值
	String oneR14, oneG14, oneB14, twoR14, twoG14, twoB14, threeR14, threeG14,
			threeB14, fourR14, fourG14, fourB14, fiveR14, fiveG14, fiveB14;
	String oneR24, oneG24, oneB24, twoR24, twoG24, twoB24, threeR24, threeG24,
			threeB24, fourR24, fourG24, fourB24, fiveR24, fiveG24, fiveB24;
	String oneR34, oneG34, oneB34, twoR34, twoG34, twoB34, threeR34, threeG34,
			threeB34, fourR34, fourG34, fourB34, fiveR34, fiveG34, fiveB34;
	String oneR44, oneG44, oneB44, twoR44, twoG44, twoB44, threeR44, threeG44,
			threeB44, fourR44, fourG44, fourB44, fiveR44, fiveG44, fiveB44;
	String oneR54, oneG54, oneB54, twoR54, twoG54, twoB54, threeR54, threeG54,
			threeB54, fourR54, fourG54, fourB54, fiveR54, fiveG54, fiveB54;
	String oneR64, oneG64, oneB64, twoR64, twoG64, twoB64, threeR64, threeG64,
			threeB64, fourR64, fourG64, fourB64, fiveR64, fiveG64, fiveB64;
	String oneR74, oneG74, oneB74, twoR74, twoG74, twoB74, threeR74, threeG74,
			threeB74, fourR74, fourG74, fourB74, fiveR74, fiveG74, fiveB74;
	String rotationTime1, rotationTime2, rotationTime3, rotationTime4,
			rotationTime5, rotationTime6, rotationTime7;
	int mode1, mode2, mode3, mode4, mode5, mode6, mode7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_device_current_plan);
		currentPlanType = getIntent().getIntExtra("currentPlanType", 0);
		myDevice = (ACUserDevice) getIntent().getSerializableExtra("myDevice");
		type = getIntent().getIntExtra("type", 0);
		inflater = this.getLayoutInflater();
		initView();

	}

	String lightType;
	String schemeType;

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		subDomain = PreferencesUtils.getString(this, "subDomain",
				Config.SUBDOMAIN);
		senddata = new SendData(subDomain, myDevice.getPhysicalDeviceId());
		lightType = ItonAdecimalConver.algorismToHEXString(type + 1, 2);
		schemeType = ItonAdecimalConver.algorismToHEXString(
				currentPlanType + 1, 2);
		subscribe("xinlian01", "topic_type", myDevice.getDeviceId() + "");// 订阅，可获取到返回值
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * 订阅方法和获取返回值
	 * 
	 * @param submain
	 * @param topic_type
	 * @param deviceId
	 */
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
						Log.e(TAG, "订阅成功");
						senddata.sendData("66030002" + lightType + schemeType
								+ "99");
					}
				});
		receiver1 = new Receiver<TopicData>() {
			@Override
			public void onReceive(TopicData arg0) {
				// Log.e("订阅onReceive", arg0.getValue());
				String jsonData = arg0.getValue();
				OnReceive onRece = GsonUtil.parseJsonWithGson(jsonData,
						OnReceive.class);
				String[] pay = onRece.getPayload();
				byte[] arraysPay = Base64.decode(pay[0], 0);
				String payload = ItonAdecimalConver.byte2hex(arraysPay)
						.replace(" ", "");
				Log.e(TAG, "接收到的值:" + payload);

				if (payload.length() > 28 && payload.contains("9966")) {
					String[] strpays = payload.split("9966");
					strpays[0] = strpays[0] + "99";
					strpays[strpays.length - 1] = "66"
							+ strpays[strpays.length - 1];
					for (int i = 1; i < strpays.length - 1; i++) {
						strpays[i] = "66" + strpays[i] + "99";
					}

					for (String strpay : strpays) {
						parsePayload(strpay);
					}
				} else {
					parsePayload(payload);
				}
			}

		};
		AC.customDataMgr().registerDataReceiver(receiver1);
	}

	/**
	 * 解析返回值payload
	 * 
	 * @param payload
	 */
	protected void parsePayload(String payload) {
		Log.e(TAG, "rgb的解析payload:" + payload.length() + "/" + payload);
		if (payload.length() > 26) {
			String lightType = payload.substring(10, 12);
			String weekTimeFrame = payload.substring(14, 16);// 周时段
			Log.e(TAG, "weekTimeFrame的值：" + weekTimeFrame);
			String workModelRgb = payload.substring(24, 26);
			String one = null,two = null,three = null,four = null
					,five = null,six = null,seven = null,eight = null
					,oneR = null,oneG = null,oneB = null,twoR = null
					,twoG = null,twoB = null,threeR = null,threeG = null
					,threeB = null,fourR = null,fourG = null,fourB = null
					,fiveR = null,fiveG = null,fiveB = null,rotationTime = null;
			if (payload.substring(2, 6).equals("0300")) {
				if (lightType.equals("03")) { // 灯类型为彩色灯,模式四
					if (workModelRgb.equals("04")) {
						one = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(26, 28))
								+ "";
						two = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(28, 30))
								+ "";
						three = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(30, 32))
								+ "";
						four = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(32, 34))
								+ "";
						five = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(34, 36))
								+ "";
						six = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(36, 38))
								+ "";
						seven = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(38, 40))
								+ "";
						eight = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(40, 42))
								+ "";
						oneR = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(42, 44))
								+ "";
						oneG = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(44, 46))
								+ "";
						oneB = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(46, 48))
								+ "";
						twoR = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(48, 50))
								+ "";
						twoG = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(50, 52))
								+ "";
						twoB = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(52, 54))
								+ "";
						threeR = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(54, 56))
								+ "";
						threeG = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(56, 58))
								+ "";
						threeB = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(58, 60))
								+ "";
						fourR = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(60, 62))
								+ "";
						fourG = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(62, 64))
								+ "";
						fourB = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(64, 66))
								+ "";
						fiveR = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(66, 68))
								+ "";
						fiveG = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(68, 70))
								+ "";
						fiveB = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(70, 72))
								+ "";
						rotationTime = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(72, 74))
								+ "";
					}
					if (weekTimeFrame.equals("10")) {// 星期一
						Log.e(TAG, "彩色灯，星期一");
						if (workModelRgb.equals("01")) { // 模式一
							mode1 = 1;
						} else if (workModelRgb.equals("02")) {
							mode1 = 2;
						} else if (workModelRgb.equals("03")) {
							mode1 = 3;
						} else if (payload.length() > 74
								&& workModelRgb.equals("04")) {
							mode1 = 4;
							one14 = one;
							two14 = two;
							three14 = three;
							four14 = four;
							five14 = five;
							six14 = six;
							seven14 = seven;
							eight14 = eight;
							oneR14 = oneR;
							oneG14 = oneG;
							oneB14 = oneB;
							twoR14 = twoR;
							twoG14 = twoG;
							twoB14 = twoB;
							threeR14 = threeR;
							threeG14 = threeG;
							threeB14 = threeB;
							fourR14 = fourR;
							fourG14 = fourG;
							fourB14 = fourB;
							fiveR14 = fiveR;
							fiveG14 = fiveG;
							fiveB14 = fiveB;
							rotationTime1 = rotationTime;
						}
						Log.e(TAG, "星期一的工作模式：" + mode1);
						addRGBMode(weekTimeFrame, mode1);
					} else if (weekTimeFrame.equals("20")) {// 星期二
						Log.e(TAG, "彩色灯，星期二");
						if (workModelRgb.equals("01")) { // 模式一
							mode2 = 1;
						} else if (workModelRgb.equals("02")) {
							mode2 = 2;
						} else if (workModelRgb.equals("03")) {
							mode2 = 3;
						} else if (payload.length() > 74
								&& workModelRgb.equals("04")) {
							mode2 = 4;
							one24 = one;
							two24 = two;
							three24 = three;
							four24 = four;
							five24 = five;
							six24 = six;
							seven24 = seven;
							eight24 = eight;
							oneR24 = oneR;
							oneG24 = oneG;
							oneB24 = oneB;
							twoR24 = twoR;
							twoG24 = twoG;
							twoB24 = twoB;
							threeR24 = threeR;
							threeG24 = threeG;
							threeB24 = threeB;
							fourR24 = fourR;
							fourG24 = fourG;
							fourB24 = fourB;
							fiveR24 = fiveR;
							fiveG24 = fiveG;
							fiveB24 = fiveB;
							rotationTime2 = rotationTime;
						}
						Log.e(TAG, "星期二的工作模式：" + mode2);
						addRGBMode(weekTimeFrame, mode2);
					} else if (weekTimeFrame.equals("30")) {// 星期三
						Log.e(TAG, "彩色灯，星期三");
						if (workModelRgb.equals("01")) { // 模式一
							mode3 = 1;
						} else if (workModelRgb.equals("02")) {
							mode3 = 2;
						} else if (workModelRgb.equals("03")) {
							mode3 = 3;
						} else if (payload.length() > 74
								&& workModelRgb.equals("04")) {
							mode3 = 4;
							one34 = one;
							two34 = two;
							three34 = three;
							four34 = four;
							five34 = five;
							six34 = six;
							seven34 = seven;
							eight34 = eight;
							oneR34 = oneR;
							oneG34 = oneG;
							oneB34 = oneB;
							twoR34 = twoR;
							twoG34 = twoG;
							twoB34 = twoB;
							threeR34 = threeR;
							threeG34 = threeG;
							threeB34 = threeB;
							fourR34 = fourR;
							fourG34 = fourG;
							fourB34 = fourB;
							fiveR34 = fiveR;
							fiveG34 = fiveG;
							fiveB34 = fiveB;
							rotationTime3 = rotationTime;
						}
						Log.e(TAG, "星期三的工作模式：" + mode3);
						addRGBMode(weekTimeFrame, mode3);
					} else if (weekTimeFrame.equals("40")) {// 星期四
						Log.e(TAG, "彩色灯，星期四");
						if (workModelRgb.equals("01")) { // 模式一
							mode4 = 1;
						} else if (workModelRgb.equals("02")) {
							mode4 = 2;
						} else if (workModelRgb.equals("03")) {
							mode4 = 3;
						} else if (payload.length() > 74
								&& workModelRgb.equals("04")) {
							mode4 = 4;
							one44 = one;
							two44 = two;
							three44 = three;
							four44 = four;
							five44 = five;
							six44 = six;
							seven44 = seven;
							eight44 = eight;
							oneR44 = oneR;
							oneG44 = oneG;
							oneB44 = oneB;
							twoR44 = twoR;
							twoG44 = twoG;
							twoB44 = twoB;
							threeR44 = threeR;
							threeG44 = threeG;
							threeB44 = threeB;
							fourR44 = fourR;
							fourG44 = fourG;
							fourB44 = fourB;
							fiveR44 = fiveR;
							fiveG44 = fiveG;
							fiveB44 = fiveB;
							rotationTime4 = rotationTime;
						}
						Log.e(TAG, "星期四的工作模式：" + mode4);
						addRGBMode(weekTimeFrame, mode4);
					} else if (weekTimeFrame.equals("50")) {// 星期五
						Log.e(TAG, "彩色灯，星期五");
						if (workModelRgb.equals("01")) { // 模式一
							mode5 = 1;
						} else if (workModelRgb.equals("02")) {
							mode5 = 2;
						} else if (workModelRgb.equals("03")) {
							mode5 = 3;
						} else if (payload.length() > 74
								&& workModelRgb.equals("04")) {
							mode5 = 4;
							one54 = one;
							two54 = two;
							three54 = three;
							four54 = four;
							five54 = five;
							six54 = six;
							seven54 = seven;
							eight54 = eight;
							oneR54 = oneR;
							oneG54 = oneG;
							oneB54 = oneB;
							twoR54 = twoR;
							twoG54 = twoG;
							twoB54 = twoB;
							threeR54 = threeR;
							threeG54 = threeG;
							threeB54 = threeB;
							fourR54 = fourR;
							fourG54 = fourG;
							fourB54 = fourB;
							fiveR54 = fiveR;
							fiveG54 = fiveG;
							fiveB54 = fiveB;
							rotationTime5 = rotationTime;
						}
						Log.e(TAG, "星期五的工作模式：" + mode5);
						addRGBMode(weekTimeFrame, mode5);
					} else if (weekTimeFrame.equals("60")) {// 星期六
						Log.e(TAG, "彩色灯，星期六");
						if (workModelRgb.equals("01")) { // 模式一
							mode6 = 1;
						} else if (workModelRgb.equals("02")) {
							mode6 = 2;
						} else if (workModelRgb.equals("03")) {
							mode6 = 3;
						} else if (payload.length() > 74
								&& workModelRgb.equals("04")) {
							mode6 = 4;
							one64 = one;
							two64 = two;
							three64 = three;
							four64 = four;
							five64 = five;
							six64 = six;
							seven64 = seven;
							eight64 = eight;
							oneR64 = oneR;
							oneG64 = oneG;
							oneB64 = oneB;
							twoR64 = twoR;
							twoG64 = twoG;
							twoB64 = twoB;
							threeR64 = threeR;
							threeG64 = threeG;
							threeB64 = threeB;
							fourR64 = fourR;
							fourG64 = fourG;
							fourB64 = fourB;
							fiveR64 = fiveR;
							fiveG64 = fiveG;
							fiveB64 = fiveB;
							rotationTime6 = rotationTime;
						}
						Log.e(TAG, "星期六的工作模式：" + mode6);
						addRGBMode(weekTimeFrame, mode6);
					} else if (weekTimeFrame.equals("70")) {// 星期天
						Log.e(TAG, "彩色灯，星期天");
						if (workModelRgb.equals("01")) { // 模式一
							mode7 = 1;
						} else if (workModelRgb.equals("02")) {
							mode7 = 2;
						} else if (workModelRgb.equals("03")) {
							mode7 = 3;
						} else if (payload.length() > 74
								&& workModelRgb.equals("04")) {
							mode7 = 4;
							one74 = one;
							two74 = two;
							three74 = three;
							four74 = four;
							five74 = five;
							six74 = six;
							seven74 = seven;
							eight74 = eight;
							oneR74 = oneR;
							oneG74 = oneG;
							oneB74 = oneB;
							twoR74 = twoR;
							twoG74 = twoG;
							twoB74 = twoB;
							threeR74 = threeR;
							threeG74 = threeG;
							threeB74 = threeB;
							fourR74 = fourR;
							fourG74 = fourG;
							fourB74 = fourB;
							fiveR74 = fiveR;
							fiveG74 = fiveG;
							fiveB74 = fiveB;
							rotationTime7 = rotationTime;
						}
						Log.e(TAG, "星期天的工作模式：" + mode7);
						addRGBMode(weekTimeFrame, mode7);
					}

				} else {// 灯类型为单色灯/色温灯
					if (lightType.equals("01")) {
						Log.e(TAG, "灯类型：单色灯");
					} else if (lightType.equals("02")) {
						Log.e(TAG, "灯类型：色温灯");
					}
					Log.e(TAG, "单色灯/色温灯的payload的长度：" + payload.length());
					Log.e(TAG, "单色灯/色温灯的weekTimeFrame的值：" + weekTimeFrame);
					if (payload.length() > 30) {
						String begin_1 = addO(payload.substring(16, 18));
						String begin_2 = addO(payload.substring(18, 20));
						String end_1 = addO(payload.substring(20, 22));
						String end_2 = addO(payload.substring(22, 24));
						String progress_1 = ItonAdecimalConver
								.hexStringToAlgorism(payload.substring(26, 28))
								+ "";
						String progress_2 = null;
						if (lightType.equals("01")) {
							progress_2 = ItonAdecimalConver
									.hexStringToAlgorism(payload.substring(28,
											30))
									+ "";
						} else if (lightType.equals("02")) {
							progress_2 = ItonAdecimalConver
									.hexStringToAlgorism(payload.substring(28,
											32))
									+ "";
						}

						if (weekTimeFrame.equals("11")) {// 星期一，时段一
							Log.e(TAG, "星期一，时段一");
							begintime11 = begin_1 + ":" + begin_2;
							endtime11 = end_1 + ":" + end_2;
							progress111 = progress_1;
							progress112 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("12")) {// 星期一，时段二
							Log.e(TAG, "星期一，时段二");
							begintime12 = begin_1 + ":" + begin_2;
							endtime12 = end_1 + ":" + end_2;
							progress121 = progress_1;
							progress122 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("13")) {// 星期一，时段三
							Log.e(TAG, "星期一，时段三");
							begintime13 = begin_1 + ":" + begin_2;
							endtime13 = end_1 + ":" + end_2;
							progress131 = progress_1;
							progress132 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("14")) {// 星期一，时段四
							Log.e(TAG, "星期一，时段四");
							begintime14 = begin_1 + ":" + begin_2;
							endtime14 = end_1 + ":" + end_2;
							progress141 = progress_1;
							progress142 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("21")) {// 星期二，时段一
							Log.e(TAG, "星期二，时段一");
							begintime21 = begin_1 + ":" + begin_2;
							endtime21 = end_1 + ":" + end_2;
							progress211 = progress_1;
							progress212 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("22")) { // 星期二，时段二
							Log.e(TAG, "星期二，时段二");
							begintime22 = begin_1 + ":" + begin_2;
							endtime22 = end_1 + ":" + end_2;
							progress221 = progress_1;
							progress222 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("23")) {// 星期二，时段三
							Log.e(TAG, "星期二，时段三");
							begintime23 = begin_1 + ":" + begin_2;
							endtime23 = end_1 + ":" + end_2;
							progress231 = progress_1;
							progress232 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("24")) {// 星期二，时段四
							Log.e(TAG, "星期二，时段四");
							begintime24 = begin_1 + ":" + begin_2;
							endtime24 = end_1 + ":" + end_2;
							progress241 = progress_1;
							progress242 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("31")) {// 星期三，时段一
							Log.e(TAG, "星期三，时段一");
							begintime31 = begin_1 + ":" + begin_2;
							endtime31 = end_1 + ":" + end_2;
							progress311 = progress_1;
							progress312 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("32")) {// 星期三，时段二
							Log.e(TAG, "星期三，时段二");
							begintime32 = begin_1 + ":" + begin_2;
							endtime32 = end_1 + ":" + end_2;
							progress321 = progress_1;
							progress322 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("33")) {// 星期三，时段三
							Log.e(TAG, "星期三，时段三");
							begintime33 = begin_1 + ":" + begin_2;
							endtime33 = end_1 + ":" + end_2;
							progress331 = progress_1;
							progress332 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("34")) {// 星期三，时段四
							Log.e(TAG, "星期三，时段四");
							begintime34 = begin_1 + ":" + begin_2;
							endtime34 = end_1 + ":" + end_2;
							progress341 = progress_1;
							progress342 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("41")) {// 星期四，时段一
							Log.e(TAG, "星期四，时段一");
							begintime41 = begin_1 + ":" + begin_2;
							endtime41 = end_1 + ":" + end_2;
							progress411 = progress_1;
							progress412 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("42")) {// 星期四，时段二
							Log.e(TAG, "星期四，时段二");
							begintime42 = begin_1 + ":" + begin_2;
							endtime42 = end_1 + ":" + end_2;
							progress421 = progress_1;
							progress422 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("43")) {// 星期四，时段三
							Log.e(TAG, "星期四，时段三");
							begintime43 = begin_1 + ":" + begin_2;
							endtime43 = end_1 + ":" + end_2;
							progress431 = progress_1;
							progress432 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("44")) {// 星期四，时段四
							Log.e(TAG, "星期四，时段四");
							begintime44 = begin_1 + ":" + begin_2;
							endtime44 = end_1 + ":" + end_2;
							progress441 = progress_1;
							progress442 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("51")) {// 星期五,时段一
							Log.e(TAG, "星期五,时段一");
							begintime51 = begin_1 + ":" + begin_2;
							endtime51 = end_1 + ":" + end_2;
							progress511 = progress_1;
							progress512 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("52")) {// 星期五,时段二
							Log.e(TAG, "星期五,时段二");
							begintime52 = begin_1 + ":" + begin_2;
							endtime52 = end_1 + ":" + end_2;
							progress521 = progress_1;
							progress522 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("53")) {// 星期五,时段三
							Log.e(TAG, "星期五,时段三");
							begintime53 = begin_1 + ":" + begin_2;
							endtime53 = end_1 + ":" + end_2;
							progress531 = progress_1;
							progress532 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("54")) {// 星期五,时段四
							Log.e(TAG, "星期五,时段四");
							begintime54 = begin_1 + ":" + begin_2;
							endtime54 = end_1 + ":" + end_2;
							progress541 = progress_1;
							progress542 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("61")) {// 星期六，时段一
							Log.e(TAG, "星期六，时段一");
							begintime61 = begin_1 + ":" + begin_2;
							endtime61 = end_1 + ":" + end_2;
							progress611 = progress_1;
							progress612 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("62")) {// 星期六，时段二
							Log.e(TAG, "星期六，时段二");
							begintime62 = begin_1 + ":" + begin_2;
							endtime62 = end_1 + ":" + end_2;
							progress621 = progress_1;
							progress622 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("63")) {// 星期六，时段三
							Log.e(TAG, "星期六，时段三");
							begintime63 = begin_1 + ":" + begin_2;
							endtime63 = end_1 + ":" + end_2;
							progress631 = progress_1;
							progress632 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("64")) {// 星期六，时段四
							Log.e(TAG, "星期六，时段四");
							begintime64 = begin_1 + ":" + begin_2;
							endtime64 = end_1 + ":" + end_2;
							progress641 = progress_1;
							progress642 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("71")) {// 星期天，时段一
							Log.e(TAG, "星期天，时段一");
							begintime71 = begin_1 + ":" + begin_2;
							endtime71 = end_1 + ":" + end_2;
							progress711 = progress_1;
							progress712 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("72")) {// 星期天，时段二
							Log.e(TAG, "星期天，时段二");
							begintime72 = begin_1 + ":" + begin_2;
							endtime72 = end_1 + ":" + end_2;
							progress721 = progress_1;
							progress722 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("73")) {// 星期天，时段三
							Log.e(TAG, "星期天，时段三");
							begintime73 = begin_1 + ":" + begin_2;
							endtime73 = end_1 + ":" + end_2;
							progress731 = progress_1;
							progress732 = progress_2;
							addCommonMode(weekTimeFrame);
						} else if (weekTimeFrame.equals("74")) {// 星期天，时段四
							Log.e(TAG, "星期天，时段四");
							begintime74 = begin_1 + ":" + begin_2;
							endtime74 = end_1 + ":" + end_2;
							progress741 = progress_1;
							progress742 = progress_2;
							addCommonMode(weekTimeFrame);
						}
					}
				}
			}
		}

	}

	private void addCommonMode(String weekTimeFrame) {
		LinearLayout.LayoutParams vParam = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		if (weekTimeFrame.equals("11")) { // 星期一，时段一
			addCommonM(ll1, vParam, 11);
		} else if (weekTimeFrame.equals("12")) {// 星期一，时段二
			addCommonM(ll1, vParam, 12);
		} else if (weekTimeFrame.equals("13")) {// 星期一，时段三
			addCommonM(ll1, vParam, 13);
		} else if (weekTimeFrame.equals("14")) {// 星期一，时段四
			addCommonM(ll1, vParam, 14);
		} else if (weekTimeFrame.equals("21")) {// 星期二，时段一
			addCommonM(ll2, vParam, 21);
		} else if (weekTimeFrame.equals("22")) {// 星期二，时段二
			addCommonM(ll2, vParam, 22);
		} else if (weekTimeFrame.equals("23")) {// 星期二，时段三
			addCommonM(ll2, vParam, 23);
		} else if (weekTimeFrame.equals("24")) {// 星期二，时段四
			addCommonM(ll2, vParam, 24);
		} else if (weekTimeFrame.equals("31")) {// 星期三，时段一
			addCommonM(ll3, vParam, 31);
		} else if (weekTimeFrame.equals("32")) {// 星期三，时段二
			addCommonM(ll3, vParam, 32);
		} else if (weekTimeFrame.equals("33")) {// 星期三，时段三
			addCommonM(ll3, vParam, 33);
		} else if (weekTimeFrame.equals("34")) {// 星期三，时段四
			addCommonM(ll3, vParam, 34);
		} else if (weekTimeFrame.equals("41")) {// 星期四，时段一
			addCommonM(ll4, vParam, 41);
		} else if (weekTimeFrame.equals("42")) {// 星期四，时段二
			addCommonM(ll4, vParam, 42);
		} else if (weekTimeFrame.equals("43")) {// 星期四，时段三
			addCommonM(ll4, vParam, 43);
		} else if (weekTimeFrame.equals("44")) {// 星期四，时段四
			addCommonM(ll4, vParam, 44);
		} else if (weekTimeFrame.equals("51")) {// 星期五，时段一
			addCommonM(ll5, vParam, 51);
		} else if (weekTimeFrame.equals("52")) {// 星期五，时段二
			addCommonM(ll5, vParam, 52);
		} else if (weekTimeFrame.equals("53")) {// 星期五，时段三
			addCommonM(ll5, vParam, 53);
		} else if (weekTimeFrame.equals("54")) {// 星期五，时段四
			addCommonM(ll5, vParam, 54);
		} else if (weekTimeFrame.equals("61")) {// 星期六，时段一
			addCommonM(ll6, vParam, 61);
		} else if (weekTimeFrame.equals("62")) {// 星期六，时段二
			addCommonM(ll6, vParam, 62);
		} else if (weekTimeFrame.equals("63")) {// 星期六，时段三
			addCommonM(ll6, vParam, 63);
		} else if (weekTimeFrame.equals("64")) {// 星期六，时段四
			addCommonM(ll6, vParam, 64);
		} else if (weekTimeFrame.equals("71")) {// 星期天，时段一
			addCommonM(ll7, vParam, 71);
		} else if (weekTimeFrame.equals("72")) {// 星期天，时段二
			addCommonM(ll7, vParam, 72);
		} else if (weekTimeFrame.equals("73")) {// 星期天，时段三
			addCommonM(ll7, vParam, 73);
		} else if (weekTimeFrame.equals("74")) {// 星期天，时段四
			addCommonM(ll7, vParam, 74);
		}

	}

	private void addCommonM(LinearLayout ll, LayoutParams vParam, int i) {
		Log.e(TAG, "addCommonM:" + i);
		View v1 = inflater.inflate(R.layout.plan_view_frame_time_common, null);
		v1.setLayoutParams(vParam);

		TextView tv11 = (TextView) v1.findViewById(R.id.tv_tv11);
		TextView tv12 = (TextView) v1.findViewById(R.id.tv_tv12);

		if (type == 0) {
			tv11.setText(getResources().getString(R.string.begin_light_0));
			tv12.setText(getResources().getString(R.string.end_light_0));

		} else if (type == 1) {
			tv12.setText(getResources().getString(R.string.cct_value));
			tv11.setText(getResources().getString(R.string.brightness_value));

		}
		TextView tvTimeTime1 = (TextView) v1.findViewById(R.id.tv_time1_time);
		TextView tvColorTem1 = (TextView) v1.findViewById(R.id.tv_color_tem1);
		TextView tvColorLight1 = (TextView) v1
				.findViewById(R.id.tv_color_light1);
		TextView timeFrame = (TextView) v1.findViewById(R.id.time_frame);
		switch (i) {
		case 11:
			timeFrame.setText(getResources().getString(R.string.time1_0));
			Log.e(TAG, "BEGINTIME11:" + begintime11);
			tvTimeTime1.setText(begintime11 + "-" + endtime11);
			tvColorTem1.setText(progress111);
			tvColorLight1.setText(progress112);
			break;
		case 12:
			timeFrame.setText(getResources().getString(R.string.time2_0));
			tvTimeTime1.setText(begintime12 + "-" + endtime12);
			tvColorTem1.setText(progress121);
			tvColorLight1.setText(progress122);
			break;
		case 13:
			timeFrame.setText(getResources().getString(R.string.time3_0));
			tvTimeTime1.setText(begintime13 + "-" + endtime13);
			tvColorTem1.setText(progress131);
			tvColorLight1.setText(progress132);
			break;
		case 14:
			timeFrame.setText(getResources().getString(R.string.time4_0));
			tvTimeTime1.setText(begintime14 + "-" + endtime14);
			tvColorTem1.setText(progress141);
			tvColorLight1.setText(progress142);
			break;
		case 21:
			timeFrame.setText(getResources().getString(R.string.time1_0));
			tvTimeTime1.setText(begintime21 + "-" + endtime21);
			tvColorTem1.setText(progress211);
			tvColorLight1.setText(progress212);
			break;
		case 22:
			timeFrame.setText(getResources().getString(R.string.time2_0));
			tvTimeTime1.setText(begintime22 + "-" + endtime22);
			tvColorTem1.setText(progress221);
			tvColorLight1.setText(progress222);
			break;
		case 23:
			timeFrame.setText(getResources().getString(R.string.time3_0));
			tvTimeTime1.setText(begintime23 + "-" + endtime23);
			tvColorTem1.setText(progress231);
			tvColorLight1.setText(progress232);
			break;
		case 24:
			timeFrame.setText(getResources().getString(R.string.time4_0));
			tvTimeTime1.setText(begintime24 + "-" + endtime24);
			tvColorTem1.setText(progress241);
			tvColorLight1.setText(progress242);
			break;
		case 31:
			timeFrame.setText(getResources().getString(R.string.time1_0));
			tvTimeTime1.setText(begintime31 + "-" + endtime31);
			tvColorTem1.setText(progress311);
			tvColorLight1.setText(progress312);
			break;
		case 32:

			timeFrame.setText(getResources().getString(R.string.time2_0));
			tvTimeTime1.setText(begintime32 + "-" + endtime32);
			tvColorTem1.setText(progress321);
			tvColorLight1.setText(progress322);
			break;
		case 33:
			timeFrame.setText(getResources().getString(R.string.time3_0));
			tvTimeTime1.setText(begintime33 + "-" + endtime33);
			tvColorTem1.setText(progress331);
			tvColorLight1.setText(progress332);
			break;
		case 34:
			timeFrame.setText(getResources().getString(R.string.time4_0));
			tvTimeTime1.setText(begintime34 + "-" + endtime34);
			tvColorTem1.setText(progress341);
			tvColorLight1.setText(progress342);
			break;
		case 41:
			timeFrame.setText(getResources().getString(R.string.time1_0));
			tvTimeTime1.setText(begintime41 + "-" + endtime41);
			tvColorTem1.setText(progress411);
			tvColorLight1.setText(progress412);
			break;
		case 42:
			timeFrame.setText(getResources().getString(R.string.time2_0));
			tvTimeTime1.setText(begintime42 + "-" + endtime42);
			tvColorTem1.setText(progress421);
			tvColorLight1.setText(progress422);
			break;
		case 43:
			timeFrame.setText(getResources().getString(R.string.time3_0));
			tvTimeTime1.setText(begintime43 + "-" + endtime43);
			tvColorTem1.setText(progress431);
			tvColorLight1.setText(progress432);
			break;
		case 44:
			timeFrame.setText(getResources().getString(R.string.time4_0));
			tvTimeTime1.setText(begintime44 + "-" + endtime44);
			tvColorTem1.setText(progress441);
			tvColorLight1.setText(progress442);
			break;
		case 51:
			timeFrame.setText(getResources().getString(R.string.time1_0));
			tvTimeTime1.setText(begintime51 + "-" + endtime51);
			tvColorTem1.setText(progress511);
			tvColorLight1.setText(progress512);
			break;
		case 52:
			timeFrame.setText(getResources().getString(R.string.time2_0));
			tvTimeTime1.setText(begintime52 + "-" + endtime52);
			tvColorTem1.setText(progress521);
			tvColorLight1.setText(progress522);
			break;
		case 53:
			timeFrame.setText(getResources().getString(R.string.time3_0));
			tvTimeTime1.setText(begintime53 + "-" + endtime53);
			tvColorTem1.setText(progress531);
			tvColorLight1.setText(progress532);
			break;
		case 54:
			timeFrame.setText(getResources().getString(R.string.time4_0));
			tvTimeTime1.setText(begintime54 + "-" + endtime54);
			tvColorTem1.setText(progress541);
			tvColorLight1.setText(progress542);
			break;
		case 61:
			timeFrame.setText(getResources().getString(R.string.time1_0));
			tvTimeTime1.setText(begintime61 + "-" + endtime61);
			tvColorTem1.setText(progress611);
			tvColorLight1.setText(progress612);
			break;
		case 62:
			timeFrame.setText(getResources().getString(R.string.time2_0));
			tvTimeTime1.setText(begintime62 + "-" + endtime62);
			tvColorTem1.setText(progress621);
			tvColorLight1.setText(progress622);
			break;
		case 63:
			timeFrame.setText(getResources().getString(R.string.time3_0));
			tvTimeTime1.setText(begintime63 + "-" + endtime63);
			tvColorTem1.setText(progress631);
			tvColorLight1.setText(progress632);
			break;
		case 64:
			timeFrame.setText(getResources().getString(R.string.time4_0));
			tvTimeTime1.setText(begintime64 + "-" + endtime64);
			tvColorTem1.setText(progress641);
			tvColorLight1.setText(progress642);
			break;
		case 71:
			timeFrame.setText(getResources().getString(R.string.time1_0));
			tvTimeTime1.setText(begintime71 + "-" + endtime71);
			tvColorTem1.setText(progress711);
			tvColorLight1.setText(progress712);
			break;
		case 72:
			timeFrame.setText(getResources().getString(R.string.time2_0));
			tvTimeTime1.setText(begintime72 + "-" + endtime72);
			tvColorTem1.setText(progress721);
			tvColorLight1.setText(progress722);
			break;
		case 73:
			timeFrame.setText(getResources().getString(R.string.time3_0));
			tvTimeTime1.setText(begintime73 + "-" + endtime73);
			tvColorTem1.setText(progress731);
			tvColorLight1.setText(progress732);
			break;
		case 74:
			timeFrame.setText(getResources().getString(R.string.time4_0));
			tvTimeTime1.setText(begintime74 + "-" + endtime74);
			tvColorTem1.setText(progress741);
			tvColorLight1.setText(progress742);
			break;

		}

		ll.addView(v1);

	}

	private void addRGBMode(String weekTimeFrame, int mode) {

		if (weekTimeFrame.equals("10")) { // 星期一
			addRgbM(ll1, mode, 0);
		} else if (weekTimeFrame.equals("20")) { // 星期二
			addRgbM(ll2, mode, 1);
		} else if (weekTimeFrame.equals("30")) { // 星期三
			addRgbM(ll3, mode, 2);
		} else if (weekTimeFrame.equals("40")) { // 星期四
			addRgbM(ll4, mode, 3);
		} else if (weekTimeFrame.equals("50")) { // 星期五
			addRgbM(ll5, mode, 4);
		} else if (weekTimeFrame.equals("60")) { // 星期六
			addRgbM(ll6, mode, 5);
		} else if (weekTimeFrame.equals("70")) { // 星期天
			addRgbM(ll7, mode, 6);
		}

	}

	private void addRgbM(LinearLayout ll, int mode, int weekday) {
		LinearLayout.LayoutParams vParam = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		View v = inflater.inflate(R.layout.plan_view_frame_time, null);
		v.setLayoutParams(vParam);
		TextView tvMode1 = (TextView) v.findViewById(R.id.tv_time1_mode);
		tvMode1.setText(mode + "");
		RelativeLayout rl1 = (RelativeLayout) v
				.findViewById(R.id.rl_time1_mode);
		TextView tvOne1 = (TextView) v.findViewById(R.id.tv_time1_one);
		TextView tvTwo1 = (TextView) v.findViewById(R.id.tv_time1_two);
		TextView tvThree1 = (TextView) v.findViewById(R.id.tv_time1_three);
		TextView tvFour1 = (TextView) v.findViewById(R.id.tv_time1_four);
		TextView tvFive1 = (TextView) v.findViewById(R.id.tv_time1_five);
		TextView tvSix1 = (TextView) v.findViewById(R.id.tv_time1_six);
		TextView tvSeven1 = (TextView) v.findViewById(R.id.tv_time1_seven);
		TextView tvEight1 = (TextView) v.findViewById(R.id.tv_time1_eight);
		TextView tvOneR1 = (TextView) v.findViewById(R.id.tv_time1_oner);
		TextView tvOneG1 = (TextView) v.findViewById(R.id.tv_time1_oneg);
		TextView tvOneB1 = (TextView) v.findViewById(R.id.tv_time1_oneb);
		TextView tvTwoR1 = (TextView) v.findViewById(R.id.tv_time1_twor);
		TextView tvTwoG1 = (TextView) v.findViewById(R.id.tv_time1_twog);
		TextView tvTwoB1 = (TextView) v.findViewById(R.id.tv_time1_twob);
		TextView tvThreeR1 = (TextView) v.findViewById(R.id.tv_time1_threer);
		TextView tvThreeG1 = (TextView) v.findViewById(R.id.tv_time1_threeg);
		TextView tvThreeB1 = (TextView) v.findViewById(R.id.tv_time1_threeb);
		TextView tvFourR1 = (TextView) v.findViewById(R.id.tv_time1_fourr);
		TextView tvFourG1 = (TextView) v.findViewById(R.id.tv_time1_fourg);
		TextView tvFourB1 = (TextView) v.findViewById(R.id.tv_time1_fourb);
		TextView tvFiveR1 = (TextView) v.findViewById(R.id.tv_time1_fiver);
		TextView tvFiveG1 = (TextView) v.findViewById(R.id.tv_time1_fiveg);
		TextView tvFiveB1 = (TextView) v.findViewById(R.id.tv_time1_fiveb);
		TextView tvRotationTime1 = (TextView) v
				.findViewById(R.id.tv_time1_rotation);

		if (mode == 4) {
			Log.e(TAG, "工作模式4");
			rl1.setVisibility(View.VISIBLE);
			switch (weekday) {
			case 0:
				one14 = (one14.length() == 0) ? "[0]" : "[" + one14 + "]";
				two14 = (two14.length() == 0) ? "[0]" : "[" + two14 + "]";
				three14 = (three14.length() == 0) ? "[0]" : "[" + three14 + "]";
				four14 = (four14.length() == 0) ? "[0]" : "[" + four14 + "]";
				five14 = (five14.length() == 0) ? "[0]" : "[" + five14 + "]";
				six14 = (six14.length() == 0) ? "[0]" : "[" + six14 + "]";
				seven14 = (seven14.length() == 0) ? "[0]" : "[" + seven14 + "]";
				eight14 = (eight14.length() == 0) ? "[0]" : "[" + eight14 + "]";
				oneR14 = (oneR14.length() == 0) ? "[0]" : "[" + oneR14 + "]";
				oneG14 = (oneG14.length() == 0) ? "[0]" : "[" + oneG14 + "]";
				oneB14 = (oneB14.length() == 0) ? "[0]" : "[" + oneB14 + "]";
				twoR14 = (twoR14.length() == 0) ? "[0]" : "[" + twoR14 + "]";
				twoG14 = (twoG14.length() == 0) ? "[0]" : "[" + twoG14 + "]";
				twoB14 = (twoB14.length() == 0) ? "[0]" : "[" + twoB14 + "]";
				threeR14 = (threeR14.length() == 0) ? "[0]" : "[" + threeR14
						+ "]";
				threeG14 = (threeG14.length() == 0) ? "[0]" : "[" + threeG14
						+ "]";
				threeB14 = (threeB14.length() == 0) ? "[0]" : "[" + threeB14
						+ "]";
				fourR14 = (fourR14.length() == 0) ? "[0]" : "[" + fourR14 + "]";
				fourG14 = (fourG14.length() == 0) ? "[0]" : "[" + fourG14 + "]";
				fourB14 = (fourB14.length() == 0) ? "[0]" : "[" + fourB14 + "]";
				fiveR14 = (fiveR14.length() == 0) ? "[0]" : "[" + fiveR14 + "]";
				fiveG14 = (fiveG14.length() == 0) ? "[0]" : "[" + fiveG14 + "]";
				fiveB14 = (fiveB14.length() == 0) ? "[0]" : "[" + fiveB14 + "]";
				rotationTime1 = (rotationTime1.length() == 0) ? "0"
						: rotationTime1;
				tvOne1.setText(one14);
				tvTwo1.setText(two14);
				tvThree1.setText(three14);
				tvFour1.setText(four14);
				tvFive1.setText(five14);
				tvSix1.setText(six14);
				tvSeven1.setText(seven14);
				tvEight1.setText(eight14);
				tvOneR1.setText(oneR14);
				tvOneG1.setText(oneG14);
				tvOneB1.setText(oneB14);
				tvTwoR1.setText(twoR14);
				tvTwoG1.setText(twoG14);
				tvTwoB1.setText(twoB14);
				tvThreeR1.setText(threeR14);
				tvThreeG1.setText(threeG14);
				tvThreeB1.setText(threeB14);
				tvFourR1.setText(fourR14);
				tvFourG1.setText(fourG14);
				tvFourB1.setText(fourB14);
				tvFiveR1.setText(fiveR14);
				tvFiveG1.setText(fiveG14);
				tvFiveB1.setText(fiveB14);
				tvRotationTime1.setText(rotationTime1);
				break;

			case 1:
				one24 = (one24.length() == 0) ? "[0]" : "[" + one24 + "]";
				two24 = (two24.length() == 0) ? "[0]" : "[" + two24 + "]";
				three24 = (three24.length() == 0) ? "[0]" : "[" + three24 + "]";
				four24 = (four24.length() == 0) ? "[0]" : "[" + four24 + "]";
				five24 = (five24.length() == 0) ? "[0]" : "[" + five24 + "]";
				six24 = (six24.length() == 0) ? "[0]" : "[" + six24 + "]";
				seven24 = (seven24.length() == 0) ? "[0]" : "[" + seven24 + "]";
				eight24 = (eight24.length() == 0) ? "[0]" : "[" + eight24 + "]";
				oneR24 = (oneR24.length() == 0) ? "[0]" : "[" + oneR24 + "]";
				oneG24 = (oneG24.length() == 0) ? "[0]" : "[" + oneG24 + "]";
				oneB24 = (oneB24.length() == 0) ? "[0]" : "[" + oneB24 + "]";
				twoR24 = (twoR24.length() == 0) ? "[0]" : "[" + twoR24 + "]";
				twoG24 = (twoG24.length() == 0) ? "[0]" : "[" + twoG24 + "]";
				twoB24 = (twoB24.length() == 0) ? "[0]" : "[" + twoB24 + "]";
				threeR24 = (threeR24.length() == 0) ? "[0]" : "[" + threeR24
						+ "]";
				threeG24 = (threeG24.length() == 0) ? "[0]" : "[" + threeG24
						+ "]";
				threeB24 = (threeB24.length() == 0) ? "[0]" : "[" + threeB24
						+ "]";
				fourR24 = (fourR24.length() == 0) ? "[0]" : "[" + fourR24 + "]";
				fourG24 = (fourG24.length() == 0) ? "[0]" : "[" + fourG24 + "]";
				fourB24 = (fourB24.length() == 0) ? "[0]" : "[" + fourB24 + "]";
				fiveR24 = (fiveR24.length() == 0) ? "[0]" : "[" + fiveR24 + "]";
				fiveG24 = (fiveG24.length() == 0) ? "[0]" : "[" + fiveG24 + "]";
				fiveB24 = (fiveB24.length() == 0) ? "[0]" : "[" + fiveB24 + "]";
				rotationTime2 = (rotationTime2.length() == 0) ? "0"
						: rotationTime2;
				tvOne1.setText(one24);
				tvTwo1.setText(two24);
				tvThree1.setText(three24);
				tvFour1.setText(four24);
				tvFive1.setText(five24);
				tvSix1.setText(six24);
				tvSeven1.setText(seven24);
				tvEight1.setText(eight24);
				tvOneR1.setText(oneR24);
				tvOneG1.setText(oneG24);
				tvOneB1.setText(oneB24);
				tvTwoR1.setText(twoR24);
				tvTwoG1.setText(twoG24);
				tvTwoB1.setText(twoB24);
				tvThreeR1.setText(threeR24);
				tvThreeG1.setText(threeG24);
				tvThreeB1.setText(threeB24);
				tvFourR1.setText(fourR24);
				tvFourG1.setText(fourG24);
				tvFourB1.setText(fourB24);
				tvFiveR1.setText(fiveR24);
				tvFiveG1.setText(fiveG24);
				tvFiveB1.setText(fiveB24);
				tvRotationTime1.setText(rotationTime2);

				break;
			case 2:
				one34 = (one34.length() == 0) ? "[0]" : "[" + one34 + "]";
				two34 = (two34.length() == 0) ? "[0]" : "[" + two34 + "]";
				three34 = (three34.length() == 0) ? "[0]" : "[" + three34 + "]";
				four34 = (four34.length() == 0) ? "[0]" : "[" + four34 + "]";
				five34 = (five34.length() == 0) ? "[0]" : "[" + five34 + "]";
				six34 = (six34.length() == 0) ? "[0]" : "[" + six34 + "]";
				seven34 = (seven34.length() == 0) ? "[0]" : "[" + seven34 + "]";
				eight34 = (eight34.length() == 0) ? "[0]" : "[" + eight34 + "]";
				oneR34 = (oneR34.length() == 0) ? "[0]" : "[" + oneR34 + "]";
				oneG34 = (oneG34.length() == 0) ? "[0]" : "[" + oneG34 + "]";
				oneB34 = (oneB34.length() == 0) ? "[0]" : "[" + oneB34 + "]";
				twoR34 = (twoR34.length() == 0) ? "[0]" : "[" + twoR34 + "]";
				twoG34 = (twoG34.length() == 0) ? "[0]" : "[" + twoG34 + "]";
				twoB34 = (twoB34.length() == 0) ? "[0]" : "[" + twoB34 + "]";
				threeR34 = (threeR34.length() == 0) ? "[0]" : "[" + threeR34
						+ "]";
				threeG34 = (threeG34.length() == 0) ? "[0]" : "[" + threeG34
						+ "]";
				threeB34 = (threeB34.length() == 0) ? "[0]" : "[" + threeB34
						+ "]";
				fourR34 = (fourR34.length() == 0) ? "[0]" : "[" + fourR34 + "]";
				fourG34 = (fourG34.length() == 0) ? "[0]" : "[" + fourG34 + "]";
				fourB34 = (fourB34.length() == 0) ? "[0]" : "[" + fourB34 + "]";
				fiveR34 = (fiveR34.length() == 0) ? "[0]" : "[" + fiveR34 + "]";
				fiveG34 = (fiveG34.length() == 0) ? "[0]" : "[" + fiveG34 + "]";
				fiveB34 = (fiveB34.length() == 0) ? "[0]" : "[" + fiveB34 + "]";
				rotationTime3 = (rotationTime3.length() == 0) ? "0"
						: rotationTime3;
				tvOne1.setText(one34);
				tvTwo1.setText(two34);
				tvThree1.setText(three34);
				tvFour1.setText(four34);
				tvFive1.setText(five34);
				tvSix1.setText(six34);
				tvSeven1.setText(seven34);
				tvEight1.setText(eight34);
				tvOneR1.setText(oneR34);
				tvOneG1.setText(oneG34);
				tvOneB1.setText(oneB34);
				tvTwoR1.setText(twoR34);
				tvTwoG1.setText(twoG34);
				tvTwoB1.setText(twoB34);
				tvThreeR1.setText(threeR34);
				tvThreeG1.setText(threeG34);
				tvThreeB1.setText(threeB34);
				tvFourR1.setText(fourR34);
				tvFourG1.setText(fourG34);
				tvFourB1.setText(fourB34);
				tvFiveR1.setText(fiveR34);
				tvFiveG1.setText(fiveG34);
				tvFiveB1.setText(fiveB34);
				tvRotationTime1.setText(rotationTime3);
				break;
			case 3:
				one44 = (one44.length() == 0) ? "[0]" : "[" + one44 + "]";
				two44 = (two44.length() == 0) ? "[0]" : "[" + two44 + "]";
				three44 = (three44.length() == 0) ? "[0]" : "[" + three44 + "]";
				four44 = (four44.length() == 0) ? "[0]" : "[" + four44 + "]";
				five44 = (five44.length() == 0) ? "[0]" : "[" + five44 + "]";
				six44 = (six44.length() == 0) ? "[0]" : "[" + six44 + "]";
				seven44 = (seven44.length() == 0) ? "[0]" : "[" + seven44 + "]";
				eight44 = (eight44.length() == 0) ? "[0]" : "[" + eight44 + "]";
				oneR44 = (oneR44.length() == 0) ? "[0]" : "[" + oneR44 + "]";
				oneG44 = (oneG44.length() == 0) ? "[0]" : "[" + oneG44 + "]";
				oneB44 = (oneB44.length() == 0) ? "[0]" : "[" + oneB44 + "]";
				twoR44 = (twoR44.length() == 0) ? "[0]" : "[" + twoR44 + "]";
				twoG44 = (twoG44.length() == 0) ? "[0]" : "[" + twoG44 + "]";
				twoB44 = (twoB44.length() == 0) ? "[0]" : "[" + twoB44 + "]";
				threeR44 = (threeR44.length() == 0) ? "[0]" : "[" + threeR44
						+ "]";
				threeG44 = (threeG44.length() == 0) ? "[0]" : "[" + threeG44
						+ "]";
				threeB44 = (threeB44.length() == 0) ? "[0]" : "[" + threeB44
						+ "]";
				fourR44 = (fourR44.length() == 0) ? "[0]" : "[" + fourR44 + "]";
				fourG44 = (fourG44.length() == 0) ? "[0]" : "[" + fourG44 + "]";
				fourB44 = (fourB44.length() == 0) ? "[0]" : "[" + fourB44 + "]";
				fiveR44 = (fiveR44.length() == 0) ? "[0]" : "[" + fiveR44 + "]";
				fiveG44 = (fiveG44.length() == 0) ? "[0]" : "[" + fiveG44 + "]";
				fiveB44 = (fiveB44.length() == 0) ? "[0]" : "[" + fiveB44 + "]";
				rotationTime4 = (rotationTime4.length() == 0) ? "0"
						: rotationTime4;
				tvOne1.setText(one44);
				tvTwo1.setText(two44);
				tvThree1.setText(three44);
				tvFour1.setText(four44);
				tvFive1.setText(five44);
				tvSix1.setText(six44);
				tvSeven1.setText(seven44);
				tvEight1.setText(eight44);
				tvOneR1.setText(oneR44);
				tvOneG1.setText(oneG44);
				tvOneB1.setText(oneB44);
				tvTwoR1.setText(twoR44);
				tvTwoG1.setText(twoG44);
				tvTwoB1.setText(twoB44);
				tvThreeR1.setText(threeR44);
				tvThreeG1.setText(threeG44);
				tvThreeB1.setText(threeB44);
				tvFourR1.setText(fourR44);
				tvFourG1.setText(fourG44);
				tvFourB1.setText(fourB44);
				tvFiveR1.setText(fiveR44);
				tvFiveG1.setText(fiveG44);
				tvFiveB1.setText(fiveB44);
				tvRotationTime1.setText(rotationTime4);
				break;
			case 4:
				one54 = (one54.length() == 0) ? "[0]" : "[" + one54 + "]";
				two54 = (two54.length() == 0) ? "[0]" : "[" + two54 + "]";
				three54 = (three54.length() == 0) ? "[0]" : "[" + three54 + "]";
				four54 = (four54.length() == 0) ? "[0]" : "[" + four54 + "]";
				five54 = (five54.length() == 0) ? "[0]" : "[" + five54 + "]";
				six54 = (six54.length() == 0) ? "[0]" : "[" + six54 + "]";
				seven54 = (seven54.length() == 0) ? "[0]" : "[" + seven54 + "]";
				eight54 = (eight54.length() == 0) ? "[0]" : "[" + eight54 + "]";
				oneR54 = (oneR54.length() == 0) ? "[0]" : "[" + oneR54 + "]";
				oneG54 = (oneG54.length() == 0) ? "[0]" : "[" + oneG54 + "]";
				oneB54 = (oneB54.length() == 0) ? "[0]" : "[" + oneB54 + "]";
				twoR54 = (twoR54.length() == 0) ? "[0]" : "[" + twoR54 + "]";
				twoG54 = (twoG54.length() == 0) ? "[0]" : "[" + twoG54 + "]";
				twoB54 = (twoB54.length() == 0) ? "[0]" : "[" + twoB54 + "]";
				threeR54 = (threeR54.length() == 0) ? "[0]" : "[" + threeR54
						+ "]";
				threeG54 = (threeG54.length() == 0) ? "[0]" : "[" + threeG54
						+ "]";
				threeB54 = (threeB54.length() == 0) ? "[0]" : "[" + threeB54
						+ "]";
				fourR54 = (fourR54.length() == 0) ? "[0]" : "[" + fourR54 + "]";
				fourG54 = (fourG54.length() == 0) ? "[0]" : "[" + fourG54 + "]";
				fourB54 = (fourB54.length() == 0) ? "[0]" : "[" + fourB54 + "]";
				fiveR54 = (fiveR54.length() == 0) ? "[0]" : "[" + fiveR54 + "]";
				fiveG54 = (fiveG54.length() == 0) ? "[0]" : "[" + fiveG54 + "]";
				fiveB54 = (fiveB54.length() == 0) ? "[0]" : "[" + fiveB54 + "]";
				rotationTime5 = (rotationTime5.length() == 0) ? "0"
						: rotationTime5;
				tvOne1.setText(one54);
				tvTwo1.setText(two54);
				tvThree1.setText(three54);
				tvFour1.setText(four54);
				tvFive1.setText(five54);
				tvSix1.setText(six54);
				tvSeven1.setText(seven54);
				tvEight1.setText(eight54);
				tvOneR1.setText(oneR54);
				tvOneG1.setText(oneG54);
				tvOneB1.setText(oneB54);
				tvTwoR1.setText(twoR54);
				tvTwoG1.setText(twoG54);
				tvTwoB1.setText(twoB54);
				tvThreeR1.setText(threeR54);
				tvThreeG1.setText(threeG54);
				tvThreeB1.setText(threeB54);
				tvFourR1.setText(fourR54);
				tvFourG1.setText(fourG54);
				tvFourB1.setText(fourB54);
				tvFiveR1.setText(fiveR54);
				tvFiveG1.setText(fiveG54);
				tvFiveB1.setText(fiveB54);
				tvRotationTime1.setText(rotationTime5);
				break;
			case 5:
				one64 = (one64.length() == 0) ? "[0]" : "[" + one64 + "]";
				two64 = (two64.length() == 0) ? "[0]" : "[" + two64 + "]";
				three64 = (three64.length() == 0) ? "[0]" : "[" + three64 + "]";
				four64 = (four64.length() == 0) ? "[0]" : "[" + four64 + "]";
				five64 = (five64.length() == 0) ? "[0]" : "[" + five64 + "]";
				six64 = (six64.length() == 0) ? "[0]" : "[" + six64 + "]";
				seven64 = (seven64.length() == 0) ? "[0]" : "[" + seven64 + "]";
				eight64 = (eight64.length() == 0) ? "[0]" : "[" + eight64 + "]";
				oneR64 = (oneR64.length() == 0) ? "[0]" : "[" + oneR64 + "]";
				oneG64 = (oneG64.length() == 0) ? "[0]" : "[" + oneG64 + "]";
				oneB64 = (oneB64.length() == 0) ? "[0]" : "[" + oneB64 + "]";
				twoR64 = (twoR64.length() == 0) ? "[0]" : "[" + twoR64 + "]";
				twoG64 = (twoG64.length() == 0) ? "[0]" : "[" + twoG64 + "]";
				twoB64 = (twoB64.length() == 0) ? "[0]" : "[" + twoB64 + "]";
				threeR64 = (threeR64.length() == 0) ? "[0]" : "[" + threeR64
						+ "]";
				threeG64 = (threeG64.length() == 0) ? "[0]" : "[" + threeG64
						+ "]";
				threeB64 = (threeB64.length() == 0) ? "[0]" : "[" + threeB64
						+ "]";
				fourR64 = (fourR64.length() == 0) ? "[0]" : "[" + fourR64 + "]";
				fourG64 = (fourG64.length() == 0) ? "[0]" : "[" + fourG64 + "]";
				fourB64 = (fourB64.length() == 0) ? "[0]" : "[" + fourB64 + "]";
				fiveR64 = (fiveR64.length() == 0) ? "[0]" : "[" + fiveR64 + "]";
				fiveG64 = (fiveG64.length() == 0) ? "[0]" : "[" + fiveG64 + "]";
				fiveB64 = (fiveB64.length() == 0) ? "[0]" : "[" + fiveB64 + "]";
				rotationTime6 = (rotationTime6.length() == 0) ? "0"
						: rotationTime6;
				tvOne1.setText(one64);
				tvTwo1.setText(two64);
				tvThree1.setText(three64);
				tvFour1.setText(four64);
				tvFive1.setText(five64);
				tvSix1.setText(six64);
				tvSeven1.setText(seven64);
				tvEight1.setText(eight64);
				tvOneR1.setText(oneR64);
				tvOneG1.setText(oneG64);
				tvOneB1.setText(oneB64);
				tvTwoR1.setText(twoR64);
				tvTwoG1.setText(twoG64);
				tvTwoB1.setText(twoB64);
				tvThreeR1.setText(threeR64);
				tvThreeG1.setText(threeG64);
				tvThreeB1.setText(threeB64);
				tvFourR1.setText(fourR64);
				tvFourG1.setText(fourG64);
				tvFourB1.setText(fourB64);
				tvFiveR1.setText(fiveR64);
				tvFiveG1.setText(fiveG64);
				tvFiveB1.setText(fiveB64);
				tvRotationTime1.setText(rotationTime6);
				break;
			case 6:
				one74 = (one74.length() == 0) ? "[0]" : "[" + one74 + "]";
				two74 = (two74.length() == 0) ? "[0]" : "[" + two74 + "]";
				three74 = (three74.length() == 0) ? "[0]" : "[" + three74 + "]";
				four74 = (four74.length() == 0) ? "[0]" : "[" + four74 + "]";
				five74 = (five74.length() == 0) ? "[0]" : "[" + five74 + "]";
				six74 = (six74.length() == 0) ? "[0]" : "[" + six74 + "]";
				seven74 = (seven74.length() == 0) ? "[0]" : "[" + seven74 + "]";
				eight74 = (eight74.length() == 0) ? "[0]" : "[" + eight74 + "]";
				oneR74 = (oneR74.length() == 0) ? "[0]" : "[" + oneR74 + "]";
				oneG74 = (oneG74.length() == 0) ? "[0]" : "[" + oneG74 + "]";
				oneB74 = (oneB74.length() == 0) ? "[0]" : "[" + oneB74 + "]";
				twoR74 = (twoR74.length() == 0) ? "[0]" : "[" + twoR74 + "]";
				twoG74 = (twoG74.length() == 0) ? "[0]" : "[" + twoG74 + "]";
				twoB74 = (twoB74.length() == 0) ? "[0]" : "[" + twoB74 + "]";
				threeR74 = (threeR74.length() == 0) ? "[0]" : "[" + threeR74
						+ "]";
				threeG74 = (threeG74.length() == 0) ? "[0]" : "[" + threeG74
						+ "]";
				threeB74 = (threeB74.length() == 0) ? "[0]" : "[" + threeB74
						+ "]";
				fourR74 = (fourR74.length() == 0) ? "[0]" : "[" + fourR74 + "]";
				fourG74 = (fourG74.length() == 0) ? "[0]" : "[" + fourG74 + "]";
				fourB74 = (fourB74.length() == 0) ? "[0]" : "[" + fourB74 + "]";
				fiveR74 = (fiveR74.length() == 0) ? "[0]" : "[" + fiveR74 + "]";
				fiveG74 = (fiveG74.length() == 0) ? "[0]" : "[" + fiveG74 + "]";
				fiveB74 = (fiveB74.length() == 0) ? "[0]" : "[" + fiveB74 + "]";
				rotationTime7 = (rotationTime7.length() == 0) ? "0"
						: rotationTime7;

				tvOne1.setText(one74);
				tvTwo1.setText(two74);
				tvThree1.setText(three74);
				tvFour1.setText(four74);
				tvFive1.setText(five74);
				tvSix1.setText(six74);
				tvSeven1.setText(seven74);
				tvEight1.setText(eight74);
				tvOneR1.setText(oneR74);
				tvOneG1.setText(oneG74);
				tvOneB1.setText(oneB74);
				tvTwoR1.setText(twoR74);
				tvTwoG1.setText(twoG74);
				tvTwoB1.setText(twoB74);
				tvThreeR1.setText(threeR74);
				tvThreeG1.setText(threeG74);
				tvThreeB1.setText(threeB74);
				tvFourR1.setText(fourR74);
				tvFourG1.setText(fourG74);
				tvFourB1.setText(fourB74);
				tvFiveR1.setText(fiveR74);
				tvFiveG1.setText(fiveG74);
				tvFiveB1.setText(fiveB74);
				tvRotationTime1.setText(rotationTime7);
				break;
			}
		} else {
			rl1.setVisibility(View.GONE);
		}

		ll.addView(v);

	}

	private void initView() {
		ll1 = (LinearLayout) findViewById(R.id.ll_current_plan_1);
		ll2 = (LinearLayout) findViewById(R.id.ll_current_plan_2);
		ll3 = (LinearLayout) findViewById(R.id.ll_current_plan_3);
		ll4 = (LinearLayout) findViewById(R.id.ll_current_plan_4);
		ll5 = (LinearLayout) findViewById(R.id.ll_current_plan_5);
		ll6 = (LinearLayout) findViewById(R.id.ll_current_plan_6);
		ll7 = (LinearLayout) findViewById(R.id.ll_current_plan_7);
		back = (Button) findViewById(R.id.btn_device_current_plan_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(DeviceCurrentPlanActivity.this,SingleColorAttributeActivity.class);
//				intent.putExtra("currentPlanType", currentPlanType); // 1表示为设备当前组计划
//				intent.putExtra("myDevice", myDevice);
//				intent.putExtra("type", type);
//				startActivity(intent);
				DeviceCurrentPlanActivity.this.finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		Log.e(TAG, "onDestroy");
		super.onDestroy();
//		AC.customDataMgr().unregisterDataReceiver(receiver1); //不要取消订阅
	}

	private String addO(String s) {
		String str = null;
		if (ItonAdecimalConver.hexStringToAlgorism(s) < 10) {
			str = "0" + ItonAdecimalConver.hexStringToAlgorism(s);
		} else {
			str = ItonAdecimalConver.hexStringToAlgorism(s) + "";
		}
		return str;
	}
}
