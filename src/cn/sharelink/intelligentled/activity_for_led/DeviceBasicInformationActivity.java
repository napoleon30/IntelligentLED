package cn.sharelink.intelligentled.activity_for_led;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.VoidCallback;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.sharelink.intelligentled.R;
import cn.sharelink.intelligentled.config.Config;
import cn.sharelink.intelligentled.utils.GsonUtil;
import cn.sharelink.intelligentled.utils.ItonAdecimalConver;
import cn.sharelink.intelligentled.utils.OnReceive;
import cn.sharelink.intelligentled.utils.SendData;

/**
 * 设备基础信息界面
 * @author Administrator
 * 
 */
public class DeviceBasicInformationActivity extends Activity {
	private static final String TAG = DeviceBasicInformationActivity.class
			.getSimpleName();

	private Button back;
	private TextView appVersion, deviceVersion, companyName, reStart, reSet;

	private ACUserDevice myDevice;
	private int type;

	Receiver<TopicData> receiver;// 订阅
	private String subDomain;
	SendData senddata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		setContentView(R.layout.activity_device_basic_information);
		myDevice = (ACUserDevice) getIntent().getSerializableExtra("myDevice");
		type = getIntent().getIntExtra("TYPE", 0);
		initView();
	}

	@Override
	protected void onStart() {
		super.onStart();
		subscribe("xinlian01", "topic_type", myDevice.getDeviceId() + "");// 订阅，可获取到返回值
	}

	@Override
	protected void onResume() {
		super.onResume();
		subDomain = PreferencesUtils.getString(this, "subDomain",
				Config.SUBDOMAIN);
		senddata = new SendData(subDomain, myDevice.getPhysicalDeviceId());
		
	}

	/**
	 * 订阅方法和获取返回值
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
						Log.e("/*/*/*", "订阅失败");
					}

					@Override
					public void success() {
						Log.e("/*/*/*", "订阅成功");
						//获取设备基础信息，软件版本号、硬件版本号、厂商名称
						senddata.sendData("6607000099");
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
				//解析06、07
				/////////////////////////////////////////

			}

		};
		AC.customDataMgr().registerDataReceiver(receiver);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AC.customDataMgr().unregisterDataReceiver(receiver);
	}

	private void initView() {
		back = (Button) findViewById(R.id.btn_device_basic_back);
		appVersion = (TextView) findViewById(R.id.tv_app_verison);
		deviceVersion = (TextView) findViewById(R.id.tv_device_verison);
		companyName = (TextView) findViewById(R.id.tv_company_name);
		reStart = (TextView) findViewById(R.id.tv_device_restart);
		reSet = (TextView) findViewById(R.id.tv_device_reset);
		back.setOnClickListener(listener);
		reStart.setOnClickListener(listener);
		reSet.setOnClickListener(listener);
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_device_basic_back:
				DeviceBasicInformationActivity.this.finish();
				break;

			case R.id.tv_device_restart:// 重启设备
				senddata.sendData("660600010599");
				break;
			case R.id.tv_device_reset: // 恢复出厂设置
				senddata.sendData("660600010699");
				break;
			}

		}
	};
}
