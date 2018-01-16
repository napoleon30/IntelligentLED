package cn.sharelink.intelligentled.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class GalleryViewPager extends ViewPager {
    //默认距离�?
    private final static float DISTANCE = 10;
    private float downX;
    private float downY;

    public GalleryViewPager(Context context) {
        super(context);
    }

    public GalleryViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            downX = ev.getX();
            downY = ev.getY();
        }else if (ev.getAction() == MotionEvent.ACTION_UP) {

            float upX = ev.getX();
            float upY = ev.getY();
            //如果 up的位置和down 的位�? 距离 > 设置的距�?,则事件继续传�?,不执行下面的点击切换事件
            if(Math.abs(upX - downX) > DISTANCE || Math.abs(upY - downY) > DISTANCE){
                return super.dispatchTouchEvent(ev);
            }

            View view = viewOfClickOnScreen(ev);
            if (view != null) {
                int index = (Integer) view.getTag();
                if (getCurrentItem() != index) {
                    setCurrentItem(index);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * @param ev
     * @return
     */
    private View viewOfClickOnScreen(MotionEvent ev) {
        int childCount = getChildCount();
        int currentIndex = getCurrentItem();
        int[] location = new int[2];
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            int position = (Integer) v.getTag();
            v.getLocationOnScreen(location);
            int minX = location[0];
            int minY = location[1];

            int maxX = location[0] + v.getWidth();
            int maxY = location[1] + v.getHeight();

            if(position < currentIndex){
                maxX -= v.getWidth() * (1 - ScalePageTransformer.MIN_SCALE) * 0.5 + v.getWidth() * (Math.abs(1 - ScalePageTransformer.MAX_SCALE)) * 0.5;
                minX -= v.getWidth() * (1 - ScalePageTransformer.MIN_SCALE) * 0.5 + v.getWidth() * (Math.abs(1 - ScalePageTransformer.MAX_SCALE)) * 0.5;
            }else if(position == currentIndex){
                minX += v.getWidth() * (Math.abs(1 - ScalePageTransformer.MAX_SCALE));
            }else if(position > currentIndex){
                maxX -= v.getWidth() * (Math.abs(1 - ScalePageTransformer.MAX_SCALE)) * 0.5;
                minX -= v.getWidth() * (Math.abs(1 - ScalePageTransformer.MAX_SCALE)) * 0.5;
            }
            float x = ev.getRawX();
            float y = ev.getRawY();

            if ((x > minX && x < maxX) && (y > minY && y < maxY)) {
                return v;
            }
        }
        return null;
    }

}
