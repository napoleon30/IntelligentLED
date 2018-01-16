package cn.sharelink.intelligentled.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.sharelink.intelligentled.R;
import cn.sharelink.intelligentled.config.Config;
import cn.sharelink.intelligentled.utils.DBOXException;
import cn.sharelink.intelligentled.utils.Pop;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.service.ACException;
import com.accloud.service.ACUserDevice;
import com.accloud.utils.PreferencesUtils;
//import com.umeng.message.PushAgent;
import com.zbar.lib.CaptureActivity;

/**
 * 使用物理ID绑定设备
 *
 * Created by sudongsheng on 16/6/13.
 */
public class BindDeviceActivity extends Activity implements View.OnClickListener {
    private TextView back,scanScan;
    private EditText physicalDeviceIdEdt;
    private EditText nameEdt;
    private Button bind;
    private String subDomain;
    String scanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
        setContentView(R.layout.activity_bind_device);
//        PushAgent.getInstance(this).onAppStart();
        
        Intent intent = getIntent();
        scanResult = intent.getStringExtra("scan_result");
        back = (TextView) findViewById(R.id.back);
        scanScan = (TextView) findViewById(R.id.scan_scan);
        physicalDeviceIdEdt = (EditText) findViewById(R.id.physicalDeviceId);
        
        nameEdt = (EditText) findViewById(R.id.name);
        physicalDeviceIdEdt.setText(scanResult);
        nameEdt.setFocusable(true);
        
        bind = (Button) findViewById(R.id.bind);
        back.setOnClickListener(this);
        scanScan.setOnClickListener(this);
        bind.setOnClickListener(this);
        subDomain = PreferencesUtils.getString(this, "subDomain", Config.SUBDOMAIN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.bind:
                String physicalDeviceId = physicalDeviceIdEdt.getText().toString();
                String name = nameEdt.getText().toString();
                AC.bindMgr().bindDevice(subDomain, physicalDeviceId, name, new PayloadCallback<ACUserDevice>() {
                    @Override
                    public void success(ACUserDevice userDevice) {
                        Pop.popToast(BindDeviceActivity.this, getString(R.string.bind_device_aty_device_bind_success,userDevice.getDeviceId()));
                        finish();
                    }

                    @Override
                    public void error(ACException e) {
//                        Pop.popToast(BindDeviceActivity.this, e.getErrorCode() + "-->" + e.getMessage());
                    	DBOXException.errorCode(BindDeviceActivity.this, e.getErrorCode());
                    }
                });
                break;
            case R.id.scan_scan:
            	Intent intent = new Intent(BindDeviceActivity.this,CaptureActivity.class);
            	overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            	startActivity(intent);
            	finish();
            	break;
        }
    }
}
