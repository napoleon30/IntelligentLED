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

public class SendDataBle {
	
	String subDomain,physicalDeviceId;
	String message3;
	String message;
	String message1 = "6974637A";
	String message5;
	
	

	public SendDataBle(String subDomain, String physicalDeviceId) {
		super();
		this.subDomain = subDomain;
		this.physicalDeviceId = physicalDeviceId;
	}

	/**
	 * ��������
	 * 
	 * @param string2
	 *            Commant_t
	 * @param string4
	 *            ��������
	 */
	public void sendData(String string2, String string4) {
		message5 = checkSum(string4);// ����У��λ

		if (string4 == "") {
			message3 = "0000";
		} else if (string4 != "") {
			if (string4.length() >= 32) {
				message3 = "00" + Integer.toHexString(string4.length() / 2);
			} else {
				message3 = "000" + Integer.toHexString(string4.length() / 2);// ???????????????ʮ����intתʮ�������ַ���
			}
		}
		message = message1 + string2 + message3 + string4 + message5;
		Log.e("message", message);
		byte[] midbytes = message.getBytes();
		// Log.e("���͵�����byte[]��ʮ���ƣ�", "*********" + Arrays.toString(midbytes));
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
					public void error(ACException e) {
						// Toast.makeText(MainActivity.this,
						// e.getErrorCode() + "-->" + e.getMessage(),
						// Toast.LENGTH_LONG).show();
					}
				});
		// Log.e("���͵���Ϣ", getDeviceMsg(b) + "");
	}
	
	protected boolean parseDeviceMsg(ACDeviceMsg msg) {
		// ע�⣺ʵ�ʿ�����ʱ����ѡ�����е�һ����Ϣ��ʽ����
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
	
	private ACDeviceMsg getDeviceMsg(byte[] b) {
		// ע�⣺ʵ�ʿ�����ʱ����ѡ�����е�һ����Ϣ��ʽ����
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
		return PreferencesUtils.getInt(MainApplication.getInstance(), "formatType",
				ConfigurationActivity.BINARY);
	}

	/**
	 * ���������ʮ���������ݣ�����У��λ
	 */
	public String checkSum(String string) {
		/**
		 * ��ָ���ַ���src����ÿ�����ַ��ָ�ת��Ϊ16������ʽ �磺"2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF,
		 * 0xD9}
		 * 
		 * @param src
		 * @return byte[]
		 */
		String results = null;
		String pingjie = null;
		if (string == null) {
			results = "00";
		} else if (string != null) {
			byte[] bytes = new byte[string.length() / 2]; // ���鳤��Ϊresult.length()/2
			int[] ints = new int[string.length() / 2];
			byte[] tmp = string.getBytes();
			int sum = 0;
			for (int i = 0; i < string.length() / 2; i++) {
				bytes[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
				ints[i] = bytes[i];
				if (ints[i] < 0) {
					ints[i] += 256;
				}
				sum += ints[i];
				// Log.e("AAAAA", ints[i] + "");
				// Log.e("sum", sum + "");
			}
			int res = 255 - sum + 1; // ʮ���Ƽ���
			// Log.e("res", res + "");

			if (res > 0) { // ���Ϊ������У����Ϊ��ʮ��������
				results = Integer.toHexString(res);// ʮ����intתʮ�������ַ���
				results = results.substring(results.length() - 2);
			} else if (res < 0) {
				String tenToBinary = Integer.toBinaryString(-res);// ʮ����ת�������ַ���
				if (tenToBinary.length() >= 8) {
					String substr = tenToBinary
							.substring(tenToBinary.length() - 8);
					// Log.e("string1", substr);
					byte[] bytes1 = substr.getBytes();
					Log.e("bytes[i]", Arrays.toString(bytes1));
					for (int i = 0; i < bytes1.length; i++) {
						if (bytes1[i] == 49) { // 49��ʾ1
							bytes1[i] = 48; // 48��ʾ0
							// Log.e("bytes[i]", bytes1[i] + "");
						} else if (bytes1[i] == 48) {
							bytes1[i] = 49;
							// Log.e("bytes[i]", bytes1[i] + "");
						}
					}
					String sub = new String(bytes1);
					// Log.e("sub", sub);
					int in = Integer.parseInt(sub, 2);
					in = in + 1;
					// Log.e("in", in + "");
					results = algorismToHEXString(in);// ʮ����intתʮ�������ַ���

				} else if (tenToBinary.length() < 8) {
					if (tenToBinary.length() == 1) {
						pingjie = "1000000" + tenToBinary;
					} else if (tenToBinary.length() == 2) {
						pingjie = "100000" + tenToBinary;
					} else if (tenToBinary.length() == 3) {
						pingjie = "10000" + tenToBinary;
					} else if (tenToBinary.length() == 4) {
						pingjie = "1000" + tenToBinary;
					} else if (tenToBinary.length() == 5) {
						pingjie = "100" + tenToBinary;
					} else if (tenToBinary.length() == 6) {
						pingjie = "10" + tenToBinary;
					} else if (tenToBinary.length() == 7) {
						pingjie = "1" + tenToBinary;
					}
					// Log.e("pingjie", pingjie);
					int BinaryToTen = binaryToAlgorism(pingjie); // �������ַ���תʮ����int
					results = algorismToHEXString(BinaryToTen);// ʮ����intתʮ�������ַ���
				}

			} else if (res == 0) {
				results = "00";
			}
		}
		return results;
	}
	
	/**
	 * ʮ����ת��Ϊʮ�������ַ���
	 * 
	 * @param algorism
	 *            int ʮ���Ƶ�����
	 * @return String ��Ӧ��ʮ�������ַ���
	 */
	public static String algorismToHEXString(int algorism) {
		String result = "";
		result = Integer.toHexString(algorism);

		if (result.length() % 2 == 1) {
			result = "0" + result;

		}
		result = result.toUpperCase();

		return result;
	}
	
	/**
	 * �������ַ���תʮ����
	 * 
	 * @param binary
	 *            �������ַ���
	 * @return ʮ������ֵ
	 */
	public static int binaryToAlgorism(String binary) {
		int max = binary.length();
		int result = 0;
		for (int i = max; i > 0; i--) {
			char c = binary.charAt(i - 1);
			int algorism = c - '0';
			result += Math.pow(2, max - i) * algorism;
		}
		return result;
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

}
