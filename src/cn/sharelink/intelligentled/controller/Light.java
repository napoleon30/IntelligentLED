package cn.sharelink.intelligentled.controller;

import java.util.Arrays;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import cn.sharelink.intelligentled.R;
import cn.sharelink.intelligentled.activity.ConfigurationActivity;
import cn.sharelink.intelligentled.application.MainApplication;
import cn.sharelink.intelligentled.config.Config;
import cn.sharelink.intelligentled.utils.Pop;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.service.ACDeviceMsg;
import com.accloud.service.ACException;
import com.accloud.utils.PreferencesUtils;
import org.json.JSONException;
import org.json.JSONObject;


public class Light {
	private static final int OPENLIGHT = 1;
	private static final int CLOSELIGHT = 0;
	private Context context;
	private String subDomain;

	public Light(Context context) {
		this.context = context;
		subDomain = PreferencesUtils.getString(MainApplication.getInstance(),
				"subDomain", Config.SUBDOMAIN);
	}

	public void openLight(String physicalDeviceId) {
		/**
		 * 通过云端服务�?设备发�?�命�?/消息
		 * 
		 * @param subDomain
		 *            子域名，如glass（智能眼镜）
		 * @param deviceId
		 *            设备逻辑id
		 * @param msg
		 *            具体的消息内�?
		 * 
		 * @return 设备返回的监听回调，返回设备的响应消�?
		 */


		AC.bindMgr().sendToDeviceWithOption(subDomain, physicalDeviceId,
				getDeviceMsg(OPENLIGHT), AC.LOCAL_FIRST,
				new PayloadCallback<ACDeviceMsg>() {
					@Override
					public void success(ACDeviceMsg msg) {
						if (parseDeviceMsg1(msg)) {
							Log.e("�?灯返回消�?", msg + "");
							Pop.popToast(
									context,
									context.getString(R.string.main_aty_openlight_success));
						} else {
							Pop.popToast(
									context,
									context.getString(R.string.main_aty_openlight_fail));
						}
					}

					@Override
					public void error(ACException e) {
						Toast.makeText(context,
								e.getErrorCode() + "-->" + e.getMessage(),
								Toast.LENGTH_LONG).show();
					}
				});
		Log.e("�?灯发送的消息", getDeviceMsg(OPENLIGHT) + "");
	}

	public void closeLight(String physicalDeviceId) {
		/**
		 * 通过云端服务�?设备发�?�命�?/消息
		 * 
		 * @param subDomain
		 *            子域名，如glass（智能眼镜）
		 * @param deviceId
		 *            设备逻辑id
		 * @param msg
		 *            具体的消息内�?
		 * 
		 * @return 设备返回的监听回调，返回设备的响应消�?
		 */
		AC.bindMgr().sendToDeviceWithOption(subDomain, physicalDeviceId,
				getDeviceMsg(CLOSELIGHT), AC.LOCAL_FIRST,
				new PayloadCallback<ACDeviceMsg>() {
					@Override
					public void success(ACDeviceMsg msg) {
						if (parseDeviceMsg0(msg)) {
							Log.e("关灯返回消息", msg + "");
							Pop.popToast(
									context,
									context.getString(R.string.main_aty_closelight_success));
						} else {
							Pop.popToast(
									context,
									context.getString(R.string.main_aty_closelight_fail));
						}
					}

					@Override
					public void error(ACException e) {
						Toast.makeText(context,
								e.getErrorCode() + "-->" + e.getMessage(),
								Toast.LENGTH_LONG).show();
					}
				});
		Log.e("关灯发�?�的消息", getDeviceMsg(CLOSELIGHT) + "");
	}

	private ACDeviceMsg getDeviceMsg(int action) {
		// 注意：实际开发的时�?�请选择其中的一种消息格式即�?
		switch (getFormatType()) {
		case ConfigurationActivity.BINARY:

			return new ACDeviceMsg(Config.LIGHT_MSGCODE, new byte[] {
					(byte) action, 0, 0, 0 });
		case ConfigurationActivity.JSON:
			JSONObject object = new JSONObject();
			try {
				object.put("switch", action);
			} catch (JSONException e) {
			}
			return new ACDeviceMsg(70, object.toString().getBytes());
		}
		return null;
	}

	private boolean parseDeviceMsg1(ACDeviceMsg msg) {
		// 注意：实际开发的时�?�请选择其中的一种消息格式即�?
		switch (getFormatType()) {
		case ConfigurationActivity.BINARY:
			byte[] bytes = msg.getContent();
			if (bytes != null)
				return bytes[0] == 1 ? true : false;
		case ConfigurationActivity.JSON:
			try {
				JSONObject object = new JSONObject(new String(msg.getContent()));
				return object.optBoolean("result");
			} catch (Exception e) {
			}
		}
		return false;
	}

	private boolean parseDeviceMsg0(ACDeviceMsg msg) {
		// 注意：实际开发的时�?�请选择其中的一种消息格式即�?
		switch (getFormatType()) {
		case ConfigurationActivity.BINARY:
			byte[] bytes = msg.getContent();
			if (bytes != null)
				return bytes[0] == 0 ? true : false;
		case ConfigurationActivity.JSON:
			try {
				JSONObject object = new JSONObject(new String(msg.getContent()));
				return object.optBoolean("result");
			} catch (Exception e) {
			}
		}
		return false;
	}

	// 注意：实际开发消息格式为已知，无�?选择
	public int getFormatType() {
		return PreferencesUtils.getInt(context, "formatType",
				ConfigurationActivity.BINARY);
	}
}
