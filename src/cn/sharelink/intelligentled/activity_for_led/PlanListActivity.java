package cn.sharelink.intelligentled.activity_for_led;

import java.util.ArrayList;
import java.util.List;

import cn.sharelink.intelligentled.R;
import cn.sharelink.intelligentled.R.layout;
import cn.sharelink.intelligentled.sql3_for_time_project.Project;
import cn.sharelink.intelligentled.sql3_for_time_project.ProjectDaoImpl;
import cn.sharelink.intelligentled.sql4_for_rgb_project.ProjectRGB;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * 单色灯和色温灯计划显示界面
 * @author Administrator
 *
 */
public class PlanListActivity extends Activity {
	private static final String TAG = PlanListActivity.class.getSimpleName();
	private Button back, edit;
	private TextView name;
	private LinearLayout ll;

	String progress111 = "0";
	String progress112 = "0";
	String progress121 = "0";
	String progress122 = "0";
	String progress131 = "0";
	String progress132 = "0";
	String progress141 = "0";
	String progress142 = "0";

	String progress211 = "0";
	String progress212 = "0";
	String progress221 = "0";
	String progress222 = "0";
	String progress231 = "0";
	String progress232 = "0";
	String progress241 = "0";
	String progress242 = "0";

	String progress311 = "0";
	String progress312 = "0";
	String progress321 = "0";
	String progress322 = "0";
	String progress331 = "0";
	String progress332 = "0";
	String progress341 = "0";
	String progress342 = "0";

	String progress411 = "0";
	String progress412 = "0";
	String progress421 = "0";
	String progress422 = "0";
	String progress431 = "0";
	String progress432 = "0";
	String progress441 = "0";
	String progress442 = "0";

	String progress511 = "0";
	String progress512 = "0";
	String progress521 = "0";
	String progress522 = "0";
	String progress531 = "0";
	String progress532 = "0";
	String progress541 = "0";
	String progress542 = "0";

	String progress611 = "0";
	String progress612 = "0";
	String progress621 = "0";
	String progress622 = "0";
	String progress631 = "0";
	String progress632 = "0";
	String progress641 = "0";
	String progress642 = "0";

	String progress711 = "0";
	String progress712 = "0";
	String progress721 = "0";
	String progress722 = "0";
	String progress731 = "0";
	String progress732 = "0";
	String progress741 = "0";
	String progress742 = "0";

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

	LayoutInflater inflater;
	ProjectDaoImpl dao;
	private String projectName;
	List<Project> projects;
	int style = 1;
	/**
	 * type=0,表示SingleColor; type=1,表示CCT Change; type=2,表示RGB;
	 */
	int type = 0;
String physical;
String groupName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		setContentView(R.layout.activity_plan_list);
		projectName=getIntent().getStringExtra("planName");
		style = getIntent().getIntExtra("style", 1);
		type = getIntent().getIntExtra("TYPE",0);
		physical = getIntent().getStringExtra("physical");
		groupName = getIntent().getStringExtra("groupName");
		if(groupName==null){
			groupName = "";
		}
		if(physical == null){
			physical = "";
		}
		dao = new ProjectDaoImpl(PlanListActivity.this);
		projects = new ArrayList<Project>();
		inflater = this.getLayoutInflater();
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initDate();
		initDateView();
	}
	
	private void initDateView() {
		LinearLayout.LayoutParams vParam = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		addMonday(vParam);
		addTuesday(vParam);
		addWednesday(vParam);
		addThursday(vParam);
		addFriday(vParam);
		addSaturday(vParam);
		addSunday(vParam);
	}

	private void addSunday(LayoutParams vParam) {
		View v7 = inflater.inflate(R.layout.plan_view_common, null);
		v7.setLayoutParams(vParam);
		TextView tvWeek = (TextView) v7.findViewById(R.id.tv_week);
		tvWeek.setText(getResources().getString(R.string.sunday));
		TextView tvTimeTime1 = (TextView) v7.findViewById(R.id.tv_time1_time);
		tvTimeTime1.setText(begintime71+"-"+endtime71);
		TextView tvTimeTime2 = (TextView) v7.findViewById(R.id.tv_time2_time);
		tvTimeTime2.setText(begintime72+"-"+endtime72);
		TextView tvTimeTime3 = (TextView) v7.findViewById(R.id.tv_time3_time);
		tvTimeTime3.setText(begintime73+"-"+endtime73);
		TextView tvTimeTime4 = (TextView) v7.findViewById(R.id.tv_time4_time);
		tvTimeTime4.setText(begintime74+"-"+endtime74);
		TextView tv11 = (TextView) v7.findViewById(R.id.tv_tv11);
		TextView tv12 = (TextView) v7.findViewById(R.id.tv_tv12);
		TextView tv21 = (TextView) v7.findViewById(R.id.tv_tv21);
		TextView tv22 = (TextView) v7.findViewById(R.id.tv_tv22);
		TextView tv31 = (TextView) v7.findViewById(R.id.tv_tv31);
		TextView tv32 = (TextView) v7.findViewById(R.id.tv_tv32);
		TextView tv41 = (TextView) v7.findViewById(R.id.tv_tv41);
		TextView tv42 = (TextView) v7.findViewById(R.id.tv_tv42);
		
		if (type==0) {
			tv11.setText(getResources().getString(R.string.begin_light_0));
			tv12.setText(getResources().getString(R.string.end_light_0));
			tv21.setText(getResources().getString(R.string.begin_light_0));
			tv22.setText(getResources().getString(R.string.end_light_0));
			tv31.setText(getResources().getString(R.string.begin_light_0));
			tv32.setText(getResources().getString(R.string.end_light_0));
			tv41.setText(getResources().getString(R.string.begin_light_0));
			tv42.setText(getResources().getString(R.string.end_light_0));
		}else if(type==1){
			tv12.setText(getResources().getString(R.string.cct_value));
			tv11.setText(getResources().getString(R.string.brightness_value));
			tv22.setText(getResources().getString(R.string.cct_value));
			tv21.setText(getResources().getString(R.string.brightness_value));
			tv32.setText(getResources().getString(R.string.cct_value));
			tv31.setText(getResources().getString(R.string.brightness_value));
			tv42.setText(getResources().getString(R.string.cct_value));
			tv41.setText(getResources().getString(R.string.brightness_value));
			
			progress712 = Integer.parseInt(progress712)+2700+"";
			progress722 = Integer.parseInt(progress722)+2700+"";
			progress732 = Integer.parseInt(progress732)+2700+"";
			progress742 = Integer.parseInt(progress742)+2700+"";
		}
		
		TextView tvColorTem1 = (TextView) v7.findViewById(R.id.tv_color_tem1);
		tvColorTem1.setText(progress711);
		TextView tvColorLight1 = (TextView) v7.findViewById(R.id.tv_color_light1);
		tvColorLight1.setText(progress712);
		
		TextView tvColorTem2 = (TextView) v7.findViewById(R.id.tv_color_tem2);
		tvColorTem2.setText(progress721);
		TextView tvColorLight2 = (TextView) v7.findViewById(R.id.tv_color_light2);
		tvColorLight2.setText(progress722);
		
		TextView tvColorTem3 = (TextView) v7.findViewById(R.id.tv_color_tem3);
		tvColorTem3.setText(progress731);
		TextView tvColorLight3 = (TextView) v7.findViewById(R.id.tv_color_light3);
		tvColorLight3.setText(progress732);
		
		TextView tvColorTem4 = (TextView) v7.findViewById(R.id.tv_color_tem4);
		tvColorTem4.setText(progress741);
		TextView tvColorLight4 = (TextView) v7.findViewById(R.id.tv_color_light4);
		tvColorLight4.setText(progress742);
		ll.addView(v7);
		
	}

	private void addSaturday(LayoutParams vParam) {
		View v6 = inflater.inflate(R.layout.plan_view_common, null);
		v6.setLayoutParams(vParam);
		TextView tvWeek = (TextView) v6.findViewById(R.id.tv_week);
		tvWeek.setText(getResources().getString(R.string.saturday));
		TextView tvTimeTime1 = (TextView) v6.findViewById(R.id.tv_time1_time);
		tvTimeTime1.setText(begintime61+"-"+endtime61);
		TextView tvTimeTime2 = (TextView) v6.findViewById(R.id.tv_time2_time);
		tvTimeTime2.setText(begintime62+"-"+endtime62);
		TextView tvTimeTime3 = (TextView) v6.findViewById(R.id.tv_time3_time);
		tvTimeTime3.setText(begintime63+"-"+endtime63);
		TextView tvTimeTime4 = (TextView) v6.findViewById(R.id.tv_time4_time);
		tvTimeTime4.setText(begintime64+"-"+endtime64);
		TextView tv11 = (TextView) v6.findViewById(R.id.tv_tv11);
		TextView tv12 = (TextView) v6.findViewById(R.id.tv_tv12);
		TextView tv21 = (TextView) v6.findViewById(R.id.tv_tv21);
		TextView tv22 = (TextView) v6.findViewById(R.id.tv_tv22);
		TextView tv31 = (TextView) v6.findViewById(R.id.tv_tv31);
		TextView tv32 = (TextView) v6.findViewById(R.id.tv_tv32);
		TextView tv41 = (TextView) v6.findViewById(R.id.tv_tv41);
		TextView tv42 = (TextView) v6.findViewById(R.id.tv_tv42);
		
		if (type==0) {
			tv11.setText(getResources().getString(R.string.begin_light_0));
			tv12.setText(getResources().getString(R.string.end_light_0));
			tv21.setText(getResources().getString(R.string.begin_light_0));
			tv22.setText(getResources().getString(R.string.end_light_0));
			tv31.setText(getResources().getString(R.string.begin_light_0));
			tv32.setText(getResources().getString(R.string.end_light_0));
			tv41.setText(getResources().getString(R.string.begin_light_0));
			tv42.setText(getResources().getString(R.string.end_light_0));
		}else if(type==1){
			tv12.setText(getResources().getString(R.string.cct_value));
			tv11.setText(getResources().getString(R.string.brightness_value));
			tv22.setText(getResources().getString(R.string.cct_value));
			tv21.setText(getResources().getString(R.string.brightness_value));
			tv32.setText(getResources().getString(R.string.cct_value));
			tv31.setText(getResources().getString(R.string.brightness_value));
			tv42.setText(getResources().getString(R.string.cct_value));
			tv41.setText(getResources().getString(R.string.brightness_value));
			
			progress612 = Integer.parseInt(progress612)+2700+"";
			progress622 = Integer.parseInt(progress622)+2700+"";
			progress632 = Integer.parseInt(progress632)+2700+"";
			progress642 = Integer.parseInt(progress642)+2700+"";
		}
		
		TextView tvColorTem1 = (TextView) v6.findViewById(R.id.tv_color_tem1);
		tvColorTem1.setText(progress611);
		TextView tvColorLight1 = (TextView) v6.findViewById(R.id.tv_color_light1);
		tvColorLight1.setText(progress612);
		
		TextView tvColorTem2 = (TextView) v6.findViewById(R.id.tv_color_tem2);
		tvColorTem2.setText(progress621);
		TextView tvColorLight2 = (TextView) v6.findViewById(R.id.tv_color_light2);
		tvColorLight2.setText(progress622);
		
		TextView tvColorTem3 = (TextView) v6.findViewById(R.id.tv_color_tem3);
		tvColorTem3.setText(progress631);
		TextView tvColorLight3 = (TextView) v6.findViewById(R.id.tv_color_light3);
		tvColorLight3.setText(progress632);
		
		TextView tvColorTem4 = (TextView) v6.findViewById(R.id.tv_color_tem4);
		tvColorTem4.setText(progress641);
		TextView tvColorLight4 = (TextView) v6.findViewById(R.id.tv_color_light4);
		tvColorLight4.setText(progress642);
		ll.addView(v6);
		
	}

	private void addFriday(LayoutParams vParam) {
		View v5 = inflater.inflate(R.layout.plan_view_common, null);
		v5.setLayoutParams(vParam);
		TextView tvWeek = (TextView) v5.findViewById(R.id.tv_week);
		tvWeek.setText(getResources().getString(R.string.friday));
		TextView tvTimeTime1 = (TextView) v5.findViewById(R.id.tv_time1_time);
		tvTimeTime1.setText(begintime51+"-"+endtime51);
		TextView tvTimeTime2 = (TextView) v5.findViewById(R.id.tv_time2_time);
		tvTimeTime2.setText(begintime52+"-"+endtime52);
		TextView tvTimeTime3 = (TextView) v5.findViewById(R.id.tv_time3_time);
		tvTimeTime3.setText(begintime53+"-"+endtime53);
		TextView tvTimeTime4 = (TextView) v5.findViewById(R.id.tv_time4_time);
		tvTimeTime4.setText(begintime54+"-"+endtime54);
		TextView tv11 = (TextView) v5.findViewById(R.id.tv_tv11);
		TextView tv12 = (TextView) v5.findViewById(R.id.tv_tv12);
		TextView tv21 = (TextView) v5.findViewById(R.id.tv_tv21);
		TextView tv22 = (TextView) v5.findViewById(R.id.tv_tv22);
		TextView tv31 = (TextView) v5.findViewById(R.id.tv_tv31);
		TextView tv32 = (TextView) v5.findViewById(R.id.tv_tv32);
		TextView tv41 = (TextView) v5.findViewById(R.id.tv_tv41);
		TextView tv42 = (TextView) v5.findViewById(R.id.tv_tv42);
		
		if (type==0) {
			tv11.setText(getResources().getString(R.string.begin_light_0));
			tv12.setText(getResources().getString(R.string.end_light_0));
			tv21.setText(getResources().getString(R.string.begin_light_0));
			tv22.setText(getResources().getString(R.string.end_light_0));
			tv31.setText(getResources().getString(R.string.begin_light_0));
			tv32.setText(getResources().getString(R.string.end_light_0));
			tv41.setText(getResources().getString(R.string.begin_light_0));
			tv42.setText(getResources().getString(R.string.end_light_0));
		}else if(type==1){
			tv12.setText(getResources().getString(R.string.cct_value));
			tv11.setText(getResources().getString(R.string.brightness_value));
			tv22.setText(getResources().getString(R.string.cct_value));
			tv21.setText(getResources().getString(R.string.brightness_value));
			tv32.setText(getResources().getString(R.string.cct_value));
			tv31.setText(getResources().getString(R.string.brightness_value));
			tv42.setText(getResources().getString(R.string.cct_value));
			tv41.setText(getResources().getString(R.string.brightness_value));
			
			progress512 = Integer.parseInt(progress512)+2700+"";
			progress522 = Integer.parseInt(progress522)+2700+"";
			progress532 = Integer.parseInt(progress532)+2700+"";
			progress542 = Integer.parseInt(progress542)+2700+"";
		}
		
		TextView tvColorTem1 = (TextView) v5.findViewById(R.id.tv_color_tem1);
		tvColorTem1.setText(progress511);
		TextView tvColorLight1 = (TextView) v5.findViewById(R.id.tv_color_light1);
		tvColorLight1.setText(progress512);
		
		TextView tvColorTem2 = (TextView) v5.findViewById(R.id.tv_color_tem2);
		tvColorTem2.setText(progress521);
		TextView tvColorLight2 = (TextView) v5.findViewById(R.id.tv_color_light2);
		tvColorLight2.setText(progress522);
		
		TextView tvColorTem3 = (TextView) v5.findViewById(R.id.tv_color_tem3);
		tvColorTem3.setText(progress531);
		TextView tvColorLight3 = (TextView) v5.findViewById(R.id.tv_color_light3);
		tvColorLight3.setText(progress532);
		
		TextView tvColorTem4 = (TextView) v5.findViewById(R.id.tv_color_tem4);
		tvColorTem4.setText(progress541);
		TextView tvColorLight4 = (TextView) v5.findViewById(R.id.tv_color_light4);
		tvColorLight4.setText(progress542);
		ll.addView(v5);
		
	}

	private void addThursday(LayoutParams vParam) {
		View v4 = inflater.inflate(R.layout.plan_view_common, null);
		v4.setLayoutParams(vParam);
		TextView tvWeek = (TextView) v4.findViewById(R.id.tv_week);
		tvWeek.setText(getResources().getString(R.string.thursday));
		TextView tvTimeTime1 = (TextView) v4.findViewById(R.id.tv_time1_time);
		tvTimeTime1.setText(begintime41+"-"+endtime41);
		TextView tvTimeTime2 = (TextView) v4.findViewById(R.id.tv_time2_time);
		tvTimeTime2.setText(begintime42+"-"+endtime42);
		TextView tvTimeTime3 = (TextView) v4.findViewById(R.id.tv_time3_time);
		tvTimeTime3.setText(begintime43+"-"+endtime43);
		TextView tvTimeTime4 = (TextView) v4.findViewById(R.id.tv_time4_time);
		tvTimeTime4.setText(begintime44+"-"+endtime44);
		TextView tv11 = (TextView) v4.findViewById(R.id.tv_tv11);
		TextView tv12 = (TextView) v4.findViewById(R.id.tv_tv12);
		TextView tv21 = (TextView) v4.findViewById(R.id.tv_tv21);
		TextView tv22 = (TextView) v4.findViewById(R.id.tv_tv22);
		TextView tv31 = (TextView) v4.findViewById(R.id.tv_tv31);
		TextView tv32 = (TextView) v4.findViewById(R.id.tv_tv32);
		TextView tv41 = (TextView) v4.findViewById(R.id.tv_tv41);
		TextView tv42 = (TextView) v4.findViewById(R.id.tv_tv42);
		
		if (type==0) {
			tv11.setText(getResources().getString(R.string.begin_light_0));
			tv12.setText(getResources().getString(R.string.end_light_0));
			tv21.setText(getResources().getString(R.string.begin_light_0));
			tv22.setText(getResources().getString(R.string.end_light_0));
			tv31.setText(getResources().getString(R.string.begin_light_0));
			tv32.setText(getResources().getString(R.string.end_light_0));
			tv41.setText(getResources().getString(R.string.begin_light_0));
			tv42.setText(getResources().getString(R.string.end_light_0));
		}else if(type==1){
			tv12.setText(getResources().getString(R.string.cct_value));
			tv11.setText(getResources().getString(R.string.brightness_value));
			tv22.setText(getResources().getString(R.string.cct_value));
			tv21.setText(getResources().getString(R.string.brightness_value));
			tv32.setText(getResources().getString(R.string.cct_value));
			tv31.setText(getResources().getString(R.string.brightness_value));
			tv42.setText(getResources().getString(R.string.cct_value));
			tv41.setText(getResources().getString(R.string.brightness_value));
			
			progress412 = Integer.parseInt(progress412)+2700+"";
			progress422 = Integer.parseInt(progress422)+2700+"";
			progress432 = Integer.parseInt(progress432)+2700+"";
			progress442 = Integer.parseInt(progress442)+2700+"";
		}
		
		TextView tvColorTem1 = (TextView) v4.findViewById(R.id.tv_color_tem1);
		tvColorTem1.setText(progress411);
		TextView tvColorLight1 = (TextView) v4.findViewById(R.id.tv_color_light1);
		tvColorLight1.setText(progress412);
		
		TextView tvColorTem2 = (TextView) v4.findViewById(R.id.tv_color_tem2);
		tvColorTem2.setText(progress421);
		TextView tvColorLight2 = (TextView) v4.findViewById(R.id.tv_color_light2);
		tvColorLight2.setText(progress422);
		
		TextView tvColorTem3 = (TextView) v4.findViewById(R.id.tv_color_tem3);
		tvColorTem3.setText(progress431);
		TextView tvColorLight3 = (TextView) v4.findViewById(R.id.tv_color_light3);
		tvColorLight3.setText(progress432);
		
		TextView tvColorTem4 = (TextView) v4.findViewById(R.id.tv_color_tem4);
		tvColorTem4.setText(progress441);
		TextView tvColorLight4 = (TextView) v4.findViewById(R.id.tv_color_light4);
		tvColorLight4.setText(progress442);
		ll.addView(v4);
		
	}

	private void addWednesday(LayoutParams vParam) {
		View v3 = inflater.inflate(R.layout.plan_view_common, null);
		v3.setLayoutParams(vParam);
		TextView tvWeek = (TextView) v3.findViewById(R.id.tv_week);
		tvWeek.setText(getResources().getString(R.string.wednesday));
		TextView tvTimeTime1 = (TextView) v3.findViewById(R.id.tv_time1_time);
		tvTimeTime1.setText(begintime31+"-"+endtime31);
		TextView tvTimeTime2 = (TextView) v3.findViewById(R.id.tv_time2_time);
		tvTimeTime2.setText(begintime32+"-"+endtime32);
		TextView tvTimeTime3 = (TextView) v3.findViewById(R.id.tv_time3_time);
		tvTimeTime3.setText(begintime33+"-"+endtime33);
		TextView tvTimeTime4 = (TextView) v3.findViewById(R.id.tv_time4_time);
		tvTimeTime4.setText(begintime34+"-"+endtime34);
		TextView tv11 = (TextView) v3.findViewById(R.id.tv_tv11);
		TextView tv12 = (TextView) v3.findViewById(R.id.tv_tv12);
		TextView tv21 = (TextView) v3.findViewById(R.id.tv_tv21);
		TextView tv22 = (TextView) v3.findViewById(R.id.tv_tv22);
		TextView tv31 = (TextView) v3.findViewById(R.id.tv_tv31);
		TextView tv32 = (TextView) v3.findViewById(R.id.tv_tv32);
		TextView tv41 = (TextView) v3.findViewById(R.id.tv_tv41);
		TextView tv42 = (TextView) v3.findViewById(R.id.tv_tv42);
		
		if (type==0) {
			tv11.setText(getResources().getString(R.string.begin_light_0));
			tv12.setText(getResources().getString(R.string.end_light_0));
			tv21.setText(getResources().getString(R.string.begin_light_0));
			tv22.setText(getResources().getString(R.string.end_light_0));
			tv31.setText(getResources().getString(R.string.begin_light_0));
			tv32.setText(getResources().getString(R.string.end_light_0));
			tv41.setText(getResources().getString(R.string.begin_light_0));
			tv42.setText(getResources().getString(R.string.end_light_0));
		}else if(type==1){
			tv12.setText(getResources().getString(R.string.cct_value));
			tv11.setText(getResources().getString(R.string.brightness_value));
			tv22.setText(getResources().getString(R.string.cct_value));
			tv21.setText(getResources().getString(R.string.brightness_value));
			tv32.setText(getResources().getString(R.string.cct_value));
			tv31.setText(getResources().getString(R.string.brightness_value));
			tv42.setText(getResources().getString(R.string.cct_value));
			tv41.setText(getResources().getString(R.string.brightness_value));
			
			progress312 = Integer.parseInt(progress312)+2700+"";
			progress322 = Integer.parseInt(progress322)+2700+"";
			progress332 = Integer.parseInt(progress332)+2700+"";
			progress342 = Integer.parseInt(progress342)+2700+"";
		}
		
		TextView tvColorTem1 = (TextView) v3.findViewById(R.id.tv_color_tem1);
		tvColorTem1.setText(progress311);
		TextView tvColorLight1 = (TextView) v3.findViewById(R.id.tv_color_light1);
		tvColorLight1.setText(progress312);
		
		TextView tvColorTem2 = (TextView) v3.findViewById(R.id.tv_color_tem2);
		tvColorTem2.setText(progress321);
		TextView tvColorLight2 = (TextView) v3.findViewById(R.id.tv_color_light2);
		tvColorLight2.setText(progress322);
		
		TextView tvColorTem3 = (TextView) v3.findViewById(R.id.tv_color_tem3);
		tvColorTem3.setText(progress331);
		TextView tvColorLight3 = (TextView) v3.findViewById(R.id.tv_color_light3);
		tvColorLight3.setText(progress332);
		
		TextView tvColorTem4 = (TextView) v3.findViewById(R.id.tv_color_tem4);
		tvColorTem4.setText(progress341);
		TextView tvColorLight4 = (TextView) v3.findViewById(R.id.tv_color_light4);
		tvColorLight4.setText(progress342);
		ll.addView(v3);
		
	}

	private void addTuesday(LayoutParams vParam) {
		View v2 = inflater.inflate(R.layout.plan_view_common, null);
		v2.setLayoutParams(vParam);
		TextView tvWeek = (TextView) v2.findViewById(R.id.tv_week);
		tvWeek.setText(getResources().getString(R.string.tuesday));
		TextView tvTimeTime1 = (TextView) v2.findViewById(R.id.tv_time1_time);
		tvTimeTime1.setText(begintime21+"-"+endtime21);
		TextView tvTimeTime2 = (TextView) v2.findViewById(R.id.tv_time2_time);
		tvTimeTime2.setText(begintime22+"-"+endtime22);
		TextView tvTimeTime3 = (TextView) v2.findViewById(R.id.tv_time3_time);
		tvTimeTime3.setText(begintime23+"-"+endtime23);
		TextView tvTimeTime4 = (TextView) v2.findViewById(R.id.tv_time4_time);
		tvTimeTime4.setText(begintime24+"-"+endtime24);
		
		TextView tv11 = (TextView) v2.findViewById(R.id.tv_tv11);
		TextView tv12 = (TextView) v2.findViewById(R.id.tv_tv12);
		TextView tv21 = (TextView) v2.findViewById(R.id.tv_tv21);
		TextView tv22 = (TextView) v2.findViewById(R.id.tv_tv22);
		TextView tv31 = (TextView) v2.findViewById(R.id.tv_tv31);
		TextView tv32 = (TextView) v2.findViewById(R.id.tv_tv32);
		TextView tv41 = (TextView) v2.findViewById(R.id.tv_tv41);
		TextView tv42 = (TextView) v2.findViewById(R.id.tv_tv42);
		
		if (type==0) {
			tv11.setText(getResources().getString(R.string.begin_light_0));
			tv12.setText(getResources().getString(R.string.end_light_0));
			tv21.setText(getResources().getString(R.string.begin_light_0));
			tv22.setText(getResources().getString(R.string.end_light_0));
			tv31.setText(getResources().getString(R.string.begin_light_0));
			tv32.setText(getResources().getString(R.string.end_light_0));
			tv41.setText(getResources().getString(R.string.begin_light_0));
			tv42.setText(getResources().getString(R.string.end_light_0));
		}else if(type==1){
			tv12.setText(getResources().getString(R.string.cct_value));
			tv11.setText(getResources().getString(R.string.brightness_value));
			tv22.setText(getResources().getString(R.string.cct_value));
			tv21.setText(getResources().getString(R.string.brightness_value));
			tv32.setText(getResources().getString(R.string.cct_value));
			tv31.setText(getResources().getString(R.string.brightness_value));
			tv42.setText(getResources().getString(R.string.cct_value));
			tv41.setText(getResources().getString(R.string.brightness_value));
			
			progress212 = Integer.parseInt(progress212)+2700+"";
			progress222 = Integer.parseInt(progress222)+2700+"";
			progress232 = Integer.parseInt(progress232)+2700+"";
			progress242 = Integer.parseInt(progress242)+2700+"";
		}
		
		TextView tvColorTem1 = (TextView) v2.findViewById(R.id.tv_color_tem1);
		tvColorTem1.setText(progress211);
		TextView tvColorLight1 = (TextView) v2.findViewById(R.id.tv_color_light1);
		tvColorLight1.setText(progress212);
		
		TextView tvColorTem2 = (TextView) v2.findViewById(R.id.tv_color_tem2);
		tvColorTem2.setText(progress221);
		TextView tvColorLight2 = (TextView) v2.findViewById(R.id.tv_color_light2);
		tvColorLight2.setText(progress222);
		
		TextView tvColorTem3 = (TextView) v2.findViewById(R.id.tv_color_tem3);
		tvColorTem3.setText(progress231);
		TextView tvColorLight3 = (TextView) v2.findViewById(R.id.tv_color_light3);
		tvColorLight3.setText(progress232);
		
		TextView tvColorTem4 = (TextView) v2.findViewById(R.id.tv_color_tem4);
		tvColorTem4.setText(progress241);
		TextView tvColorLight4 = (TextView) v2.findViewById(R.id.tv_color_light4);
		tvColorLight4.setText(progress242);
		ll.addView(v2);
		
	}

	private void addMonday(LayoutParams vParam) {
			View v1 = inflater.inflate(R.layout.plan_view_common, null);
			v1.setLayoutParams(vParam);
			TextView tvWeek = (TextView) v1.findViewById(R.id.tv_week);
			tvWeek.setText(getResources().getString(R.string.monday));
			TextView tvTimeTime1 = (TextView) v1.findViewById(R.id.tv_time1_time);
			tvTimeTime1.setText(begintime11+"-"+endtime11);
			TextView tvTimeTime2 = (TextView) v1.findViewById(R.id.tv_time2_time);
			tvTimeTime2.setText(begintime12+"-"+endtime12);
			TextView tvTimeTime3 = (TextView) v1.findViewById(R.id.tv_time3_time);
			tvTimeTime3.setText(begintime13+"-"+endtime13);
			TextView tvTimeTime4 = (TextView) v1.findViewById(R.id.tv_time4_time);
			tvTimeTime4.setText(begintime14+"-"+endtime14);
			
			TextView tv11 = (TextView) v1.findViewById(R.id.tv_tv11);
			TextView tv12 = (TextView) v1.findViewById(R.id.tv_tv12);
			TextView tv21 = (TextView) v1.findViewById(R.id.tv_tv21);
			TextView tv22 = (TextView) v1.findViewById(R.id.tv_tv22);
			TextView tv31 = (TextView) v1.findViewById(R.id.tv_tv31);
			TextView tv32 = (TextView) v1.findViewById(R.id.tv_tv32);
			TextView tv41 = (TextView) v1.findViewById(R.id.tv_tv41);
			TextView tv42 = (TextView) v1.findViewById(R.id.tv_tv42);
			
			if (type==0) {
				tv11.setText(getResources().getString(R.string.begin_light_0));
				tv12.setText(getResources().getString(R.string.end_light_0));
				tv21.setText(getResources().getString(R.string.begin_light_0));
				tv22.setText(getResources().getString(R.string.end_light_0));
				tv31.setText(getResources().getString(R.string.begin_light_0));
				tv32.setText(getResources().getString(R.string.end_light_0));
				tv41.setText(getResources().getString(R.string.begin_light_0));
				tv42.setText(getResources().getString(R.string.end_light_0));
			}else if(type==1){
				tv12.setText(getResources().getString(R.string.cct_value));
				tv11.setText(getResources().getString(R.string.brightness_value));
				tv22.setText(getResources().getString(R.string.cct_value));
				tv21.setText(getResources().getString(R.string.brightness_value));
				tv32.setText(getResources().getString(R.string.cct_value));
				tv31.setText(getResources().getString(R.string.brightness_value));
				tv42.setText(getResources().getString(R.string.cct_value));
				tv41.setText(getResources().getString(R.string.brightness_value));
				
				progress112 = Integer.parseInt(progress112)+2700+"";
				progress122 = Integer.parseInt(progress122)+2700+"";
				progress132 = Integer.parseInt(progress132)+2700+"";
				progress142 = Integer.parseInt(progress142)+2700+"";
			}
			
			TextView tvColorTem1 = (TextView) v1.findViewById(R.id.tv_color_tem1);
			tvColorTem1.setText(progress111);
			TextView tvColorLight1 = (TextView) v1.findViewById(R.id.tv_color_light1);
			tvColorLight1.setText(progress112);
			
			TextView tvColorTem2 = (TextView) v1.findViewById(R.id.tv_color_tem2);
			tvColorTem2.setText(progress121);
			TextView tvColorLight2 = (TextView) v1.findViewById(R.id.tv_color_light2);
			tvColorLight2.setText(progress122);
			
			TextView tvColorTem3 = (TextView) v1.findViewById(R.id.tv_color_tem3);
			tvColorTem3.setText(progress131);
			TextView tvColorLight3 = (TextView) v1.findViewById(R.id.tv_color_light3);
			tvColorLight3.setText(progress132);
			
			TextView tvColorTem4 = (TextView) v1.findViewById(R.id.tv_color_tem4);
			tvColorTem4.setText(progress141);
			TextView tvColorLight4 = (TextView) v1.findViewById(R.id.tv_color_light4);
			tvColorLight4.setText(progress142);
			ll.addView(v1);
	}

	private void initDate() {
		List<Project> pros = dao.query(null, null);
		if (pros.size()>0) {
			for(Project pro:pros){
				if (pro.getName().equals(projectName) 
						&& pro.getPhysical().equals(physical)
						&& pro.getType()==type
						&& pro.getGroupName().equals(groupName)) {
					projects.add(pro);
				}
			}
			
			for(int i =0 ;i<projects.size();i++){
				begintime11 = projects.get(6).getBegintime1();
				endtime11 = projects.get(6).getEndtime1();
				progress111 = projects.get(6).getSeek11();
				progress112 = projects.get(6).getSeek12();
				begintime12 = projects.get(6).getBegintime2();
				endtime12 = projects.get(6).getEndtime2();
				progress121 = projects.get(6).getSeek21();
				progress122 = projects.get(6).getSeek22();
				begintime13 = projects.get(6).getBegintime3();
				endtime13 = projects.get(6).getEndtime3();
				progress131 = projects.get(6).getSeek31();
				progress132 = projects.get(6).getSeek32();
				begintime14 = projects.get(6).getBegintime4();
				endtime14 = projects.get(6).getEndtime4();
				progress141 = projects.get(6).getSeek41();
				progress142 = projects.get(6).getSeek42();
				
				begintime21 = projects.get(5).getBegintime1();
				endtime21 = projects.get(5).getEndtime1();
				progress211 = projects.get(5).getSeek11();
				progress212 = projects.get(5).getSeek12();
				begintime22 = projects.get(5).getBegintime2();
				endtime22 = projects.get(5).getEndtime2();
				progress221 = projects.get(5).getSeek21();
				progress222 = projects.get(5).getSeek22();
				begintime23 = projects.get(5).getBegintime3();
				endtime23 = projects.get(5).getEndtime3();
				progress231 = projects.get(5).getSeek31();
				progress232 = projects.get(5).getSeek32();
				begintime24 = projects.get(5).getBegintime4();
				endtime24 = projects.get(5).getEndtime4();
				progress241 = projects.get(5).getSeek41();
				progress242 = projects.get(5).getSeek42();
				
				begintime31 = projects.get(4).getBegintime1();
				endtime31 = projects.get(4).getEndtime1();
				progress311 = projects.get(4).getSeek11();
				progress312 = projects.get(4).getSeek12();
				begintime32 = projects.get(4).getBegintime2();
				endtime32 = projects.get(4).getEndtime2();
				progress321 = projects.get(4).getSeek21();
				progress322 = projects.get(4).getSeek22();
				begintime33 = projects.get(4).getBegintime3();
				endtime33 = projects.get(4).getEndtime3();
				progress331 = projects.get(4).getSeek31();
				progress332 = projects.get(4).getSeek32();
				begintime34 = projects.get(4).getBegintime4();
				endtime34 = projects.get(4).getEndtime4();
				progress341 = projects.get(4).getSeek41();
				progress342 = projects.get(4).getSeek42();
				
				begintime41 = projects.get(3).getBegintime1();
				endtime41 = projects.get(3).getEndtime1();
				progress411 = projects.get(3).getSeek11();
				progress412 = projects.get(3).getSeek12();
				begintime42 = projects.get(3).getBegintime2();
				endtime42 = projects.get(3).getEndtime2();
				progress421 = projects.get(3).getSeek21();
				progress422 = projects.get(3).getSeek22();
				begintime43 = projects.get(3).getBegintime3();
				endtime43 = projects.get(3).getEndtime3();
				progress431 = projects.get(3).getSeek31();
				progress432 = projects.get(3).getSeek32();
				begintime44 = projects.get(3).getBegintime4();
				endtime44 = projects.get(3).getEndtime4();
				progress441 = projects.get(3).getSeek41();
				progress442 = projects.get(3).getSeek42();
				
				begintime51 = projects.get(2).getBegintime1();
				endtime51 = projects.get(2).getEndtime1();
				progress511 = projects.get(2).getSeek11();
				progress512 = projects.get(2).getSeek12();
				begintime52 = projects.get(2).getBegintime2();
				endtime52 = projects.get(2).getEndtime2();
				progress521 = projects.get(2).getSeek21();
				progress522 = projects.get(2).getSeek22();
				begintime53 = projects.get(2).getBegintime3();
				endtime53 = projects.get(2).getEndtime3();
				progress531 = projects.get(2).getSeek31();
				progress532 = projects.get(2).getSeek32();
				begintime54 = projects.get(2).getBegintime4();
				endtime54 = projects.get(2).getEndtime4();
				progress541 = projects.get(2).getSeek41();
				progress542 = projects.get(2).getSeek42();
				
				begintime61 = projects.get(1).getBegintime1();
				endtime61 = projects.get(1).getEndtime1();
				progress611 = projects.get(1).getSeek11();
				progress612 = projects.get(1).getSeek12();
				begintime62 = projects.get(1).getBegintime2();
				endtime62 = projects.get(1).getEndtime2();
				progress621 = projects.get(1).getSeek21();
				progress622 = projects.get(1).getSeek22();
				begintime63 = projects.get(1).getBegintime3();
				endtime63 = projects.get(1).getEndtime3();
				progress631 = projects.get(1).getSeek31();
				progress632 = projects.get(1).getSeek32();
				begintime64 = projects.get(1).getBegintime4();
				endtime64 = projects.get(1).getEndtime4();
				progress641 = projects.get(1).getSeek41();
				progress642 = projects.get(1).getSeek42();
				
				begintime71 = projects.get(0).getBegintime1();
				endtime71 = projects.get(0).getEndtime1();
				progress711 = projects.get(0).getSeek11();
				progress712 = projects.get(0).getSeek12();
				begintime72 = projects.get(0).getBegintime2();
				endtime72 = projects.get(0).getEndtime2();
				progress721 = projects.get(0).getSeek21();
				progress722 = projects.get(0).getSeek22();
				begintime73 = projects.get(0).getBegintime3();
				endtime73 = projects.get(0).getEndtime3();
				progress731 = projects.get(0).getSeek31();
				progress732 = projects.get(0).getSeek32();
				begintime74 = projects.get(0).getBegintime4();
				endtime74 = projects.get(0).getEndtime4();
				progress741 = projects.get(0).getSeek41();
				progress742 = projects.get(0).getSeek42();
				
			}
		}
		
	}

	private void initView() {
		back = (Button) findViewById(R.id.tv_plan_back);
		edit = (Button) findViewById(R.id.tv_plan_edit);
		name = (TextView) findViewById(R.id.tv_plan_name);
		name.setText(projectName);
		ll = (LinearLayout) findViewById(R.id.ll);
		back.setOnClickListener(listener);
		edit.setOnClickListener(listener);

	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_plan_back:
				PlanListActivity.this.finish();
				break;

			case R.id.tv_plan_edit:
				Intent intent = new Intent(PlanListActivity.this,SingleColorAttributeEditActivity.class);
				intent.putExtra("planName", projectName);
				intent.putExtra("style", style);
				intent.putExtra("TYPE", type);
				intent.putExtra("physical", physical);
				intent.putExtra("groupName", groupName);
				startActivity(intent);
				PlanListActivity.this.finish();
				break;
			}

		}
	};
}
