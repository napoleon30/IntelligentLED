package cn.sharelink.intelligentled.utils;

import cn.sharelink.intelligentled.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GridItem extends RelativeLayout implements Checkable {
	private Context mContext;
	private boolean mChecked;// �жϸ�ѡ���Ƿ�ѡ�ϵı�־��
	private ImageView mImgView = null; // ���ǰ��ͼƬ
	private ImageView mSecletView = null; // ��������ͼƬ
	int textColor;

	public GridItem(Context context) {
		this(context, null, 0);
	}

	public GridItem(Context context, int color) {
		this(context, null, 0);
		this.textColor = color;
	}

	public GridItem(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GridItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		LayoutInflater.from(mContext).inflate(R.layout.gridview_item1, this); // ��ʼ��item����
		mImgView = (ImageView) findViewById(R.id.textview_item); // ��ʼ��ԭʼ����id
		mSecletView = (ImageView) findViewById(R.id.textview_item_bg); // �����id
	}

	@Override
	public void setChecked(boolean checked) {
		// TODO Auto-generated method stub
		mChecked = checked;
		mSecletView.setVisibility(checked ? View.VISIBLE : View.GONE);// ѡ����ͼƬ��ɺ�ɫ
//		mSecletView.setBackgroundColor(textColor);
	}

	@Override
	public boolean isChecked() {
		return mChecked;
	}

	@Override
	public void toggle() {
		setChecked(!mChecked);
	}
	
	public void setImgResId(int resId) {
        if (mImgView != null) {
            mImgView.setBackgroundResource(resId);//���ñ���
        }
    }
	
	public void setBackgroudColor(int color){
		mSecletView.setBackgroundColor(color);
	}
	
	
}
