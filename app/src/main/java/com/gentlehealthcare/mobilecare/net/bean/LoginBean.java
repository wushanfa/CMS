package com.gentlehealthcare.mobilecare.net.bean;

/**
 * @author ouyang
 * @ClassName: LoginBean
 * @Description: 登录请求实体类
 * @date 2015年3月6日 上午9:25:40
 */
public class LoginBean {
    private String username;//登录帐号
    private String password;//登录密码
    private boolean result;//应答结果
    private String key;
    private String resultText;//错误信息
    private String msg;
    private String serviceTime;//服务器时间
    public String getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
	}

	private BasicInfoItem basicInfo;//基本信息

    @Override
    public String toString() {
        return "?username=" + getUsername() + "&password=" + getPassword();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public BasicInfoItem getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(BasicInfoItem basicInfo) {
        this.basicInfo = basicInfo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isResult() {
        return result;
    }
}
