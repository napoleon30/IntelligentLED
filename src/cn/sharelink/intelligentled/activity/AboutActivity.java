package cn.sharelink.intelligentled.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import cn.sharelink.intelligentled.R;
//import com.umeng.message.PushAgent;

/**
 * 关于页面
 */
public class AboutActivity extends Activity {
	TextView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		setContentView(R.layout.activity_about);
		
		back = (TextView) findViewById(R.id.about_back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AboutActivity.this.finish();
			}
		});

	}
}
