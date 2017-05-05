package com.gentlehealthcare.mobilecare.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.constant.GestureConstant;
import com.gentlehealthcare.mobilecare.tool.PersonCustomSave;

import java.util.Map;

/**
 * 
 * @ClassName: GestFloatWindow
 * @Description: 手势操作 悬浮框
 * @author ouyang
 * @date 2015年3月2日 上午10:31:37
 *
 */
public class GestFloatWindow extends ImageView implements OnLongClickListener {
	private static final String TAG = "GestFlateoatWindow";

	private WindowManager wm;
	private WindowManager.LayoutParams params;

	private float x;
	private float y;
	private float xUp;
	private float xDown;
	private float xMove;
	private float yMove;
	private float yUp;
	private float yDown;
	private long time;
	private long upTime;
	private long downTime;
	private float mTouchStartX;
	private float mTouchStartY;

	private boolean isLongPressed = false;

	private boolean initViewPlace = false;

    private int intScreenWidth;
    private int intScreenHeight;
	private boolean ismove;
    private enum PLACE{
        Left,
        Right
    }
    private PLACE my_place=PLACE.Left;

	public GestFloatWindow(Context context) {
		super(context);
		initView(context);
        DisplayMetrics dm= context.getApplicationContext().getResources().getDisplayMetrics();
        intScreenWidth=dm.widthPixels;
        intScreenHeight=dm.heightPixels;
		// TODO Auto-generated constructor stub
	}

	public GestFloatWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public GestFloatWindow(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	private void initView(Context context) {
		params = new WindowManager.LayoutParams();

		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

		// 设置window type
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		/*
		 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
		 * 即拉下通知栏不可见
		 */

		params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

		// 设置Window flag
		params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_FULLSCREEN;
		/*
		 * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */

		// 设置悬浮窗的长得宽
		params.width = 100;
		params.height = 200;
        setBackgroundResource(R.drawable.btn_zero_normal);
        Map<String,?> map=PersonCustomSave.getInstance(context).get();


        if (map==null||!map.containsKey("place")||map.get("place").toString().equals("左边")) {
			params.gravity = Gravity.LEFT ;
			params.x=0;
            my_place = PLACE.Left;
        } else {
            params.gravity = Gravity.RIGHT ;
            setBackgroundResource(R.drawable.btn_zero_normal_right);
            my_place=PLACE.Right;
        }
		params.y=intScreenHeight;
		setOnLongClickListener(this);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return doOnTouch(event);
            }
        });

		wm.addView(this, params);

	}

    public void updatePlace(String place){
        if (place.equals("左边")){
            params.gravity = Gravity.LEFT | Gravity.CENTER;
            setBackgroundResource(R.drawable.btn_zero_normal);
            my_place=PLACE.Left;
        }else{
            params.gravity = Gravity.RIGHT | Gravity.CENTER;
            setBackgroundResource(R.drawable.btn_zero_normal_right);
            my_place=PLACE.Right;
        }

        wm.updateViewLayout(this, params);
    }



	public boolean doOnTouch(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {

		case MotionEvent.ACTION_DOWN:
			ismove=false;

			xDown = ev.getRawX();

			yDown = ev.getRawY();

			downTime = ev.getEventTime();

			if (!initViewPlace) {

				initViewPlace = true;

				// 获取初始位置
				xMove = ev.getRawX();

				yMove = ev.getRawY();

				mTouchStartX = ev.getRawX();

				mTouchStartY = ev.getRawY();

			} else {

				if (isLongPressed) {

					// 根据上次手指离开的位置与此次点击的位置进行初始位置微调
					mTouchStartX += (xDown - xMove);

					mTouchStartY += (yDown - yMove);
				}
			}

			break;

		case MotionEvent.ACTION_MOVE:


			if (isLongPressed) {

				xMove = ev.getRawX();

				yMove = ev.getRawY();

				ismove=true;

				// params.x = (int) (x - mTouchStartX);

				params.y = (int) (yMove - mTouchStartY);

				wm.updateViewLayout(this, params);
			}

			break;

		case MotionEvent.ACTION_UP:

			xUp = ev.getRawX();

			yUp = ev.getRawY();

			upTime = ev.getEventTime();

			x = xUp - xDown;

			y = yUp - yDown;

			time = upTime - downTime;

			Message message = new Message();

			GestureConstant.Type type = null;

			if (!isLongPressed) {

				// 快速点击
				if (Math.abs(x) < 20 && Math.abs(y) < 20 && time < 500) {
					//ActivityControlTool.currentActivity.doChick();

				} else if (Math.abs(y / x) <= 1) {

					// 右滑
					if (x > 20) {

						//ActivityControlTool.currentActivity.doGestRight();

						// 左滑
					} else if (x < -20) {
						//ActivityControlTool.currentActivity.doGestLeft();

					}
				} else {

					// 下滑
					if (y > 20) {
						//ActivityControlTool.currentActivity.doGestDown();

						// 上滑
					} else if (y < -20) {
					//	ActivityControlTool.currentActivity.doGestUp();

					}
				}
			} else if (!ismove){
				//ActivityControlTool.currentActivity.doLongChick();
			}

			if (isLongPressed) {
                if (my_place==PLACE.Left)
				setBackgroundResource(R.drawable.btn_zero_normal);
                else
                    setBackgroundResource(R.drawable.btn_zero_normal_right);

				isLongPressed = false;
			}
			break;
		}
		return false;
	}

	@Override
	public boolean onLongClick(View arg0) {
		// // TODO Auto-generated method stub
		 isLongPressed = true;
		if (my_place==PLACE.Left)
		 setBackgroundResource(R.drawable.btn_zero_move);
        else
            setBackgroundResource(R.drawable.btn_zero_move_right);
		return false;
	}

}
