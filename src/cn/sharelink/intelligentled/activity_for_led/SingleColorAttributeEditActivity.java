package cn.sharelink.intelligentled.activity_for_led;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.sharelink.intelligentled.R;
import cn.sharelink.intelligentled.application.MainApplication;
import cn.sharelink.intelligentled.pickerview.TimePickerView;
import cn.sharelink.intelligentled.sql3_for_time_project.Project;
import cn.sharelink.intelligentled.sql3_for_time_project.ProjectDaoImpl;
import cn.sharelink.intelligentled.sql6_all_plan_name.PlanName;
import cn.sharelink.intelligentled.sql6_all_plan_name.PlanNameDaoImpl;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * SingleColor灯和CCT Change灯的属性计划编辑界面
 * 
 * @author Administrator
 */
public class SingleColorAttributeEditActivity extends Activity {
	private static final String TAG = SingleColorAttributeEditActivity.class
			.getSimpleName();
	private Button left, right;

	/**
	 * 0表示SingleColor;1表示CCTChange;(2表示RBG,
	 * 另开一个界面SingleColorAttributeEidt2Activity)
	 */
	int type = -1;

	private TextView tvWeek, time1_left, time1_right, time2_left, time2_right,
			time3_left, time3_right, time4_left, time4_right;
	int weekday = 0;

	private TextView tv1_1, tv1_2, tv2_1, tv2_2, tv3_1, tv3_2, tv4_1, tv4_2;
	private SeekBar seekbar11, seekbar12, seekbar21, seekbar22, seekbar31,
			seekbar32, seekbar41, seekbar42;

	private TextView back, save, tvPlanName;

	private TimePickerView pvTime;
	ProjectDaoImpl dao;
	List<Project> projects;
	/**
	 * 接收的已存在的计划名集合
	 */
//	List<String> proNames;
	/**
	 * 存储Project的SQLite的tableName;
	 */
	String projectName;

	String beginTime11="00:00";
	String beginTime12="23:57";
	String beginTime13="23:58";
	String beginTime14="23:59";
	String endTime11="23:57";
	String endTime12="23:58";
	String endTime13="23:59";
	String endTime14="24:00";
	int progress111 = 0;
	int progress112 = 0;
	int progress121 = 0;
	int progress122 = 0;
	int progress131 = 0;
	int progress132 = 0;
	int progress141 = 0;
	int progress142 = 0;

	String beginTime21="00:00";
	String beginTime22="23:57";
	String beginTime23="23:58";
	String beginTime24="23:59";
	String endTime21="23:57";
	String endTime22="23:58";
	String endTime23="23:59";
	String endTime24="24:00";

	int progress211 = 0;
	int progress212 = 0;
	int progress221 = 0;
	int progress222 = 0;
	int progress231 = 0;
	int progress232 = 0;
	int progress241 = 0;
	int progress242 = 0;

	String beginTime31="00:00";
	String beginTime32="23:57";
	String beginTime33="23:58";
	String beginTime34="23:59";
	String endTime31="23:57";
	String endTime32="23:58";
	String endTime33="23:59";
	String endTime34="24:00";

	int progress311 = 0;
	int progress312 = 0;
	int progress321 = 0;
	int progress322 = 0;
	int progress331 = 0;
	int progress332 = 0;
	int progress341 = 0;
	int progress342 = 0;

	String beginTime41="00:00";
	String beginTime42="23:57";
	String beginTime43="23:58";
	String beginTime44="23:59";
	String endTime41="23:57";
	String endTime42="23:58";
	String endTime43="23:59";
	String endTime44="24:00";

	int progress411 = 0;
	int progress412 = 0;
	int progress421 = 0;
	int progress422 = 0;
	int progress431 = 0;
	int progress432 = 0;
	int progress441 = 0;
	int progress442 = 0;

	String beginTime51="00:00";
	String beginTime52="23:57";
	String beginTime53="23:58";
	String beginTime54="23:59";
	String endTime51="23:57";
	String endTime52="23:58";
	String endTime53="23:59";
	String endTime54="24:00";

	int progress511 = 0;
	int progress512 = 0;
	int progress521 = 0;
	int progress522 = 0;
	int progress531 = 0;
	int progress532 = 0;
	int progress541 = 0;
	int progress542 = 0;

	String beginTime61="00:00";
	String beginTime62="23:57";
	String beginTime63="23:58";
	String beginTime64="23:59";
	String endTime61="23:57";
	String endTime62="23:58";
	String endTime63="23:59";
	String endTime64="24:00";

	int progress611 = 0;
	int progress612 = 0;
	int progress621 = 0;
	int progress622 = 0;
	int progress631 = 0;
	int progress632 = 0;
	int progress641 = 0;
	int progress642 = 0;

	String beginTime71="00:00";
	String beginTime72="23:57";
	String beginTime73="23:58";
	String beginTime74="23:59";
	String endTime71="23:57";
	String endTime72="23:58";
	String endTime73="23:59";
	String endTime74="24:00";

	int progress711 = 0;
	int progress712 = 0;
	int progress721 = 0;
	int progress722 = 0;
	int progress731 = 0;
	int progress732 = 0;
	int progress741 = 0;
	int progress742 = 0;

	int minNum = 0;
	private PlanNameDaoImpl planNameDao;
	
	String physical="";
	String groupName="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		setContentView(R.layout.activity_single_color_attribute_edit);
		type = getIntent().getIntExtra("TYPE", 0); // type为0，表示SingleColor跳转；type为1，表示CCT
													// Change跳转
		physical = getIntent().getStringExtra("physical");
		groupName = getIntent().getStringExtra("groupName");
		if(physical==null){
			physical="";
		}
		if(groupName==null){
			groupName="";
		}
		Log.e(TAG, "groupName:"+groupName);
		if(groupName==null){
			groupName="";
		}
		
		minNum = (type == 0 ? 0 : 2700);
		projects = new ArrayList<Project>();
		tvPlanName = (TextView) findViewById(R.id.tv_plan_name);
		planNameDao = new PlanNameDaoImpl(SingleColorAttributeEditActivity.this);
		dao = new ProjectDaoImpl(SingleColorAttributeEditActivity.this);
		if (getIntent().getIntExtra("style", 0) == 0) { // style为0，表示从新创建计划；stype为1，表示是打开已存在的计划
			projectNameDialog();
		} else if (getIntent().getIntExtra("style", 0) == 1) {
			projectName = getIntent().getStringExtra("planName");
			Log.e(TAG, "projectName:" + projectName);
			if (tvPlanName == null) {
				tvPlanName = (TextView) findViewById(R.id.tv_plan_name);
			}
			tvPlanName.setText(projectName);
			initData();
		}
		initView();
		inintTimePicker();
	}

	private void initData() {
		Log.e(TAG, "***---*"+projectName+"/"+type+"/"+groupName+"/"+physical);
		List<Project> pros = dao.query(null, null);
		if (pros.size() > 0) {
			for (Project pro : pros) {
				Log.e(TAG, "pro的开始时间：" + pro.getBegintime1());
				if (pro.getName().equals(projectName) && pro.getType() == type
						&& pro.getGroupName().equals(groupName)
						&& pro.getPhysical().equals(physical)) {
				
					projects.add(pro);
				}
			}

			for (int i = 0; i < projects.size(); i++) {
//				beginTime11 = projects.get(6).getBegintime1();
				beginTime11 = "00:00";
				endTime11 = projects.get(6).getEndtime1();
				progress111 = Integer.parseInt(projects.get(6).getSeek11());
				progress112 = Integer.parseInt(projects.get(6).getSeek12());

				beginTime12 = projects.get(6).getBegintime2();
				endTime12 = projects.get(6).getEndtime2();
				progress121 = Integer.parseInt(projects.get(6).getSeek21());
				progress122 = Integer.parseInt(projects.get(6).getSeek22());

				beginTime13 = projects.get(6).getBegintime3();
				endTime13 = projects.get(6).getEndtime3();
				progress131 = Integer.parseInt(projects.get(6).getSeek31());
				progress132 = Integer.parseInt(projects.get(6).getSeek32());

				beginTime14 = projects.get(6).getBegintime4();
//				endTime14 = projects.get(6).getEndtime4();
				endTime14 = "24:00";
				progress141 = Integer.parseInt(projects.get(6).getSeek41());
				progress142 = Integer.parseInt(projects.get(6).getSeek42());

//				beginTime21 = projects.get(5).getBegintime1();
				beginTime21 = "00:00";
				endTime21 = projects.get(5).getEndtime1();
				progress211 = Integer.parseInt(projects.get(5).getSeek11());
				progress212 = Integer.parseInt(projects.get(5).getSeek12());

				beginTime22 = projects.get(5).getBegintime2();
				endTime22 = projects.get(5).getEndtime2();
				progress221 = Integer.parseInt(projects.get(5).getSeek21());
				progress222 = Integer.parseInt(projects.get(5).getSeek22());

				beginTime23 = projects.get(5).getBegintime3();
				endTime23 = projects.get(5).getEndtime3();
				progress231 = Integer.parseInt(projects.get(5).getSeek31());
				progress232 = Integer.parseInt(projects.get(5).getSeek32());

				beginTime24 = projects.get(5).getBegintime4();
//				endTime24 = projects.get(5).getEndtime4();
				endTime24 = "24:00";
				progress241 = Integer.parseInt(projects.get(5).getSeek41());
				progress242 = Integer.parseInt(projects.get(5).getSeek42());

//				beginTime31 = projects.get(4).getBegintime1();
				beginTime31 = "00:00";
				endTime31 = projects.get(4).getEndtime1();
				progress311 = Integer.parseInt(projects.get(4).getSeek11());
				progress312 = Integer.parseInt(projects.get(4).getSeek12());

				beginTime32 = projects.get(4).getBegintime2();
				endTime32 = projects.get(4).getEndtime2();
				progress321 = Integer.parseInt(projects.get(4).getSeek21());
				progress322 = Integer.parseInt(projects.get(4).getSeek22());

				beginTime33 = projects.get(4).getBegintime3();
				endTime33 = projects.get(4).getEndtime3();
				progress331 = Integer.parseInt(projects.get(4).getSeek31());
				progress332 = Integer.parseInt(projects.get(4).getSeek32());

				beginTime34 = projects.get(4).getBegintime4();
//				endTime34 = projects.get(4).getEndtime4();
				endTime34 = "24:00";
				progress341 = Integer.parseInt(projects.get(4).getSeek41());
				progress342 = Integer.parseInt(projects.get(4).getSeek42());

//				beginTime41 = projects.get(3).getBegintime1();
				beginTime41 = "00:00";
				endTime41 = projects.get(3).getEndtime1();
				progress411 = Integer.parseInt(projects.get(3).getSeek11());
				progress412 = Integer.parseInt(projects.get(3).getSeek12());

				beginTime42 = projects.get(3).getBegintime2();
				endTime42 = projects.get(3).getEndtime2();
				progress421 = Integer.parseInt(projects.get(3).getSeek21());
				progress422 = Integer.parseInt(projects.get(3).getSeek22());

				beginTime43 = projects.get(3).getBegintime3();
				endTime43 = projects.get(3).getEndtime3();
				progress431 = Integer.parseInt(projects.get(3).getSeek31());
				progress432 = Integer.parseInt(projects.get(3).getSeek32());

				beginTime44 = projects.get(3).getBegintime4();
//				endTime44 = projects.get(3).getEndtime4();
				endTime44 = "24:00";
				progress441 = Integer.parseInt(projects.get(3).getSeek41());
				progress442 = Integer.parseInt(projects.get(3).getSeek42());

//				beginTime51 = projects.get(2).getBegintime1();
				beginTime51 = "00:00";
				endTime51 = projects.get(2).getEndtime1();
				progress511 = Integer.parseInt(projects.get(2).getSeek11());
				progress512 = Integer.parseInt(projects.get(2).getSeek12());

				beginTime52 = projects.get(2).getBegintime2();
				endTime52 = projects.get(2).getEndtime2();
				progress521 = Integer.parseInt(projects.get(2).getSeek21());
				progress522 = Integer.parseInt(projects.get(2).getSeek22());

				beginTime53 = projects.get(2).getBegintime3();
				endTime53 = projects.get(2).getEndtime3();
				progress531 = Integer.parseInt(projects.get(2).getSeek31());
				progress532 = Integer.parseInt(projects.get(2).getSeek32());

				beginTime54 = projects.get(2).getBegintime4();
//				endTime54 = projects.get(2).getEndtime4();
				endTime54 = "24:00";
				progress541 = Integer.parseInt(projects.get(2).getSeek41());
				progress542 = Integer.parseInt(projects.get(2).getSeek42());

//				beginTime61 = projects.get(1).getBegintime1();
				beginTime61 = "00:00";
				endTime61 = projects.get(1).getEndtime1();
				progress611 = Integer.parseInt(projects.get(1).getSeek11());
				progress612 = Integer.parseInt(projects.get(1).getSeek12());

				beginTime62 = projects.get(1).getBegintime2();
				endTime62 = projects.get(1).getEndtime2();
				progress621 = Integer.parseInt(projects.get(1).getSeek21());
				progress622 = Integer.parseInt(projects.get(1).getSeek22());

				beginTime63 = projects.get(1).getBegintime3();
				endTime63 = projects.get(1).getEndtime3();
				progress631 = Integer.parseInt(projects.get(1).getSeek31());
				progress632 = Integer.parseInt(projects.get(1).getSeek32());

				beginTime64 = projects.get(1).getBegintime4();
//				endTime64 = projects.get(1).getEndtime4();
				endTime64 = "24:00";
				progress641 = Integer.parseInt(projects.get(1).getSeek41());
				progress642 = Integer.parseInt(projects.get(1).getSeek42());

//				beginTime71 = projects.get(0).getBegintime1();
				beginTime71 = "00:00";
				endTime71 = projects.get(0).getEndtime1();
				progress711 = Integer.parseInt(projects.get(0).getSeek11());
				progress712 = Integer.parseInt(projects.get(0).getSeek12());

				beginTime72 = projects.get(0).getBegintime2();
				endTime72 = projects.get(0).getEndtime2();
				progress721 = Integer.parseInt(projects.get(0).getSeek21());
				progress722 = Integer.parseInt(projects.get(0).getSeek22());

				beginTime73 = projects.get(0).getBegintime3();
				endTime73 = projects.get(0).getEndtime3();
				progress731 = Integer.parseInt(projects.get(0).getSeek31());
				progress732 = Integer.parseInt(projects.get(0).getSeek32());

				beginTime74 = projects.get(0).getBegintime4();
//				endTime74 = projects.get(0).getEndtime4();
				endTime74 = "24:00";
				progress741 = Integer.parseInt(projects.get(0).getSeek41());
				progress742 = Integer.parseInt(projects.get(0).getSeek42());

			}
		}
	}

	private void projectNameDialog() {
		AlertDialog.Builder builder = new Builder(
				SingleColorAttributeEditActivity.this);
		View view = LayoutInflater.from(SingleColorAttributeEditActivity.this)
				.inflate(R.layout.dialog_for_project_name, null);
		builder.setView(view);
		final EditText name = (EditText) view.findViewById(R.id.edit_name);
		Button confirm = (Button) view.findViewById(R.id.confirm_btn);
		Button cancel = (Button) view.findViewById(R.id.cancel_btn);
		builder.setCancelable(false);
		final AlertDialog dialog = builder.create();
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e(TAG, "确定");
				if (TextUtils.isEmpty(name.getText().toString().trim())) {
					Toast.makeText(
							SingleColorAttributeEditActivity.this,
							getResources().getString(
									R.string.project_name_cannot_be_empty),
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					projectName = name.getText().toString().trim();
					if (planNameDao.query(null, null).size()>0) {
						for(PlanName planName:planNameDao.query(null, null)){
							Log.e(TAG, "planName:"+planName.getPlanName()+"/"+ planName.getPhysical()
									+"/"+planName.getType()+"/"+planName.getGroupName());
							if(planName.getPlanName().equals(projectName) 
									&& planName.getPhysical().equals(physical)
									&& planName.getType()==type
									&& planName.getGroupName().equals(groupName)){
						
						Toast.makeText(
								SingleColorAttributeEditActivity.this,
								getResources()
										.getString(
												R.string.duplicate_program_name),
								Toast.LENGTH_SHORT).show();
						Log.e(TAG, "新建名称："+projectName+"/"+physical+"/"+type+"/"+groupName);
						return;
							}
						}
					}
						
					tvPlanName.setText(projectName);
					planNameDao.insert(new PlanName(type,projectName,physical,groupName));
				}

				dialog.dismiss();

			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent();
				setResult(2112);
				SingleColorAttributeEditActivity.this.finish();

			}
		});
		dialog.show();

	}

	private void inintTimePicker() {
		// 控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
		// 因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
		Calendar selectedDate = Calendar.getInstance();
		// Calendar startDate = Calendar.getInstance();
		// startDate.set(2013, 0, 23);
		// Calendar endDate = Calendar.getInstance();
		// endDate.set(2019, 11, 28);
		// 时间选择器
		pvTime = new TimePickerView.Builder(this,
				new TimePickerView.OnTimeSelectListener() {
					@Override
					public void onTimeSelect(Date date, View v) {// 选中事件回调
						// 这里回调过来的v,就是show()方法里面所添加的 View
						// 参数，如果show的时候没有添加参数，v则为null

						/* btn_Time.setText(getTime(date)); */
						Log.e(TAG, getTime(date));
						TextView btn = (TextView) v;
						int result = 0;

						switch (v.getId()) {
						case R.id.tv_right_time1:
							if (weekday == 0) {
								int result1 = timeCompare(beginTime11, getTime(date));
								int result2 = timeCompare(endTime12, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								
								if (result==1) {
									endTime11 = getTime(date);
									beginTime12 = getTime(date);
									time1_right.setText(endTime11);
									time2_left.setText(beginTime12);
								}
								
							} else if (weekday == 1) {
								int result1 = timeCompare(beginTime21, getTime(date));
								int result2 = timeCompare(endTime22, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								if (result==1) {
									endTime21 = getTime(date);
									beginTime22 = getTime(date);
									time1_right.setText(endTime21);
									time2_left.setText(beginTime22);
								}
							} else if (weekday == 2) {
								int result1 = timeCompare(beginTime31, getTime(date));
								int result2 = timeCompare(endTime32, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								if (result==1) {
									endTime31 = getTime(date);
									beginTime32 = getTime(date);
									time1_right.setText(endTime31);
									time2_left.setText(beginTime32);
								}
							} else if (weekday == 3) {
								int result1 = timeCompare(beginTime41, getTime(date));
								int result2 = timeCompare(endTime42, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								if (result==1) {
									endTime41 = getTime(date);
									beginTime42 = getTime(date);
									time1_right.setText(endTime41);
									time2_left.setText(beginTime42);
								}
							} else if (weekday == 4) {
								int result1 = timeCompare(beginTime51, getTime(date));
								int result2 = timeCompare(endTime52, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								if (result==1) {
									endTime51 = getTime(date);
									beginTime52 = getTime(date);
									time1_right.setText(endTime51);
									time2_left.setText(beginTime52);
								}
							} else if (weekday == 5) {
								int result1 = timeCompare(beginTime61, getTime(date));
								int result2 = timeCompare(endTime62, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								if (result==1) {
									endTime61 = getTime(date);
									beginTime62 = getTime(date);
									time1_right.setText(endTime61);
									time2_left.setText(beginTime62);
								}
							} else if (weekday == 6) {
								int result1 = timeCompare(beginTime71, getTime(date));
								int result2 = timeCompare(endTime72, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								if (result==1) {
									endTime71 = getTime(date);
									beginTime72 = getTime(date);
									time1_right.setText(endTime71);
									time2_left.setText(beginTime72);
								}
							}
							break;

//						
						case R.id.tv_right_time2:
							if (weekday == 0) {
								int result1 = timeCompare(beginTime12, getTime(date));
								int result2 = timeCompare(endTime13, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								
								if (result==1) {
									endTime12 = getTime(date);
									beginTime13 = getTime(date);
									time2_right.setText(endTime12);
									time3_left.setText(beginTime13);
								}
							} else if (weekday == 1) {
								int result1 = timeCompare(beginTime22, getTime(date));
								int result2 = timeCompare(endTime23, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								if (result==1) {
									endTime22 = getTime(date);
									beginTime23 = getTime(date);
									time2_right.setText(endTime22);
									time3_left.setText(beginTime23);
								}
							} else if (weekday == 2) {
								int result1 = timeCompare(beginTime32, getTime(date));
								int result2 = timeCompare(endTime33, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								if (result==1) {
									endTime32 = getTime(date);
									beginTime33 = getTime(date);
									time2_right.setText(endTime32);
									time3_left.setText(beginTime33);
								}
							} else if (weekday == 3) {
								int result1 = timeCompare(beginTime42, getTime(date));
								int result2 = timeCompare(endTime43, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								if (result==1) {
									endTime42 = getTime(date);
									beginTime43 = getTime(date);
									time2_right.setText(endTime42);
									time3_left.setText(beginTime43);
								}
							} else if (weekday == 4) {
								int result1 = timeCompare(beginTime52, getTime(date));
								int result2 = timeCompare(endTime53, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								if (result==1) {
									endTime52 = getTime(date);
									beginTime53 = getTime(date);
									time2_right.setText(endTime52);
									time3_left.setText(beginTime53);
								}
							} else if (weekday == 5) {
								int result1 = timeCompare(beginTime62, getTime(date));
								int result2 = timeCompare(endTime63, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								if (result==1) {
									endTime62 = getTime(date);
									beginTime63 = getTime(date);
									time2_right.setText(endTime62);
									time3_left.setText(beginTime63);
								}
							} else if (weekday == 6) {
								int result1 = timeCompare(beginTime72, getTime(date));
								int result2 = timeCompare(endTime73, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								if (result==1) {
									endTime72 = getTime(date);
									beginTime73 = getTime(date);
									time2_right.setText(endTime72);
									time3_left.setText(beginTime73);
								}
							}
							break;

//						
						case R.id.tv_right_time3:
							if (weekday == 0) {
								int result1 = timeCompare(beginTime13, getTime(date));
								int result2 = timeCompare(endTime14, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								
								if (result==1) {
									endTime13 = getTime(date);
									beginTime14 = getTime(date);
									time3_right.setText(endTime13);
									time4_left.setText(beginTime14);
								}
							} else if (weekday == 1) {
								int result1 = timeCompare(beginTime23, getTime(date));
								int result2 = timeCompare(endTime24, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								if (result==1) {
									endTime23 = getTime(date);
									beginTime24 = getTime(date);
									time3_right.setText(endTime23);
									time4_left.setText(beginTime24);
								}
							} else if (weekday == 2) {
								int result1 = timeCompare(beginTime33, getTime(date));
								int result2 = timeCompare(endTime34, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								if (result==1) {
									endTime33 = getTime(date);
									beginTime34 = getTime(date);
									time3_right.setText(endTime33);
									time4_left.setText(beginTime34);
								}
							} else if (weekday == 3) {
								int result1 = timeCompare(beginTime43, getTime(date));
								int result2 = timeCompare(endTime44, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								if (result==1) {
									endTime43 = getTime(date);
									beginTime44 = getTime(date);
									time3_right.setText(endTime43);
									time4_left.setText(beginTime44);
								}
							} else if (weekday == 4) {
								int result1 = timeCompare(beginTime53, getTime(date));
								int result2 = timeCompare(endTime54, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								if (result==1) {
									endTime53 = getTime(date);
									beginTime54 = getTime(date);
									time3_right.setText(endTime53);
									time4_left.setText(beginTime54);
								}
							} else if (weekday == 5) {
								int result1 = timeCompare(beginTime63, getTime(date));
								int result2 = timeCompare(endTime64, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								if (result==1) {
									endTime63 = getTime(date);
									beginTime64 = getTime(date);
									time3_right.setText(endTime63);
									time4_left.setText(beginTime64);
								}
							} else if (weekday == 6) {
								int result1 = timeCompare(beginTime73, getTime(date));
								int result2 = timeCompare(endTime74, getTime(date));
								if(result1==1 && result2==-1){
									result =1;
								}
								if (result==1) {
									endTime73 = getTime(date);
									beginTime74 = getTime(date);
									time3_right.setText(endTime73);
									time4_left.setText(beginTime74);
								}
							}
							break;

//					

						}
						if (result!=1) {
							final AlertDialog dialog;
							AlertDialog.Builder builder = new AlertDialog.Builder(SingleColorAttributeEditActivity.this);
							View viewDia=LayoutInflater.from(SingleColorAttributeEditActivity.this)
									.inflate(R.layout.reminder_dialog, null);
							builder.setView(viewDia);
							dialog = builder.create();
							ImageView cancelImg = (ImageView) viewDia.findViewById(R.id.iv_cancel);
							cancelImg.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									dialog.dismiss();
									
								}
							});
							dialog.show();
							
						}

					}
				})
				// 年月日时分秒 的显示与否，不设置则默认全部显示
				.setType(
						new boolean[] { false, false, false, true, true, false })
				.setLabel("", "", "", "", "", "").isCenterLabel(false)
				.setDividerColor(Color.DKGRAY).setContentSize(21)
				.setDate(selectedDate)
				// .setRangDate(startDate, endDate)
				.setBackgroundId(0x00FFFFFF) // 设置外部遮罩颜色
				.setDecorView(null).build();

	}
	
	/**
	 * 比较时间大小
	 * @param time1,time2
	 * @return 1表示time2大于time1,0表示time2等于time1,-1表示time2小于time1
	 */
	private int timeCompare(String time1,String time2){
		int result = 0;
		SimpleDateFormat dateFromat = new SimpleDateFormat("HH:mm");
		try {
			Date date1 = dateFromat.parse(time1);
			Date date2 = dateFromat.parse(time2);
			if (date2.getTime()>date1.getTime()) {
				result = 1;
			}else if(date2.getTime()==date1.getTime()){
				result=0;
			}else if(date2.getTime()<date1.getTime()){
				result=-1;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private String getTime(Date date) {// 可根据需要自行截取数据显示
		// SimpleDateFormat format = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(date);
	}

	private void initView() {

		back = (TextView) findViewById(R.id.btn_single_color_edit_back);
		back.setOnClickListener(listener);
		save = (TextView) findViewById(R.id.btn_single_color_edit_save);
		save.setOnClickListener(listener);
		left = (Button) findViewById(R.id.btn_single_color_edit_left);
		left.setOnClickListener(listener);
		right = (Button) findViewById(R.id.btn_single_color_edit_right);
		right.setOnClickListener(listener);
		time1_left = (TextView) findViewById(R.id.tv_left_time1);
//		time1_left.setOnClickListener(listener);
		time1_right = (TextView) findViewById(R.id.tv_right_time1);
		time1_right.setOnClickListener(listener);
		time2_left = (TextView) findViewById(R.id.tv_left_time2);
		time2_left.setOnClickListener(listener);
		time2_right = (TextView) findViewById(R.id.tv_right_time2);
		time2_right.setOnClickListener(listener);
		time3_left = (TextView) findViewById(R.id.tv_left_time3);
		time3_left.setOnClickListener(listener);
		time3_right = (TextView) findViewById(R.id.tv_right_time3);
		time3_right.setOnClickListener(listener);
		time4_left = (TextView) findViewById(R.id.tv_left_time4);
		time4_left.setOnClickListener(listener);
		time4_right = (TextView) findViewById(R.id.tv_right_time4);
//		time4_right.setOnClickListener(listener);

		tvWeek = (TextView) findViewById(R.id.tv_week);
		tvWeek.setText(getResources().getString(R.string.monday));

		tv1_1 = (TextView) findViewById(R.id.tv_1_1);
		tv1_2 = (TextView) findViewById(R.id.tv_1_2);
		tv2_1 = (TextView) findViewById(R.id.tv_2_1);
		tv2_2 = (TextView) findViewById(R.id.tv_2_2);
		tv3_1 = (TextView) findViewById(R.id.tv_3_1);
		tv3_2 = (TextView) findViewById(R.id.tv_3_2);
		tv4_1 = (TextView) findViewById(R.id.tv_4_1);
		tv4_2 = (TextView) findViewById(R.id.tv_4_2);
		seekbar11 = (SeekBar) findViewById(R.id.seekbar_1_1);
		seekbar12 = (SeekBar) findViewById(R.id.seekbar_1_2);
		seekbar21 = (SeekBar) findViewById(R.id.seekbar_2_1);
		seekbar22 = (SeekBar) findViewById(R.id.seekbar_2_2);
		seekbar31 = (SeekBar) findViewById(R.id.seekbar_3_1);
		seekbar32 = (SeekBar) findViewById(R.id.seekbar_3_2);
		seekbar41 = (SeekBar) findViewById(R.id.seekbar_4_1);
		seekbar42 = (SeekBar) findViewById(R.id.seekbar_4_2);
		if (type == 0) {
			seekbar11.setMax(100);
			seekbar12.setMax(100);
			seekbar21.setMax(100);
			seekbar22.setMax(100);
			seekbar31.setMax(100);
			seekbar32.setMax(100);
			seekbar41.setMax(100);
			seekbar42.setMax(100);
		} else if (type == 1) {
			seekbar11.setMax(100);
			seekbar12.setMax(4300);
			seekbar21.setMax(100);
			seekbar22.setMax(4300);
			seekbar31.setMax(100);
			seekbar32.setMax(4300);
			seekbar41.setMax(100);
			seekbar42.setMax(4300);
		}
		changeWeekDay(weekday);
		seekbar11.setOnSeekBarChangeListener(seek);
		seekbar12.setOnSeekBarChangeListener(seek);
		seekbar21.setOnSeekBarChangeListener(seek);
		seekbar22.setOnSeekBarChangeListener(seek);
		seekbar31.setOnSeekBarChangeListener(seek);
		seekbar32.setOnSeekBarChangeListener(seek);
		seekbar41.setOnSeekBarChangeListener(seek);
		seekbar42.setOnSeekBarChangeListener(seek);

	}

	OnSeekBarChangeListener seek = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			int pro = minNum + progress;
			switch (seekBar.getId()) {
			case R.id.seekbar_1_1:
				tv1_1.setText(progress+"");
				switch (weekday) {
				case 0:
					progress111 = progress;
					break;
				case 1:
					progress211 = progress;
					break;
				case 2:
					progress311 = progress;
					break;
				case 3:
					progress411 = progress;
					break;
				case 4:
					progress511 = progress;
					break;
				case 5:
					progress611 = progress;
					break;
				case 6:
					progress711 = progress;
					break;
				}
				break;
			case R.id.seekbar_1_2:
				tv1_2.setText(pro + "");
				switch (weekday) {
				case 0:
					progress112 = progress;
					break;
				case 1:
					progress212 = progress;
					break;
				case 2:
					progress312 = progress;
					break;
				case 3:
					progress412 = progress;
					break;
				case 4:
					progress512 = progress;
					break;
				case 5:
					progress612 = progress;
					break;
				case 6:
					progress712 = progress;
					break;
				}
				break;
			case R.id.seekbar_2_1:
				tv2_1.setText(progress + "");
				switch (weekday) {
				case 0:
					progress121 = progress;
					break;
				case 1:
					progress221 = progress;
					break;
				case 2:
					progress321 = progress;
					break;
				case 3:
					progress421 = progress;
					break;
				case 4:
					progress521 = progress;
					break;
				case 5:
					progress621 = progress;
					break;
				case 6:
					progress721 = progress;
					break;
				}
				break;
			case R.id.seekbar_2_2:
				tv2_2.setText(pro + "");
				switch (weekday) {
				case 0:
					progress122 = progress;
					break;
				case 1:
					progress222 = progress;
					break;
				case 2:
					progress322 = progress;
					break;
				case 3:
					progress422 = progress;
					break;
				case 4:
					progress522 = progress;
					break;
				case 5:
					progress622 = progress;
					break;
				case 6:
					progress722 = progress;
					break;
				}
				break;
			case R.id.seekbar_3_1:
				tv3_1.setText(progress + "");
				switch (weekday) {
				case 0:
					progress131 = progress;
					break;
				case 1:
					progress231 = progress;
					break;
				case 2:
					progress331 = progress;
					break;
				case 3:
					progress431 = progress;
					break;
				case 4:
					progress531 = progress;
					break;
				case 5:
					progress631 = progress;
					break;
				case 6:
					progress731 = progress;
					break;
				}
				break;
			case R.id.seekbar_3_2:
				tv3_2.setText(pro + "");
				switch (weekday) {
				case 0:
					progress132 = progress;
					break;
				case 1:
					progress232 = progress;
					break;
				case 2:
					progress332 = progress;
					break;
				case 3:
					progress432 = progress;
					break;
				case 4:
					progress532 = progress;
					break;
				case 5:
					progress632 = progress;
					break;
				case 6:
					progress732 = progress;
					break;
				}
				break;
			case R.id.seekbar_4_1:
				tv4_1.setText(progress + "");
				switch (weekday) {
				case 0:
					progress141 = progress;
					break;
				case 1:
					progress241 = progress;
					break;
				case 2:
					progress341 = progress;
					break;
				case 3:
					progress441 = progress;
					break;
				case 4:
					progress541 = progress;
					break;
				case 5:
					progress641 = progress;
					break;
				case 6:
					progress741 = progress;
					break;
				}
				break;
			case R.id.seekbar_4_2:
				tv4_2.setText(pro + "");
				switch (weekday) {
				case 0:
					progress142 = progress;
					break;
				case 1:
					progress242 = progress;
					break;
				case 2:
					progress342 = progress;
					break;
				case 3:
					progress442 = progress;
					break;
				case 4:
					progress542 = progress;
					break;
				case 5:
					progress642 = progress;
					break;
				case 6:
					progress742 = progress;
					break;
				}
				break;
			}

		}
	};

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_single_color_edit_back: // 返回
				Intent intent = new Intent();
				if (projectName != null) {
					intent.putExtra("planName", projectName);
				}
				setResult(2111, intent);
				SingleColorAttributeEditActivity.this.finish();
				break;
			case R.id.btn_single_color_edit_save: // 保存
				Log.e(TAG, "保存时的projectName:" + projectName);
				// 保存之前需先删掉之前projectName名称相同的数据，再添加
				if (projects.size() > 0) {
					for (Project pro : projects) {
						if (pro.getName().equals(projectName)) {
							dao.delete(pro.getId());
						}
					}
				}

				dao.insert(new Project(projectName, type, 0,
						beginTime11 = (beginTime11 == null ? "00:00"
								: beginTime11),
						endTime11 = (endTime11 == null ? "00:00" : endTime11),
						progress111 + "", progress112 + "",
						beginTime12 = (beginTime12 == null ? "00:00"
								: beginTime12),
						endTime12 = (endTime12 == null ? "00:00" : endTime12),
						progress121 + "", progress122 + "",
						beginTime13 = (beginTime13 == null ? "00:00"
								: beginTime13),
						endTime13 = (endTime13 == null ? "00:00" : endTime13),
						progress131 + "", progress132 + "",
						beginTime14 = (beginTime14 == null ? "00:00"
								: beginTime14),
						endTime14 = (endTime14 == null ? "00:00" : endTime14),
						progress141 + "", progress142 + "",
						groupName,physical));
				dao.insert(new Project(projectName, type, 1,
						beginTime21 = (beginTime21 == null ? "00:00"
								: beginTime21),
						endTime21 = (endTime21 == null ? "00:00" : endTime21),
						progress211 + "", progress212 + "",
						beginTime22 = (beginTime22 == null ? "00:00"
								: beginTime22),
						endTime22 = (endTime22 == null ? "00:00" : endTime22),
						progress221 + "", progress222 + "",
						beginTime23 = (beginTime23 == null ? "00:00"
								: beginTime23),
						endTime23 = (endTime23 == null ? "00:00" : endTime23),
						progress231 + "", progress232 + "",
						beginTime24 = (beginTime24 == null ? "00:00"
								: beginTime24),
						endTime24 = (endTime24 == null ? "00:00" : endTime24),
						progress241 + "", progress242 + "",
						groupName,physical));
				dao.insert(new Project(projectName, type, 2,
						beginTime31 = (beginTime31 == null ? "00:00"
								: beginTime31),
						endTime31 = (endTime31 == null ? "00:00" : endTime31),
						progress311 + "", progress312 + "",
						beginTime32 = (beginTime32 == null ? "00:00"
								: beginTime32),
						endTime32 = (endTime32 == null ? "00:00" : endTime32),
						progress321 + "", progress322 + "",
						beginTime33 = (beginTime33 == null ? "00:00"
								: beginTime33),
						endTime33 = (endTime33 == null ? "00:00" : endTime33),
						progress331 + "", progress332 + "",
						beginTime34 = (beginTime34 == null ? "00:00"
								: beginTime34),
						endTime34 = (endTime34 == null ? "00:00" : endTime34),
						progress341 + "", progress342 + "",
						groupName,physical));
				dao.insert(new Project(projectName, type, 3,
						beginTime41 = (beginTime41 == null ? "00:00"
								: beginTime41),
						endTime41 = (endTime41 == null ? "00:00" : endTime41),
						progress411 + "", progress412 + "",
						beginTime42 = (beginTime42 == null ? "00:00"
								: beginTime42),
						endTime42 = (endTime42 == null ? "00:00" : endTime42),
						progress421 + "", progress422 + "",
						beginTime43 = (beginTime43 == null ? "00:00"
								: beginTime43),
						endTime43 = (endTime43 == null ? "00:00" : endTime43),
						progress431 + "", progress432 + "",
						beginTime44 = (beginTime44 == null ? "00:00"
								: beginTime44),
						endTime44 = (endTime44 == null ? "00:00" : endTime44),
						progress441 + "", progress442 + "",
						groupName,physical));
				dao.insert(new Project(projectName, type, 4,
						beginTime51 = (beginTime51 == null ? "00:00"
								: beginTime51),
						endTime51 = (endTime51 == null ? "00:00" : endTime51),
						progress511 + "", progress512 + "",
						beginTime52 = (beginTime52 == null ? "00:00"
								: beginTime52),
						endTime52 = (endTime52 == null ? "00:00" : endTime52),
						progress521 + "", progress522 + "",
						beginTime53 = (beginTime53 == null ? "00:00"
								: beginTime53),
						endTime53 = (endTime53 == null ? "00:00" : endTime53),
						progress531 + "", progress532 + "",
						beginTime54 = (beginTime54 == null ? "00:00"
								: beginTime54),
						endTime54 = (endTime54 == null ? "00:00" : endTime54),
						progress541 + "", progress542 + "",
						groupName,physical));
				dao.insert(new Project(projectName, type, 5,
						beginTime61 = (beginTime61 == null ? "00:00"
								: beginTime61),
						endTime61 = (endTime61 == null ? "00:00" : endTime61),
						progress611 + "", progress612 + "",
						beginTime62 = (beginTime62 == null ? "00:00"
								: beginTime62),
						endTime62 = (endTime62 == null ? "00:00" : endTime62),
						progress621 + "", progress622 + "",
						beginTime63 = (beginTime63 == null ? "00:00"
								: beginTime63),
						endTime63 = (endTime63 == null ? "00:00" : endTime63),
						progress631 + "", progress632 + "",
						beginTime64 = (beginTime64 == null ? "00:00"
								: beginTime64),
						endTime64 = (endTime64 == null ? "00:00" : endTime64),
						progress641 + "", progress642 + "",
						groupName,physical));
				dao.insert(new Project(projectName, type, 6,
						beginTime71 = (beginTime71 == null ? "00:00"
								: beginTime71),
						endTime71 = (endTime71 == null ? "00:00" : endTime71),
						progress711 + "", progress712 + "",
						beginTime72 = (beginTime72 == null ? "00:00"
								: beginTime72),
						endTime72 = (endTime72 == null ? "00:00" : endTime72),
						progress721 + "", progress722 + "",
						beginTime73 = (beginTime73 == null ? "00:00"
								: beginTime73),
						endTime73 = (endTime73 == null ? "00:00" : endTime73),
						progress731 + "", progress732 + "",
						beginTime74 = (beginTime74 == null ? "00:00"
								: beginTime74),
						endTime74 = (endTime74 == null ? "00:00" : endTime74),
						progress741 + "", progress742 + "",
						groupName,physical));
				
				Intent intent6 = new Intent();
				if (projectName != null) {
					intent6.putExtra("planName", projectName);
				}
				setResult(2111, intent6);
				SingleColorAttributeEditActivity.this.finish();
				break;
			case R.id.btn_single_color_edit_left:
				weekday--;
				if (weekday < 0) {
					weekday = 6;
				}
				changeWeekDay(weekday);
				break;
			case R.id.btn_single_color_edit_right:
				weekday++;
				if (weekday > 6) {
					weekday = 0;
				}
				changeWeekDay(weekday);
				break;
			case R.id.tv_right_time1:
				if (pvTime != null) {
					pvTime.show(v);
				}
				break;
			case R.id.tv_right_time2:
				if (pvTime != null) {
					pvTime.show(v);
				}
				break;
			case R.id.tv_right_time3:
				if (pvTime != null) {
					pvTime.show(v);
				}
				break;

			}

		}
	};

	/**
	 * 星期的改变，影响seekbar的progress的改变
	 * @param po
	 */
	private void changeWeekDay(int po) {
		switch (po) {
		case 0:
			tvWeek.setText(getResources().getString(R.string.monday));
			seekbar11.setProgress(progress111);
			seekbar12.setProgress(progress112);
			seekbar21.setProgress(progress121);
			seekbar22.setProgress(progress122);
			seekbar31.setProgress(progress131);
			seekbar32.setProgress(progress132);
			seekbar41.setProgress(progress141);
			seekbar42.setProgress(progress142);
			time1_left.setText(beginTime11);
			time1_right.setText(endTime11);
			time2_left.setText(beginTime12);
			time2_right.setText(endTime12);
			time3_left.setText(beginTime13);
			time3_right.setText(endTime13);
			time4_left.setText(beginTime14);
			time4_right.setText(endTime14);
			tv1_1.setText(progress111 + "");
			tv1_2.setText(progress112 + minNum + "");
			tv2_1.setText(progress121 +  "");
			tv2_2.setText(progress122 + minNum +"");
			tv3_1.setText(progress131 +  "");
			tv3_2.setText(progress132 + minNum +"");
			tv4_1.setText(progress141 +  "");
			tv4_2.setText(progress142 + minNum +"");
			Log.e(TAG, "changeweekday中的progress111的值：" + progress111);
			break;
		case 1:
			tvWeek.setText(getResources().getString(R.string.tuesday));
			seekbar11.setProgress(progress211);
			seekbar12.setProgress(progress212);
			seekbar21.setProgress(progress221);
			seekbar22.setProgress(progress222);
			seekbar31.setProgress(progress231);
			seekbar32.setProgress(progress232);
			seekbar41.setProgress(progress241);
			seekbar42.setProgress(progress242);
			time1_left.setText(beginTime21);
			time1_right.setText(endTime21);
			time2_left.setText(beginTime22);
			time2_right.setText(endTime22);
			time3_left.setText(beginTime23);
			time3_right.setText(endTime23);
			time4_left.setText(beginTime24);
			time4_right.setText(endTime24);
			tv1_1.setText(progress211 + "");
			tv1_2.setText(progress212 +  minNum +"");
			tv2_1.setText(progress221 +  "");
			tv2_2.setText(progress222 + minNum +"");
			tv3_1.setText(progress231 +  "");
			tv3_2.setText(progress232 + minNum +"");
			tv4_1.setText(progress241 +  "");
			tv4_2.setText(progress242 + minNum +"");
			break;
		case 2:
			tvWeek.setText(getResources().getString(R.string.wednesday));
			seekbar11.setProgress(progress311);
			seekbar12.setProgress(progress312);
			seekbar21.setProgress(progress321);
			seekbar22.setProgress(progress322);
			seekbar31.setProgress(progress331);
			seekbar32.setProgress(progress332);
			seekbar41.setProgress(progress341);
			seekbar42.setProgress(progress342);
			time1_left.setText(beginTime31);
			time1_right.setText(endTime31);
			time2_left.setText(beginTime32);
			time2_right.setText(endTime32);
			time3_left.setText(beginTime33);
			time3_right.setText(endTime33);
			time4_left.setText(beginTime34);
			time4_right.setText(endTime34);
			tv1_1.setText(progress311 + "");
			tv1_2.setText(progress312 + minNum +"");
			tv2_1.setText(progress321 + "");
			tv2_2.setText(progress322 + minNum +"");
			tv3_1.setText(progress331 + "");
			tv3_2.setText(progress332 + minNum +"");
			tv4_1.setText(progress341 + "");
			tv4_2.setText(progress342 + minNum +"");
			break;
		case 3:
			tvWeek.setText(getResources().getString(R.string.thursday));
			seekbar11.setProgress(progress411);
			seekbar12.setProgress(progress412);
			seekbar21.setProgress(progress421);
			seekbar22.setProgress(progress422);
			seekbar31.setProgress(progress431);
			seekbar32.setProgress(progress432);
			seekbar41.setProgress(progress441);
			seekbar42.setProgress(progress442);
			time1_left.setText(beginTime41);
			time1_right.setText(endTime41);
			time2_left.setText(beginTime42);
			time2_right.setText(endTime42);
			time3_left.setText(beginTime43);
			time3_right.setText(endTime43);
			time4_left.setText(beginTime44);
			time4_right.setText(endTime44);
			tv1_1.setText(progress411 + "");
			tv1_2.setText(progress412 + minNum + "");
			tv2_1.setText(progress421 + "");
			tv2_2.setText(progress422 + minNum + "");
			tv3_1.setText(progress431 + "");
			tv3_2.setText(progress432 + minNum + "");
			tv4_1.setText(progress441 + "");
			tv4_2.setText(progress442 + minNum + "");
			break;
		case 4:
			tvWeek.setText(getResources().getString(R.string.friday));
			seekbar11.setProgress(progress511);
			seekbar12.setProgress(progress512);
			seekbar21.setProgress(progress521);
			seekbar22.setProgress(progress522);
			seekbar31.setProgress(progress531);
			seekbar32.setProgress(progress532);
			seekbar41.setProgress(progress541);
			seekbar42.setProgress(progress542);
			time1_left.setText(beginTime51);
			time1_right.setText(endTime51);
			time2_left.setText(beginTime52);
			time2_right.setText(endTime52);
			time3_left.setText(beginTime53);
			time3_right.setText(endTime53);
			time4_left.setText(beginTime54);
			time4_right.setText(endTime54);
			tv1_1.setText(progress511 + "");
			tv1_2.setText(progress512 + minNum + "");
			tv2_1.setText(progress521 + "");
			tv2_2.setText(progress522 + minNum + "");
			tv3_1.setText(progress531 + "");
			tv3_2.setText(progress532 + minNum + "");
			tv4_1.setText(progress541 + "");
			tv4_2.setText(progress542 + minNum + "");
			break;
		case 5:
			tvWeek.setText(getResources().getString(R.string.saturday));
			seekbar11.setProgress(progress611);
			seekbar12.setProgress(progress612);
			seekbar21.setProgress(progress621);
			seekbar22.setProgress(progress622);
			seekbar31.setProgress(progress631);
			seekbar32.setProgress(progress632);
			seekbar41.setProgress(progress641);
			seekbar42.setProgress(progress642);
			time1_left.setText(beginTime61);
			time1_right.setText(endTime61);
			time2_left.setText(beginTime62);
			time2_right.setText(endTime62);
			time3_left.setText(beginTime63);
			time3_right.setText(endTime63);
			time4_left.setText(beginTime64);
			time4_right.setText(endTime64);
			tv1_1.setText(progress611 + "");
			tv1_2.setText(progress612 + minNum + "");
			tv2_1.setText(progress621 + "");
			tv2_2.setText(progress622 + minNum + "");
			tv3_1.setText(progress631 + "");
			tv3_2.setText(progress632 + minNum + "");
			tv4_1.setText(progress641 + "");
			tv4_2.setText(progress642 + minNum + "");
			break;
		case 6:
			tvWeek.setText(getResources().getString(R.string.sunday));
			seekbar11.setProgress(progress711);
			seekbar12.setProgress(progress712);
			seekbar21.setProgress(progress721);
			seekbar22.setProgress(progress722);
			seekbar31.setProgress(progress731);
			seekbar32.setProgress(progress732);
			seekbar41.setProgress(progress741);
			seekbar42.setProgress(progress742);
			time1_left.setText(beginTime71);
			time1_right.setText(endTime71);
			time2_left.setText(beginTime72);
			time2_right.setText(endTime72);
			time3_left.setText(beginTime73);
			time3_right.setText(endTime73);
			time4_left.setText(beginTime74);
			time4_right.setText(endTime74);
			tv1_1.setText(progress711 + "");
			tv1_2.setText(progress712 + minNum + "");
			tv2_1.setText(progress721 + "");
			tv2_2.setText(progress722 + minNum + "");
			tv3_1.setText(progress731 + "");
			tv3_2.setText(progress732 + minNum + "");
			tv4_1.setText(progress741 + "");
			tv4_2.setText(progress742 + minNum + "");
			break;
		}
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0) {
			Intent intent = new Intent();
			if (projectName != null) {
				intent.putExtra("planName", projectName);
			}
			setResult(2111, intent);
			SingleColorAttributeEditActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
