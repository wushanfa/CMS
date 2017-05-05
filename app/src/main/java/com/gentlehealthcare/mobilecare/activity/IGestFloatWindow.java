package com.gentlehealthcare.mobilecare.activity;
/**
 * 
 * @ClassName: IGestFloatWindow 
 * @Description: 悬浮框手势处理
 * @author ouyang
 * @date 2015年3月2日 上午10:51:26 
 *
 */
public interface IGestFloatWindow {
	/**
	 * 
	 * @Title: doGestUp 
	 * @Description: 悬浮框 手势上移 
	 * @param    
	 * @return void   
	 * @throws
	 */
	public  void doGestUp();
	/**
	 * 
	 * @Title: doGestDown 
	 * @Description: 悬浮框 手势下移 
	 * @param    
	 * @return void   
	 * @throws
	 */
	public  void doGestDown();
	/**
	 * 
	 * @Title: doGestLeft 
	 * @Description: 悬浮框 手势左移 
	 * @param    
	 * @return void   
	 * @throws
	 */
	public  void doGestLeft();
	/**
	 * 
	 * @Title: doGestRight 
	 * @Description: 悬浮框 手势右移 
	 * @param    
	 * @return void   
	 * @throws
	 */
	public  void doGestRight();
	/**
	 * 
	 * @Title: doChick 
	 * @Description: 悬浮框 点击
	 * @param    
	 * @return void   
	 * @throws
	 */
	public void  doChick();


    /**
     *
     * @Title: doLongChick
     * @Description: 悬浮框 长击
     * @param
     * @return void
     * @throws
     */
	public void doLongChick();
}
