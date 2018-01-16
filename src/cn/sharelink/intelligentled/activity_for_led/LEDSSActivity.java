package cn.sharelink.intelligentled.activity_for_led;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import cn.sharelink.intelligentled.R;
import cn.sharelink.intelligentled.R.id;
import cn.sharelink.intelligentled.R.layout;
import cn.sharelink.intelligentled.activity.ConfigurationActivity;
import cn.sharelink.intelligentled.application.MainApplication;
import cn.sharelink.intelligentled.config.Config;
import cn.sharelink.intelligentled.utils.GsonUtil;
import cn.sharelink.intelligentled.utils.ItonAdecimalConver;
import cn.sharelink.intelligentled.utils.MyDevice;
import cn.sharelink.intelligentled.utils.OnReceive;
import cn.sharelink.intelligentled.utils.SendData;

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

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class LEDSSActivity extends Activity {
	private static final String TAG = LEDSSActivity.class.getSimpleName();
	List<MyDevice> myDeviceList;
	Map<String, Receiver<TopicData>> maps;
	// Receiver<TopicData> receiver;// 订阅
	private String subDomain;

	private Button send;
	String message= "0102030405060708090A";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		setContentView(R.layout.activity_ledss);
		myDeviceList = (List<MyDevice>) getIntent().getSerializableExtra(
				"device");
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 54; i++) {
			builder.append(message);
		}
		message = builder.toString()+"01020304050607";
		
		Log.e(TAG, message.length()/2+"");
		maps = new HashMap<String, Receiver<TopicData>>();
		send = (Button) findViewById(R.id.btn_sends);
		send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				for (MyDevice device : myDeviceList) {
					sendData(device.getPhysicalDeviceID(), message);
				}
			}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		for (int i = 0; i < myDeviceList.size(); i++) {
			Log.e(TAG, myDeviceList.get(i).getPhysicalDeviceID() + "/"
					+ myDeviceList.get(i).getDeviceID());
			subscribe("xinlian01", "topic_type", myDeviceList.get(i)
					.getDeviceID());// 订阅，可获取到返回值
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
						Log.e(TAG, deviceId+"订阅成功");
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
				Log.e("接收到的返回值", payload);

			}

		};
		maps.put(deviceId, receiver);
		AC.customDataMgr().registerDataReceiver(receiver);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		subDomain = PreferencesUtils.getString(this, "subDomain",
				Config.SUBDOMAIN);
	}

	public void sendData(final String physicalDeviceId, String message) {
		Log.e(TAG, "物理ID的6060 ：" + physicalDeviceId + ","
//	+ "发送数据message :"+ message
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
						Log.e(TAG, "失败的回调"+arg0.getMessage());
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		for(String str:maps.keySet()){
			Log.e(TAG, "OnDestroy");
			AC.customDataMgr().unregisterDataReceiver(maps.get(str));
		}
	}

}
