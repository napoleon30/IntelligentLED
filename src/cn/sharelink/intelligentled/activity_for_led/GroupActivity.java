package cn.sharelink.intelligentled.activity_for_led;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.service.ACBindMgr;
import com.accloud.service.ACException;
import com.accloud.service.ACUserDevice;
import com.accloud.service.Receiver;
import com.accloud.service.Topic;
import com.accloud.service.TopicData;
import com.accloud.utils.PreferencesUtils;


import cn.sharelink.intelligentled.R;
import cn.sharelink.intelligentled.config.Config;
import cn.sharelink.intelligentled.sql2.Device;
import cn.sharelink.intelligentled.sql2.DeviceDaoImpl;
import cn.sharelink.intelligentled.sql3_for_time_project.Project;
import cn.sharelink.intelligentled.sql3_for_time_project.ProjectDaoImpl;
import cn.sharelink.intelligentled.sql5_for_group_plan_name.GroupN;
import cn.sharelink.intelligentled.sql5_for_group_plan_name.GroupNDaoImpl;
import cn.sharelink.intelligentled.sql7_group_device_list.GroupDeviceList;
import cn.sharelink.intelligentled.sql7_group_device_list.GroupDeviceListDaoImpl;
import cn.sharelink.intelligentled.utils.ActionSheetDialog;
import cn.sharelink.intelligentled.utils.DBOXException;
import cn.sharelink.intelligentled.utils.GsonUtil;
import cn.sharelink.intelligentled.utils.ItonAdecimalConver;
import cn.sharelink.intelligentled.utils.MyDevice;
import cn.sharelink.intelligentled.utils.OnReceive;
import cn.sharelink.intelligentled.utils.ActionSheetDialog.OnSheetItemClickListener;
import cn.sharelink.intelligentled.utils.ActionSheetDialog.SheetItemColor;
import cn.sharelink.intelligentled.utils.SendData;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 单色灯群组界面
 * @author Administrator
 *
 */
public class GroupActivity extends Activity {
	private static final String TAG = GroupActivity.class.getSimpleName();
	private Button back, addGroup;
	private ListView groupList;
	private List<String> groups;
	GroupDeviceListDaoImpl groupdevicelistDao;
	MyGroupAdapter groupAdapter;
	
	private String subDomain;
	SendData senddata;
	ACBindMgr bindMgr;
	
	List<ACUserDevice> containsDevices;
	Map<String, Receiver<TopicData>> maps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		setContentView(R.layout.activity_group);
		bindMgr = AC.bindMgr();
		containsDevices = new ArrayList<ACUserDevice>();
		maps = new HashMap<String, Receiver<TopicData>>();
		groupdevicelistDao = new GroupDeviceListDaoImpl(GroupActivity.this);
		groups = new ArrayList<String>();
		for(GroupDeviceList groupdevicelist:groupdevicelistDao.query(null, null)){
			if(groupdevicelist.getType().equals("0") && groupdevicelist.getPhysical().equals("")){
				groups.add(groupdevicelist.getGroupName());
			}
		}
		if(groups.size()>0){//去重
			List<String> ss = new ArrayList<String>();
			for(String str:groups){
				if(!ss.contains(str)){
					ss.add(str);
				}
			}
			groups.clear();
			groups.addAll(ss);
		}
		
		initView();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		subDomain = PreferencesUtils.getString(this, "subDomain",
				Config.SUBDOMAIN);
	
	}
	
@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	getDeviceList();
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
			Log.e(TAG, respondId+"接收到的返回值:" + payload);
		}

	};
	maps.put(deviceId, receiver);
	AC.customDataMgr().registerDataReceiver(receiver);
}

//获取设备列表
	public void getDeviceList() {
		Log.e(TAG, "更新6060设备列表");
		bindMgr.listDevicesWithStatus(new PayloadCallback<List<ACUserDevice>>() {
			@Override
			public void success(List<ACUserDevice> deviceList) {
				for(ACUserDevice de:deviceList){
					containsDevices.add(de);
				}
			}

			@Override
			public void error(ACException e) {
				
			}
		});
	}


	private void initView() {
		
		groupAdapter = new MyGroupAdapter(this, groups);
		back = (Button) findViewById(R.id.btn_single_color_back);
		addGroup = (Button) findViewById(R.id.btn_add_group);
		back.setOnClickListener(listener);
		addGroup.setOnClickListener(listener);
		groupList = (ListView) findViewById(R.id.group_list);
		groupList.setAdapter(groupAdapter);

		groupList.setOnItemClickListener(itemListener);
		groupList.setOnItemLongClickListener(itemLongListener);
		
		
	}

	protected void rename(final int position) {
		AlertDialog.Builder builder = new Builder(GroupActivity.this);
		View view = LayoutInflater.from(GroupActivity.this).inflate(
				R.layout.dialog_rename_group, null);
		builder.setView(view);
		final EditText etRename = (EditText) view
				.findViewById(R.id.edit_rename);
		Button confirm = (Button) view.findViewById(R.id.confirm_btn);
		Button cancel = (Button) view.findViewById(R.id.cancel_btn);
		final AlertDialog dialog = builder.create();
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			
				String strName = etRename.getText().toString().trim();
				if (TextUtils.isEmpty(strName)) {
					Toast.makeText(GroupActivity.this, getResources().getString(R.string.group_name_cannot_be_empty), 0).show();
					return;
				}
				
				for(String strg:groups){
					if(strName.equals(strg)){
						Toast.makeText(GroupActivity.this, getResources().getString(R.string.duplicate_group_name), 0).show();
						return;
					}
				}
				
				//修改sql2
				DeviceDaoImpl deviceDao = new DeviceDaoImpl(GroupActivity.this);
				for(Device devv:deviceDao.query(null, null)){
					if(devv.getGroup().equals(groups.get(position))){
						deviceDao.update(devv.getId(), devv.getName(),
								devv.getDeviceID(), devv.getPhysicalDeviceID(), 
								devv.getType(), strName, devv.getGrouptype());
					}
				}
					
				//修改sql5
				GroupNDaoImpl groupNDao = new GroupNDaoImpl(GroupActivity.this);
				for(GroupN groupn:groupNDao.query(null, null)){
					if(groupn.getType()==0 && groupn.getGroupName().equals(groups.get(position))){
						groupNDao.update(groupn.getId(), groupn.getType(), strName, groupn.getGroupPlanName());
					}
				}
				
				//修改群组中包含的的设备物理ID数据库sql7
				GroupDeviceListDaoImpl groupdevicelistDao = new GroupDeviceListDaoImpl(GroupActivity.this);
				for(GroupDeviceList groupdevicelist:groupdevicelistDao.query(null, null)){
					if(groupdevicelist.getType().equals("0") && groupdevicelist.getGroupName().equals(groups.get(position))){
						groupdevicelistDao.update(groupdevicelist.getId(), groupdevicelist.getType(),
								groupdevicelist.getPhysical(), strName);
					}
				}
				
				//修改sql3
				ProjectDaoImpl projectDao = new ProjectDaoImpl(GroupActivity.this);
				for(Project pro:projectDao.query(null, null)){
					if(pro.getType()==0 && pro.getGroupName().equals(groups.get(position))){
						projectDao.update(pro.getId(), pro.getName(), pro.getType(), pro.getWeekday(),
								pro.getBegintime1(), pro.getEndtime1(),
								pro.getSeek11(), pro.getSeek12(), 
								pro.getBegintime2(),pro.getEndtime2(), 
								pro.getSeek21(),pro.getSeek22(), 
								pro.getBegintime3(),pro.getEndtime3(),  
								pro.getSeek31(),pro.getSeek32(), 
								pro.getBegintime4(),pro.getEndtime4(),  
								pro.getSeek41(),pro.getSeek42(),
								strName, pro.getPhysical());
					}
				}
				
				
				
				groups.set(position, strName);
				
				groupAdapter.notifyDataSetChanged();
				dialog.dismiss();
				}
			
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		dialog.show();

	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_single_color_back:
				GroupActivity.this.finish();
				break;

			case R.id.btn_add_group:
				buildNewGroupDialog();
				break;
			}

		}
	};

	OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent intent = new Intent(GroupActivity.this,
					GroupDeviceListActivity.class);
			intent.putExtra("name", groups.get(position));
			startActivity(intent);

		}
	};

	OnItemLongClickListener itemLongListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			showItemLongDialog(position);
			return true;
		}
	};

	public class MyGroupAdapter extends BaseAdapter {
		LayoutInflater inflater = null;
		List<String> groups;

		private MyGroupAdapter(Context context, List<String> groups) {
			inflater = LayoutInflater.from(context);
			this.groups = groups;
		}

		@Override
		public int getCount() {
			return groups.size();
		}

		@Override
		public Object getItem(int i) {
			return groups.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View convertView, ViewGroup viewGroup) {
			ViewHolder viewHolder = null;
			if (viewHolder == null) {
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(R.layout.group_list_item, null);
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.group_name);
				viewHolder.imge = (ImageView) convertView.findViewById(R.id.iv);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.imge.setBackgroundResource(R.drawable.group_list_view);
			viewHolder.name.setText(groups.get(i));
			return convertView;
		}

	}

	class ViewHolder {
		public TextView name;
		public ImageView imge;
	}

	protected void buildNewGroupDialog() {
		AlertDialog.Builder builder = new Builder(GroupActivity.this);
		View view = LayoutInflater.from(GroupActivity.this).inflate(
				R.layout.dialog_new_group, null);
		builder.setView(view);
		final EditText name = (EditText) view.findViewById(R.id.edit_name);
		Button confirm = (Button) view.findViewById(R.id.confirm_btn);
		Button cancel = (Button) view.findViewById(R.id.cancel_btn);
		final AlertDialog dialog = builder.create();
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String strName = name.getText().toString().trim();
				if (TextUtils.isEmpty(strName)) {
					Toast.makeText(GroupActivity.this, getResources().getString(R.string.group_name_cannot_be_empty), 0).show();
					return;
				}
				for (String group: groups) {
					if (group.equals(strName)) {
						Toast.makeText(GroupActivity.this, getResources().getString(R.string.group_name_repeat_rename),
								Toast.LENGTH_SHORT).show();
						return;
					}
				}
				
				//新建群组，插入sql5
				GroupNDaoImpl groupNDao = new GroupNDaoImpl(GroupActivity.this);
				groupNDao.insert(new GroupN(0, strName, ""));
				
				//新建群组插入sql7
				GroupDeviceListDaoImpl groupdevicelistDao = 
						new GroupDeviceListDaoImpl(GroupActivity.this);
				groupdevicelistDao.insert(new GroupDeviceList("0","",strName));
				
				groups.add(strName);
				
				groupAdapter.notifyDataSetChanged();
				dialog.dismiss();
				
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		dialog.show();
	}

	protected void showItemLongDialog(final int position) {
		new ActionSheetDialog(GroupActivity.this)
				.builder()
				.setCancelable(false)
				.setCanceledOnTouchOutside(false)
				.addSheetItem(getResources().getString(R.string.delete),
						SheetItemColor.Blue, new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								
								//sql2的grouptype更新为0
								DeviceDaoImpl deviceDao = new DeviceDaoImpl(GroupActivity.this);
								for(Device devi : deviceDao.query(null, null)){
									if(devi.getType()==0 && devi.getGroup().equals(groups.get(position))){
										deviceDao.update(devi.getId(), devi.getName(), 
												devi.getDeviceID(), devi.getPhysicalDeviceID(),
												devi.getType(), getResources().getString(R.string.no_group), 0);
									}
								}
								
								//刪除sql7
								for(GroupDeviceList groupdevicelist:groupdevicelistDao.query(null, null)){
									if(groupdevicelist.getType().equals("0") && groupdevicelist.getGroupName().equals(groups.get(position))){
										groupdevicelistDao.delete(groupdevicelist.getId());
									}
								}
								
								String planName = null;
								//删除当前组中的组名称和组计划名称sql5
								GroupNDaoImpl groupDao = new GroupNDaoImpl(GroupActivity.this);
								for(GroupN groupN:groupDao.query(null, null)){
									if(groupN.getType()==0 && groupN.getGroupName().equals(groups.get(position))){
										planName = groupN.getGroupPlanName();
										groupDao.delete(groupN.getId());
									}
								}
								
								//删除组计划内容
								ProjectDaoImpl proDao = new ProjectDaoImpl(GroupActivity.this);
								for(Project pro:proDao.query(null, null)){
									if (planName!=null && pro.getName().equals(planName)
											&& pro.getType()==0) {
										proDao.delete(pro.getId());
									}
								}
								
								
								
								//将群组包含的设备修正为“未分组”
								DeviceDaoImpl dao = new DeviceDaoImpl(GroupActivity.this);
								List<Device> sqlDevices = dao.query(null, null);
								if (sqlDevices.size()>0) {
									for (Device dev:sqlDevices) {
										if (dev.getGroup().equals(groups.get(position))) {
											dao.update(dev.getId(), dev.getName(), dev.getDeviceID(),
													dev.getPhysicalDeviceID(), dev.getType(), getResources().getString(R.string.no_group),0);
											
										}
									}
								}
								
								groups.remove(position);
								groupAdapter.notifyDataSetChanged();
								
							}
						})
				.addSheetItem(getResources().getString(R.string.rename),
						SheetItemColor.Blue, new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								
								rename(position);
							}
						})
				.addSheetItem(getResources().getString(R.string.group_plan_execution), //打开群组计划执行
						SheetItemColor.Blue, new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								
								List<MyDevice> mydevices = new ArrayList<MyDevice>();
								GroupDeviceListDaoImpl groupdevicelistDao = new GroupDeviceListDaoImpl(GroupActivity.this);
								for(GroupDeviceList groupdevicelist:groupdevicelistDao.query(null, null)){
									if(groupdevicelist.getType().equals("0") &&
											groupdevicelist.getGroupName().equals(groups.get(position)) &&
											!groupdevicelist.getPhysical().equals("")){
										
										for(ACUserDevice dev:containsDevices){
											if(dev.getPhysicalDeviceId().equals(groupdevicelist.getPhysical())){
												
												mydevices.add(new MyDevice(dev.getName(), dev.getDeviceId()+"", dev.getPhysicalDeviceId()));
											}
										}
										
										for (int i = 0; i < mydevices.size(); i++) {
											subscribe("xinlian01", "topic_type", mydevices.get(i).getDeviceID());// 订阅，可获取到返回值
											senddata = new SendData(subDomain,groupdevicelist.getPhysical());
											senddata.sendData("660600010499");
										}
										
									}
								}
							}
						})	
						.addSheetItem(getResources().getString(R.string.group_plan_close),//关闭群组计划执行
								SheetItemColor.Blue, new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								GroupDeviceListDaoImpl groupdevicelistDao = new GroupDeviceListDaoImpl(GroupActivity.this);
								for(GroupDeviceList groupdevicelist:groupdevicelistDao.query(null, null)){
									if(groupdevicelist.getType().equals("0") && 
											groupdevicelist.getGroupName().equals(groups.get(position)) &&
											!groupdevicelist.getPhysical().equals("")){
										senddata = new SendData(subDomain,groupdevicelist.getPhysical());
										senddata.sendData("660600010A99");
									}
								}
							}
						})	
						.show();

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
