package com.gentlehealthcare.mobilecare.bean;

/**
 * 主界面模块实体类
 * Created by ouyang on 2015/5/26.
 */
public class HomeBean {
    private String content;//内容
    private int sum;//标识数量
    private boolean visible;//是否可见

    public HomeBean(String content, int sum, boolean visible) {
        this.content = content;
        this.sum = sum;
        this.visible = visible;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
