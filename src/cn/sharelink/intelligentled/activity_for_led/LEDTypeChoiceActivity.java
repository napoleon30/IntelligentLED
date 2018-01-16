package cn.sharelink.intelligentled.activity_for_led;

import com.accloud.cloudservice.AC;
import com.accloud.utils.PreferencesUtils;
import com.hyphenate.chat.EMClient;
//import com.umeng.message.PushAgent;
//import com.umeng.message.UTrack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharelink.intelligentled.R;
import cn.sharelink.intelligentled.activity.AboutActivity;
import cn.sharelink.intelligentled.activity.LoginActivity;
import cn.sharelink.intelligentled.activity.MainActivity;
import cn.sharelink.intelligentled.activity.MenuActivity;
import cn.sharelink.intelligentled.activity.ResetPasswordActivity;
import cn.sharelink.intelligentled.application.MainApplication;
import cn.sharelink.intelligentled.config.Config;

/**
 * 灯类型选择界面
 * @author Administrator
 *
 */
public class LEDTypeChoiceActivity extends Activity {
	private static final String TAG = LEDTypeChoiceActivity.class
			.getSimpleName();
	private Button btnSingleColor;
	private Button btnCCTChange;
	private Button btnRGB;
	private String subDomain;

	private LinearLayout ll_body1;
	private LinearLayout ll_body2;
	
	private TextView tvUserName;
	private TextView tvResetPassword;
	private TextView tvVersion;
	private TextView tvAboutUs;
	private TextView tvExiteLoad;
	
	private LinearLayout ll_control;
	private LinearLayout ll_user;
	private TextView tvControl;
	private ImageView ivControl;
	private TextView tvUser;
	private ImageView ivUser;
	
	String userName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		setContentView(R.layout.activity_ledtype_choice);
		userName = PreferencesUtils.getString(LEDTypeChoiceActivity.this, "phone_or_email");
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		subDomain = PreferencesUtils.getString(this, "subDomain",
				Config.SUBDOMAIN);
		if (!AC.accountMgr().isLogin()) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}

	private void initView() {
		btnSingleColor = (Button) findViewById(R.id.btn_single_color);
		btnCCTChange = (Button) findViewById(R.id.btn_cct_change);
		btnRGB = (Button) findViewById(R.id.btn_rgb);
		btnSingleColor.setOnClickListener(listener);
		btnCCTChange.setOnClickListener(listener);
		btnRGB.setOnClickListener(listener);
		
		ll_control = (LinearLayout)findViewById(R.id.ll_control);
		ll_user = (LinearLayout)findViewById(R.id.ll_user);
		ll_control.setOnClickListener(listener);
		ll_user.setOnClickListener(listener);

		ll_body1 = (LinearLayout) findViewById(R.id.ll_body1);
		ll_body2 = (LinearLayout) findViewById(R.id.ll_body2);
		tvUserName = (TextView) findViewById(R.id.tv_user_name);
		tvUserName.setText(userName);
		tvResetPassword = (TextView) findViewById(R.id.tv_reset_password);
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvAboutUs = (TextView) findViewById(R.id.tv_about_us);
		tvExiteLoad = (TextView) findViewById(R.id.tv_exit_load);
		
		tvResetPassword.setOnClickListener(listener);
		tvAboutUs.setOnClickListener(listener);
		tvExiteLoad.setOnClickListener(listener);
		
		tvControl = (TextView) findViewById(R.id.tv_control);
		ivControl = (ImageView) findViewById(R.id.iv_control);
		tvUser = (TextView) findViewById(R.id.tv_user);
		ivUser= (ImageView) findViewById(R.id.iv_user);

	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.btn_single_color:
				startActivity(new Intent(LEDTypeChoiceActivity.this,
						SingleColorListActivity.class));
				break;
			case R.id.btn_cct_change:
				startActivity(new Intent(LEDTypeChoiceActivity.this,
						CCTChangeActivity.class));
				break;
			case R.id.btn_rgb:
				startActivity(new Intent(LEDTypeChoiceActivity.this, RGBActivity.class));
				break;
			case R.id.ll_control:
				ll_body1.setVisibility(View.VISIBLE);
				ll_body2.setVisibility(View.GONE);
				tvControl.setTextColor(getResources().getColor(R.color.unchoice));
				ivControl.setBackgroundResource(R.drawable.control_choice);
				
				tvUser.setTextColor(getResources().getColor(R.color.white));
				ivUser.setBackgroundResource(R.drawable.user_unchoice);
				break;
			case R.id.ll_user:
				ll_body1.setVisibility(View.GONE);
				ll_body2.setVisibility(View.VISIBLE);
				tvUser.setTextColor(getResources().getColor(R.color.unchoice));
				ivUser.setBackgroundResource(R.drawable.user_choice);
				tvControl.setTextColor(getResources().getColor(R.color.white));
				ivControl.setBackgroundResource(R.drawable.control_unchoice);
				break;
			case R.id.tv_reset_password:
				startActivity(new Intent(LEDTypeChoiceActivity.this, ResetPasswordActivity.class));
				break;
			case R.id.tv_about_us:
				startActivity(new Intent(LEDTypeChoiceActivity.this, ManagerDeviceActivity.class));
				break;
			case R.id.tv_exit_load:
				AlertDialog.Builder builder = new AlertDialog.Builder(LEDTypeChoiceActivity.this);
				builder.setMessage(getResources().getString(R.string.are_you_sure_to_log_out));
				builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 退出登录
						EMClient.getInstance().logout(true);
						Log.e(TAG, "环信退出登录");
						AC.accountMgr().logout();
						Log.e(TAG, "ablecloud退出登录");
						Long userid = PreferencesUtils.getLong(LEDTypeChoiceActivity.this, "userId");
						startActivity(new Intent(LEDTypeChoiceActivity.this, LoginActivity.class));
						finish();
					}
				});
				builder.create().show();
				
			
				break;
			}
		}
	};

}
