/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015��3��30��	| duanbokan 	| 	create the file                       
 */

package cn.sharelink.intelligentled.country;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import taobe.tec.jcc.JChineseConvertor;

import cn.sharelink.intelligentled.R;
import cn.sharelink.intelligentled.activity.LoginActivity;
import cn.sharelink.intelligentled.country.SideBar.OnTouchingLetterChangedListener;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * ���Ҫ����
 * 
 * <p>
 * ����ϸ����
 * </p>
 * 
 * @author duanbokan
 * 
 */

public class CountryActivity extends Activity {
	String TAG = "CountryActivity";

	private List<CountrySortModel> mAllCountryList;

	private EditText country_edt_search;

	private ListView country_lv_countryList;

	private ImageView country_iv_clearText;

	private CountrySortAdapter adapter;

	private SideBar sideBar;

	private TextView dialog;
	private Button back;

	private CountryComparator pinyinComparator;

	private GetCountryNameSort countryChangeUtil;

	private CharacterParserUtil characterParserUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.coogame_country);

		initView();

		setListener();

		getCountryList();

	}

	/**
	 * ��ʼ������
	 */
	private void initView() {
		country_edt_search = (EditText) findViewById(R.id.country_et_search);
		country_lv_countryList = (ListView) findViewById(R.id.country_lv_list);
		country_iv_clearText = (ImageView) findViewById(R.id.country_iv_cleartext);

		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		dialog = (TextView) findViewById(R.id.country_dialog);
		sideBar = (SideBar) findViewById(R.id.country_sidebar);
		sideBar.setTextView(dialog);

		mAllCountryList = new ArrayList<CountrySortModel>();
		pinyinComparator = new CountryComparator();
		countryChangeUtil = new GetCountryNameSort();
		characterParserUtil = new CharacterParserUtil();

		// ����ϵ�˽������򣬰���A~Z��˳��
		Collections.sort(mAllCountryList, pinyinComparator);
		adapter = new CountrySortAdapter(this, mAllCountryList);
		country_lv_countryList.setAdapter(adapter);

	}

	/****
	 * ��Ӽ���
	 */
	private void setListener() {
		country_iv_clearText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				country_edt_search.setText("");
				Collections.sort(mAllCountryList, pinyinComparator);
				adapter.updateListView(mAllCountryList);
			}
		});

		country_edt_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String searchContent = country_edt_search.getText().toString();
				if (searchContent.equals("")) {
					country_iv_clearText.setVisibility(View.INVISIBLE);
				} else {
					country_iv_clearText.setVisibility(View.VISIBLE);
				}

				if (searchContent.length() > 0) {
					// �����������ݽ���ƥ��
					ArrayList<CountrySortModel> fileterList = (ArrayList<CountrySortModel>) countryChangeUtil
							.search(searchContent, mAllCountryList);

					adapter.updateListView(fileterList);
				} else {
					adapter.updateListView(mAllCountryList);
				}
				country_lv_countryList.setSelection(0);
			}
		});

		// �Ҳ�sideBar����
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// ����ĸ�״γ��ֵ�λ��
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					country_lv_countryList.setSelection(position);
				}
			}
		});

		country_lv_countryList
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int position, long arg3) {
						String countryName = null;
						String countryNumber = null;
						String searchContent = country_edt_search.getText()
								.toString();

						if (searchContent.length() > 0) {
							// �����������ݽ���ƥ��
							ArrayList<CountrySortModel> fileterList = (ArrayList<CountrySortModel>) countryChangeUtil
									.search(searchContent, mAllCountryList);
							countryName = fileterList.get(position).countryName;
							countryNumber = fileterList.get(position).countryNumber;
						} else {
							// ����󷵻�
							countryName = mAllCountryList.get(position).countryName;
							countryNumber = mAllCountryList.get(position).countryNumber;
						}

						Intent intent = new Intent();
						intent.setClass(CountryActivity.this,
								LoginActivity.class);
						intent.putExtra("countryName", countryName);
						intent.putExtra("countryNumber", countryNumber);
						setResult(RESULT_OK, intent);
						Log.e(TAG, "countryName: + " + countryName
								+ "countryNumber: " + countryNumber);
						finish();

					}
				});

	}

	/**
	 * ��ȡ�����б�
	 */
	private void getCountryList() {
		// �жϵ�ǰϵͳ����
		String locale = getResources().getConfiguration().locale.getCountry();
		Log.e("CountryActivity", locale);
		String[] countryList;
		if (locale.equals("CN")) {
			countryList = getResources().getStringArray(
					R.array.country_code_list_ch);
		} else if (locale.equals("TW") || locale.equals("HK")) {
			countryList = getResources().getStringArray(
					R.array.country_code_list_ch_tw);
		} else {
			countryList = getResources().getStringArray(
					R.array.country_code_list_en);
		}

		for (int i = 0, length = countryList.length; i < length; i++) {
			String[] country = countryList[i].split("\\*");

			String countryName = country[0];
			String countryNumber = country[1];
			String countrySortKey = characterParserUtil
					.getSelling(change1(countryName));
			CountrySortModel countrySortModel = new CountrySortModel(
					countryName, countryNumber, countrySortKey);
			String sortLetter = countryChangeUtil
					.getSortLetterBySortKey(countrySortKey);
			if (sortLetter == null) {
				sortLetter = countryChangeUtil
						.getSortLetterBySortKey(countryName);
			}

			countrySortModel.sortLetters = sortLetter;
			mAllCountryList.add(countrySortModel);
		}

		Collections.sort(mAllCountryList, pinyinComparator);
		adapter.updateListView(mAllCountryList);
		Log.e(TAG, "changdu" + mAllCountryList.size());
	}

	// ����ת�ɼ���
	public String change1(String changeText) {
		try {
			JChineseConvertor jChineseConvertor = JChineseConvertor
					.getInstance();
			changeText = jChineseConvertor.t2s(changeText);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return changeText;
	}
}
