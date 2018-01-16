package cn.sharelink.intelligentled.activity_for_led;

import java.util.ArrayList;
import java.util.List;

import cn.sharelink.intelligentled.R;
import cn.sharelink.intelligentled.R.layout;
import cn.sharelink.intelligentled.sql4_for_rgb_project.ProjectRGB;
import cn.sharelink.intelligentled.sql4_for_rgb_project.ProjectRGBDaoImpl;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 彩色灯计划显示界面
 * @author Administrator
 *
 */
public class PlanListRGBActivity extends Activity {
	private static final String TAG = PlanListRGBActivity.class.getSimpleName();

	private Button back, edit;
	private TextView name;
	private LinearLayout ll;
	LayoutInflater inflater;

	ProjectRGBDaoImpl dao;

	/**
	 * 新建计划的名称
	 */
	private String projectName;

	String one14, two14, three14, four14, five14, six14, seven14,eight14; // 星期一，模式4的值
	String one24, two24, three24, four24, five24, six24, seven24,eight24;// 星期二，模式4的值
	String one34, two34, three34, four34, five34, six34, seven34,eight34;// 星期三，模式4的值
	String one44, two44, three44, four44, five44, six44, seven44,eight44;// 星期四，模式4的值
	String one54, two54, three54, four54, five54, six54, seven54,eight54;//星期五，模式4的值
	String one64, two64, three64, four64, five64, six64, seven64,eight64;//星期六，模式4的值
	String one74, two74, three74, four74, five74, six74, seven74,eight74;//星期日，模式4的值
	String oneR14, oneG14, oneB14, twoR14, twoG14, twoB14, threeR14,
			threeG14, threeB14, fourR14, fourG14, fourB14, fiveR14,
			fiveG14, fiveB14;
	String oneR24, oneG24, oneB24, twoR24, twoG24, twoB24, threeR24,
			threeG24, threeB24, fourR24, fourG24, fourB24, fiveR24,
			fiveG24, fiveB24;
	String oneR34, oneG34, oneB34, twoR34, twoG34, twoB34, threeR34,
			threeG34, threeB34, fourR34, fourG34, fourB34, fiveR34,
			fiveG34, fiveB34;
	String oneR44, oneG44, oneB44, twoR44, twoG44, twoB44, threeR44,
			threeG44, threeB44, fourR44, fourG44, fourB44, fiveR44,
			fiveG44, fiveB44;
	String oneR54, oneG54, oneB54, twoR54, twoG54, twoB54, threeR54,
			threeG54, threeB54, fourR54, fourG54, fourB54, fiveR54,
			fiveG54, fiveB54;
	String oneR64, oneG64, oneB64, twoR64, twoG64, twoB64, threeR64,
			threeG64, threeB64, fourR64, fourG64, fourB64, fiveR64,
			fiveG64, fiveB64;
	String oneR74, oneG74, oneB74, twoR74, twoG74, twoB74, threeR74,
			threeG74, threeB74, fourR74, fourG74, fourB74, fiveR74,
			fiveG74, fiveB74;

	String rotationTime1,rotationTime2,rotationTime3,rotationTime4,rotationTime5,rotationTime6,rotationTime7;
	int mode1,mode2,mode3,mode4,mode5,mode6,mode7;
	
	List<ProjectRGB> projectrbgs;
	int style=1;
	
	String physical;
	String groupName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		setContentView(R.layout.activity_plan_list_rgb);
		projectName=getIntent().getStringExtra("planName");
		style = getIntent().getIntExtra("style", 1);
		physical = getIntent().getStringExtra("physical");
		groupName = getIntent().getStringExtra("groupName");
		if(groupName==null){
			groupName="";
		}
		if(physical==null){
			physical="";
		}
		dao = new ProjectRGBDaoImpl(PlanListRGBActivity.this);
		projectrbgs = new ArrayList<ProjectRGB>();
		
		back = (Button) findViewById(R.id.btn_plan_rgb_back);
		edit = (Button) findViewById(R.id.btn_plan_rgb_edit);
		name = (TextView) findViewById(R.id.tv_plan_rgb_name);
		name.setText(projectName);
		ll = (LinearLayout) findViewById(R.id.ll);
		back.setOnClickListener(listener);
		edit.setOnClickListener(listener);
		inflater = this.getLayoutInflater();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
		initDate();
		initDateView();
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_plan_rgb_back:
				PlanListRGBActivity.this.finish();
				break;

			case R.id.btn_plan_rgb_edit:
				Intent intent = new Intent(PlanListRGBActivity.this,SingleColorAttributeEdit2Activity.class);
				intent.putExtra("planName", projectName);
				intent.putExtra("style", style);
				intent.putExtra("physical", physical);
				intent.putExtra("groupName", groupName);
				startActivity(intent);
				PlanListRGBActivity.this.finish();
				break;
			}

		}
	};

	private void initDateView() {
		LinearLayout.LayoutParams vParam = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		// vParam.setMargins(5, 10, 5, 0);
		addMonday(vParam);
		addTuesday(vParam);
		addWednesday(vParam);
		addThursday(vParam);
		addFriday(vParam);
		addSaturday(vParam);
		addSunday(vParam);
	}

	private void addSunday(LayoutParams vParam) {
		View v7 = inflater.inflate(R.layout.plan_view, null);
		v7.setLayoutParams(vParam);
		TextView tvWeek = (TextView) v7.findViewById(R.id.tv_week);
		tvWeek.setText(getResources().getString(R.string.sunday));
		TextView tvMode1 = (TextView) v7.findViewById(R.id.tv_time1_mode);
		tvMode1.setText(mode7+1+"");
		RelativeLayout rl1 = (RelativeLayout) v7.findViewById(R.id.rl_monday_time1_mode);
		
		if (mode7==3) {
			one74 = (one74.length()==0)?"[0]":"["+one74+"]";
			two74=(two74.length()==0)?"[0]":"["+two74+"]";
			three74=(three74.length()==0)?"[0]":"["+three74+"]";
			four74=(four74.length()==0)?"[0]":"["+four74+"]";
			five74=(five74.length()==0)?"[0]":"["+five74+"]";
			six74=(six74.length()==0)?"[0]":"["+six74+"]";
			seven74=(seven74.length()==0)?"[0]":"["+seven74+"]";
			eight74=(eight74.length()==0)?"[0]":"["+eight74+"]";
			oneR74 = (oneR74.length()==0)?"[0]":"["+oneR74+"]";
			oneG74 = (oneG74.length()==0)?"[0]":"["+oneG74+"]";
			oneB74 = (oneB74.length()==0)?"[0]":"["+oneB74+"]";
			twoR74=(twoR74.length()==0)?"[0]":"["+twoR74+"]";
			twoG74=(twoG74.length()==0)?"[0]":"["+twoG74+"]";
			twoB74=(twoB74.length()==0)?"[0]":"["+twoB74+"]";
			threeR74=(threeR74.length()==0)?"[0]":"["+threeR74+"]";
			threeG74=(threeG74.length()==0)?"[0]":"["+threeG74+"]";
			threeB74=(threeB74.length()==0)?"[0]":"["+threeB74+"]";
			fourR74=(fourR74.length()==0)?"[0]":"["+fourR74+"]";
			fourG74=(fourG74.length()==0)?"[0]":"["+fourG74+"]";
			fourB74=(fourB74.length()==0)?"[0]":"["+fourB74+"]";
			fiveR74=(fiveR74.length()==0)?"[0]":"["+fiveR74+"]";
			fiveG74=(fiveG74.length()==0)?"[0]":"["+fiveG74+"]";
			fiveB74=(fiveB74.length()==0)?"[0]":"["+fiveB74+"]";
			rotationTime7 = (rotationTime7.length()==0)?"0":rotationTime7;
			rl1.setVisibility(View.VISIBLE);
			TextView tvOne1 = (TextView) v7.findViewById(R.id.tv_time1_one);
			tvOne1.setText(one74);
			TextView tvTwo1 = (TextView) v7.findViewById(R.id.tv_time1_two);
			tvTwo1.setText(two74);
			TextView tvThree1 = (TextView) v7.findViewById(R.id.tv_time1_three);
			tvThree1.setText(three74);
			TextView tvFour1 = (TextView) v7.findViewById(R.id.tv_time1_four);
			tvFour1.setText(four74);
			TextView tvFive1 = (TextView) v7.findViewById(R.id.tv_time1_five);
			tvFive1.setText(five74);
			TextView tvSix1 = (TextView) v7.findViewById(R.id.tv_time1_six);
			tvSix1.setText(six74);
			TextView tvSeven1 = (TextView) v7.findViewById(R.id.tv_time1_seven);
			tvSeven1.setText(seven74);
			TextView tvEight1 = (TextView) v7.findViewById(R.id.tv_time1_eight);
			tvEight1.setText(eight74);
			TextView tvOneR1 = (TextView) v7.findViewById(R.id.tv_time1_oner);
			tvOneR1.setText(oneR74);
			TextView tvOneG1 = (TextView) v7.findViewById(R.id.tv_time1_oneg);
			tvOneG1.setText(oneG74);
			TextView tvOneB1 = (TextView) v7.findViewById(R.id.tv_time1_oneb);
			tvOneB1.setText(oneB74);
			TextView tvTwoR1 = (TextView) v7.findViewById(R.id.tv_time1_twor);
			tvTwoR1.setText(twoR74);
			TextView tvTwoG1 = (TextView) v7.findViewById(R.id.tv_time1_twog);
			tvTwoG1.setText(twoG74);
			TextView tvTwoB1 = (TextView) v7.findViewById(R.id.tv_time1_twob);
			tvTwoB1.setText(twoB74);
			TextView tvThreeR1 = (TextView) v7.findViewById(R.id.tv_time1_threer);
			tvThreeR1.setText(threeR74);
			TextView tvThreeG1 = (TextView) v7.findViewById(R.id.tv_time1_threeg);
			tvThreeG1.setText(threeG74);
			TextView tvThreeB1 = (TextView) v7.findViewById(R.id.tv_time1_threeb);
			tvThreeB1.setText(threeB74);
			TextView tvFourR1 = (TextView) v7.findViewById(R.id.tv_time1_fourr);
			tvFourR1.setText(fourR74);
			TextView tvFourG1 = (TextView) v7.findViewById(R.id.tv_time1_fourg);
			tvFourG1.setText(fourG74);
			TextView tvFourB1 = (TextView) v7.findViewById(R.id.tv_time1_fourb);
			tvFourB1.setText(fourB74);
			TextView tvFiveR1 = (TextView) v7.findViewById(R.id.tv_time1_fiver);
			tvFiveR1.setText(fiveR74);
			TextView tvFiveG1 = (TextView) v7.findViewById(R.id.tv_time1_fiveg);
			tvFiveG1.setText(fiveG74);
			TextView tvFiveB1 = (TextView) v7.findViewById(R.id.tv_time1_fiveb);
			tvFiveB1.setText(fiveB74);
			TextView tvRotationTime1 = (TextView) v7.findViewById(R.id.tv_time1_rotation);
			tvRotationTime1.setText(rotationTime7);
		}else{
			rl1.setVisibility(View.GONE);
		}
		
		ll.addView(v7);
		
	}

	private void addSaturday(LayoutParams vParam) {
		View v6 = inflater.inflate(R.layout.plan_view, null);
		v6.setLayoutParams(vParam);
		TextView tvWeek = (TextView) v6.findViewById(R.id.tv_week);
		tvWeek.setText(getResources().getString(R.string.saturday));
		TextView tvMode1 = (TextView) v6.findViewById(R.id.tv_time1_mode);
		tvMode1.setText(mode6+1+"");
		RelativeLayout rl1 = (RelativeLayout) v6.findViewById(R.id.rl_monday_time1_mode);
		if (mode6==3) {
			one64 = (one64.length()==0)?"[0]":"["+one64+"]";
			two64=(two64.length()==0)?"[0]":"["+two64+"]";
			three64=(three64.length()==0)?"[0]":"["+three64+"]";
			four64=(four64.length()==0)?"[0]":"["+four64+"]";
			five64=(five64.length()==0)?"[0]":"["+five64+"]";
			six64=(six64.length()==0)?"[0]":"["+six64+"]";
			seven64=(seven64.length()==0)?"[0]":"["+seven64+"]";
			eight64=(eight64.length()==0)?"[0]":"["+eight64+"]";
			oneR64 = (oneR64.length()==0)?"[0]":"["+oneR64+"]";
			oneG64 = (oneG64.length()==0)?"[0]":"["+oneG64+"]";
			oneB64 = (oneB64.length()==0)?"[0]":"["+oneB64+"]";
			twoR64=(twoR64.length()==0)?"[0]":"["+twoR64+"]";
			twoG64=(twoG64.length()==0)?"[0]":"["+twoG64+"]";
			twoB64=(twoB64.length()==0)?"[0]":"["+twoB64+"]";
			threeR64=(threeR64.length()==0)?"[0]":"["+threeR64+"]";
			threeG64=(threeG64.length()==0)?"[0]":"["+threeG64+"]";
			threeB64=(threeB64.length()==0)?"[0]":"["+threeB64+"]";
			fourR64=(fourR64.length()==0)?"[0]":"["+fourR64+"]";
			fourG64=(fourG64.length()==0)?"[0]":"["+fourG64+"]";
			fourB64=(fourB64.length()==0)?"[0]":"["+fourB64+"]";
			fiveR64=(fiveR64.length()==0)?"[0]":"["+fiveR64+"]";
			fiveG64=(fiveG64.length()==0)?"[0]":"["+fiveG64+"]";
			fiveB64=(fiveB64.length()==0)?"[0]":"["+fiveB64+"]";
			rotationTime6 = (rotationTime6.length()==0)?"0":rotationTime6;
			rl1.setVisibility(View.VISIBLE);
			TextView tvOne1 = (TextView) v6.findViewById(R.id.tv_time1_one);
			tvOne1.setText(one64);
			TextView tvTwo1 = (TextView) v6.findViewById(R.id.tv_time1_two);
			tvTwo1.setText(two64);
			TextView tvThree1 = (TextView) v6.findViewById(R.id.tv_time1_three);
			tvThree1.setText(three64);
			TextView tvFour1 = (TextView) v6.findViewById(R.id.tv_time1_four);
			tvFour1.setText(four64);
			TextView tvFive1 = (TextView) v6.findViewById(R.id.tv_time1_five);
			tvFive1.setText(five64);
			TextView tvSix1 = (TextView) v6.findViewById(R.id.tv_time1_six);
			tvSix1.setText(six64);
			TextView tvSeven1 = (TextView) v6.findViewById(R.id.tv_time1_seven);
			tvSeven1.setText(seven64);
			TextView tvEight1 = (TextView) v6.findViewById(R.id.tv_time1_eight);
			tvEight1.setText(eight64);
			TextView tvOneR1 = (TextView) v6.findViewById(R.id.tv_time1_oner);
			tvOneR1.setText(oneR64);
			TextView tvOneG1 = (TextView) v6.findViewById(R.id.tv_time1_oneg);
			tvOneG1.setText(oneG64);
			TextView tvOneB1 = (TextView) v6.findViewById(R.id.tv_time1_oneb);
			tvOneB1.setText(oneB64);
			TextView tvTwoR1 = (TextView) v6.findViewById(R.id.tv_time1_twor);
			tvTwoR1.setText(twoR64);
			TextView tvTwoG1 = (TextView) v6.findViewById(R.id.tv_time1_twog);
			tvTwoG1.setText(twoG64);
			TextView tvTwoB1 = (TextView) v6.findViewById(R.id.tv_time1_twob);
			tvTwoB1.setText(twoB64);
			TextView tvThreeR1 = (TextView) v6.findViewById(R.id.tv_time1_threer);
			tvThreeR1.setText(threeR64);
			TextView tvThreeG1 = (TextView) v6.findViewById(R.id.tv_time1_threeg);
			tvThreeG1.setText(threeG64);
			TextView tvThreeB1 = (TextView) v6.findViewById(R.id.tv_time1_threeb);
			tvThreeB1.setText(threeB64);
			TextView tvFourR1 = (TextView) v6.findViewById(R.id.tv_time1_fourr);
			tvFourR1.setText(fourR64);
			TextView tvFourG1 = (TextView) v6.findViewById(R.id.tv_time1_fourg);
			tvFourG1.setText(fourG64);
			TextView tvFourB1 = (TextView) v6.findViewById(R.id.tv_time1_fourb);
			tvFourB1.setText(fourB64);
			TextView tvFiveR1 = (TextView) v6.findViewById(R.id.tv_time1_fiver);
			tvFiveR1.setText(fiveR64);
			TextView tvFiveG1 = (TextView) v6.findViewById(R.id.tv_time1_fiveg);
			tvFiveG1.setText(fiveG64);
			TextView tvFiveB1 = (TextView) v6.findViewById(R.id.tv_time1_fiveb);
			tvFiveB1.setText(fiveB64);
			TextView tvRotationTime1 = (TextView) v6.findViewById(R.id.tv_time1_rotation);
			tvRotationTime1.setText(rotationTime6);
		}else{
			rl1.setVisibility(View.GONE);
		}
		
		ll.addView(v6);
		
	}

	private void addFriday(LayoutParams vParam) {
		View v5 = inflater.inflate(R.layout.plan_view, null);
		v5.setLayoutParams(vParam);
		TextView tvWeek = (TextView) v5.findViewById(R.id.tv_week);
		tvWeek.setText(getResources().getString(R.string.friday));
		TextView tvMode1 = (TextView) v5.findViewById(R.id.tv_time1_mode);
		tvMode1.setText(mode5+1+"");
		RelativeLayout rl1 = (RelativeLayout) v5.findViewById(R.id.rl_monday_time1_mode);
		if (mode5==3) {
			one54 = (one54.length()==0)?"[0]":"["+one54+"]";
			two54=(two54.length()==0)?"[0]":"["+two54+"]";
			three54=(three54.length()==0)?"[0]":"["+three54+"]";
			four54=(four54.length()==0)?"[0]":"["+four54+"]";
			five54=(five54.length()==0)?"[0]":"["+five54+"]";
			six54=(six54.length()==0)?"[0]":"["+six54+"]";
			seven54=(seven54.length()==0)?"[0]":"["+seven54+"]";
			eight54=(eight54.length()==0)?"[0]":"["+eight54+"]";
			oneR54 = (oneR54.length()==0)?"[0]":"["+oneR54+"]";
			oneG54 = (oneG54.length()==0)?"[0]":"["+oneG54+"]";
			oneB54 = (oneB54.length()==0)?"[0]":"["+oneB54+"]";
			twoR54=(twoR54.length()==0)?"[0]":"["+twoR54+"]";
			twoG54=(twoG54.length()==0)?"[0]":"["+twoG54+"]";
			twoB54=(twoB54.length()==0)?"[0]":"["+twoB54+"]";
			threeR54=(threeR54.length()==0)?"[0]":"["+threeR54+"]";
			threeG54=(threeG54.length()==0)?"[0]":"["+threeG54+"]";
			threeB54=(threeB54.length()==0)?"[0]":"["+threeB54+"]";
			fourR54=(fourR54.length()==0)?"[0]":"["+fourR54+"]";
			fourG54=(fourG54.length()==0)?"[0]":"["+fourG54+"]";
			fourB54=(fourB54.length()==0)?"[0]":"["+fourB54+"]";
			fiveR54=(fiveR54.length()==0)?"[0]":"["+fiveR54+"]";
			fiveG54=(fiveG54.length()==0)?"[0]":"["+fiveG54+"]";
			fiveB54=(fiveB54.length()==0)?"[0]":"["+fiveB54+"]";
			rotationTime5 = (rotationTime5.length()==0)?"0":rotationTime5;
			rl1.setVisibility(View.VISIBLE);
			TextView tvOne1 = (TextView) v5.findViewById(R.id.tv_time1_one);
			tvOne1.setText(one54);
			TextView tvTwo1 = (TextView) v5.findViewById(R.id.tv_time1_two);
			tvTwo1.setText(two54);
			TextView tvThree1 = (TextView) v5.findViewById(R.id.tv_time1_three);
			tvThree1.setText(three54);
			TextView tvFour1 = (TextView) v5.findViewById(R.id.tv_time1_four);
			tvFour1.setText(four54);
			TextView tvFive1 = (TextView) v5.findViewById(R.id.tv_time1_five);
			tvFive1.setText(five54);
			TextView tvSix1 = (TextView) v5.findViewById(R.id.tv_time1_six);
			tvSix1.setText(six54);
			TextView tvSeven1 = (TextView) v5.findViewById(R.id.tv_time1_seven);
			tvSeven1.setText(seven54);
			TextView tvEight1 = (TextView) v5.findViewById(R.id.tv_time1_eight);
			tvEight1.setText(eight54);
			TextView tvOneR1 = (TextView) v5.findViewById(R.id.tv_time1_oner);
			tvOneR1.setText(oneR54);
			TextView tvOneG1 = (TextView) v5.findViewById(R.id.tv_time1_oneg);
			tvOneG1.setText(oneG54);
			TextView tvOneB1 = (TextView) v5.findViewById(R.id.tv_time1_oneb);
			tvOneB1.setText(oneB54);
			TextView tvTwoR1 = (TextView) v5.findViewById(R.id.tv_time1_twor);
			tvTwoR1.setText(twoR54);
			TextView tvTwoG1 = (TextView) v5.findViewById(R.id.tv_time1_twog);
			tvTwoG1.setText(twoG54);
			TextView tvTwoB1 = (TextView) v5.findViewById(R.id.tv_time1_twob);
			tvTwoB1.setText(twoB54);
			TextView tvThreeR1 = (TextView) v5.findViewById(R.id.tv_time1_threer);
			tvThreeR1.setText(threeR54);
			TextView tvThreeG1 = (TextView) v5.findViewById(R.id.tv_time1_threeg);
			tvThreeG1.setText(threeG54);
			TextView tvThreeB1 = (TextView) v5.findViewById(R.id.tv_time1_threeb);
			tvThreeB1.setText(threeB54);
			TextView tvFourR1 = (TextView) v5.findViewById(R.id.tv_time1_fourr);
			tvFourR1.setText(fourR54);
			TextView tvFourG1 = (TextView) v5.findViewById(R.id.tv_time1_fourg);
			tvFourG1.setText(fourG54);
			TextView tvFourB1 = (TextView) v5.findViewById(R.id.tv_time1_fourb);
			tvFourB1.setText(fourB54);
			TextView tvFiveR1 = (TextView) v5.findViewById(R.id.tv_time1_fiver);
			tvFiveR1.setText(fiveR54);
			TextView tvFiveG1 = (TextView) v5.findViewById(R.id.tv_time1_fiveg);
			tvFiveG1.setText(fiveG54);
			TextView tvFiveB1 = (TextView) v5.findViewById(R.id.tv_time1_fiveb);
			tvFiveB1.setText(fiveB54);
			TextView tvRotationTime1 = (TextView) v5.findViewById(R.id.tv_time1_rotation);
			tvRotationTime1.setText(rotationTime5);
		}else{
			rl1.setVisibility(View.GONE);
		}
		ll.addView(v5);
		
	}

	private void addThursday(LayoutParams vParam) {
		View v4 = inflater.inflate(R.layout.plan_view, null);
		v4.setLayoutParams(vParam);
		TextView tvWeek = (TextView) v4.findViewById(R.id.tv_week);
		tvWeek.setText(getResources().getString(R.string.thursday));
		TextView tvMode1 = (TextView) v4.findViewById(R.id.tv_time1_mode);
		tvMode1.setText(mode4+1+"");
		RelativeLayout rl1 = (RelativeLayout) v4.findViewById(R.id.rl_monday_time1_mode);
		if (mode4==3) {
			one44 = (one44.length()==0)?"[0]":"["+one44+"]";
			two44=(two44.length()==0)?"[0]":"["+two44+"]";
			three44=(three44.length()==0)?"[0]":"["+three44+"]";
			four44=(four44.length()==0)?"[0]":"["+four44+"]";
			five44=(five44.length()==0)?"[0]":"["+five44+"]";
			six44=(six44.length()==0)?"[0]":"["+six44+"]";
			seven44=(seven44.length()==0)?"[0]":"["+seven44+"]";
			eight44=(eight44.length()==0)?"[0]":"["+eight44+"]";
			oneR44 = (oneR44.length()==0)?"[0]":"["+oneR44+"]";
			oneG44 = (oneG44.length()==0)?"[0]":"["+oneG44+"]";
			oneB44 = (oneB44.length()==0)?"[0]":"["+oneB44+"]";
			twoR44=(twoR44.length()==0)?"[0]":"["+twoR44+"]";
			twoG44=(twoG44.length()==0)?"[0]":"["+twoG44+"]";
			twoB44=(twoB44.length()==0)?"[0]":"["+twoB44+"]";
			threeR44=(threeR44.length()==0)?"[0]":"["+threeR44+"]";
			threeG44=(threeG44.length()==0)?"[0]":"["+threeG44+"]";
			threeB44=(threeB44.length()==0)?"[0]":"["+threeB44+"]";
			fourR44=(fourR44.length()==0)?"[0]":"["+fourR44+"]";
			fourG44=(fourG44.length()==0)?"[0]":"["+fourG44+"]";
			fourB44=(fourB44.length()==0)?"[0]":"["+fourB44+"]";
			fiveR44=(fiveR44.length()==0)?"[0]":"["+fiveR44+"]";
			fiveG44=(fiveG44.length()==0)?"[0]":"["+fiveG44+"]";
			fiveB44=(fiveB44.length()==0)?"[0]":"["+fiveB44+"]";
			rotationTime4 = (rotationTime4.length()==0)?"0":rotationTime4;
			rl1.setVisibility(View.VISIBLE);
			TextView tvOne1 = (TextView) v4.findViewById(R.id.tv_time1_one);
			tvOne1.setText(one44);
			TextView tvTwo1 = (TextView) v4.findViewById(R.id.tv_time1_two);
			tvTwo1.setText(two44);
			TextView tvThree1 = (TextView) v4.findViewById(R.id.tv_time1_three);
			tvThree1.setText(three44);
			TextView tvFour1 = (TextView) v4.findViewById(R.id.tv_time1_four);
			tvFour1.setText(four44);
			TextView tvFive1 = (TextView) v4.findViewById(R.id.tv_time1_five);
			tvFive1.setText(five44);
			TextView tvSix1 = (TextView) v4.findViewById(R.id.tv_time1_six);
			tvSix1.setText(six44);
			TextView tvSeven1 = (TextView) v4.findViewById(R.id.tv_time1_seven);
			tvSeven1.setText(seven44);
			TextView tvEight1 = (TextView) v4.findViewById(R.id.tv_time1_eight);
			tvEight1.setText(eight44);
			TextView tvOneR1 = (TextView) v4.findViewById(R.id.tv_time1_oner);
			tvOneR1.setText(oneR44);
			TextView tvOneG1 = (TextView) v4.findViewById(R.id.tv_time1_oneg);
			tvOneG1.setText(oneG44);
			TextView tvOneB1 = (TextView) v4.findViewById(R.id.tv_time1_oneb);
			tvOneB1.setText(oneB44);
			TextView tvTwoR1 = (TextView) v4.findViewById(R.id.tv_time1_twor);
			tvTwoR1.setText(twoR44);
			TextView tvTwoG1 = (TextView) v4.findViewById(R.id.tv_time1_twog);
			tvTwoG1.setText(twoG44);
			TextView tvTwoB1 = (TextView) v4.findViewById(R.id.tv_time1_twob);
			tvTwoB1.setText(twoB44);
			TextView tvThreeR1 = (TextView) v4.findViewById(R.id.tv_time1_threer);
			tvThreeR1.setText(threeR44);
			TextView tvThreeG1 = (TextView) v4.findViewById(R.id.tv_time1_threeg);
			tvThreeG1.setText(threeG44);
			TextView tvThreeB1 = (TextView) v4.findViewById(R.id.tv_time1_threeb);
			tvThreeB1.setText(threeB44);
			TextView tvFourR1 = (TextView) v4.findViewById(R.id.tv_time1_fourr);
			tvFourR1.setText(fourR44);
			TextView tvFourG1 = (TextView) v4.findViewById(R.id.tv_time1_fourg);
			tvFourG1.setText(fourG44);
			TextView tvFourB1 = (TextView) v4.findViewById(R.id.tv_time1_fourb);
			tvFourB1.setText(fourB44);
			TextView tvFiveR1 = (TextView) v4.findViewById(R.id.tv_time1_fiver);
			tvFiveR1.setText(fiveR44);
			TextView tvFiveG1 = (TextView) v4.findViewById(R.id.tv_time1_fiveg);
			tvFiveG1.setText(fiveG44);
			TextView tvFiveB1 = (TextView) v4.findViewById(R.id.tv_time1_fiveb);
			tvFiveB1.setText(fiveB44);
			TextView tvRotationTime1 = (TextView) v4.findViewById(R.id.tv_time1_rotation);
			tvRotationTime1.setText(rotationTime4);
		}else{
			rl1.setVisibility(View.GONE);
		}
		ll.addView(v4);
		
	}

	private void addWednesday(LayoutParams vParam) {
		View v3 = inflater.inflate(R.layout.plan_view, null);
		v3.setLayoutParams(vParam);
		TextView tvWeek = (TextView) v3.findViewById(R.id.tv_week);
		tvWeek.setText(getResources().getString(R.string.wednesday));
		TextView tvMode1 = (TextView) v3.findViewById(R.id.tv_time1_mode);
		tvMode1.setText(mode3+1+"");
		RelativeLayout rl1 = (RelativeLayout) v3.findViewById(R.id.rl_monday_time1_mode);
		if (mode3==3) {
			one34 = (one34.length()==0)?"[0]":"["+one34+"]";
			two34=(two34.length()==0)?"[0]":"["+two34+"]";
			three34=(three34.length()==0)?"[0]":"["+three34+"]";
			four34=(four34.length()==0)?"[0]":"["+four34+"]";
			five34=(five34.length()==0)?"[0]":"["+five34+"]";
			six34=(six34.length()==0)?"[0]":"["+six34+"]";
			seven34=(seven34.length()==0)?"[0]":"["+seven34+"]";
			eight34=(eight34.length()==0)?"[0]":"["+eight34+"]";
			oneR34 = (oneR34.length()==0)?"[0]":"["+oneR34+"]";
			oneG34 = (oneG34.length()==0)?"[0]":"["+oneG34+"]";
			oneB34 = (oneB34.length()==0)?"[0]":"["+oneB34+"]";
			twoR34=(twoR34.length()==0)?"[0]":"["+twoR34+"]";
			twoG34=(twoG34.length()==0)?"[0]":"["+twoG34+"]";
			twoB34=(twoB34.length()==0)?"[0]":"["+twoB34+"]";
			threeR34=(threeR34.length()==0)?"[0]":"["+threeR34+"]";
			threeG34=(threeG34.length()==0)?"[0]":"["+threeG34+"]";
			threeB34=(threeB34.length()==0)?"[0]":"["+threeB34+"]";
			fourR34=(fourR34.length()==0)?"[0]":"["+fourR34+"]";
			fourG34=(fourG34.length()==0)?"[0]":"["+fourG34+"]";
			fourB34=(fourB34.length()==0)?"[0]":"["+fourB34+"]";
			fiveR34=(fiveR34.length()==0)?"[0]":"["+fiveR34+"]";
			fiveG34=(fiveG34.length()==0)?"[0]":"["+fiveG34+"]";
			fiveB34=(fiveB34.length()==0)?"[0]":"["+fiveB34+"]";
			rotationTime3 = (rotationTime3.length()==0)?"0":rotationTime3;
			rl1.setVisibility(View.VISIBLE);
			TextView tvOne1 = (TextView) v3.findViewById(R.id.tv_time1_one);
			tvOne1.setText(one34);
			TextView tvTwo1 = (TextView) v3.findViewById(R.id.tv_time1_two);
			tvTwo1.setText(two34);
			TextView tvThree1 = (TextView) v3.findViewById(R.id.tv_time1_three);
			tvThree1.setText(three34);
			TextView tvFour1 = (TextView) v3.findViewById(R.id.tv_time1_four);
			tvFour1.setText(four34);
			TextView tvFive1 = (TextView) v3.findViewById(R.id.tv_time1_five);
			tvFive1.setText(five34);
			TextView tvSix1 = (TextView) v3.findViewById(R.id.tv_time1_six);
			tvSix1.setText(six34);
			TextView tvSeven1 = (TextView) v3.findViewById(R.id.tv_time1_seven);
			tvSeven1.setText(seven34);
			TextView tvEight1 = (TextView) v3.findViewById(R.id.tv_time1_eight);
			tvEight1.setText(eight34);
			TextView tvOneR1 = (TextView) v3.findViewById(R.id.tv_time1_oner);
			tvOneR1.setText(oneR34);
			TextView tvOneG1 = (TextView) v3.findViewById(R.id.tv_time1_oneg);
			tvOneG1.setText(oneG34);
			TextView tvOneB1 = (TextView) v3.findViewById(R.id.tv_time1_oneb);
			tvOneB1.setText(oneB34);
			TextView tvTwoR1 = (TextView) v3.findViewById(R.id.tv_time1_twor);
			tvTwoR1.setText(twoR34);
			TextView tvTwoG1 = (TextView) v3.findViewById(R.id.tv_time1_twog);
			tvTwoG1.setText(twoG34);
			TextView tvTwoB1 = (TextView) v3.findViewById(R.id.tv_time1_twob);
			tvTwoB1.setText(twoB34);
			TextView tvThreeR1 = (TextView) v3.findViewById(R.id.tv_time1_threer);
			tvThreeR1.setText(threeR34);
			TextView tvThreeG1 = (TextView) v3.findViewById(R.id.tv_time1_threeg);
			tvThreeG1.setText(threeG34);
			TextView tvThreeB1 = (TextView) v3.findViewById(R.id.tv_time1_threeb);
			tvThreeB1.setText(threeB34);
			TextView tvFourR1 = (TextView) v3.findViewById(R.id.tv_time1_fourr);
			tvFourR1.setText(fourR34);
			TextView tvFourG1 = (TextView) v3.findViewById(R.id.tv_time1_fourg);
			tvFourG1.setText(fourG34);
			TextView tvFourB1 = (TextView) v3.findViewById(R.id.tv_time1_fourb);
			tvFourB1.setText(fourB34);
			TextView tvFiveR1 = (TextView) v3.findViewById(R.id.tv_time1_fiver);
			tvFiveR1.setText(fiveR34);
			TextView tvFiveG1 = (TextView) v3.findViewById(R.id.tv_time1_fiveg);
			tvFiveG1.setText(fiveG34);
			TextView tvFiveB1 = (TextView) v3.findViewById(R.id.tv_time1_fiveb);
			tvFiveB1.setText(fiveB34);
			TextView tvRotationTime1 = (TextView) v3.findViewById(R.id.tv_time1_rotation);
			tvRotationTime1.setText(rotationTime3);
		}else{
			rl1.setVisibility(View.GONE);
		}
		
		ll.addView(v3);
		
	}

	private void addTuesday(LayoutParams vParam) {
		View v2 = inflater.inflate(R.layout.plan_view, null);
		v2.setLayoutParams(vParam);
		TextView tvWeek = (TextView) v2.findViewById(R.id.tv_week);
		tvWeek.setText(getResources().getString(R.string.tuesday));
		TextView tvMode1 = (TextView) v2.findViewById(R.id.tv_time1_mode);
		tvMode1.setText(mode2+1+"");
		RelativeLayout rl1 = (RelativeLayout) v2.findViewById(R.id.rl_monday_time1_mode);
		if (mode2==3) {
			one24 = (one24.length()==0)?"[0]":"["+one24+"]";
			two24=(two24.length()==0)?"[0]":"["+two24+"]";
			three24=(three24.length()==0)?"[0]":"["+three24+"]";
			four24=(four24.length()==0)?"[0]":"["+four24+"]";
			five24=(five24.length()==0)?"[0]":"["+five24+"]";
			six24=(six24.length()==0)?"[0]":"["+six24+"]";
			seven24=(seven24.length()==0)?"[0]":"["+seven24+"]";
			eight24=(eight24.length()==0)?"[0]":"["+eight24+"]";
			oneR24 = (oneR24.length()==0)?"[0]":"["+oneR24+"]";
			oneG24 = (oneG24.length()==0)?"[0]":"["+oneG24+"]";
			oneB24 = (oneB24.length()==0)?"[0]":"["+oneB24+"]";
			twoR24=(twoR24.length()==0)?"[0]":"["+twoR24+"]";
			twoG24=(twoG24.length()==0)?"[0]":"["+twoG24+"]";
			twoB24=(twoB24.length()==0)?"[0]":"["+twoB24+"]";
			threeR24=(threeR24.length()==0)?"[0]":"["+threeR24+"]";
			threeG24=(threeG24.length()==0)?"[0]":"["+threeG24+"]";
			threeB24=(threeB24.length()==0)?"[0]":"["+threeB24+"]";
			fourR24=(fourR24.length()==0)?"[0]":"["+fourR24+"]";
			fourG24=(fourG24.length()==0)?"[0]":"["+fourG24+"]";
			fourB24=(fourB24.length()==0)?"[0]":"["+fourB24+"]";
			fiveR24=(fiveR24.length()==0)?"[0]":"["+fiveR24+"]";
			fiveG24=(fiveG24.length()==0)?"[0]":"["+fiveG24+"]";
			fiveB24=(fiveB24.length()==0)?"[0]":"["+fiveB24+"]";
			rotationTime2 = (rotationTime2.length()==0)?"0":rotationTime2;
			rl1.setVisibility(View.VISIBLE);
			TextView tvOne1 = (TextView) v2.findViewById(R.id.tv_time1_one);
			tvOne1.setText(one24);
			TextView tvTwo1 = (TextView) v2.findViewById(R.id.tv_time1_two);
			tvTwo1.setText(two24);
			TextView tvThree1 = (TextView) v2.findViewById(R.id.tv_time1_three);
			tvThree1.setText(three24);
			TextView tvFour1 = (TextView) v2.findViewById(R.id.tv_time1_four);
			tvFour1.setText(four24);
			TextView tvFive1 = (TextView) v2.findViewById(R.id.tv_time1_five);
			tvFive1.setText(five24);
			TextView tvSix1 = (TextView) v2.findViewById(R.id.tv_time1_six);
			tvSix1.setText(six24);
			TextView tvSeven1 = (TextView) v2.findViewById(R.id.tv_time1_seven);
			tvSeven1.setText(seven24);
			TextView tvEight1 = (TextView) v2.findViewById(R.id.tv_time1_eight);
			tvEight1.setText(eight24);
			TextView tvOneR1 = (TextView) v2.findViewById(R.id.tv_time1_oner);
			tvOneR1.setText(oneR24);
			TextView tvOneG1 = (TextView) v2.findViewById(R.id.tv_time1_oneg);
			tvOneG1.setText(oneG24);
			TextView tvOneB1 = (TextView) v2.findViewById(R.id.tv_time1_oneb);
			tvOneB1.setText(oneB24);
			TextView tvTwoR1 = (TextView) v2.findViewById(R.id.tv_time1_twor);
			tvTwoR1.setText(twoR24);
			TextView tvTwoG1 = (TextView) v2.findViewById(R.id.tv_time1_twog);
			tvTwoG1.setText(twoG24);
			TextView tvTwoB1 = (TextView) v2.findViewById(R.id.tv_time1_twob);
			tvTwoB1.setText(twoB24);
			TextView tvThreeR1 = (TextView) v2.findViewById(R.id.tv_time1_threer);
			tvThreeR1.setText(threeR24);
			TextView tvThreeG1 = (TextView) v2.findViewById(R.id.tv_time1_threeg);
			tvThreeG1.setText(threeG24);
			TextView tvThreeB1 = (TextView) v2.findViewById(R.id.tv_time1_threeb);
			tvThreeB1.setText(threeB24);
			TextView tvFourR1 = (TextView) v2.findViewById(R.id.tv_time1_fourr);
			tvFourR1.setText(fourR24);
			TextView tvFourG1 = (TextView) v2.findViewById(R.id.tv_time1_fourg);
			tvFourG1.setText(fourG24);
			TextView tvFourB1 = (TextView) v2.findViewById(R.id.tv_time1_fourb);
			tvFourB1.setText(fourB24);
			TextView tvFiveR1 = (TextView) v2.findViewById(R.id.tv_time1_fiver);
			tvFiveR1.setText(fiveR24);
			TextView tvFiveG1 = (TextView) v2.findViewById(R.id.tv_time1_fiveg);
			tvFiveG1.setText(fiveG24);
			TextView tvFiveB1 = (TextView) v2.findViewById(R.id.tv_time1_fiveb);
			tvFiveB1.setText(fiveB24);
			TextView tvRotationTime1 = (TextView) v2.findViewById(R.id.tv_time1_rotation);
			tvRotationTime1.setText(rotationTime2);
		}else{
			rl1.setVisibility(View.GONE);
		}
		
		ll.addView(v2);
		
	}

	private void addMonday(LayoutParams vParam) {
		View v1 = inflater.inflate(R.layout.plan_view, null);
		v1.setLayoutParams(vParam);
		TextView tvWeek = (TextView) v1.findViewById(R.id.tv_week);
		tvWeek.setText(getResources().getString(R.string.monday));
		TextView tvMode1 = (TextView) v1.findViewById(R.id.tv_time1_mode);
		tvMode1.setText(mode1+1+"");
		RelativeLayout rl1 = (RelativeLayout) v1.findViewById(R.id.rl_monday_time1_mode);
		if (mode1==3) {
			one14 = (one14.length()==0)?"[0]":"["+one14+"]";
			two14=(two14.length()==0)?"[0]":"["+two14+"]";
			three14=(three14.length()==0)?"[0]":"["+three14+"]";
			four14=(four14.length()==0)?"[0]":"["+four14+"]";
			five14=(five14.length()==0)?"[0]":"["+five14+"]";
			six14=(six14.length()==0)?"[0]":"["+six14+"]";
			seven14=(seven14.length()==0)?"[0]":"["+seven14+"]";
			eight14=(eight14.length()==0)?"[0]":"["+eight14+"]";
			oneR14 = (oneR14.length()==0)?"[0]":"["+oneR14+"]";
			oneG14 = (oneG14.length()==0)?"[0]":"["+oneG14+"]";
			oneB14 = (oneB14.length()==0)?"[0]":"["+oneB14+"]";
			twoR14=(twoR14.length()==0)?"[0]":"["+twoR14+"]";
			twoG14=(twoG14.length()==0)?"[0]":"["+twoG14+"]";
			twoB14=(twoB14.length()==0)?"[0]":"["+twoB14+"]";
			threeR14=(threeR14.length()==0)?"[0]":"["+threeR14+"]";
			threeG14=(threeG14.length()==0)?"[0]":"["+threeG14+"]";
			threeB14=(threeB14.length()==0)?"[0]":"["+threeB14+"]";
			fourR14=(fourR14.length()==0)?"[0]":"["+fourR14+"]";
			fourG14=(fourG14.length()==0)?"[0]":"["+fourG14+"]";
			fourB14=(fourB14.length()==0)?"[0]":"["+fourB14+"]";
			fiveR14=(fiveR14.length()==0)?"[0]":"["+fiveR14+"]";
			fiveG14=(fiveG14.length()==0)?"[0]":"["+fiveG14+"]";
			fiveB14=(fiveB14.length()==0)?"[0]":"["+fiveB14+"]";
			rotationTime1 = (rotationTime1.length()==0)?"0":rotationTime1;
			rl1.setVisibility(View.VISIBLE);
			TextView tvOne1 = (TextView) v1.findViewById(R.id.tv_time1_one);
			tvOne1.setText(one14);
			TextView tvTwo1 = (TextView) v1.findViewById(R.id.tv_time1_two);
			tvTwo1.setText(two14);
			TextView tvThree1 = (TextView) v1.findViewById(R.id.tv_time1_three);
			tvThree1.setText(three14);
			TextView tvFour1 = (TextView) v1.findViewById(R.id.tv_time1_four);
			tvFour1.setText(four14);
			TextView tvFive1 = (TextView) v1.findViewById(R.id.tv_time1_five);
			tvFive1.setText(five14);
			TextView tvSix1 = (TextView) v1.findViewById(R.id.tv_time1_six);
			tvSix1.setText(six14);
			TextView tvSeven1 = (TextView) v1.findViewById(R.id.tv_time1_seven);
			tvSeven1.setText(seven14);
			TextView tvEight1 = (TextView) v1.findViewById(R.id.tv_time1_eight);
			tvEight1.setText(eight14);
			TextView tvOneR1 = (TextView) v1.findViewById(R.id.tv_time1_oner);
			tvOneR1.setText(oneR14);
			TextView tvOneG1 = (TextView) v1.findViewById(R.id.tv_time1_oneg);
			tvOneG1.setText(oneG14);
			TextView tvOneB1 = (TextView) v1.findViewById(R.id.tv_time1_oneb);
			tvOneB1.setText(oneB14);
			TextView tvTwoR1 = (TextView) v1.findViewById(R.id.tv_time1_twor);
			tvTwoR1.setText(twoR14);
			TextView tvTwoG1 = (TextView) v1.findViewById(R.id.tv_time1_twog);
			tvTwoG1.setText(twoG14);
			TextView tvTwoB1 = (TextView) v1.findViewById(R.id.tv_time1_twob);
			tvTwoB1.setText(twoB14);
			TextView tvThreeR1 = (TextView) v1.findViewById(R.id.tv_time1_threer);
			tvThreeR1.setText(threeR14);
			TextView tvThreeG1 = (TextView) v1.findViewById(R.id.tv_time1_threeg);
			tvThreeG1.setText(threeG14);
			TextView tvThreeB1 = (TextView) v1.findViewById(R.id.tv_time1_threeb);
			tvThreeB1.setText(threeB14);
			TextView tvFourR1 = (TextView) v1.findViewById(R.id.tv_time1_fourr);
			tvFourR1.setText(fourR14);
			TextView tvFourG1 = (TextView) v1.findViewById(R.id.tv_time1_fourg);
			tvFourG1.setText(fourG14);
			TextView tvFourB1 = (TextView) v1.findViewById(R.id.tv_time1_fourb);
			tvFourB1.setText(fourB14);
			TextView tvFiveR1 = (TextView) v1.findViewById(R.id.tv_time1_fiver);
			tvFiveR1.setText(fiveR14);
			TextView tvFiveG1 = (TextView) v1.findViewById(R.id.tv_time1_fiveg);
			tvFiveG1.setText(fiveG14);
			TextView tvFiveB1 = (TextView) v1.findViewById(R.id.tv_time1_fiveb);
			tvFiveB1.setText(fiveB14);
			TextView tvRotationTime1 = (TextView) v1.findViewById(R.id.tv_time1_rotation);
			tvRotationTime1.setText(rotationTime1);
		}else{
			rl1.setVisibility(View.GONE);
		}
		
		ll.addView(v1);
		
	}

	/**
	 * 获取SQLite数据库数据
	 */
	private void initDate() {
		List<ProjectRGB> prorgbs = dao.query(null, null);
		Log.e(TAG, "prorgbs的长度：" + prorgbs.size());
		if (prorgbs.size() > 0) {
			for (ProjectRGB prorg : prorgbs) {
				if (prorg.getName().equals(projectName) 
						&& prorg.getPhysical().equals(physical)
						&& prorg.getGroupName().equals(groupName)) {
					projectrbgs.add(prorg);
				}
			}

			for (int i = 0; i < projectrbgs.size(); i++) {
				mode1 = projectrbgs.get(6).getMode();
				one14 = projectrbgs.get(6).getEtOne();
				two14 = projectrbgs.get(6).getEtTwo();
				three14 = projectrbgs.get(6).getEtThree();
				four14 = projectrbgs.get(6).getEtFour();
				five14 = projectrbgs.get(6).getEtFive();
				six14 = projectrbgs.get(6).getEtSix();
				seven14 = projectrbgs.get(6).getEtSeven();
				eight14 = projectrbgs.get(6).getEtEight();
				oneR14 = projectrbgs.get(6).getEtOneR();
				oneG14 = projectrbgs.get(6).getEtOneG();
				oneB14 = projectrbgs.get(6).getEtOneB();
				twoR14 = projectrbgs.get(6).getEtTwoR();
				twoG14 = projectrbgs.get(6).getEtTwoG();
				twoB14 = projectrbgs.get(6).getEtTwoB();
				threeR14 = projectrbgs.get(6).getEtThreeR();
				threeG14 = projectrbgs.get(6).getEtThreeG();
				threeB14 = projectrbgs.get(6).getEtThreeB();
				fourR14 = projectrbgs.get(6).getEtFourR();
				fourG14 = projectrbgs.get(6).getEtFourG();
				fourB14 = projectrbgs.get(6).getEtFourB();
				fiveR14 = projectrbgs.get(6).getEtFiveR();
				fiveG14 = projectrbgs.get(6).getEtFiveG();
				fiveB14 = projectrbgs.get(6).getEtFiveB();
				rotationTime1 = projectrbgs.get(6).getEtRotationTime();


				mode2 = projectrbgs.get(5).getMode();
				one24 = projectrbgs.get(5).getEtOne();
				two24 = projectrbgs.get(5).getEtTwo();
				three24 = projectrbgs.get(5).getEtThree();
				four24 = projectrbgs.get(5).getEtFour();
				five24 = projectrbgs.get(5).getEtFive();
				six24 = projectrbgs.get(5).getEtSix();
				seven24 = projectrbgs.get(5).getEtSeven();
				eight24 = projectrbgs.get(5).getEtEight();
				oneR24 = projectrbgs.get(5).getEtOneR();
				oneG24 = projectrbgs.get(5).getEtOneG();
				oneB24 = projectrbgs.get(5).getEtOneB();
				twoR24 = projectrbgs.get(5).getEtTwoR();
				twoG24 = projectrbgs.get(5).getEtTwoG();
				twoB24 = projectrbgs.get(5).getEtTwoB();
				threeR24 = projectrbgs.get(5).getEtThreeR();
				threeG24 = projectrbgs.get(5).getEtThreeG();
				threeB24 = projectrbgs.get(5).getEtThreeB();
				fourR24 = projectrbgs.get(5).getEtFourR();
				fourG24 = projectrbgs.get(5).getEtFourG();
				fourB24 = projectrbgs.get(5).getEtFourB();
				fiveR24 = projectrbgs.get(5).getEtFiveR();
				fiveG24 = projectrbgs.get(5).getEtFiveG();
				fiveB24 = projectrbgs.get(5).getEtFiveB();
				rotationTime2 = projectrbgs.get(5).getEtRotationTime();

				mode3 = projectrbgs.get(4).getMode();
				one34 = projectrbgs.get(4).getEtOne();
				two34 = projectrbgs.get(4).getEtTwo();
				three34 = projectrbgs.get(4).getEtThree();
				four34 = projectrbgs.get(4).getEtFour();
				five34 = projectrbgs.get(4).getEtFive();
				six34 = projectrbgs.get(4).getEtSix();
				seven34 = projectrbgs.get(4).getEtSeven();
				eight34 = projectrbgs.get(4).getEtEight();
				oneR34 = projectrbgs.get(4).getEtOneR();
				oneG34 = projectrbgs.get(4).getEtOneG();
				oneB34 = projectrbgs.get(4).getEtOneB();
				twoR34 = projectrbgs.get(4).getEtTwoR();
				twoG34 = projectrbgs.get(4).getEtTwoG();
				twoB34 = projectrbgs.get(4).getEtTwoB();
				threeR34 = projectrbgs.get(4).getEtThreeR();
				threeG34 = projectrbgs.get(4).getEtThreeG();
				threeB34 = projectrbgs.get(4).getEtThreeB();
				fourR34 = projectrbgs.get(4).getEtFourR();
				fourG34 = projectrbgs.get(4).getEtFourG();
				fourB34 = projectrbgs.get(4).getEtFourB();
				fiveR34 = projectrbgs.get(4).getEtFiveR();
				fiveG34 = projectrbgs.get(4).getEtFiveG();
				fiveB34 = projectrbgs.get(4).getEtFiveB();
				rotationTime3 = projectrbgs.get(4).getEtRotationTime();

				mode4 = projectrbgs.get(3).getMode();
				one44 = projectrbgs.get(3).getEtOne();
				two44 = projectrbgs.get(3).getEtTwo();
				three44 = projectrbgs.get(3).getEtThree();
				four44 = projectrbgs.get(3).getEtFour();
				five44 = projectrbgs.get(3).getEtFive();
				six44 = projectrbgs.get(3).getEtSix();
				seven44 = projectrbgs.get(3).getEtSeven();
				eight44 = projectrbgs.get(3).getEtEight();
				oneR44 = projectrbgs.get(3).getEtOneR();
				oneG44 = projectrbgs.get(3).getEtOneG();
				oneB44 = projectrbgs.get(3).getEtOneB();
				twoR44 = projectrbgs.get(3).getEtTwoR();
				twoG44 = projectrbgs.get(3).getEtTwoG();
				twoB44 = projectrbgs.get(3).getEtTwoB();
				threeR44 = projectrbgs.get(3).getEtThreeR();
				threeG44 = projectrbgs.get(3).getEtThreeG();
				threeB44 = projectrbgs.get(3).getEtThreeB();
				fourR44 = projectrbgs.get(3).getEtFourR();
				fourG44 = projectrbgs.get(3).getEtFourG();
				fourB44 = projectrbgs.get(3).getEtFourB();
				fiveR44 = projectrbgs.get(3).getEtFiveR();
				fiveG44 = projectrbgs.get(3).getEtFiveG();
				fiveB44 = projectrbgs.get(3).getEtFiveB();
				rotationTime4 = projectrbgs.get(3).getEtRotationTime();

				mode5 = projectrbgs.get(2).getMode();
				one54 = projectrbgs.get(2).getEtOne();
				two54 = projectrbgs.get(2).getEtTwo();
				three54 = projectrbgs.get(2).getEtThree();
				four54 = projectrbgs.get(2).getEtFour();
				five54 = projectrbgs.get(2).getEtFive();
				six54 = projectrbgs.get(2).getEtSix();
				seven54 = projectrbgs.get(2).getEtSeven();
				eight54 = projectrbgs.get(2).getEtEight();
				oneR54 = projectrbgs.get(2).getEtOneR();
				oneG54 = projectrbgs.get(2).getEtOneG();
				oneB54 = projectrbgs.get(2).getEtOneB();
				twoR54 = projectrbgs.get(2).getEtTwoR();
				twoG54 = projectrbgs.get(2).getEtTwoG();
				twoB54 = projectrbgs.get(2).getEtTwoB();
				threeR54 = projectrbgs.get(2).getEtThreeR();
				threeG54 = projectrbgs.get(2).getEtThreeG();
				threeB54 = projectrbgs.get(2).getEtThreeB();
				fourR54 = projectrbgs.get(2).getEtFourR();
				fourG54 = projectrbgs.get(2).getEtFourG();
				fourB54 = projectrbgs.get(2).getEtFourB();
				fiveR54 = projectrbgs.get(2).getEtFiveR();
				fiveG54 = projectrbgs.get(2).getEtFiveG();
				fiveB54 = projectrbgs.get(2).getEtFiveB();
				rotationTime5 = projectrbgs.get(2).getEtRotationTime();

				mode6 = projectrbgs.get(1).getMode();
				one64 = projectrbgs.get(1).getEtOne();
				two64 = projectrbgs.get(1).getEtTwo();
				three64 = projectrbgs.get(1).getEtThree();
				four64 = projectrbgs.get(1).getEtFour();
				five64 = projectrbgs.get(1).getEtFive();
				six64 = projectrbgs.get(1).getEtSix();
				seven64 = projectrbgs.get(1).getEtSeven();
				eight64 = projectrbgs.get(1).getEtEight();
				oneR64 = projectrbgs.get(1).getEtOneR();
				oneG64 = projectrbgs.get(1).getEtOneG();
				oneB64 = projectrbgs.get(1).getEtOneB();
				twoR64 = projectrbgs.get(1).getEtTwoR();
				twoG64 = projectrbgs.get(1).getEtTwoG();
				twoB64 = projectrbgs.get(1).getEtTwoB();
				threeR64 = projectrbgs.get(1).getEtThreeR();
				threeG64 = projectrbgs.get(1).getEtThreeG();
				threeB64 = projectrbgs.get(1).getEtThreeB();
				fourR64 = projectrbgs.get(1).getEtFourR();
				fourG64 = projectrbgs.get(1).getEtFourG();
				fourB64 = projectrbgs.get(1).getEtFourB();
				fiveR64 = projectrbgs.get(1).getEtFiveR();
				fiveG64 = projectrbgs.get(1).getEtFiveG();
				fiveB64 = projectrbgs.get(1).getEtFiveB();
				rotationTime6 = projectrbgs.get(1).getEtRotationTime();

				mode7 = projectrbgs.get(0).getMode();
				one74 = projectrbgs.get(0).getEtOne();
				two74 = projectrbgs.get(0).getEtTwo();
				three74 = projectrbgs.get(0).getEtThree();
				four74 = projectrbgs.get(0).getEtFour();
				five74 = projectrbgs.get(0).getEtFive();
				six74 = projectrbgs.get(0).getEtSix();
				seven74 = projectrbgs.get(0).getEtSeven();
				eight74 = projectrbgs.get(0).getEtEight();
				oneR74 = projectrbgs.get(0).getEtOneR();
				oneG74 = projectrbgs.get(0).getEtOneG();
				oneB74 = projectrbgs.get(0).getEtOneB();
				twoR74 = projectrbgs.get(0).getEtTwoR();
				twoG74 = projectrbgs.get(0).getEtTwoG();
				twoB74 = projectrbgs.get(0).getEtTwoB();
				threeR74 = projectrbgs.get(0).getEtThreeR();
				threeG74 = projectrbgs.get(0).getEtThreeG();
				threeB74 = projectrbgs.get(0).getEtThreeB();
				fourR74 = projectrbgs.get(0).getEtFourR();
				fourG74 = projectrbgs.get(0).getEtFourG();
				fourB74 = projectrbgs.get(0).getEtFourB();
				fiveR74 = projectrbgs.get(0).getEtFiveR();
				fiveG74 = projectrbgs.get(0).getEtFiveG();
				fiveB74 = projectrbgs.get(0).getEtFiveB();
				rotationTime7 = projectrbgs.get(0).getEtRotationTime();

				one14=(one14==null)?"":one14;
				two14=(two14==null)?"":two14;
				three14=(three14==null)?"":three14;
				four14=(four14==null)?"":four14;
				five14=(five14==null)?"":five14;
				six14 = (six14==null)?"":six14;
				seven14=(seven14==null)?"":seven14;
				eight14=(eight14==null)?"":eight14;
				oneR14=(oneR14==null)?"":oneR14;
				oneG14=(oneG14==null)?"":oneG14;
				oneB14=(oneB14==null)?"":oneB14;
				twoR14=(twoR14==null)?"":twoR14;
				twoG14=(twoG14==null)?"":twoG14;
				twoB14=(twoB14==null)?"":twoB14;
				threeR14=(threeR14==null)?"":threeR14;
				threeG14=(threeG14==null)?"":threeG14;
				threeB14=(threeB14==null)?"":threeB14;
				fourR14=(fourR14==null)?"":fourR14;
				fourG14=(fourG14==null)?"":fourG14;
				fourB14=(fourB14==null)?"":fourB14;
				fiveR14=(fiveR14==null)?"":fiveR14;
				fiveG14=(fiveG14==null)?"":fiveG14;
				fiveB14=(fiveB14==null)?"":fiveB14;
				rotationTime1 = (rotationTime1==null)?"":rotationTime1;
				
				one24=(one24==null)?"":one24;
				two24=(two24==null)?"":two24;
				three24=(three24==null)?"":three24;
				four24=(four24==null)?"":four24;
				five24=(five24==null)?"":five24;
				six24 = (six24==null)?"":six24;
				seven24=(seven24==null)?"":seven24;
				eight24=(eight24==null)?"":eight24;
				oneR24=(oneR24==null)?"":oneR24;
				oneG24=(oneG24==null)?"":oneG24;
				oneB24=(oneB24==null)?"":oneB24;
				twoR24=(twoR24==null)?"":twoR24;
				twoG24=(twoG24==null)?"":twoG24;
				twoB24=(twoB24==null)?"":twoB24;
				threeR24=(threeR24==null)?"":threeR24;
				threeG24=(threeG24==null)?"":threeG24;
				threeB24=(threeB24==null)?"":threeB24;
				fourR24=(fourR24==null)?"":fourR24;
				fourG24=(fourG24==null)?"":fourG24;
				fourB24=(fourB24==null)?"":fourB24;
				fiveR24=(fiveR24==null)?"":fiveR24;
				fiveG24=(fiveG24==null)?"":fiveG24;
				fiveB24=(fiveB24==null)?"":fiveB24;
				rotationTime2 = (rotationTime2==null)?"":rotationTime2;
				
				one34=(one34==null)?"":one34;
				two34=(two34==null)?"":two34;
				three34=(three34==null)?"":three34;
				four34=(four34==null)?"":four34;
				five34=(five34==null)?"":five34;
				six34 = (six34==null)?"":six34;
				seven34=(seven34==null)?"":seven34;
				eight34=(eight34==null)?"":eight34;
				oneR34=(oneR34==null)?"":oneR34;
				oneG34=(oneG34==null)?"":oneG34;
				oneB34=(oneB34==null)?"":oneB34;
				twoR34=(twoR34==null)?"":twoR34;
				twoG34=(twoG34==null)?"":twoG34;
				twoB34=(twoB34==null)?"":twoB34;
				threeR34=(threeR34==null)?"":threeR34;
				threeG34=(threeG34==null)?"":threeG34;
				threeB34=(threeB34==null)?"":threeB34;
				fourR34=(fourR34==null)?"":fourR34;
				fourG34=(fourG34==null)?"":fourG34;
				fourB34=(fourB34==null)?"":fourB34;
				fiveR34=(fiveR34==null)?"":fiveR34;
				fiveG34=(fiveG34==null)?"":fiveG34;
				fiveB34=(fiveB34==null)?"":fiveB34;
				rotationTime3 = (rotationTime3==null)?"":rotationTime3;
				
				one44=(one44==null)?"":one44;
				two44=(two44==null)?"":two44;
				three44=(three44==null)?"":three44;
				four44=(four44==null)?"":four44;
				five44=(five44==null)?"":five44;
				six44 = (six44==null)?"":six44;
				seven44=(seven44==null)?"":seven44;
				eight44=(eight44==null)?"":eight44;
				oneR44=(oneR44==null)?"":oneR44;
				oneG44=(oneG44==null)?"":oneG44;
				oneB44=(oneB44==null)?"":oneB44;
				twoR44=(twoR44==null)?"":twoR44;
				twoG44=(twoG44==null)?"":twoG44;
				twoB44=(twoB44==null)?"":twoB44;
				threeR44=(threeR44==null)?"":threeR44;
				threeG44=(threeG44==null)?"":threeG44;
				threeB44=(threeB44==null)?"":threeB44;
				fourR44=(fourR44==null)?"":fourR44;
				fourG44=(fourG44==null)?"":fourG44;
				fourB44=(fourB44==null)?"":fourB44;
				fiveR44=(fiveR44==null)?"":fiveR44;
				fiveG44=(fiveG44==null)?"":fiveG44;
				fiveB44=(fiveB44==null)?"":fiveB44;
				rotationTime4 = (rotationTime4==null)?"":rotationTime4;
				
				one54=(one54==null)?"":one54;
				two54=(two54==null)?"":two54;
				three54=(three54==null)?"":three54;
				four54=(four54==null)?"":four54;
				five54=(five54==null)?"":five54;
				six54 = (six54==null)?"":six54;
				seven54=(seven54==null)?"":seven54;
				eight54=(eight54==null)?"":eight54;
				oneR54=(oneR54==null)?"":oneR54;
				oneG54=(oneG54==null)?"":oneG54;
				oneB54=(oneB54==null)?"":oneB54;
				twoR54=(twoR54==null)?"":twoR54;
				twoG54=(twoG54==null)?"":twoG54;
				twoB54=(twoB54==null)?"":twoB54;
				threeR54=(threeR54==null)?"":threeR54;
				threeG54=(threeG54==null)?"":threeG54;
				threeB54=(threeB54==null)?"":threeB54;
				fourR54=(fourR54==null)?"":fourR54;
				fourG54=(fourG54==null)?"":fourG54;
				fourB54=(fourB54==null)?"":fourB54;
				fiveR54=(fiveR54==null)?"":fiveR54;
				fiveG54=(fiveG54==null)?"":fiveG54;
				fiveB54=(fiveB54==null)?"":fiveB54;
				rotationTime5 = (rotationTime5==null)?"":rotationTime5;
				
				one64=(one64==null)?"":one64;
				two64=(two64==null)?"":two64;
				three64=(three64==null)?"":three64;
				four64=(four64==null)?"":four64;
				five64=(five64==null)?"":five64;
				six64 = (six64==null)?"":six64;
				seven64=(seven64==null)?"":seven64;
				eight64=(eight64==null)?"":eight64;
				oneR64=(oneR64==null)?"":oneR64;
				oneG64=(oneG64==null)?"":oneG64;
				oneB64=(oneB64==null)?"":oneB64;
				twoR64=(twoR64==null)?"":twoR64;
				twoG64=(twoG64==null)?"":twoG64;
				twoB64=(twoB64==null)?"":twoB64;
				threeR64=(threeR64==null)?"":threeR64;
				threeG64=(threeG64==null)?"":threeG64;
				threeB64=(threeB64==null)?"":threeB64;
				fourR64=(fourR64==null)?"":fourR64;
				fourG64=(fourG64==null)?"":fourG64;
				fourB64=(fourB64==null)?"":fourB64;
				fiveR64=(fiveR64==null)?"":fiveR64;
				fiveG64=(fiveG64==null)?"":fiveG64;
				fiveB64=(fiveB64==null)?"":fiveB64;
				rotationTime6 = (rotationTime6==null)?"":rotationTime6;
			
				one74=(one74==null)?"":one74;
				two74=(two74==null)?"":two74;
				three74=(three74==null)?"":three74;
				four74=(four74==null)?"":four74;
				five74=(five74==null)?"":five74;
				six74 = (six74==null)?"":six74;
				seven74=(seven74==null)?"":seven74;
				eight74=(eight74==null)?"":eight74;
				oneR74=(oneR74==null)?"":oneR74;
				oneG74=(oneG74==null)?"":oneG74;
				oneB74=(oneB74==null)?"":oneB74;
				twoR74=(twoR74==null)?"":twoR74;
				twoG74=(twoG74==null)?"":twoG74;
				twoB74=(twoB74==null)?"":twoB74;
				threeR74=(threeR74==null)?"":threeR74;
				threeG74=(threeG74==null)?"":threeG74;
				threeB74=(threeB74==null)?"":threeB74;
				fourR74=(fourR74==null)?"":fourR74;
				fourG74=(fourG74==null)?"":fourG74;
				fourB74=(fourB74==null)?"":fourB74;
				fiveR74=(fiveR74==null)?"":fiveR74;
				fiveG74=(fiveG74==null)?"":fiveG74;
				fiveB74=(fiveB74==null)?"":fiveB74;
				rotationTime7 = (rotationTime7==null)?"":rotationTime7;

			}

		}
	}
}
