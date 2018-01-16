/*
 * Copyright 2014 - 2017 Henning Dodenhof
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.sharelink.intelligentled.utils;

import cn.sharelink.intelligentled.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * ���̿��ƵıȽ��Ͻ�������setup������ʹ��
 * updateShaderMatrix��֤ͼƬ��ʧ����С��ʼ�ջ���ͼƬ��������ǲ���
 * ����˼·�ǻ�Բ����Ⱦ��λͼ��䣬�����ǰ�Bitmap�ػ��и��һ��Բ��ͼƬ��
 */
public class CircleImageView extends ImageView {

	 //��������
	private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

	private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	private static final int COLORDRAWABLE_DIMENSION = 2;

	 // Ĭ�ϱ߽���
	private static final int DEFAULT_BORDER_WIDTH = 0;
	private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
	private static final int DEFAULT_FILL_COLOR = Color.TRANSPARENT;
	private static final boolean DEFAULT_BORDER_OVERLAY = false;

	private final RectF mDrawableRect = new RectF();
	private final RectF mBorderRect = new RectF();

	private final Matrix mShaderMatrix = new Matrix();
	  //�����������Ҫ���ǹ�����mBitmapShader ʹcanvas��ִ�е�ʱ������и�ԭͼƬ(mBitmapShader�ǹ�����ԭͼ��bitmap��)
	private final Paint mBitmapPaint = new Paint();
	//�����ߣ����뱾���ԭͼbitmapû���κι�����
	private final Paint mBorderPaint = new Paint();
	private final Paint mFillPaint = new Paint();

	//���ﶨ���� Բ�α�Ե��Ĭ�Ͽ�Ⱥ���ɫ
	private int mBorderColor = DEFAULT_BORDER_COLOR;
	private int mBorderWidth = DEFAULT_BORDER_WIDTH;
	private int mFillColor = DEFAULT_FILL_COLOR;

	private Bitmap mBitmap;
	private BitmapShader mBitmapShader;// λͼ��Ⱦ
	private int mBitmapWidth;// λͼ���
	private int mBitmapHeight;// λͼ�߶�

	private float mDrawableRadius;// ͼƬ�뾶
	private float mBorderRadius;// ���߿�ĵ�ͼƬ�뾶

	private ColorFilter mColorFilter;

	//��ʼfalse
	private boolean mReady;
	private boolean mSetupPending;
	private boolean mBorderOverlay;
	private boolean mDisableCircularTransformation;

	//���캯��
	public CircleImageView(Context context) {
		super(context);

		init();
	}

	//���캯��
	public CircleImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	//���캯��
	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		//ͨ��obtainStyledAttributes ���һ��ֵ���� TypedArray�����飩 , ��һ��ֵ������res/values/attrs.xml�е�name="CircleImageView"��declare-styleable�С�
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CircleImageView, defStyle, 0);

		//ͨ��TypedArray�ṩ��һϵ�з���getXXXXȡ��������xml�ﶨ��Ĳ���ֵ��
        // ��ȡ�߽�Ŀ��
		mBorderWidth = a.getDimensionPixelSize(
				R.styleable.CircleImageView_civ_border_width,
				DEFAULT_BORDER_WIDTH);
		 // ��ȡ�߽����ɫ
		mBorderColor = a.getColor(R.styleable.CircleImageView_civ_border_color,
				DEFAULT_BORDER_COLOR);
		mBorderOverlay = a.getBoolean(
				R.styleable.CircleImageView_civ_border_overlay,
				DEFAULT_BORDER_OVERLAY);
		mFillColor = a.getColor(R.styleable.CircleImageView_civ_fill_color,
				DEFAULT_FILL_COLOR);

		//���� recycle() ����TypedArray,�Ա��������
		a.recycle();

		init();
	}

	/**
     * ���þ��Ǳ�֤��һ��ִ��setup�������������Ҫ�ڹ��캯��ִ�����ʱ���� 
     */
	private void init() {
		 //������ScaleType��ǿ���趨ΪCENTER_CROP�����ǽ�ͼƬˮƽ��ֱ���У���������
		super.setScaleType(SCALE_TYPE);
		mReady = true;

		if (mSetupPending) {
			setup();
			mSetupPending = false;
		}
	}

	@Override
	public ScaleType getScaleType() {
		return SCALE_TYPE;
	}

	/**
     * ������ȷָ�� ����imageview ֻ֧��CENTER_CROP ��һ������
     *
     * @param scaleType
     */
	@Override
	public void setScaleType(ScaleType scaleType) {
		if (scaleType != SCALE_TYPE) {
			throw new IllegalArgumentException(String.format(
					"ScaleType %s not supported.", scaleType));
		}
	}

	@Override
	public void setAdjustViewBounds(boolean adjustViewBounds) {
		if (adjustViewBounds) {
			throw new IllegalArgumentException(
					"adjustViewBounds not supported.");
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		if (mDisableCircularTransformation) {
			super.onDraw(canvas);
			return;
		}

		//���ͼƬ�����ھͲ���
		if (mBitmap == null) {
			return;
		}

		if (mFillColor != Color.TRANSPARENT) {
			canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(),
					mDrawableRadius, mFillPaint);
		}
		 //������Բ�� ͼƬ ����ΪmBitmapPaint
		canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(),
				mDrawableRadius, mBitmapPaint);
		if (mBorderWidth > 0) {
			canvas.drawCircle(mBorderRect.centerX(), mBorderRect.centerY(),
					mBorderRadius, mBorderPaint);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setup();
	}

	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		super.setPadding(left, top, right, bottom);
		setup();
	}

	@Override
	public void setPaddingRelative(int start, int top, int end, int bottom) {
		super.setPaddingRelative(start, top, end, bottom);
		setup();
	}

	public int getBorderColor() {
		return mBorderColor;
	}

	public void setBorderColor( int borderColor) {
		if (borderColor == mBorderColor) {
			return;
		}

		mBorderColor = borderColor;
		mBorderPaint.setColor(mBorderColor);
		invalidate();
	}

	/**
	 * @deprecated Use {@link #setBorderColor(int)} instead
	 */
	@Deprecated
	public void setBorderColorResource(int borderColorRes) {
		setBorderColor(getContext().getResources().getColor(borderColorRes));
	}

	/**
	 * Return the color drawn behind the circle-shaped drawable.
	 * 
	 * @return The color drawn behind the drawable
	 * 
	 * @deprecated Fill color support is going to be removed in the future
	 */
	@Deprecated
	public int getFillColor() {
		return mFillColor;
	}

	/**
	 * Set a color to be drawn behind the circle-shaped drawable. Note that this
	 * has no effect if the drawable is opaque or no drawable is set.
	 * 
	 * @param fillColor
	 *            The color to be drawn behind the drawable
	 * 
	 * @deprecated Fill color support is going to be removed in the future
	 */
	@Deprecated
	public void setFillColor(int fillColor) {
		if (fillColor == mFillColor) {
			return;
		}

		mFillColor = fillColor;
		mFillPaint.setColor(fillColor);
		invalidate();
	}

	/**
	 * Set a color to be drawn behind the circle-shaped drawable. Note that this
	 * has no effect if the drawable is opaque or no drawable is set.
	 * 
	 * @param fillColorRes
	 *            The color resource to be resolved to a color and drawn behind
	 *            the drawable
	 * 
	 * @deprecated Fill color support is going to be removed in the future
	 */
	@Deprecated
	public void setFillColorResource(int fillColorRes) {
		setFillColor(getContext().getResources().getColor(fillColorRes));
	}

	public int getBorderWidth() {
		return mBorderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		if (borderWidth == mBorderWidth) {
			return;
		}

		mBorderWidth = borderWidth;
		setup();
	}

	public boolean isBorderOverlay() {
		return mBorderOverlay;
	}

	public void setBorderOverlay(boolean borderOverlay) {
		if (borderOverlay == mBorderOverlay) {
			return;
		}

		mBorderOverlay = borderOverlay;
		setup();
	}

	public boolean isDisableCircularTransformation() {
		return mDisableCircularTransformation;
	}

	public void setDisableCircularTransformation(
			boolean disableCircularTransformation) {
		if (mDisableCircularTransformation == disableCircularTransformation) {
			return;
		}

		mDisableCircularTransformation = disableCircularTransformation;
		initializeBitmap();
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		initializeBitmap();
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		initializeBitmap();
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		initializeBitmap();
	}

	@Override
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		initializeBitmap();
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		if (cf == mColorFilter) {
			return;
		}

		mColorFilter = cf;
		applyColorFilter();
		invalidate();
	}

	@Override
	public ColorFilter getColorFilter() {
		return mColorFilter;
	}

	private void applyColorFilter() {
		if (mBitmapPaint != null) {
			mBitmapPaint.setColorFilter(mColorFilter);
		}
	}

	private Bitmap getBitmapFromDrawable(Drawable drawable) {
		if (drawable == null) {
			return null;
		}

		if (drawable instanceof BitmapDrawable) {
			//ͨ����˵ ���ǵĴ������ִ�е�����ͷ����ˡ����صľ���������ԭʼ��bitmap
			return ((BitmapDrawable) drawable).getBitmap();
		}

		try {
			Bitmap bitmap;

			if (drawable instanceof ColorDrawable) {
				bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,
						COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
			} else {
				bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(), BITMAP_CONFIG);
			}

			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void initializeBitmap() {
		if (mDisableCircularTransformation) {
			mBitmap = null;
		} else {
			mBitmap = getBitmapFromDrawable(getDrawable());
		}
		setup();
	}

	/**
     * ��������ܹؼ�������ͼƬ���ʱ߽续��(Paint)һЩ�ػ������ʼ����
     * ������Ⱦ��BitmapShader��Bitmap������������,������ʽ�Լ�����Բ�뾶����ȣ�
     * �Լ�����updateShaderMatrix()������ invalidate()������
     */
	private void setup() {
		 //��ΪmReadyĬ��ֵΪfalse,���Ե�һ�ν����������ʱ��if���Ϊ�������������
        //����mSetupPendingΪtrueȻ��ֱ�ӷ��أ�����Ĵ��벢û��ִ�С�
		if (!mReady) {
			mSetupPending = true;
			return;
		}

		if (getWidth() == 0 && getHeight() == 0) {
			return;
		}

		if (mBitmap == null) {
			invalidate();
			return;
		}

		// ������Ⱦ������mBitmapλͼ������������ ������ֵ�������ͼƬ̫С�Ļ� ��ֱ������
		mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP,
				Shader.TileMode.CLAMP);

		// ����ͼƬ���ʷ����
		mBitmapPaint.setAntiAlias(true);
		// ����ͼƬ������Ⱦ��
		mBitmapPaint.setShader(mBitmapShader);

		// ���ñ߽续����ʽ
		mBorderPaint.setStyle(Paint.Style.STROKE);//�軭��Ϊ����
		mBorderPaint.setAntiAlias(true);
		mBorderPaint.setColor(mBorderColor); //������ɫ
		mBorderPaint.setStrokeWidth(mBorderWidth);//���ʱ߽���

		mFillPaint.setStyle(Paint.Style.FILL);
		mFillPaint.setAntiAlias(true);
		mFillPaint.setColor(mFillColor);

		//����ط���ȡ��ԭͼƬ�Ŀ��
		mBitmapHeight = mBitmap.getHeight();
		mBitmapWidth = mBitmap.getWidth();

		mBorderRect.set(calculateBounds());
		mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2.0f,
				(mBorderRect.width() - mBorderWidth) / 2.0f);

		// ��ʼͼƬ��ʾ����ΪmBorderRect��CircleImageView�Ĳ���ʵ�ʴ�С��
		mDrawableRect.set(mBorderRect);
		if (!mBorderOverlay && mBorderWidth > 0) {
			mDrawableRect.inset(mBorderWidth - 1.0f, mBorderWidth - 1.0f);
		}
		//������������Բ����С�뾶��Ҳ��ȥ���߽��ȵİ뾶
		mDrawableRadius = Math.min(mDrawableRect.height() / 2.0f,
				mDrawableRect.width() / 2.0f);

		applyColorFilter();
		//������Ⱦ���ı任����Ҳ����mBitmap�ú���������ʽ���
		updateShaderMatrix();
		 //�ֶ�����ondraw()���� ������յĻ���
		invalidate();
	}

	private RectF calculateBounds() {
		int availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
		int availableHeight = getHeight() - getPaddingTop()
				- getPaddingBottom();

		int sideLength = Math.min(availableWidth, availableHeight);

		float left = getPaddingLeft() + (availableWidth - sideLength) / 2f;
		float top = getPaddingTop() + (availableHeight - sideLength) / 2f;

		return new RectF(left, top, left + sideLength, top + sideLength);
	}

	/**
	    * �������Ϊ����BitmapShader��Matrix������������С���ű�����ƽ�Ʋ�����
	    * ���ã���֤ͼƬ��ʧ����С��ʼ�ջ���ͼƬ��������ǲ���
	    */
	private void updateShaderMatrix() {
		float scale;
		float dx = 0;
		float dy = 0;

		mShaderMatrix.set(null);

		// ���ﲻ����� �������ʽҲ����(mBitmapWidth / mDrawableRect.width()) > (mBitmapHeight / mDrawableRect.height())
        //ȡ��С�����ű���
		if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width()
				* mBitmapHeight) {
			 //y������ x��ƽ�� ʹ��ͼƬ��y�᷽��ıߵĳߴ����ŵ�ͼƬ��ʾ����mDrawableRect��һ����
			scale = mDrawableRect.height() / (float) mBitmapHeight;
			dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
		} else {
			//x������ y��ƽ�� ʹ��ͼƬ��x�᷽��ıߵĳߴ����ŵ�ͼƬ��ʾ����mDrawableRect��һ����
			scale = mDrawableRect.width() / (float) mBitmapWidth;
			dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
		}

		 // shaeder�ı任��������������Ҫ���ڷŴ������С
		mShaderMatrix.setScale(scale, scale);
		// ƽ��
		mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left,
				(int) (dy + 0.5f) + mDrawableRect.top);
		// ���ñ任����
		mBitmapShader.setLocalMatrix(mShaderMatrix);
	}

}
