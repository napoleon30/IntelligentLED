package cn.sharelink.intelligentled.activity_for_led;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.service.ACBindMgr;
import com.accloud.service.ACException;
import com.accloud.service.ACUserDevice;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.sharelink.intelligentled.R;
import cn.sharelink.intelligentled.for_led_other.MyAdapter;
import cn.sharelink.intelligentled.for_led_other.MyAddAdapter;
import cn.sharelink.intelligentled.for_led_other.TypeModel;
import cn.sharelink.intelligentled.sql2.Device;
import cn.sharelink.intelligentled.sql2.DeviceDaoImpl;
import cn.sharelink.intelligentled.utils.DBOXException;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 添加设备进群组界面
 * @author Administrator
 *
 */
public class AddDeviceToGroupActivity extends Activity implements
		MyAddAdapter.OnCallBack {
	private static final String TAG = AddDeviceToGroupActivity.class
			.getSimpleName();
	private LinearLayout mSelectAll;// 选择所有
	private ListView mLv;
	private Button confirm, cancel;

	private MyAddAdapter adapter;
	private List<TypeModel> mList; // mLv集合的数据集
	private ArrayList<String> physicals; // 被选中的物理ID集合

	ACBindMgr bindMgr;
	DeviceDaoImpl dao;
	/**
	 * 6060类型，0表示单色灯；1表示色温灯;2表示彩色灯
	 */
	int model9 = -1;
	/**
	 * 数据库存储的数据
	 */
	List<Device> sqlDevices;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		setContentView(R.layout.activity_add_device_to_group);
		model9 = getIntent().getIntExtra("model", 0);
		Log.e(TAG, "model模式为："+model9);
		bindMgr = AC.bindMgr();
		dao = new DeviceDaoImpl(AddDeviceToGroupActivity.this);
		sqlDevices = new ArrayList<Device>();
		sqlDevices = dao.query(null, null);
		
		initView();

	}

	private void initView() {
		physicals = new ArrayList<String>();
		mList = new ArrayList<TypeModel>();
		// getDeviceList();
		getData();
		mSelectAll = (LinearLayout) findViewById(R.id.select_all);
		mSelectAll.setOnClickListener(listener);
		confirm = (Button) findViewById(R.id.bn_confirm);
		confirm.setOnClickListener(listener);
		cancel = (Button) findViewById(R.id.bn_cancel);
		cancel.setOnClickListener(listener);
		mLv = (ListView) findViewById(R.id.listView);
		adapter = new MyAddAdapter(this, mList, this);
		mLv.setAdapter(adapter);
	}

	private void getData() {
		if (sqlDevices.size() > 0) {
			
			for (Device dev : sqlDevices) {
				
				if (dev.getType() == model9 && dev.getGrouptype()==0) {
					mList.add(new TypeModel(dev.getPhysicalDeviceID(), "0"));// "0"表示未选中
				}
			}
		}
		mList = removeDuplicate(mList);
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.select_all:
				if (mSelectAll.isSelected()) {
					mSelectAll.setSelected(false);
					for (TypeModel model : mList) {
						model.setIsSelect("0");
					}
					adapter.notifyDataSetChanged();
				} else {
					mSelectAll.setSelected(true);
					for (TypeModel model : mList) {
						model.setIsSelect("1");
					}
					adapter.notifyDataSetChanged();
				}
				break;

			case R.id.bn_confirm:
				for (int i = 0; i < mList.size(); i++) {
					if (mList.get(i).getIsSelect() == "1") {
						Log.e(TAG, "确定被选中位置/物理ID：" + i + "/"
								+ mList.get(i).getPhysicalID());
						physicals.add(mList.get(i).getPhysicalID());

					}
				}
				Intent intent = new Intent();
				intent.putStringArrayListExtra("physicals", physicals);
				setResult(101, intent);
				finish();
				break;

			case R.id.bn_cancel:
				Intent intent2 = new Intent();
				setResult(102, intent2);
				finish();
				break;
			}
		}
	};

	// // 获取设备列表
	// public void getDeviceList() {
	//
	// bindMgr.listDevicesWithStatus(new PayloadCallback<List<ACUserDevice>>() {
	// public void success(List<ACUserDevice> deviceList) {
	// for (ACUserDevice device : deviceList) {
	// device.getStatus();
	// mList.add(new TypeModel(device.getPhysicalDeviceId(), "0"));
	// dao.insert(new Device(device.getName(), device.getDeviceId()+"",
	// device.getPhysicalDeviceId(),model,
	// getResources().getString(R.string.not_grouped)));
	// }
	// adapter.notifyDataSetChanged();
	// }
	//
	// @Override
	// public void error(ACException e) {
	// DBOXException.errorCode(AddDeviceToGroupActivity.this,
	// e.getErrorCode());
	// }
	// });
	// }
	
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

	@Override
	public void onSelectedListener(int pos) {
		if (mList.get(pos).getIsSelect().equals("0")) {
			mList.get(pos).setIsSelect("1");
		} else {
			mList.get(pos).setIsSelect("0");
			mSelectAll.setSelected(false);
		}
		adapter.notifyDataSetChanged();

	}

}