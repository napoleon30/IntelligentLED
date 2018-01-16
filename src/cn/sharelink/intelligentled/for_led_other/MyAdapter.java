package cn.sharelink.intelligentled.for_led_other;

import java.util.List;

import cn.sharelink.intelligentled.R;
import cn.sharelink.intelligentled.controller.Light;
import cn.sharelink.intelligentled.sql2.Device;
import cn.sharelink.intelligentled.utils.ViewHolder;

import com.accloud.service.ACUserDevice;
import com.accloud.utils.PreferencesUtils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{
	public List<ACUserDevice> deviceList;
	private Context context;
	private Light light;
	String physicalDeviceId,deviceId;

	public MyAdapter(Context context,List<ACUserDevice> deviceList) {
		this.context = context;
		light = new Light(context);
		this.deviceList = deviceList;
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
				R.layout.adapter_list_device, null);

		ImageView pic = ViewHolder.get(view, R.id.iv_pic);
		final TextView deviceName = ViewHolder
				.get(view, R.id.tv_deviceName);
		TextView physical = ViewHolder.get(view, R.id.tv_physicalDeviceId);
		ACUserDevice device = deviceList.get(i);

		physicalDeviceId = device.getPhysicalDeviceId();

		deviceId = device.getDeviceId()+"";

		physical.setText(physicalDeviceId);
		deviceName.setText(device.getName());
		switch (device.getStatus()) {

		case ACUserDevice.OFFLINE:
			deviceName.setTextColor(Color.WHITE);
			physical.setTextColor(Color.WHITE);
			pic.setBackgroundResource(R.drawable.cloud_off);
			Log.e("LINE", "OFFLINE");

			break;
		case ACUserDevice.NETWORK_ONLINE:
			deviceName.setTextColor(Color.GREEN);
			physical.setTextColor(Color.GREEN);
			pic.setBackgroundResource(R.drawable.cloud_on);
			Log.e("LINE", "NETWORK_ONLINE");
			break;
		case ACUserDevice.LOCAL_ONLINE:
			deviceName.setTextColor(Color.GREEN);
			physical.setTextColor(Color.GREEN);
			pic.setBackgroundResource(R.drawable.cloud_on);
			Log.e("LINE", "LOCAL_ONLINE");
			break;
		case ACUserDevice.BOTH_ONLINE:
			deviceName.setTextColor(Color.GREEN);
			physical.setTextColor(Color.GREEN);
			pic.setBackgroundResource(R.drawable.cloud_on);
			Log.e("LINE", "BOTH_ONLINE");
			break;
		}

		
		return view;
	}
}


