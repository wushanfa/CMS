package com.gentlehealthcare.mobilecare.net;

public interface IRespose<T> {

	/**
	 * 请求结果的回调方法
	 * 
	 * @param t
	 *            请求返回的结果
	 * @param id
	 *            请求的id标识
	 */
	public void doResult(T t, int id);

    /**
     * 请求结果的回调函数，仅仅只获取服务器返回的数据,不对该数据进行任何操作
     * @param result
     */
    public void doResult(String result)  throws KeyObsoleteException;

	/**
	 * 请求异常的结果回调方法。所有的异常均在前台进行处理
	 * <p/>
	 * 如连接超时，访问无权限等
	 * 
	 * @param e
	 *            异常
     * @param  networkState 网络状态 true 为有网情况  false 为网络断开
	 */
	public void doException(Exception e, boolean networkState);


	
}
