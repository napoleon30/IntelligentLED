package cn.sharelink.intelligentled.utils;

import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import cn.sharelink.intelligentled.activity.ConfigurationActivity;
import cn.sharelink.intelligentled.application.MainApplication;
import cn.sharelink.intelligentled.config.Config;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.service.ACDeviceMsg;
import com.accloud.service.ACException;
import com.accloud.utils.PreferencesUtils;

public class SendData {

	String subDomain, physicalDeviceId;

	public SendData(String subDomain, String physicalDeviceId) {
		super();
		this.subDomain = subDomain;
		this.physicalDeviceId = physicalDeviceId;
	}

	public void sendData(String message) {
		Log.e("send_message", message);

		if (message.equals("66C50CnullnullnullnullnullnullnullnullFF1D100099")) {
			message = "66C50C0000000000000000FF1D100099";
		} else if (message
				.equals("66C50Cnullnullnullnullnullnullnullnull1086FF0099")) {
			message = "66C50C00000000000000001086FF0099";
		}

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
						if (parseDeviceMsg(msg)) {
							String returnedValue = ItonAdecimalConver
									.byte2hex(msg.getContent());
							Log.e("callBack���ص���Ϣʮ������ת��", returnedValue);
						}
					}

					@Override
					public void error(ACException arg0) {
						// TODO Auto-generated method stub
					}

				});
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

	private ACDeviceMsg getDeviceMsg(byte[] b) {
		switch (getFormatType()) {
		case ConfigurationActivity.BINARY:

			return new ACDeviceMsg(Config.LIGHT_MSGCODE, b);
		case ConfigurationActivity.JSON:
			JSONObject object = new JSONObject();
			try {
				object.put("switch", b);
			} catch (JSONException e) {
			}
			return new ACDeviceMsg(70, object.toString().getBytes());
		}
		return null;
	}

	private int getFormatType() {
		return PreferencesUtils.getInt(MainApplication.getInstance(),
				"formatType", ConfigurationActivity.BINARY);
	}

	protected boolean parseDeviceMsg(ACDeviceMsg msg) {
		switch (getFormatType()) {
		case ConfigurationActivity.BINARY:
			byte[] bytes = msg.getContent();
			if (bytes != null)
				// return bytes[0] == 0x69 ? true : false;//
				return true;
		case ConfigurationActivity.JSON:
			try {
				JSONObject object = new JSONObject(new String(msg.getContent()));
				return object.optBoolean("result");
			} catch (Exception e) {
			}
		}
		return false;
	}

}
