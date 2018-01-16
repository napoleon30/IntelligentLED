package cn.sharelink.intelligentled.activity_for_led;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.sharelink.intelligentled.R;
import cn.sharelink.intelligentled.R.layout;
import cn.sharelink.intelligentled.pickerview.TimePickerView;
import cn.sharelink.intelligentled.sql4_for_rgb_project.ProjectRGB;
import cn.sharelink.intelligentled.sql4_for_rgb_project.ProjectRGBDaoImpl;
import cn.sharelink.intelligentled.sql6_all_plan_name.PlanName;
import cn.sharelink.intelligentled.sql6_all_plan_name.PlanNameDaoImpl;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TimeFormatException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * RGB灯的属性计划编辑界面
 */
public class SingleColorAttributeEdit2Activity extends Activity {
	private static final String TAG = SingleColorAttributeEdit2Activity.class
			.getSimpleName();
	private TextView back, save, tvPlanName;
	private Button weekLeft, weekRight, modeLeft,
			modeRight;
	private TextView weeks, times, modes;
	private EditText etOne, etTwo, etThree, etFour, etFive, etSix, etSeven,
			etEight, etOneR, etOneG, etOneB, etTwoR, etTwoG, etTwoB, etThreeR,
			etThreeG, etThreeB, etFourR, etFourG, etFourB, etFiveR, etFiveG,
			etFiveB;
	private EditText etRotationTime;
	private ScrollView scrollview;
	private LinearLayout llDescribe1,llDescribe2,llDescribe3;
	

	/**
	 * style=0表示由“属性编辑”跳转，需新建计划，获取SingleColorAttributeActivity的计划ListView数据，
	 * 防止新建计划名重复 style=1表示由计划ListView的Item点击跳转，需要将该计划的存储数据读取显示
	 */
	int style = -1;
	/**
	 * 0表示为单色灯跳转，1表示为色温灯跳转，2为彩色灯跳转
	 */
	int type = 0;

	/**
	 * 接收的已存在的计划名集合，用于新建计划名是否重复的判断
	 */
//	List<String> proNames;

	int weekNum = 0;
	int modeNum = 0;

	/**
	 * 新建计划的名称
	 */
	private String projectName;

	String one14, two14, three14, four14, five14, six14, seven14, eight14; // 星期一，模式4的值
	String one24, two24, three24, four24, five24, six24, seven24, eight24;
	String one34, two34, three34, four34, five34, six34, seven34, eight34;
	String one44, two44, three44, four44, five44, six44, seven44, eight44;
	String one54, two54, three54, four54, five54, six54, seven54, eight54;
	String one64, two64, three64, four64, five64, six64, seven64, eight64;
	String one74, two74, three74, four74, five74, six74, seven74, eight74;
	String oneR14, oneG14, oneB14, twoR14, twoG14, twoB14, threeR14, threeG14,
			threeB14, fourR14, fourG14, fourB14, fiveR14, fiveG14, fiveB14;
	String oneR24, oneG24, oneB24, twoR24, twoG24, twoB24, threeR24, threeG24,
			threeB24, fourR24, fourG24, fourB24, fiveR24, fiveG24, fiveB24;
	String oneR34, oneG34, oneB34, twoR34, twoG34, twoB34, threeR34, threeG34,
			threeB34, fourR34, fourG34, fourB34, fiveR34, fiveG34, fiveB34;
	String oneR44, oneG44, oneB44, twoR44, twoG44, twoB44, threeR44, threeG44,
			threeB44, fourR44, fourG44, fourB44, fiveR44, fiveG44, fiveB44;
	String oneR54, oneG54, oneB54, twoR54, twoG54, twoB54, threeR54, threeG54,
			threeB54, fourR54, fourG54, fourB54, fiveR54, fiveG54, fiveB54;

	String oneR64, oneG64, oneB64, twoR64, twoG64, twoB64, threeR64, threeG64,
			threeB64, fourR64, fourG64, fourB64, fiveR64, fiveG64, fiveB64;

	String oneR74, oneG74, oneB74, twoR74, twoG74, twoB74, threeR74, threeG74,
			threeB74, fourR74, fourG74, fourB74, fiveR74, fiveG74, fiveB74;

	String rotationTime1,rotationTime2,rotationTime3,rotationTime4,rotationTime5,rotationTime6,rotationTime7;
	int mode1, mode2, mode3, mode4, mode5, mode6, mode7;

	ProjectRGBDaoImpl dao;
	List<ProjectRGB> projectrbgs;
	
	private PlanNameDaoImpl planNameDao;
	
	String physical="";
	
	String groupName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		setContentView(R.layout.activity_rgb_attribute_edit2);
		style = getIntent().getIntExtra("style", 0);
		type = getIntent().getIntExtra("type", 2);
		physical = getIntent().getStringExtra("physical");
		groupName = getIntent().getStringExtra("groupName");
		Log.e(TAG, "groupName:"+groupName);
		if(groupName==null){
			groupName="";
		}
		if(physical==null){
			physical="";
		}
		
		planNameDao = new PlanNameDaoImpl(SingleColorAttributeEdit2Activity.this);
		dao = new ProjectRGBDaoImpl(SingleColorAttributeEdit2Activity.this);
//		proNames = new ArrayList<String>();
		projectrbgs = new ArrayList<ProjectRGB>();

		tvPlanName = (TextView) findViewById(R.id.tv_plan_name);
		if (style == 0) {
//			proNames = getIntent().getStringArrayListExtra("plans");
			projectNameDialog();
		} else if (style == 1) {
			projectName = getIntent().getStringExtra("planName");
			tvPlanName.setText(projectName);
			initDate();
		}

		initView();
		viewListener();
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
						&& prorg.getGroupName().equals(groupName)
						&& prorg.getPhysical().equals(physical)) {
					projectrbgs.add(prorg);
				}
			}

			Log.e(TAG, "projectrbgs的长度：" + projectrbgs.size());
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

	/**
	 * 新建计划名称
	 */
	private void projectNameDialog() {
		AlertDialog.Builder builder = new Builder(
				SingleColorAttributeEdit2Activity.this);
		View view = LayoutInflater.from(SingleColorAttributeEdit2Activity.this)
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

				if (TextUtils.isEmpty(name.getText().toString().trim())) {
					Toast.makeText(
							SingleColorAttributeEdit2Activity.this,
							getResources().getString(
									R.string.project_name_cannot_be_empty),
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					projectName = name.getText().toString().trim();
					if (planNameDao.query(null, null).size()>0) {
						for(PlanName planName:planNameDao.query(null, null)){
							if(planName.getPlanName().equals(projectName) 
									&& planName.getPhysical().equals(physical)
									&& planName.getType()==type
									&& planName.getGroupName().equals(groupName)){
						Toast.makeText(
								SingleColorAttributeEdit2Activity.this,
								getResources()
										.getString(
												R.string.duplicate_program_name),
								Toast.LENGTH_SHORT).show();
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
				setResult(21123);
				SingleColorAttributeEdit2Activity.this.finish();

			}
		});
		dialog.show();

	}

	private void viewListener() {
		back.setOnClickListener(listener);
		save.setOnClickListener(listener);
		weekLeft.setOnClickListener(listener);
		weekRight.setOnClickListener(listener);
		modeLeft.setOnClickListener(listener);
		modeRight.setOnClickListener(listener);

	}

	private void initView() {
		scrollview = (ScrollView) findViewById(R.id.scrollview);
		back = (TextView) findViewById(R.id.btn_rgb_back);
		save = (TextView) findViewById(R.id.btn_rgb_save);
		weekLeft = (Button) findViewById(R.id.btn_rgb_week_left);
		weekRight = (Button) findViewById(R.id.btn_rgb_week_right);
		modeLeft = (Button) findViewById(R.id.btn_rgb_mode_left);
		modeRight = (Button) findViewById(R.id.btn_rgb_mode_right);
		weeks = (TextView) findViewById(R.id.tv_week);
		modes = (TextView) findViewById(R.id.tv_modes);

		etOne = (EditText) findViewById(R.id.et_one);
		etTwo = (EditText) findViewById(R.id.et_two);
		etThree = (EditText) findViewById(R.id.et_three);
		etFour = (EditText) findViewById(R.id.et_four);
		etFive = (EditText) findViewById(R.id.et_five);
		etSix = (EditText) findViewById(R.id.et_six);
		etSeven = (EditText) findViewById(R.id.et_seven);
		etEight = (EditText) findViewById(R.id.et_eight);

		etOneR = (EditText) findViewById(R.id.et_one_R);
		etOneG = (EditText) findViewById(R.id.et_one_G);
		etOneB = (EditText) findViewById(R.id.et_one_B);
		etTwoR = (EditText) findViewById(R.id.et_two_R);
		etTwoG = (EditText) findViewById(R.id.et_two_G);
		etTwoB = (EditText) findViewById(R.id.et_two_B);
		etThreeR = (EditText) findViewById(R.id.et_three_R);
		etThreeG = (EditText) findViewById(R.id.et_three_G);
		etThreeB = (EditText) findViewById(R.id.et_three_B);
		etFourR = (EditText) findViewById(R.id.et_four_R);
		etFourG = (EditText) findViewById(R.id.et_four_G);
		etFourB = (EditText) findViewById(R.id.et_four_B);
		etFiveR = (EditText) findViewById(R.id.et_five_R);
		etFiveG = (EditText) findViewById(R.id.et_five_G);
		etFiveB = (EditText) findViewById(R.id.et_five_B);
		etRotationTime = (EditText)findViewById(R.id.et_rotation_time);
		setRegion(etOne,0,100);
		setRegion(etTwo,0,100);
		setRegion(etThree,0,100);
		setRegion(etFour,0,100);
		setRegion(etFive,0,100);
		setRegion(etSix,0,100);
		setRegion(etSeven,0,100);
		setRegion(etEight,0,100);
		setRegion(etRotationTime, 0, 100);
		
		setRegion(etOneR, 0, 255);
		setRegion(etOneG, 0, 255);
		setRegion(etOneB, 0, 255);
		setRegion(etTwoR, 0, 255);
		setRegion(etTwoG, 0, 255);
		setRegion(etTwoB, 0, 255);
		setRegion(etThreeR, 0, 255);
		setRegion(etThreeG, 0, 255);
		setRegion(etThreeB, 0, 255);
		setRegion(etFourR, 0, 255);
		setRegion(etFourG, 0, 255);
		setRegion(etFourB, 0, 255);
		setRegion(etFiveR, 0, 255);
		setRegion(etFiveG, 0, 255);
		setRegion(etFiveB, 0, 255);
		
		llDescribe1 = (LinearLayout) findViewById(R.id.ll_mode1_describe);
		llDescribe2 = (LinearLayout) findViewById(R.id.ll_mode2_describe);
		llDescribe3 = (LinearLayout) findViewById(R.id.ll_mode3_describe);
	}
	

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_rgb_back: // 返回
				if (style == 0) {
					Intent intent = new Intent();
					if (projectName != null) {
						intent.putExtra("planName", projectName);
					}
					setResult(21113, intent);
				}
				SingleColorAttributeEdit2Activity.this.finish();
				break;
			case R.id.btn_rgb_save:// 保存
				// 保存之前需先删掉之前projectName名称相同的数据，再添加
				if (projectrbgs.size() > 0) {
					for (ProjectRGB rgb : projectrbgs) {
						if (rgb.getName().equals(projectName)) {
							Log.e(TAG, "保存前删除");
							dao.delete(rgb.getId());
						}
					}
				}
				saveDate();
				// 星期一
				dao.insert(new ProjectRGB(projectName, 0, 
						mode1, mode1 == 3 ? one14 : "0", mode1 == 3 ? two14
								: "0", mode1 == 3 ? three14 : "0",
						mode1 == 3 ? four14 : "0", mode1 == 3 ? five14 : "0",
						mode1 == 3 ? six14 : "0", mode1 == 3 ? seven14 : "0",
						mode1 == 3 ? eight14 : "0", mode1 == 3 ? oneR14 : "0",
						mode1 == 3 ? oneG14 : "0", mode1 == 3 ? oneB14 : "0",
						mode1 == 3 ? twoR14 : "0", mode1 == 3 ? twoG14 : "0",
						mode1 == 3 ? twoB14 : "0", mode1 == 3 ? threeR14 : "0",
						mode1 == 3 ? threeG14 : "0", mode1 == 3 ? threeB14 : "0",
						mode1 == 3 ? fourR14 : "0", mode1 == 3 ? fourG14 : "0",
						mode1 == 3 ? fourB14 : "0", mode1 == 3 ? fiveR14 : "0",
						mode1 == 3 ? fourG14 : "0", mode1 == 3 ? fourB14 : "0",
						mode1==3 ? rotationTime1:"0",
						groupName,physical));
				// 星期二
				dao.insert(new ProjectRGB(projectName, 1, mode2, 
						mode2 == 3 ? one24 : "0", mode2 == 3 ? two24
								: "0", mode2 == 3 ? three24 : "0",
						mode2 == 3 ? four24 : "0", mode2 == 3 ? five24 : "0",
						mode2 == 3 ? six24 : "0", mode2 == 3 ? seven24 : "0",
						mode2 == 3 ? eight24 : "0", mode2 == 3 ? oneR24 : "0",
						mode2 == 3 ? oneG24 : "0", mode2 == 3 ? oneB24 : "0",
						mode2 == 3 ? twoR24 : "0", mode2 == 3 ? twoG24 : "0",
						mode2 == 3 ? twoB24 : "0", mode2 == 3 ? threeR24 : "0",
						mode2 == 3 ? threeG24 : "0", mode2 == 3 ? threeB24 : "0",
						mode2 == 3 ? fourR24 : "0", mode2 == 3 ? fourG24 : "0",
						mode2 == 3 ? fourB24 : "0", mode2 == 3 ? fiveR24 : "0",
						mode2 == 3 ? fourG24 : "0", mode2 == 3 ? fourB24 : "0",
						mode2==3 ? rotationTime2:"0",
								groupName,physical));
				// 星期三
				dao.insert(new ProjectRGB(projectName, 2, mode3, 
						mode3 == 3 ? one34 : "0", mode3 == 3 ? two34
								: "0", mode3 == 3 ? three34 : "0",
						mode3 == 3 ? four34 : "0", mode3 == 3 ? five34 : "0",
						mode3 == 3 ? six34 : "0", mode3 == 3 ? seven34 : "0",
						mode3 == 3 ? eight34 : "0", mode3 == 3 ? oneR34 : "0",
						mode3 == 3 ? oneG34 : "0", mode3 == 3 ? oneB34 : "0",
						mode3 == 3 ? twoR34 : "0", mode3 == 3 ? twoG34 : "0",
						mode3 == 3 ? twoB34 : "0", mode3 == 3 ? threeR34 : "0",
						mode3 == 3 ? threeG34 : "0", mode3 == 3 ? threeB34 : "0",
						mode3 == 3 ? fourR34 : "0", mode3 == 3 ? fourG34 : "0",
						mode3 == 3 ? fourB34 : "0", mode3 == 3 ? fiveR34 : "0",
						mode3 == 3 ? fourG34 : "0", mode3 == 3 ? fourB34 : "0",
						mode3==3 ? rotationTime3:"0",
								groupName,physical));
				// 星期四
				dao.insert(new ProjectRGB(projectName, 3, mode4, 
						mode4 == 3 ? one44 : "0", mode4 == 3 ? two44
								: "0", mode4 == 3 ? three44 : "0",
						mode4 == 3 ? four44 : "0", mode4 == 3 ? five44 : "0",
						mode4 == 3 ? six44 : "0", mode4 == 3 ? seven44 : "0",
						mode4 == 3 ? eight44 : "0", mode4 == 3 ? oneR44 : "0",
						mode4 == 3 ? oneG44 : "0", mode4 == 3 ? oneB44 : "0",
						mode4 == 3 ? twoR44 : "0", mode4 == 3 ? twoG44 : "0",
						mode4 == 3 ? twoB44 : "0", mode4 == 3 ? threeR44 : "0",
						mode4 == 3 ? threeG44 : "0", mode4 == 3 ? threeB44 : "0",
						mode4 == 3 ? fourR44 : "0", mode4 == 3 ? fourG44 : "0",
						mode4 == 3 ? fourB44 : "0", mode4 == 3 ? fiveR44 : "0",
						mode4 == 3 ? fourG44 : "0", mode4 == 3 ? fourB44 : "0",
						mode4==3 ? rotationTime4:"0",
								groupName,physical));
				// 星期五
				dao.insert(new ProjectRGB(projectName, 4, mode5, 
						mode5 == 3 ? one54 : "0", mode5 == 3 ? two54
								: "0", mode5 == 3 ? three54 : "0",
						mode5 == 3 ? four54 : "0", mode5 == 3 ? five54 : "0",
						mode5 == 3 ? six54 : "0", mode5 == 3 ? seven54 : "0",
						mode5 == 3 ? eight54 : "0", mode5 == 3 ? oneR54 : "0",
						mode5 == 3 ? oneG54 : "0", mode5 == 3 ? oneB54 : "0",
						mode5 == 3 ? twoR54 : "0", mode5 == 3 ? twoG54 : "0",
						mode5 == 3 ? twoB54 : "0", mode5 == 3 ? threeR54 : "0",
						mode5 == 3 ? threeG54 : "0", mode5 == 3 ? threeB54 : "0",
						mode5 == 3 ? fourR54 : "0", mode5 == 3 ? fourG54 : "0",
						mode5 == 3 ? fourB54 : "0", mode5 == 3 ? fiveR54 : "0",
						mode5 == 3 ? fourG54 : "0", mode5 == 3 ? fourB54 : "0",
						mode5==3 ? rotationTime5:"0",
								groupName,physical));
				// 星期六
				dao.insert(new ProjectRGB(projectName, 5, mode6,
						mode6 == 3 ? one64 : "0", mode6 == 3 ? two64
								: "0", mode6 == 3 ? three64 : "0",
						mode6 == 3 ? four64 : "0", mode6 == 3 ? five64 : "0",
						mode6 == 3 ? six64 : "0", mode6 == 3 ? seven64 : "0",
						mode6 == 3 ? eight64 : "0", mode6 == 3 ? oneR64 : "0",
						mode6 == 3 ? oneG64 : "0", mode6 == 3 ? oneB64 : "0",
						mode6 == 3 ? twoR64 : "0", mode6 == 3 ? twoG64 : "0",
						mode6 == 3 ? twoB64 : "0", mode6 == 3 ? threeR64 : "0",
						mode6 == 3 ? threeG64 : "0", mode6 == 3 ? threeB64 : "0",
						mode6 == 3 ? fourR64 : "0", mode6 == 3 ? fourG64 : "0",
						mode6 == 3 ? fourB64 : "0", mode6 == 3 ? fiveR64 : "0",
						mode6 == 3 ? fourG64 : "0", mode6 == 3 ? fourB64 : "0",
						mode6==3 ? rotationTime6:"0",
								groupName,physical));
				// 星期天
				dao.insert(new ProjectRGB(projectName, 6, mode7,
						mode7 == 3 ? one74 : "0", mode7 == 3 ? two74
								: "0", mode7 == 3 ? three74 : "0",
						mode7 == 3 ? four74 : "0", mode7 == 3 ? five74 : "0",
						mode7 == 3 ? six74 : "0", mode7 == 3 ? seven74 : "0",
						mode7 == 3 ? eight74 : "0", mode7 == 3 ? oneR74 : "0",
						mode7 == 3 ? oneG74 : "0", mode7 == 3 ? oneB74 : "0",
						mode7 == 3 ? twoR74 : "0", mode7 == 3 ? twoG74 : "0",
						mode7 == 3 ? twoB74 : "0", mode7 == 3 ? threeR74 : "0",
						mode7 == 3 ? threeG74 : "0", mode7 == 3 ? threeB74 : "0",
						mode7 == 3 ? fourR74 : "0", mode7 == 3 ? fourG74 : "0",
						mode7 == 3 ? fourB74 : "0", mode7 == 3 ? fiveR74 : "0",
						mode7 == 3 ? fourG74 : "0", mode7 == 3 ? fourB74 : "0",
						mode7==3 ? rotationTime7:"0",
								groupName,physical));
				
				if (style == 0) {
					Intent intent = new Intent();
					if (projectName != null) {
						intent.putExtra("planName", projectName);
					}
					setResult(21113, intent);
				}
				SingleColorAttributeEdit2Activity.this.finish();
				break;
			case R.id.btn_rgb_week_left:// 星期左
				if (modeNum == 3) {
					saveDate();
				}
				weekNum--;
				if (weekNum < 0) {
					weekNum = 6;
				}
				changeWeekNum(weekNum);
				modeNum = 0;
				changeModeNum(modeNum);
				break;
			case R.id.btn_rgb_week_right:// 星期右
				if (modeNum == 3) {
					saveDate();
				}
				weekNum++;
				if (weekNum > 6) {
					weekNum = 0;
				}
				changeWeekNum(weekNum);
				modeNum = 0;
				changeModeNum(modeNum);
				break;
			case R.id.btn_rgb_mode_left: // 模式左
				modeNum--;
				if (modeNum < 0) {
					modeNum = 3;
					valutionDate();
				}
				if (modeNum == 2) {
					saveDate();
				}
				changeModeNum(modeNum);
				break;
			case R.id.btn_rgb_mode_right:// 模式右
				modeNum++;
				if (modeNum > 3) {
					modeNum = 0;
					saveDate();// 保存
				}
				if (modeNum == 3) {
					valutionDate(); // 赋值
				}
				changeModeNum(modeNum);

				break;
			}

		}
	};

	
	private void modeValue(int num) {
		switch (weekNum) {
		case 0:
			mode1 = num;
			break;
		case 1:
			mode2 = num;
			break;
		case 2:
			mode3 = num;
			break;
		case 3:
			mode4 = num;
			break;
		case 4:
			mode5 = num;
			break;
		case 5:
			mode6 = num;
			break;
		case 6:
			mode7 = num;
			break;
		}
	}

	/**
	 * 切换星期
	 * @param weekNum
	 */
	private void changeWeekNum(int weekNum) {
		switch (weekNum) {
		case 0:
			weeks.setText(getResources().getString(R.string.monday));
			break;
		case 1:
			weeks.setText(getResources().getString(R.string.tuesday));
			break;
		case 2:
			weeks.setText(getResources().getString(R.string.wednesday));
			break;
		case 3:
			weeks.setText(getResources().getString(R.string.thursday));
			break;
		case 4:
			weeks.setText(getResources().getString(R.string.friday));
			break;
		case 5:
			weeks.setText(getResources().getString(R.string.saturday));
			break;
		case 6:
			weeks.setText(getResources().getString(R.string.sunday));
			break;
		}
	}

	/**
	 * 切换模式
	 */
	private void changeModeNum(int modeNum) {
		modeValue(modeNum);
		Log.e(TAG, "changeModeNum");
		Log.e(TAG, "modeNum的值："+modeNum);
		switch (modeNum) {
		case 0:
			modes.setText(getResources().getString(R.string.mode_1));
			llDescribe1.setVisibility(View.VISIBLE);
			llDescribe2.setVisibility(View.GONE);
			llDescribe3.setVisibility(View.GONE); 
			scrollview.setVisibility(View.GONE);
			break;
		case 1:
			modes.setText(getResources().getString(R.string.mode_2));
			llDescribe1.setVisibility(View.GONE);
			llDescribe2.setVisibility(View.VISIBLE);
			llDescribe3.setVisibility(View.GONE);
			scrollview.setVisibility(View.GONE);
			break;
		case 2:
			modes.setText(getResources().getString(R.string.mode_3));
			llDescribe1.setVisibility(View.GONE);
			llDescribe2.setVisibility(View.GONE);
			llDescribe3.setVisibility(View.VISIBLE);
			scrollview.setVisibility(View.GONE);
			break;
		case 3:
			modes.setText(getResources().getString(R.string.mode_4));
			llDescribe1.setVisibility(View.GONE);
			llDescribe2.setVisibility(View.GONE);
			llDescribe3.setVisibility(View.GONE);
			scrollview.setVisibility(View.VISIBLE);
			break;
		}

	}

	/**
	 * 刚进入模式四时，给模式四中的EditText赋值
	 */
	private void valutionDate() {
		Log.e(TAG, "valutionDate");
		switch (weekNum) {
		case 0: // 星期一
			etOne.setText(one14);
			etTwo.setText(two14);
			etThree.setText(three14);
			etFour.setText(four14);
			etFive.setText(five14);
			etSix.setText(six14);
			etSeven.setText(seven14);
			etEight.setText(eight14);
			etOneR.setText(oneR14);
			etOneG.setText(oneG14);
			etOneB.setText(oneB14);
			etTwoR.setText(twoR14);
			etTwoG.setText(twoG14);
			etTwoB.setText(twoB14);
			etThreeR.setText(threeR14);
			etThreeG.setText(threeG14);
			etThreeB.setText(threeB14);
			etFourR.setText(fourR14);
			etFourG.setText(fourG14);
			etFourB.setText(fourB14);
			etFiveR.setText(fiveR14);
			etFiveG.setText(fiveG14);
			etFiveB.setText(fiveB14);
			etRotationTime.setText(rotationTime1);
			break;
		case 1: // 星期二
			etOne.setText(one24);
			etTwo.setText(two24);
			etThree.setText(three24);
			etFour.setText(four24);
			etFive.setText(five24);
			etSix.setText(six24);
			etSeven.setText(seven24);
			etEight.setText(eight24);
			etOneR.setText(oneR24);
			etOneG.setText(oneG24);
			etOneB.setText(oneB24);
			etTwoR.setText(twoR24);
			etTwoG.setText(twoG24);
			etTwoB.setText(twoB24);
			etThreeR.setText(threeR24);
			etThreeG.setText(threeG24);
			etThreeB.setText(threeB24);
			etFourR.setText(fourR24);
			etFourG.setText(fourG24);
			etFourB.setText(fourB24);
			etFiveR.setText(fiveR24);
			etFiveG.setText(fiveG24);
			etFiveB.setText(fiveB24);
			etRotationTime.setText(rotationTime2);
			break;
		case 2: // 星期三
			etOne.setText(one34);
			etTwo.setText(two34);
			etThree.setText(three34);
			etFour.setText(four34);
			etFive.setText(five34);
			etSix.setText(six34);
			etSeven.setText(seven34);
			etEight.setText(eight34);
			etOneR.setText(oneR34);
			etOneG.setText(oneG34);
			etOneB.setText(oneB34);
			etTwoR.setText(twoR34);
			etTwoG.setText(twoG34);
			etTwoB.setText(twoB34);
			etThreeR.setText(threeR34);
			etThreeG.setText(threeG34);
			etThreeB.setText(threeB34);
			etFourR.setText(fourR34);
			etFourG.setText(fourG34);
			etFourB.setText(fourB34);
			etFiveR.setText(fiveR34);
			etFiveG.setText(fiveG34);
			etFiveB.setText(fiveB34);
			etRotationTime.setText(rotationTime3);
			break;
		case 3: // 星期四
			etOne.setText(one44);
			etTwo.setText(two44);
			etThree.setText(three44);
			etFour.setText(four44);
			etFive.setText(five44);
			etSix.setText(six44);
			etSeven.setText(seven44);
			etEight.setText(eight44);
			etOneR.setText(oneR44);
			etOneG.setText(oneG44);
			etOneB.setText(oneB44);
			etTwoR.setText(twoR44);
			etTwoG.setText(twoG44);
			etTwoB.setText(twoB44);
			etThreeR.setText(threeR44);
			etThreeG.setText(threeG44);
			etThreeB.setText(threeB44);
			etFourR.setText(fourR44);
			etFourG.setText(fourG44);
			etFourB.setText(fourB44);
			etFiveR.setText(fiveR44);
			etFiveG.setText(fiveG44);
			etFiveB.setText(fiveB44);
			etRotationTime.setText(rotationTime4);
			break;
		case 4: // 星期五
			etOne.setText(one54);
			etTwo.setText(two54);
			etThree.setText(three54);
			etFour.setText(four54);
			etFive.setText(five54);
			etSix.setText(six54);
			etSeven.setText(seven54);
			etEight.setText(eight54);
			etOneR.setText(oneR54);
			etOneG.setText(oneG54);
			etOneB.setText(oneB54);
			etTwoR.setText(twoR54);
			etTwoG.setText(twoG54);
			etTwoB.setText(twoB54);
			etThreeR.setText(threeR54);
			etThreeG.setText(threeG54);
			etThreeB.setText(threeB54);
			etFourR.setText(fourR54);
			etFourG.setText(fourG54);
			etFourB.setText(fourB54);
			etFiveR.setText(fiveR54);
			etFiveG.setText(fiveG54);
			etFiveB.setText(fiveB54);
			etRotationTime.setText(rotationTime5);
			break;
		case 5:// 星期六
			etOne.setText(one64);
			etTwo.setText(two64);
			etThree.setText(three64);
			etFour.setText(four64);
			etFive.setText(five64);
			etSix.setText(six64);
			etSeven.setText(seven64);
			etEight.setText(eight64);
			etOneR.setText(oneR64);
			etOneG.setText(oneG64);
			etOneB.setText(oneB64);
			etTwoR.setText(twoR64);
			etTwoG.setText(twoG64);
			etTwoB.setText(twoB64);
			etThreeR.setText(threeR64);
			etThreeG.setText(threeG64);
			etThreeB.setText(threeB64);
			etFourR.setText(fourR64);
			etFourG.setText(fourG64);
			etFourB.setText(fourB64);
			etFiveR.setText(fiveR64);
			etFiveG.setText(fiveG64);
			etFiveB.setText(fiveB64);
			etRotationTime.setText(rotationTime6);
			break;
		case 6:// 星期日
			etOne.setText(one74);
			etTwo.setText(two74);
			etThree.setText(three74);
			etFour.setText(four74);
			etFive.setText(five74);
			etSix.setText(six74);
			etSeven.setText(seven74);
			etEight.setText(eight74);
			etOneR.setText(oneR74);
			etOneG.setText(oneG74);
			etOneB.setText(oneB74);
			etTwoR.setText(twoR74);
			etTwoG.setText(twoG74);
			etTwoB.setText(twoB74);
			etThreeR.setText(threeR74);
			etThreeG.setText(threeG74);
			etThreeB.setText(threeB74);
			etFourR.setText(fourR74);
			etFourG.setText(fourG74);
			etFourB.setText(fourB74);
			etFiveR.setText(fiveR74);
			etFiveG.setText(fiveG74);
			etFiveB.setText(fiveB74);
			etRotationTime.setText(rotationTime7);
			break;
		}

	}

	/**
	 * 模式四切换时保留填写的数据
	 */
	protected void saveDate() {
		Log.e(TAG, "saveDate");
		switch (weekNum) {
		case 0: // 星期一
			one14 = etOne.getText().toString();
			two14 = etTwo.getText().toString();
			three14 = etThree.getText().toString();
			four14 = etFour.getText().toString();
			five14 = etFive.getText().toString();
			six14 = etSix.getText().toString();
			seven14 = etSeven.getText().toString();
			eight14 = etEight.getText().toString();
			oneR14 = etOneR.getText().toString();
			oneG14 = etOneG.getText().toString();
			oneB14 = etOneB.getText().toString();
			twoR14 = etTwoR.getText().toString();
			twoG14 = etTwoG.getText().toString();
			twoB14 = etTwoB.getText().toString();
			threeR14 = etThreeR.getText().toString();
			threeG14 = etThreeG.getText().toString();
			threeB14 = etThreeB.getText().toString();
			fourR14 = etFourR.getText().toString();
			fourG14 = etFourG.getText().toString();
			fourB14 = etFourB.getText().toString();
			fiveR14 = etFiveR.getText().toString();
			fiveG14 = etFiveG.getText().toString();
			fiveB14 = etFiveB.getText().toString();
			rotationTime1 = etRotationTime.getText().toString();
			break;
		case 1: // 星期二
			one24 = etOne.getText().toString();
			two24 = etTwo.getText().toString();
			three24 = etThree.getText().toString();
			four24 = etFour.getText().toString();
			five24 = etFive.getText().toString();
			six24 = etSix.getText().toString();
			seven24 = etSeven.getText().toString();
			eight24 = etEight.getText().toString();
			oneR24 = etOneR.getText().toString();
			oneG24 = etOneG.getText().toString();
			oneB24 = etOneB.getText().toString();
			twoR24 = etTwoR.getText().toString();
			twoG24 = etTwoG.getText().toString();
			twoB24 = etTwoB.getText().toString();
			threeR24 = etThreeR.getText().toString();
			threeG24 = etThreeG.getText().toString();
			threeB24 = etThreeB.getText().toString();
			fourR24 = etFourR.getText().toString();
			fourG24 = etFourG.getText().toString();
			fourB24 = etFourB.getText().toString();
			fiveR24 = etFiveR.getText().toString();
			fiveG24 = etFiveG.getText().toString();
			fiveB24 = etFiveB.getText().toString();
			rotationTime2 = etRotationTime.getText().toString();
			break;
		case 2: // 星期三
			one34 = etOne.getText().toString();
			two34 = etTwo.getText().toString();
			three34 = etThree.getText().toString();
			four34 = etFour.getText().toString();
			five34 = etFive.getText().toString();
			six34 = etSix.getText().toString();
			seven34 = etSeven.getText().toString();
			eight34 = etEight.getText().toString();
			oneR34 = etOneR.getText().toString();
			oneG34 = etOneG.getText().toString();
			oneB34 = etOneB.getText().toString();
			twoR34 = etTwoR.getText().toString();
			twoG34 = etTwoG.getText().toString();
			twoB34 = etTwoB.getText().toString();
			threeR34 = etThreeR.getText().toString();
			threeG34 = etThreeG.getText().toString();
			threeB34 = etThreeB.getText().toString();
			fourR34 = etFourR.getText().toString();
			fourG34 = etFourG.getText().toString();
			fourB34 = etFourB.getText().toString();
			fiveR34 = etFiveR.getText().toString();
			fiveG34 = etFiveG.getText().toString();
			fiveB34 = etFiveB.getText().toString();
			rotationTime3 = etRotationTime.getText().toString();
			break;
		case 3: // 星期四
			one44 = etOne.getText().toString();
			two44 = etTwo.getText().toString();
			three44 = etThree.getText().toString();
			four44 = etFour.getText().toString();
			five44 = etFive.getText().toString();
			six44 = etSix.getText().toString();
			seven44 = etSeven.getText().toString();
			eight44 = etEight.getText().toString();
			oneR44 = etOneR.getText().toString();
			oneG44 = etOneG.getText().toString();
			oneB44 = etOneB.getText().toString();
			twoR44 = etTwoR.getText().toString();
			twoG44 = etTwoG.getText().toString();
			twoB44 = etTwoB.getText().toString();
			threeR44 = etThreeR.getText().toString();
			threeG44 = etThreeG.getText().toString();
			threeB44 = etThreeB.getText().toString();
			fourR44 = etFourR.getText().toString();
			fourG44 = etFourG.getText().toString();
			fourB44 = etFourB.getText().toString();
			fiveR44 = etFiveR.getText().toString();
			fiveG44 = etFiveG.getText().toString();
			fiveB44 = etFiveB.getText().toString();
			rotationTime4 = etRotationTime.getText().toString();
			break;
		case 4: // 星期五
			one54 = etOne.getText().toString();
			two54 = etTwo.getText().toString();
			three54 = etThree.getText().toString();
			four54 = etFour.getText().toString();
			five54 = etFive.getText().toString();
			six54 = etSix.getText().toString();
			seven54 = etSeven.getText().toString();
			eight54 = etEight.getText().toString();
			oneR54 = etOneR.getText().toString();
			oneG54 = etOneG.getText().toString();
			oneB54 = etOneB.getText().toString();
			twoR54 = etTwoR.getText().toString();
			twoG54 = etTwoG.getText().toString();
			twoB54 = etTwoB.getText().toString();
			threeR54 = etThreeR.getText().toString();
			threeG54 = etThreeG.getText().toString();
			threeB54 = etThreeB.getText().toString();
			fourR54 = etFourR.getText().toString();
			fourG54 = etFourG.getText().toString();
			fourB54 = etFourB.getText().toString();
			fiveR54 = etFiveR.getText().toString();
			fiveG54 = etFiveG.getText().toString();
			fiveB54 = etFiveB.getText().toString();
			rotationTime5 = etRotationTime.getText().toString();
			break;
		case 5: // 星期六
			one64 = etOne.getText().toString();
			two64 = etTwo.getText().toString();
			three64 = etThree.getText().toString();
			four64 = etFour.getText().toString();
			five64 = etFive.getText().toString();
			six64 = etSix.getText().toString();
			seven64 = etSeven.getText().toString();
			eight64 = etEight.getText().toString();
			oneR64 = etOneR.getText().toString();
			oneG64 = etOneG.getText().toString();
			oneB64 = etOneB.getText().toString();
			twoR64 = etTwoR.getText().toString();
			twoG64 = etTwoG.getText().toString();
			twoB64 = etTwoB.getText().toString();
			threeR64 = etThreeR.getText().toString();
			threeG64 = etThreeG.getText().toString();
			threeB64 = etThreeB.getText().toString();
			fourR64 = etFourR.getText().toString();
			fourG64 = etFourG.getText().toString();
			fourB64 = etFourB.getText().toString();
			fiveR64 = etFiveR.getText().toString();
			fiveG64 = etFiveG.getText().toString();
			fiveB64 = etFiveB.getText().toString();
			rotationTime6 = etRotationTime.getText().toString();
			break;
		case 6:// 星期天
			one74 = etOne.getText().toString();
			two74 = etTwo.getText().toString();
			three74 = etThree.getText().toString();
			four74 = etFour.getText().toString();
			five74 = etFive.getText().toString();
			six74 = etSix.getText().toString();
			seven74 = etSeven.getText().toString();
			eight74 = etEight.getText().toString();
			oneR74 = etOneR.getText().toString();
			oneG74 = etOneG.getText().toString();
			oneB74 = etOneB.getText().toString();
			twoR74 = etTwoR.getText().toString();
			twoG74 = etTwoG.getText().toString();
			twoB74 = etTwoB.getText().toString();
			threeR74 = etThreeR.getText().toString();
			threeG74 = etThreeG.getText().toString();
			threeB74 = etThreeB.getText().toString();
			fourR74 = etFourR.getText().toString();
			fourG74 = etFourG.getText().toString();
			fourB74 = etFourB.getText().toString();
			fiveR74 = etFiveR.getText().toString();
			fiveG74 = etFiveG.getText().toString();
			fiveB74 = etFiveB.getText().toString();
			rotationTime7 = etRotationTime.getText().toString();
			break;
		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0) {
			if (style == 0) {
				Intent intent = new Intent();
				if (projectName != null) {
					intent.putExtra("planName", projectName);
				}
				setResult(21113, intent);
			}
			SingleColorAttributeEdit2Activity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/** edittext只能输入数值的时候做最大最小的限制 */  
    public void setRegion(final EditText edit, final int MIN_MARK, final int MAX_MARK) {  
        edit.addTextChangedListener(new TextWatcher() {  
            @Override  
            public void onTextChanged(CharSequence s, int start, int before, int count) {  
            	Log.e(TAG, "onTextChanged");
                if (start > 1) {  
                    if (MIN_MARK != -1 && MAX_MARK != -1) {  
                        double num = Double.parseDouble(s.toString());  
                        if (num > MAX_MARK) {  
                            s = String.valueOf(MAX_MARK);  
                            edit.setText(s);  
                        } else if (num < MIN_MARK) {  
                            s = String.valueOf(MIN_MARK);  
                            edit.setText(s); 
                        }  
                        edit.setSelection(s.length());  
                    }  
                }  
                
                
            }  
  
            @Override  
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { 
            	Log.e(TAG, "beforeTextChanged");
            	if(etOne.getText().toString().equals("100")||
                		etTwo.getText().toString().equals("100")||
                		etThree.getText().toString().equals("100")||
                		etFour.getText().toString().equals("100")||
                		etFive.getText().toString().equals("100")||
                		etSix.getText().toString().equals("100")||
                		etSeven.getText().toString().equals("100")||
                		etEight.getText().toString().equals("100")
                			)
                	  switch (edit.getId()) {
    					case R.id.et_one:
    						if(etTwo.getText().toString().equals("100")){
    							etTwo.setText("0");
    						}else if(etThree.getText().toString().equals("100")){
    							etThree.setText("0");
    						}else if(etFour.getText().toString().equals("100")){
    							etFour.setText("0");
    						}else if(etFive.getText().toString().equals("100")){
    							etFive.setText("0");
    						}else if(etSix.getText().toString().equals("100")){
    							etSix.setText("0");
    						}else if(etSeven.getText().toString().equals("100")){
    							etSeven.setText("0");
    						}else if(etEight.getText().toString().equals("100")){
    							etEight.setText("0");
    						}
    						break;
    					case R.id.et_two:
    						if(etOne.getText().toString().equals("100")){
    							etOne.setText("0");
    						}else if(etThree.getText().toString().equals("100")){
    							etThree.setText("0");
    						}else if(etFour.getText().toString().equals("100")){
    							etFour.setText("0");
    						}else if(etFive.getText().toString().equals("100")){
    							etFive.setText("0");
    						}else if(etSix.getText().toString().equals("100")){
    							etSix.setText("0");
    						}else if(etSeven.getText().toString().equals("100")){
    							etSeven.setText("0");
    						}else if(etEight.getText().toString().equals("100")){
    							etEight.setText("0");
    						}
    						break;
    					case R.id.et_three:
    						if(etOne.getText().toString().equals("100")){
    							etOne.setText("0");
    						}else if(etTwo.getText().toString().equals("100")){
    							etTwo.setText("0");
    						}else if(etFour.getText().toString().equals("100")){
    							etFour.setText("0");
    						}else if(etFive.getText().toString().equals("100")){
    							etFive.setText("0");
    						}else if(etSix.getText().toString().equals("100")){
    							etSix.setText("0");
    						}else if(etSeven.getText().toString().equals("100")){
    							etSeven.setText("0");
    						}else if(etEight.getText().toString().equals("100")){
    							etEight.setText("0");
    						}
    						break;
    					case R.id.et_four:
    						if(etOne.getText().toString().equals("100")){
    							etOne.setText("0");
    						}else if(etTwo.getText().toString().equals("100")){
    							etTwo.setText("0");
    						}else if(etThree.getText().toString().equals("100")){
    							etThree.setText("0");
    						}else if(etFive.getText().toString().equals("100")){
    							etFive.setText("0");
    						}else if(etSix.getText().toString().equals("100")){
    							etSix.setText("0");
    						}else if(etSeven.getText().toString().equals("100")){
    							etSeven.setText("0");
    						}else if(etEight.getText().toString().equals("100")){
    							etEight.setText("0");
    						}
    						break;
    					case R.id.et_five:
    						if(etOne.getText().toString().equals("100")){
    							etOne.setText("0");
    						}else if(etTwo.getText().toString().equals("100")){
    							etTwo.setText("0");
    						}else if(etThree.getText().toString().equals("100")){
    							etThree.setText("0");
    						}else if(etFour.getText().toString().equals("100")){
    							etFour.setText("0");
    						}else if(etSix.getText().toString().equals("100")){
    							etSix.setText("0");
    						}else if(etSeven.getText().toString().equals("100")){
    							etSeven.setText("0");
    						}else if(etEight.getText().toString().equals("100")){
    							etEight.setText("0");
    						}
    						break;
    					case R.id.et_six:
    						if(etOne.getText().toString().equals("100")){
    							etOne.setText("0");
    						}else if(etTwo.getText().toString().equals("100")){
    							etTwo.setText("0");
    						}else if(etThree.getText().toString().equals("100")){
    							etThree.setText("0");
    						}else if(etFour.getText().toString().equals("100")){
    							etFour.setText("0");
    						}else if(etFive.getText().toString().equals("100")){
    							etFive.setText("0");
    						}else if(etSeven.getText().toString().equals("100")){
    							etSeven.setText("0");
    						}else if(etEight.getText().toString().equals("100")){
    							etEight.setText("0");
    						}
    						break;
    					case R.id.et_seven:
    						if(etOne.getText().toString().equals("100")){
    							etOne.setText("0");
    						}else if(etTwo.getText().toString().equals("100")){
    							etTwo.setText("0");
    						}else if(etThree.getText().toString().equals("100")){
    							etThree.setText("0");
    						}else if(etFour.getText().toString().equals("100")){
    							etFour.setText("0");
    						}else if(etFive.getText().toString().equals("100")){
    							etFive.setText("0");
    						}else if(etSix.getText().toString().equals("100")){
    							etSix.setText("0");
    						}else if(etEight.getText().toString().equals("100")){
    							etEight.setText("0");
    						}
    						break;
    					case R.id.et_eight:
    						if(etOne.getText().toString().equals("100")){
    							etOne.setText("0");
    						}else if(etTwo.getText().toString().equals("100")){
    							etTwo.setText("0");
    						}else if(etThree.getText().toString().equals("100")){
    							etThree.setText("0");
    						}else if(etFour.getText().toString().equals("100")){
    							etFour.setText("0");
    						}else if(etFive.getText().toString().equals("100")){
    							etFive.setText("0");
    						}else if(etSix.getText().toString().equals("100")){
    							etSix.setText("0");
    						}else if(etSeven.getText().toString().equals("100")){
    							etSeven.setText("0");
    						}
    						break;
    					}
            }  
  
            @Override  
            public void afterTextChanged(Editable s) {  
                if (s != null && !s.equals("")) {  
                    if (MIN_MARK != -1 && MAX_MARK != -1) {  
                        double markVal = 0;  
                        try {  
                            markVal = Double.parseDouble(s.toString());  
                        } catch (NumberFormatException e) {  
                            markVal = 0;  
                        }  
                        if (markVal > MAX_MARK) {  
                            edit.setText(String.valueOf(MAX_MARK));  
                            edit.setSelection(String.valueOf(MAX_MARK).length());
                            Log.e(TAG, "afterTextChanged");
                            switch (edit.getId()) {
							case R.id.et_one:
								etTwo.setText("0");
								etThree.setText("0");
								etFour.setText("0");
								etFive.setText("0");
								etSix.setText("0");
								etSeven.setText("0");
								etEight.setText("0");
								break;
							case R.id.et_two:
								etOne.setText("0");
								etThree.setText("0");
								etFour.setText("0");
								etFive.setText("0");
								etSix.setText("0");
								etSeven.setText("0");
								etEight.setText("0");
								break;
							case R.id.et_three:
								etOne.setText("0");
								etTwo.setText("0");
								etFour.setText("0");
								etFive.setText("0");
								etSix.setText("0");
								etSeven.setText("0");
								etEight.setText("0");
								break;
							case R.id.et_four:
								etOne.setText("0");
								etTwo.setText("0");
								etThree.setText("0");
								etFive.setText("0");
								etSix.setText("0");
								etSeven.setText("0");
								etEight.setText("0");
								break;
							case R.id.et_five:
								etOne.setText("0");
								etTwo.setText("0");
								etThree.setText("0");
								etFour.setText("0");
								etSix.setText("0");
								etSeven.setText("0");
								etEight.setText("0");
								break;
							case R.id.et_six:
								etOne.setText("0");
								etTwo.setText("0");
								etThree.setText("0");
								etFour.setText("0");
								etFive.setText("0");
								etSeven.setText("0");
								etEight.setText("0");
								break;
							case R.id.et_seven:
								etOne.setText("0");
								etTwo.setText("0");
								etThree.setText("0");
								etFour.setText("0");
								etFive.setText("0");
								etSix.setText("0");
								etEight.setText("0");
								break;
							case R.id.et_eight:
								etOne.setText("0");
								etTwo.setText("0");
								etThree.setText("0");
								etFour.setText("0");
								etFive.setText("0");
								etSix.setText("0");
								etSeven.setText("0");
								break;
							}
                        }  
                        return;  
                    }  
                }  
            }  
        });  
    } 
}
